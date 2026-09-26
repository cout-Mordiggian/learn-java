# Kapitel 05 — OOP I: Klassen und Objekte

**Ziel:** Du entwirfst eigene Typen: mit Feldern, Konstruktoren, Kapselung und
den drei Methoden, die jedes Objekt von `Object` erbt.

---

## 5.1 Klasse und Objekt

Eine **Klasse** ist der Bauplan, ein **Objekt** (Instanz) das gebaute Ding.

```java
public class Hund {
    String name;          // Feld (Instanzvariable)
    int alter;

    void bellen() {       // Instanzmethode
        System.out.println(name + " sagt Wuff");
    }
}

Hund rex = new Hund();    // Objekt erzeugen
rex.name = "Rex";
rex.bellen();             // "Rex sagt Wuff"
```

```
   Stack                     Heap

   rex [ o-]-------->  +-------------------+
                       | Hund              |
                       |   name  = o------------>  "Rex"
                       |   alter = 0       |
                       +-------------------+
```

Beachte: Auch das Feld `name` enthält nur einen Pfeil auf ein `String`-Objekt.
`int`-Felder dagegen enthalten den Wert direkt.

`new` tut drei Dinge: Speicher auf dem **Heap** reservieren, Felder mit
Standardwerten füllen (`0`, `false`, `null`), den Konstruktor ausführen.
Die (lokale) Variable `rex` liegt dagegen auf dem **Stack** und enthält nur
die Referenz — sozusagen die Adresse des Objekts, nicht das Objekt selbst.

## 5.2 Konstruktoren

```java
public class Hund {
    String name;
    int alter;

    public Hund(String name, int alter) {
        this.name = name;      // this.name = Feld, name = Parameter
        this.alter = alter;
    }

    public Hund(String name) {
        this(name, 0);         // ruft den anderen Konstruktor auf - MUSS erste Zeile sein
    }
}
```

> *Seit Java 25* dürfen vor `this(...)` bzw. `super(...)` einfache Anweisungen
> stehen, die das Objekt noch nicht benutzen — etwa eine Prüfung der
> Parameter. In Java 21 ist `this(...)` zwingend die erste Anweisung.

Merkmale: heisst wie die Klasse, hat **keinen** Rückgabetyp (auch nicht `void`).

`this` ist die Referenz auf das aktuelle Objekt. Man braucht es, wenn Parameter
und Feld gleich heissen (was guter Stil ist — der Name ist ja derselbe Begriff).

**Der Standardkonstruktor:** Schreibst du gar keinen Konstruktor, erzeugt der
Compiler einen parameterlosen. Sobald du **einen** eigenen schreibst,
verschwindet er. `new Hund()` ist dann ein Compilerfehler.

### Konstruktoren sollen gültige Objekte garantieren

```java
public Konto(String inhaber, long startguthaben) {
    if (inhaber == null || inhaber.isBlank()) {
        throw new IllegalArgumentException("Inhaber fehlt");
    }
    if (startguthaben < 0) {
        throw new IllegalArgumentException("Startguthaben negativ");
    }
    this.inhaber = inhaber;
    this.guthaben = startguthaben;
}
```

Wenn der Konstruktor durchläuft, ist das Objekt gültig — diese Zusage nennt
man **Invariante**. Sie ist der eigentliche Zweck von Kapselung.

(Die `throw`-Syntax lernst du in Kapitel 7 richtig. Für jetzt reicht: Diese
Zeile bricht die Methode mit einem Fehler ab.)

## 5.3 Kapselung

```java
public class Konto {
    private long guthaben;                 // von aussen unsichtbar

    public long getGuthaben() {            // lesender Zugriff
        return guthaben;
    }

    public void einzahlen(long betrag) {   // schreibender Zugriff MIT Regeln
        if (betrag <= 0) throw new IllegalArgumentException("Betrag muss positiv sein");
        guthaben += betrag;
    }
}
```

Warum nicht einfach `public long guthaben`? Weil dann jeder `konto.guthaben = -5000`
schreiben könnte und deine Invariante wertlos wäre. Der eigentliche Gewinn ist
nicht "Getter/Setter", sondern **Kontrolle darüber, wie sich der Zustand ändert**.

Beachte: `einzahlen` ist ein besserer Name als `setGuthaben`. Modelliere die
**Fachlichkeit**, nicht die Felder. Ein blinder Setter für jedes Feld ist
Kapselung nur dem Namen nach.

### Sichtbarkeiten (Vollständig in Kapitel 6)

| Modifier | Sichtbar in |
|----------|-------------|
| `private` | nur in derselben Klasse |
| (nichts) | im selben Paket ("package-private") |
| `protected` | Paket + Unterklassen |
| `public` | überall |

**Faustregel:** Felder `private`, Methoden so eng wie möglich.

## 5.4 `static` — geteilt von allen Instanzen

