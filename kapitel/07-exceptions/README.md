# Kapitel 07 — Exceptions

**Ziel:** Du unterscheidest Programmierfehler von erwartbaren Ausnahmefaellen,
behandelst sie an der richtigen Stelle und gibst Ressourcen zuverlaessig frei.

---

## 7.1 Die Hierarchie

```
Throwable
├── Error                     JVM ist am Ende: OutOfMemoryError, StackOverflowError
│                             -> NICHT fangen
└── Exception
    ├── RuntimeException      unchecked - Programmierfehler
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   │   └── NumberFormatException
    │   ├── IllegalStateException
    │   ├── ArithmeticException
    │   ├── IndexOutOfBoundsException
    │   │   ├── ArrayIndexOutOfBoundsException
    │   │   └── StringIndexOutOfBoundsException
    │   ├── ClassCastException
    │   └── UnsupportedOperationException
    └── alles andere          checked - erwartbare Aussenwelt-Probleme
        ├── IOException
        │   └── FileNotFoundException
        ├── InterruptedException
        └── deine eigenen
```

Die Einrueckung ist wichtig: `catch (IllegalArgumentException e)` faengt auch
jede `NumberFormatException`, `catch (IOException e)` auch jede
`FileNotFoundException`. Ein `catch` faengt immer den Typ **und alle Untertypen**.

## 7.2 Checked gegen unchecked — die zentrale Unterscheidung

**Checked** (alles unter `Exception`, ausser `RuntimeException`):
Der Compiler zwingt dich, sie zu behandeln oder weiterzureichen.

```java
public void lesen(Path p) throws IOException {   // weiterreichen
    Files.readString(p);
}
```

Vergisst du das `throws`, bekommst du:
`unreported exception IOException; must be caught or declared to be thrown`

**Unchecked** (`RuntimeException` und Unterklassen): Der Compiler sagt nichts.
Sie duerfen ueberall auftreten.

### Wann was?

| Situation | Typ | Beispiel |
|-----------|-----|----------|
| Aufrufer hat Mist gebaut | unchecked | `IllegalArgumentException` bei negativem Betrag |
| Objekt im falschen Zustand | unchecked | `IllegalStateException` bei geschlossener Verbindung |
| Aussenwelt kann fehlschlagen, Aufrufer kann sinnvoll reagieren | checked | Datei fehlt, Netzwerk weg |

Die Faustregel: **Kann der Aufrufer den Fehler sinnvoll behandeln — checked.
Ist es ein Bug — unchecked.**

Java ist die einzige Mainstream-Sprache mit checked Exceptions, und das ist
umstritten. In der Praxis: Sparsam einsetzen. Zu viele erzeugen genau die
leeren `catch`-Bloecke, die das Konzept verhindern wollte.

## 7.3 `try` / `catch` / `finally`

```java
try {
    int wert = Integer.parseInt(text);
    System.out.println(100 / wert);
} catch (NumberFormatException e) {
    System.out.println("Keine Zahl: " + text);
} catch (ArithmeticException e) {
    System.out.println("Division durch null");
} finally {
    System.out.println("laeuft immer");
}
```

Reihenfolge der `catch`-Bloecke: **spezifisch vor allgemein**. Steht
`catch (Exception e)` zuerst, ist alles danach unerreichbar — Compilerfehler.

`finally` laeuft **immer**: nach normalem Ende, nach gefangener Exception, sogar
nach einem `return` im `try`-Block. (Die einzige praktische Ausnahme:
`System.exit(...)` beendet die JVM sofort.)

### Multi-Catch

```java
catch (NumberFormatException | ArithmeticException e) {
    System.out.println("Rechnung nicht moeglich: " + e.getMessage());
}
```

Nur sinnvoll, wenn die Behandlung wirklich identisch ist. Die Typen duerfen
nicht voneinander erben: `NumberFormatException | IllegalArgumentException`
ist ein Compilerfehler, weil der zweite Typ den ersten schon enthaelt.

### Niemals `return` im `finally`

```java
try {
    return 1;
} finally {
    return 2;      // schluckt das return VON OBEN und auch jede Exception
}
```

Das liefert immer `2` und verschluckt Fehler still. Der Compiler warnt
standardmaessig nicht (erst mit `javac -Xlint:finally`).

## 7.4 `throw` und `throws`

