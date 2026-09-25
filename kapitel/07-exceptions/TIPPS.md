# Kapitel 07 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

> **Reihenfolge:** Aufgabe 6 braucht `UnzureichendeDeckungException`, Aufgabe 7
> braucht `Tresor`. Es lohnt sich, diese beiden Dateien (unten) vor den
> Aufgaben 6 und 7 zu bauen.

## Aufgabe 1: `sicherTeilen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.3 (`try` / `catch` / `finally`). Frag dich: Welche Exception wirft
die Ganzzahl-Division `a / b`, wenn `b` null ist? Die Hierarchie in 7.1 hilft.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Division steht im `try`-Block und wird dort direkt zurueckgegeben. Im
`catch` fuer genau diese eine Exception gibst du `0` zurueck. Fang nicht
`Exception` — nur den Typ, den du wirklich erwartest (Abschnitt 7.8).

Das Abschneiden Richtung null (`-10 / 3` ergibt `-3`) erledigt die
`int`-Division von selbst. Nebenbei: Nur die **Ganzzahl**-Division wirft; bei
`double` ergibt `10.0 / 0` einfach `Infinity`.

</details>

## Aufgabe 2: `parseOderStandard`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.3 und die Hierarchie in 7.1. Frag dich: Welche Exception wirft
`Integer.parseInt` bei `"abc"` — und welche bei `null`? Probier es in `jshell`
aus, bevor du raetst:

```java
Integer.parseInt(null)
```

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Integer.parseInt(null)` wirft **keine** `NullPointerException`, sondern eine
`NumberFormatException` mit der Meldung `Cannot parse null string`. Ein
einziger `catch`-Block fuer `NumberFormatException` deckt also `null`, `""` und
`"abc"` ab — alle drei stehen in den Tests. Eine zusaetzliche `if`-Pruefung
auf `null` ist nicht noetig.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
try {
    return ...;
} catch (... e) {
    return ...;
}
```

</details>

## Aufgabe 3: `auswerten`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.3, Unterabschnitt "Multi-Catch". Frag dich: Welche **zwei**
verschiedenen Dinge koennen in `Integer.parseInt(werte[index])` schiefgehen, und
welche Exception gehoert zu welchem?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Ein falscher Index beim Arrayzugriff wirft eine
`ArrayIndexOutOfBoundsException` — auch bei **negativem** Index, der Test prueft
`-1` ausdruecklich. Unparsbarer Text wirft eine `NumberFormatException`. Beide
faengst du in **einem** Block mit `Typ1 | Typ2 e`.

Wichtig: Auch der Arrayzugriff muss **im** `try` stehen. Holst du
`werte[index]` vorher in eine Variable, fliegt die Exception, bevor der
`try`-Block beginnt.

Die beiden Typen duerfen nicht voneinander erben —
`NumberFormatException | IllegalArgumentException` waere ein Compilerfehler,
weil der zweite den ersten schon enthaelt.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
try {
    return Integer.parseInt(...);
} catch (... | ... e) {
    return -1;
}
```

</details>

## Aufgabe 4: `konfigWert`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.6 (Ursachen verketten). Frag dich: Wie gibst du einer neuen
Exception die alte mit, damit im Stacktrace `Caused by` erscheint?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Fang die `NumberFormatException` und wirf **in** ihrem `catch`-Block eine neue
`IllegalStateException`. Sie hat einen Konstruktor `(String message, Throwable cause)` —
das zweite Argument ist die gefangene Exception.

Die Tests pruefen zwei Dinge: die Nachricht zeichengenau
(`"Ungueltiger Konfigurationswert: abc"`) und dass `getCause()` eine
`NumberFormatException` ist. Vergisst du das zweite Argument, ist `getCause()`
`null` und der zweite Test wird rot.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
try {
    return ...;
} catch (NumberFormatException e) {
    throw new IllegalStateException("Ungueltiger Konfigurationswert: " + ..., ...);
}
```

</details>

## Aufgabe 5: `ablauf`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.3, der Absatz zu `finally`. Frag dich: Welcher der drei Bloecke
laeuft in **jedem** Fall — und welcher nur im Fehlerfall?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Das Geruest steht schon als Kommentar im TODO. Im `try` wirfst du bei
`fehlerWerfen` eine `RuntimeException`; die Zeile, die `"ok|"` anhaengt, steht
**danach** — sie wird im Fehlerfall uebersprungen. Das `catch` haengt
`"fehler|"` an, das `finally` haengt `"ende"` an. Das `return sb.toString()`
bleibt hinter dem ganzen Konstrukt stehen. Achte auf die Trennzeichen: nach
`"ende"` kommt kein `|` mehr.