```java
public class Konto {
    private static int anzahlKonten = 0;   // EIN Wert fuer alle Objekte
    private final int nummer;              // eigener Wert pro Objekt

    public Konto() {
        anzahlKonten++;
        this.nummer = anzahlKonten;
    }

    public static int getAnzahlKonten() {  // ohne Objekt aufrufbar
        return anzahlKonten;
    }
}

Konto.getAnzahlKonten();   // an der Klasse, nicht am Objekt
```

Eine `static`-Methode hat **kein** `this` und kann deshalb nicht auf
Instanzfelder zugreifen — ein häufiger Compilerfehler:
`non-static variable x cannot be referenced from a static context`.

**Konstanten:**

```java
public static final double MWST = 0.19;   // static final, GROSS_MIT_UNTERSTRICH
```

## 5.5 `final` bei Feldern

```java
private final String name;   // muss im Konstruktor gesetzt werden, danach nie wieder
```

`final` heisst: Die **Referenz** ist unveränderlich, nicht das Objekt dahinter.

```java
private final List<String> namen = new ArrayList<>();
namen.add("Anna");           // erlaubt! die Liste selbst ist veraenderlich
namen = new ArrayList<>();   // Compilerfehler
```

Mach Felder standardmäßig `final` und lockere nur, wo nötig. Der Compiler
erinnert dich dann daran, welcher Zustand sich wirklich ändert.

## 5.6 Die drei Methoden von `Object`

Jede Klasse in Java erbt automatisch von `Object` und damit u. a. diese drei.

### `toString()` — lesbare Darstellung

```java
@Override
public String toString() {
    return "Konto[nummer=" + nummer + ", guthaben=" + guthaben + "]";
}
```

Ohne Überschreibung bekommst du `Konto@1b6d3586`. `System.out.println(objekt)`
und String-Verkettung rufen `toString()` automatisch auf.

`@Override` ist eine Annotation. Sie ist optional, aber **schreib sie immer**:
Der Compiler prüft dann, ob du wirklich etwas überschreibst. Ein Tippfehler
(`toStrng`) fällt so sofort auf statt erst im Betrieb.

### `equals(Object)` — inhaltliche Gleichheit

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;                       // gleiche Referenz -> fertig
    if (!(o instanceof Punkt p)) return false;        // null und falscher Typ raus
    return Double.compare(x, p.x) == 0                // Felder vergleichen
        && Double.compare(y, p.y) == 0;
}
```

Warum `Double.compare` statt `==`? Bei `double` hat `==` zwei Ecken:
`Double.NaN == Double.NaN` ist `false` (zwei Punkte mit `NaN` wären nie gleich),
und `0.0 == -0.0` ist `true`, obwohl `Objects.hash` für beide verschiedene
Hashwerte liefert — damit wäre der `hashCode`-Vertrag (unten) gebrochen.
Für `int`, `long`, `char`, `boolean` ist `==` völlig in Ordnung; für
Objekt-Felder nimmst du `Objects.equals(a, b)` (null-sicher).

Der Parametertyp ist `Object` — nicht `Punkt`. Schreibst du
`public boolean equals(Punkt p)`, hast du **überladen** statt überschrieben,
und Collections nutzen weiter die falsche Methode. Genau davor schützt `@Override`.

Das `instanceof Punkt p` mit Variablenname ist *Pattern Matching* (Java 16+):
prüfen und casten in einem Schritt.

### `hashCode()` — und der Vertrag

```java
@Override
public int hashCode() {
    return Objects.hash(x, y);
}
```

**Der Vertrag:** Sind zwei Objekte `equals`, **müssen** sie denselben
`hashCode` haben. (Umgekehrt nicht — Kollisionen sind erlaubt.)

Wer `equals` überschreibt und `hashCode` vergisst, baut einen Fehler, der
erst in Kapitel 8 sichtbar wird: Das Objekt verschwindet in einer `HashMap`
oder taucht doppelt in einem `HashSet` auf. Deshalb: **immer beide zusammen.**

## 5.7 `null` und die NullPointerException

`null` heisst "diese Referenz zeigt auf nichts". Jeder Methodenaufruf darauf:

```
Exception in thread "main" java.lang.NullPointerException:
    Cannot invoke "String.length()" because "name" is null
```

Seit Java 14 sagen die Meldungen sehr genau, *welche* Referenz `null` war —
lies sie, sie ersparen dir das Raten. (Steht dort `"<local1>"` statt eines
Namens, wurde ohne Debug-Infos kompiliert; `javac -g` behebt das. `lerne.sh`
macht das bereits für dich.)

Strategien:

- Felder im Konstruktor prüfen: `Objects.requireNonNull(name, "name")`
- Nie `null` zurückgeben, wo eine leere Liste oder ein leerer String reicht
- `Optional` für "vielleicht kein Wert" (Kapitel 9)

## 5.8 Unveränderliche Objekte

```java
public final class Punkt {
    private final double x;
    private final double y;

    public Punkt(double x, double y) { this.x = x; this.y = y; }