```java
public void abheben(long betrag) throws UnzureichendeDeckungException {
//                                ^^^^^^ Deklaration: "kann passieren"
    if (betrag <= 0) {
        throw new IllegalArgumentException("Betrag muss positiv sein");
//      ^^^^^ Aktion: wirft jetzt
    }
    ...
}
```

`throw` = wirf jetzt. `throws` = kann passieren, Aufrufer sei gewarnt.
Ein Buchstabe Unterschied, zwei voellig verschiedene Dinge.

## 7.5 Eigene Exceptions

```java
public class UnzureichendeDeckungException extends Exception {
    private final long fehlbetrag;

    public UnzureichendeDeckungException(long fehlbetrag) {
        super("Es fehlen " + fehlbetrag + " Cent");   // Nachricht an Exception
        this.fehlbetrag = fehlbetrag;
    }

    public long getFehlbetrag() { return fehlbetrag; }
}
```

`extends Exception` -> checked. `extends RuntimeException` -> unchecked.

Gib der Exception **Daten mit**, nicht nur Text. Ein `getFehlbetrag()` kann der
Aufrufer auswerten; einen String muss er parsen.

## 7.6 Ursachen verketten (Exception Chaining)

Wenn du eine Exception in eine andere uebersetzt, **gib die Ursache mit**:

```java
try {
    return Integer.parseInt(text);
} catch (NumberFormatException e) {
    throw new IllegalStateException("Ungueltiger Konfigurationswert: " + text, e);
    //                                                                       ^ cause
}
```

Ohne das `e` verlierst du den urspruenglichen Stacktrace — und damit die
Information, wo es wirklich schiefging. Im Log erscheint dann:

```
IllegalStateException: Ungueltiger Konfigurationswert: abc
    at Konfig.lesen(Konfig.java:12)
Caused by: java.lang.NumberFormatException: For input string: "abc"
    at java.base/java.lang.Integer.parseInt(Integer.java:652)
```

`Caused by` ist oft die einzige Zeile, die dich zum echten Problem fuehrt.

## 7.7 try-with-resources

```java
// Alt und fehleranfaellig:
BufferedReader r = null;
try {
    r = new BufferedReader(new FileReader("datei.txt"));
    ...
} finally {
    if (r != null) r.close();       // und close() wirft selbst IOException...
}

// Modern:
try (BufferedReader r = new BufferedReader(new FileReader("datei.txt"))) {
    ...
}   // close() laeuft automatisch - auch bei Exception, auch bei return
```

Alles, was `AutoCloseable` implementiert, funktioniert hier. Mehrere Ressourcen
werden mit `;` getrennt und in **umgekehrter** Reihenfolge geschlossen.

Wirft sowohl der Block als auch `close()`, gewinnt die Exception aus dem Block;
die aus `close()` wird als *suppressed* angehaengt (`e.getSuppressed()`) — statt
sie, wie beim alten Muster, zu verlieren.

**Nimm try-with-resources fuer alles, was geschlossen werden muss.**

## 7.8 Anti-Muster

```java
// 1. Verschlucken - der schlimmste Fehler ueberhaupt
try { riskant(); } catch (Exception e) { }

// 2. Alles fangen, inklusive echter Bugs
try { ... } catch (Exception e) { System.out.println("Fehler"); }

// 3. Kontrollfluss ueber Exceptions
try { while (true) liste.get(i++); } catch (IndexOutOfBoundsException e) { }

// 4. printStackTrace in Produktivcode
catch (IOException e) { e.printStackTrace(); }   // landet irgendwo, niemand sieht es

// 5. Ursache wegwerfen
catch (SQLException e) { throw new RuntimeException("DB-Fehler"); }   // wo ist e?
```

Wenn du eine Exception wirklich nicht behandeln kannst, **reiche sie weiter**.
Ein leerer `catch`-Block ist ein Fehler, der auf seine Entdeckung wartet.

## 7.9 Stacktraces lesen

```
Exception in thread "main" java.lang.NullPointerException:
        Cannot invoke "String.length()" because "name" is null
    at Person.laenge(Person.java:15)      <- HIER ist es passiert
    at Kunde.pruefen(Kunde.java:33)
    at Main.main(Main.java:8)             <- hier hat es angefangen
Caused by: ...
```

Lies **von oben**: Die erste Zeile ist der Ort des Fehlers, darunter der Weg
dorthin. Suche die oberste Zeile, die zu **deinem** Code gehoert — dort
faengst du an.

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Drei Dateien in [`src/`](src/) — pruefen mit `./lerne.sh 07`.

### `Aufgaben.java`