</details>

## Aufgabe 6: `abheben`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.4 (`throw` und `throws`) und 7.2 (checked gegen unchecked). Frag
dich: Welcher der beiden Fehlerfaelle ist ein Programmierfehler des Aufrufers,
und welcher ein erwartbarer Geschaeftsfall? Das bestimmt, welche Exception du
wirfst. (Voraussetzung: `UnzureichendeDeckungException` ist fertig.)

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `betrag <= 0` -> `IllegalArgumentException` (unchecked). Diese Pruefung kommt
  **zuerst**.
- Betrag groesser als Guthaben -> `new UnzureichendeDeckungException(fehlbetrag)`.
  Der Fehlbetrag ist das, was **fehlt**: Bei Guthaben 100 und Betrag 350 sind
  das 250, nicht 350 und nicht -250.
- Sonst das neue Guthaben zurueckgeben.

Der Test "exakt aufgebraucht" (500 von 500) muss `0` liefern, nicht werfen —
die Deckung fehlt erst, wenn der Betrag **groesser** ist.

Das `throws UnzureichendeDeckungException` steht schon in der Signatur; ohne es
wuerde der Compiler das `throw` der checked Exception ablehnen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
if (...) {
    throw new IllegalArgumentException("...");
}
if (...) {
    throw new UnzureichendeDeckungException(... - ...);
}
return ...;
```

</details>

## Aufgabe 7: `protokoll`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.7 (try-with-resources). Frag dich: Was muss eine Klasse
implementieren, damit sie in den Klammern von `try (...)` stehen darf — und wann
genau wird dann `close()` aufgerufen? (Voraussetzung: `Tresor` ist fertig.)

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Das Geruest steht als Kommentar im TODO — du musst es nur einkommentieren. Der
Punkt der Aufgabe ist das Verstehen: Der Tresor wird **in** den runden Klammern
erzeugt, und `close()` laeuft automatisch am Ende des Blocks, **bevor** das
`catch` ausgefuehrt wird. Deshalb steht auch im Fehlerfall `geschlossen` im
Protokoll, obwohl nirgends `close()` im Code steht.

Die Variable `t` ist nur innerhalb des `try`-Blocks sichtbar; das Protokoll
liest du am Ende ueber `log`. Wenn der Test hier rot ist, liegt es fast immer
an `Tresor` (fehlendes `"geoeffnet|"`, `"benutzt|"` oder `"geschlossen"`).

</details>

## `UnzureichendeDeckungException.java`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.5 (Eigene Exceptions) zeigt diese Klasse. Frag dich: Wer fuellt
eigentlich `getMessage()` — und wie kommt deine Nachricht dorthin?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`extends Exception` steht schon da und macht sie **checked**. Die Nachricht
gibst du per `super(...)` an `Exception` weiter; `getMessage()` musst du nicht
selbst schreiben. `super(...)` muss (in Java 21) die erste Anweisung sein, also
baust du die Nachricht direkt aus dem **Parameter** `fehlbetrag` zusammen, nicht
aus dem Feld — das ist in dem Moment noch gar nicht gesetzt.

Danach setzt du das Feld `private final long fehlbetrag`, und `getFehlbetrag()`
gibt es zurueck. Die Nachricht muss zeichengenau `"Es fehlen 250 Cent"` lauten.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
private final long fehlbetrag;

public UnzureichendeDeckungException(long fehlbetrag) {
    super("Es fehlen " + ... + " Cent");
    this.fehlbetrag = ...;
}
```

</details>

## `Tresor.java`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 7.7 (`AutoCloseable`) und 7.2 ("Objekt im falschen Zustand"). Frag
dich: Welchen Zustand muss sich der Tresor merken, damit `benutzen()` und ein
zweites `close()` richtig reagieren koennen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zwei Felder: der uebergebene `StringBuilder` (`final`) und ein `boolean offen`.

- Konstruktor: Feld setzen, `offen = true`, `"geoeffnet|"` anhaengen.
- `benutzen()`: ist der Tresor zu, `IllegalStateException` — es liegt am
  **Zustand** des Objekts, nicht an einem Argument. Sonst `"benutzt|"` anhaengen.