    public Punkt verschoben(double dx, double dy) {
        return new Punkt(x + dx, y + dy);   // neues Objekt statt Aenderung
    }
}
```

Vorteile: kein ungültiger Zwischenzustand, sicher als `Map`-Schlüssel,
automatisch thread-sicher. Java geht diesen Weg selbst bei `String`, `Integer`
und der ganzen `java.time`-API.

In Kapitel 10 siehst du, dass `record` genau diese Klasse in einer Zeile schreibt.

## 5.9 Objektlebenszyklus

Objekte werden mit `new` erzeugt und vom **Garbage Collector** wieder
eingesammelt, sobald keine Referenz mehr auf sie zeigt. Du gibst nichts
manuell frei. `finalize()` ist veraltet — nicht benutzen. Für Ressourcen
(Dateien, Verbindungen) gibt es try-with-resources (Kapitel 7).

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Zwei Klassen, beide in [`src/`](src/) — prüfen mit `./lerne.sh 05`.

### `Konto.java` — Kapselung und Invarianten

Ein Bankkonto. **Beträge werden in Cent als `long` geführt** — nie in `double`
(Kapitel 1: Kommazahlen sind ungenau, und Geld verträgt keine Rundungsfehler).

Zu bauen:

- Felder: `nummer` (`final int`), `inhaber` (`final String`), `guthaben` (`long`)
- Ein statischer Zähler, der jedem Konto eine fortlaufende Nummer ab 1 gibt
- Konstruktor `Konto(String inhaber, long startguthaben)`, der fehlende (`null`),
  leere oder nur aus Leerzeichen bestehende Inhaber (`isBlank()`) und negatives
  Startguthaben mit `IllegalArgumentException` ablehnt. Ein abgelehnter Aufruf
  verbraucht **keine** Kontonummer.
- Getter für alle drei Felder — aber **keinen** Setter für `guthaben`
- `einzahlen(long betrag)` — nur positive Beträge, sonst `IllegalArgumentException`
- `abheben(long betrag)` — gibt `boolean` zurück: `false` bei zu wenig Deckung,
  `IllegalArgumentException` bei nicht-positivem Betrag. Das komplette Guthaben
  abzuheben ist erlaubt.
- `ueberweiseAn(Konto ziel, long betrag)` — `boolean`; nur wenn das Abheben klappt
- `toString()` -> `Konto[1, Anna, 5000 Cent]`
- `static int getAnzahlKonten()`

*Entwurfsfrage:* Warum gibt `abheben` einen `boolean` zurück, während ein
negativer Betrag eine Exception wirft? Weil "kein Geld da" ein normaler
Geschäftsfall ist, "minus 5 Euro abheben" aber ein Programmierfehler.
Diese Unterscheidung vertiefst du in Kapitel 7.

### `Punkt.java` — Unveränderlichkeit und Objektidentität

- `final class Punkt` mit `private final double x, y`
- Konstruktor, `getX()`, `getY()`
- `abstand(Punkt anderer)` — euklidisch, `Math.hypot` oder `Math.sqrt`
- `abstandZumUrsprung()`
- `verschoben(double dx, double dy)` — gibt einen **neuen** Punkt zurück
- `toString()` -> `Punkt(1.0, 2.0)`
- `equals` und `hashCode` — vollständig und vertragstreu. Die Tests prüfen
  auch die Ecken `0.0`/`-0.0` und `NaN` (siehe 5.6: `Double.compare` statt `==`).

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
class Z {
    static int s = 0;
    int i = 0;
    Z() { s++; i++; }
}

Z a = new Z();
Z b = new Z();
System.out.println(Z.s + " " + a.i);
```

<details><summary>Auflösung</summary>

`2 1` — `s` gibt es nur **einmal** für die Klasse, beide Konstruktoraufrufe erhöhen dasselbe Feld. `i` hat jedes Objekt für sich.

</details>

**2.**

```java
class P {
    int x;
    P(int x) { this.x = x; }
}

System.out.println(new P(1).equals(new P(1)));
```

<details><summary>Auflösung</summary>

`false` — Ohne überschriebenes `equals` gilt das geerbte aus `Object`, und das vergleicht nur die Referenz, also dasselbe wie `==`. Zwei `new` sind zwei Objekte.

</details>

**3.**

```java
final List<String> l = new ArrayList<>();
l.add("a");
l.add("b");
System.out.println(l.size());
```

<details><summary>Auflösung</summary>

`2` — `final` verbietet nur, `l` auf eine andere Liste zeigen zu lassen. Die Liste selbst bleibt veränderbar (Abschnitt 5.5).

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Was passiert mit dem Standardkonstruktor, sobald du selbst einen schreibst?
- Warum ist `einzahlen(long)` besser als `setGuthaben(long)`?
- Warum darf eine `static`-Methode nicht auf Instanzfelder zugreifen?
- Was geht kaputt, wenn du `equals` ohne `hashCode` überschreibst?
- Warum ist `private final List<X> liste` trotzdem veränderbar?