1. **`sicherTeilen(int a, int b)`** — `a / b`, aber `0` statt einer
   `ArithmeticException`. Loese es mit `try`/`catch`, nicht mit `if`
   (auch wenn `if` hier die bessere Praxis waere — der Punkt ist die Uebung).
2. **`parseOderStandard(String text, int standard)`** — `Integer.parseInt`,
   bei `null` oder Unsinn den Standardwert.
3. **`auswerten(String[] werte, int index)`** — Element als Zahl liefern,
   `-1` bei ungueltigem Index **oder** unparsbarem Text. **Multi-Catch.**
4. **`konfigWert(String text)`** — parst; bei Fehler eine
   `IllegalStateException("Ungueltiger Konfigurationswert: abc")`
   **mit der urspruenglichen Exception als `cause`**.
5. **`ablauf(boolean fehlerWerfen)`** — gibt `"start|ok|ende"` bzw.
   `"start|fehler|ende"` zurueck. Zeigt, dass `finally` immer laeuft.
6. **`abheben(long guthaben, long betrag)`** — gibt das neue Guthaben zurueck,
   wirft bei zu wenig Deckung eine `UnzureichendeDeckungException` mit dem
   Fehlbetrag, bei `betrag <= 0` eine `IllegalArgumentException`.
7. **`protokoll(boolean fehlerWerfen)`** — nutzt `Tresor` in einem
   **try-with-resources** und liefert das Protokoll. Auch im Fehlerfall muss
   `geschlossen` im Protokoll stehen.

### `UnzureichendeDeckungException.java`

Checked Exception mit `long fehlbetrag`, Getter `getFehlbetrag()` und der
Nachricht `"Es fehlen 250 Cent"` (fuer einen Fehlbetrag von 250).

### `Tresor.java`

`implements AutoCloseable`. Der Konstruktor bekommt einen `StringBuilder` als
Protokoll und haengt `"geoeffnet|"` an, `benutzen()` haengt `"benutzt|"` an
und wirft bei geschlossenem Tresor `IllegalStateException`, `close()` haengt
`"geschlossen"` an. Ein zweiter `close()`-Aufruf tut nichts mehr (idempotent).

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
static String f() {
    try {
        return "try";
    } finally {
        System.out.print("finally ");
    }
}

System.out.println(f());
```

<details><summary>Aufloesung</summary>

`finally try` — Der Rueckgabewert `"try"` steht schon fest, trotzdem laeuft `finally` noch, **bevor** die Methode wirklich zurueckkehrt. Erst danach druckt `println` das Ergebnis.

</details>

**2.**

```java
try {
    int[] a = new int[2];
    a[2] = 1;
    System.out.print("A");
} catch (ArithmeticException e) {
    System.out.print("B");
} catch (RuntimeException e) {
    System.out.print("C");
} finally {
    System.out.print("D");
}
```

<details><summary>Aufloesung</summary>

`CD` — `a[2]` wirft eine `ArrayIndexOutOfBoundsException`. Die ist keine `ArithmeticException`, aber (ueber `IndexOutOfBoundsException`) eine `RuntimeException`. Es greift der erste passende `catch`, danach `finally`. `A` wird nie erreicht.

</details>

**3.**

```java
class R implements AutoCloseable {
    String n;
    R(String n) { this.n = n; System.out.print("auf" + n + " "); }
    public void close() { System.out.print("zu" + n + " "); }
}

try (R a = new R("1"); R b = new R("2")) {
    System.out.print("rumpf ");
}
```

<details><summary>Aufloesung</summary>

`auf1 auf2 rumpf zu2 zu1` — Geoeffnet wird in der angegebenen Reihenfolge, geschlossen in der **umgekehrten**. Das ist wichtig, wenn `b` von `a` abhaengt (etwa ein Reader auf einer Datei).

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Wann checked, wann unchecked?
- Was ist der Unterschied zwischen `throw` und `throws`?
- Warum ist ein leerer `catch`-Block gefaehrlicher als gar kein `catch`?
- Warum gehoert `e` in `new RuntimeException("...", e)`?
- In welcher Reihenfolge werden mehrere try-with-resources geschlossen?

---

**Wie geht es weiter?** Empfohlen ist jetzt [Kapitel 13 — Testen und Fehlersuche](../13-testen-und-fehlersuche/README.md):
Du kannst jetzt genug, um deine eigenen Tests zu schreiben. Danach geht es mit Kapitel 8 weiter.