- `close()`: ist der Tresor schon zu, sofort zurueckkehren. Sonst `offen = false`
  und `"geschlossen"` (ohne `|`) anhaengen.

**Die Falle der Tests:** Der Test ruft `close()` zweimal auf und erwartet
danach genau `"geoeffnet|geschlossen"`. Ohne die Pruefung am Anfang von
`close()` steht `geschlossen` doppelt im Protokoll.

`close()` braucht kein `throws`: Eine Ueberschreibung darf weniger Exceptions
deklarieren als `AutoCloseable.close()` — das erspart jedem Aufrufer ein `catch`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
private final StringBuilder protokoll;
private boolean offen;

public void benutzen() {
    if (...) {
        throw new IllegalStateException("...");
    }
    protokoll.append(...);
}

@Override
public void close() {
    if (...) {
        return;
    }
    offen = ...;
    protokoll.append(...);
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Wann checked, wann unchecked?</summary>

**Checked** (von `Exception`, aber nicht von `RuntimeException` abgeleitet) nimmst
du fuer erwartbare Probleme der Aussenwelt, auf die der Aufrufer sinnvoll
reagieren kann: Datei fehlt, Netzwerk weg, zu wenig Deckung. Der Compiler zwingt
ihn dann zum Fangen oder Weiterreichen. **Unchecked** (`RuntimeException` und
Unterklassen) steht fuer Programmierfehler: ungueltiges Argument
(`IllegalArgumentException`), falscher Objektzustand (`IllegalStateException`),
`null`, wo keins sein darf. Faustregel: Kann der Aufrufer es sinnvoll behandeln —
checked. Ist es ein Bug — unchecked. Und checked Exceptions sparsam einsetzen.

</details>

<details><summary>Was ist der Unterschied zwischen <code>throw</code> und <code>throws</code>?</summary>

`throw` ist eine **Anweisung** im Methodenrumpf und wirft jetzt, in diesem
Moment, ein konkretes Exception-Objekt: `throw new IllegalArgumentException("...");`.
`throws` steht in der **Signatur** und ist eine Deklaration: "Diese Methode
kann diese Exception werfen, Aufrufer sei gewarnt." Fuer checked Exceptions ist
`throws` Pflicht, wenn du sie nicht selbst faengst; fuer unchecked ist es
erlaubt, aber unueblich.

</details>

<details><summary>Warum ist ein leerer <code>catch</code>-Block gefaehrlicher als gar kein <code>catch</code>?</summary>

Ohne `catch` fliegt die Exception weiter nach oben und bricht das Programm im
schlimmsten Fall mit einem Stacktrace ab — laut, aber du siehst sofort, was und
wo es passiert ist. Ein leerer `catch`-Block verschluckt den Fehler still: Das
Programm laeuft mit einem womoeglich kaputten Zustand weiter, und der Folgefehler
taucht irgendwann ganz woanders auf, ohne jeden Hinweis auf die Ursache. Kannst
du eine Exception nicht sinnvoll behandeln, reich sie weiter.

</details>

<details><summary>Warum gehoert <code>e</code> in <code>new RuntimeException("...", e)</code>?</summary>

Das zweite Argument ist die **Ursache** (cause). Mit ihr haengt die neue
Exception die alte samt ihrem Stacktrace an, und im Log erscheint die Zeile
`Caused by: ...` mit dem Ort, an dem es wirklich schiefging. Ohne `e` siehst du
nur die neue, allgemeine Meldung und die Stelle, an der uebersetzt wurde — die
eigentliche Fehlerquelle ist verloren. Abfragen kannst du sie spaeter mit
`getCause()`.

</details>

<details><summary>In welcher Reihenfolge werden mehrere try-with-resources geschlossen?</summary>

In **umgekehrter** Reihenfolge der Deklaration: Bei `try (A a = ...; B b = ...)`
wird zuerst `b`, dann `a` geschlossen. Das ist sinnvoll, weil eine spaeter
geoeffnete Ressource oft von einer frueheren abhaengt (etwa ein Reader, der auf
einem Stream aufsetzt). Wirft neben dem Block auch ein `close()`, gewinnt die
Exception aus dem Block, und die aus `close()` haengt als *suppressed* daran.

</details>
