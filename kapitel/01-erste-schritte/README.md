# Kapitel 01 — Erste Schritte

**Ziel:** Du verstehst, was beim Kompilieren und Starten passiert, kennst Javas
primitive Datentypen und weisst, warum `1/2` in Java `0` ergibt.

---

## 1.1 Das kleinste vollstaendige Programm

```java
public class Hallo {
    public static void main(String[] args) {
        System.out.println("Hallo, Java!");
    }
}
```

Jedes Wort hat eine Aufgabe:

| Teil | Bedeutung |
|------|-----------|
| `public` | Sichtbar von ueberall (mehr dazu in Kapitel 6) |
| `class Hallo` | Java kennt keinen Code ausserhalb von Klassen. Alles wohnt in einer Klasse. |
| `static` | Gehoert zur Klasse, nicht zu einem Objekt — die JVM kann sie aufrufen, ohne vorher ein `Hallo`-Objekt zu bauen. |
| `void` | Der Rueckgabetyp: "gibt nichts zurueck". |
| `main` | Der genaue Name, nach dem die JVM sucht. |
| `String[] args` | Kommandozeilenargumente. `java Hallo abc` fuellt `args[0] = "abc"`. |
| `System.out.println(...)` | `System` ist eine Klasse, `out` ein Feld darin, `println` eine Methode darauf. |

**Warum so umstaendlich?** Weil Java 1995 fuer grosse, langlebige Systeme
entworfen wurde, nicht fuer Einzeiler. Die Ausfuehrlichkeit ist Absicht: Der
Compiler soll dir moeglichst viel abnehmen, bevor das Programm ueberhaupt laeuft.

## 1.2 Der Weg vom Text zum laufenden Programm

```
Aufgaben.java   --javac-->   Aufgaben.class   --java-->   Ausgabe
  (Quelltext)                  (Bytecode)                 (JVM fuehrt aus)
```

Der Bytecode ist plattformunabhaengig. Dieselbe `.class`-Datei laeuft auf Linux,
Windows und macOS — die JVM uebersetzt sie zur Laufzeit in Maschinencode
(**JIT**, Just-in-Time-Compiler). Deshalb ist Java beim Start langsam und im
Dauerbetrieb schnell.

## 1.3 Variablen und Typen

Java ist **statisch typisiert**: Jede Variable hat einen Typ, der zur
Compile-Zeit feststeht und sich nie aendert.

```java
int alter = 34;              // Typ, Name, Wert
final double MWST = 0.19;    // final = kann nicht mehr geaendert werden
var name = "Gregor";         // seit Java 10: Typ wird abgeleitet -> String
```

`var` ist **keine** dynamische Typisierung. `var name = "Gregor"` erzeugt eine
Variable vom Typ `String`, endgueltig. Der Compiler tippt nur weniger.

### Die acht primitiven Typen

| Typ | Groesse | Wertebereich | Literal |
|-----|---------|--------------|---------|
| `byte` | 8 Bit | −128 … 127 | `(byte) 100` |
| `short` | 16 Bit | −32.768 … 32.767 | `(short) 1000` |
| `int` | 32 Bit | ca. ±2,1 Mrd. | `42` |
| `long` | 64 Bit | ca. ±9,2 Trillionen | `42L` |
| `float` | 32 Bit | ~7 Stellen genau | `3.14f` |
| `double` | 64 Bit | ~15 Stellen genau | `3.14` |
| `char` | 16 Bit | ein UTF-16-Zeichen (genauer: eine UTF-16-Codeeinheit) | `'A'` |
| `boolean` | — | `true` / `false` | `true` |

**Merke:** `int` und `double` sind die Standardwahl. `long` erst, wenn `int`
zu klein wird; `float` praktisch nie.

Primitive Typen sind keine Objekte. Zu jedem gibt es eine **Wrapper-Klasse**
(`Integer`, `Double`, `Boolean`, …), die man braucht, sobald ein Objekt
verlangt wird — z. B. in Collections (Kapitel 8).

```java
int primitiv = 5;
Integer objekt = 5;   // Autoboxing: automatisch verpackt
int wieder = objekt;  // Unboxing: automatisch ausgepackt
```

## 1.4 Drei Fallstricke, die jeden Anfaenger erwischen

### Ganzzahldivision

```java
int a = 1 / 2;          // 0   <- keine Nachkommastellen!
double b = 1 / 2;       // 0.0 <- zuerst int-Division, dann erst umgewandelt
double c = 1.0 / 2;     // 0.5 <- richtig: mindestens ein Operand ist double
double d = (double) 1 / 2;  // 0.5 <- explizit umgewandelt
```

Sobald **einer** der beiden Operanden eine Kommazahl ist, rechnet Java mit Kommazahlen.

Das Gegenstueck zur Ganzzahldivision ist der **Rest-Operator** `%` (Modulo):

```java
int ganz = 17 / 5;    // 3  - wie oft passt 5 in 17?
int rest = 17 % 5;    // 2  - was bleibt uebrig?  (3 * 5 + 2 = 17)
int ziffer = 1234 % 10;   // 4  - die letzte Ziffer
```

Zusammen zerlegen `/` und `%` eine Zahl in Einheiten: `125` Minuten sind
`125 / 60` = `2` Stunden und `125 % 60` = `5` Minuten. Mehr zu `%` in Kapitel 2.1.

### Ueberlauf ohne Warnung

```java
int gross = 2_000_000_000;
System.out.println(gross + gross);   // -294967296  (!!)
```

`int` laeuft still ueber. Kein Fehler, kein Hinweis — nur ein falsches Ergebnis.
Loesung: `long` verwenden und beim Literal `L` anhaengen. Willst du einen
Ueberlauf lieber als Fehler sehen, gibt es `Math.addExact(a, b)` und
`Math.multiplyExact(a, b)` — die werfen eine `ArithmeticException`, statt
still falsch zu rechnen. Die Grenzen stehen in `Integer.MAX_VALUE` und
`Integer.MIN_VALUE`.

```java
long richtig = 2_000_000_000L + 2_000_000_000L;   // 4000000000
```

Die Unterstriche in `2_000_000_000` sind reine Lesehilfe, der Compiler ignoriert sie.

### Kommazahlen sind ungenau

```java
System.out.println(0.1 + 0.2);   // 0.30000000000000004
```

Das ist kein Java-Bug, sondern der IEEE-754-Standard: `0.1` ist im Binaersystem
periodisch. Fuer Geldbetraege deshalb **niemals** `double` — nimm `BigDecimal`
oder rechne in Cent mit `long`.

## 1.5 Casting: Typen umwandeln

```java
// Erweiternd (widening) - passiert automatisch, kein Datenverlust:
int i = 42;
long l = i;
double d = l;

// Einengend (narrowing) - musst du explizit hinschreiben, Daten koennen verloren gehen:
double pi = 3.99;
int abgeschnitten = (int) pi;   // 3  <- schneidet ab, rundet NICHT
```

`(int)` schneidet immer Richtung Null ab. Zum Runden: `Math.round(3.99)` -> `4`.

Achtung: `Math.round(double)` liefert einen `long`, keinen `int`.
`int r = Math.round(3.99);` ist deshalb ein Compilerfehler
(`possible lossy conversion from long to int`) — du brauchst
`int r = (int) Math.round(3.99);`.

## 1.6 Ausgabe formatieren

```java
System.out.println("Zeile mit Umbruch");
System.out.print("ohne Umbruch");
System.out.printf("%s ist %d Jahre alt.%n", "Anna", 34);
String s = String.format("%.2f EUR", 19.999);   // "20,00 EUR" (Locale-abhaengig!)
```

Wichtige Platzhalter: `%s` String, `%d` Ganzzahl, `%f` Kommazahl,
`%.2f` zwei Nachkommastellen, `%02d` zweistellig mit fuehrender Null,
`%n` Zeilenumbruch (plattformrichtig), `%%` ein echtes Prozentzeichen.

## 1.7 Kommentare

```java
// einzeilig

/* mehrzeilig
   ueber mehrere Zeilen */

/**
 * Javadoc - wird zu HTML-Dokumentation und in der IDE angezeigt.
 * @param name der Name der begruesst wird
 * @return den fertigen Begruessungssatz
 */
```

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Datei: [`src/Aufgaben.java`](src/Aufgaben.java) — sechs Methoden mit `TODO`.

Pruefen mit:

```bash
./lerne.sh 01
```

### Einmal durchgespielt: So gehst du jede Aufgabe an

Am Beispiel von Aufgabe 1. **1. Pruefen, bevor du etwas schreibst:**

```
$ bash lerne.sh 01

  Aufgabe 1: begruessung
  ----------------------
  FEHL begruessung("Anna")
       erwartet: "Hallo, Anna!"  |  bekommen: ""
```

Das ist keine Niederlage, sondern die Aufgabenstellung: Die Methode liefert
bisher `""` (den Platzhalter), gewuenscht ist `"Hallo, Anna!"`. Den Test dazu
findest du in `tests/Tests.java`:

```java
Pruef.gleich("Hallo, Anna!", Aufgaben.begruessung("Anna"), "begruessung(\"Anna\")");
//            ^ erwartet      ^ was deine Methode liefert   ^ Name der Pruefung
```

**2. Den kleinsten Schritt schreiben.** In `src/Aufgaben.java` ersetzt du die
TODO-Zeile, etwa so:

```java
return "Hallo," + name + "!";
```

**3. Erneut pruefen und die Meldung genau lesen:**

```
  FEHL begruessung("Anna")
       erwartet: "Hallo, Anna!"  |  bekommen: "Hallo,Anna!"
```

Fast. Vergleiche Zeichen fuer Zeichen: Das Leerzeichen nach dem Komma fehlt.
Solche Kleinigkeiten sind der haeufigste Grund fuer rote Tests — die
Anfuehrungszeichen in der Ausgabe helfen dir, auch Leerzeichen am Rand zu sehen.

**4. Korrigieren, pruefen, gruen:**

```
  OK  begruessung("Anna")
  OK  begruessung("Gregor")
```

**Und wenn es gar nicht kompiliert?** Vergisst du das Semikolon, laeuft kein
einziger Test, stattdessen meldet der Compiler:

```
kapitel/01-erste-schritte/src/Aufgaben.java:17: error: ';' expected
        return "Hallo, " + name + "!"
                                     ^
```

Datei, Zeile, Art des Fehlers, und das `^` zeigt auf die Stelle. Details dazu
stehen in [`SETUP.md`](../../SETUP.md), Abschnitt 5.

Diesen Kreislauf — **pruefen, kleinsten Schritt schreiben, pruefen** — machst
du in jedem Kapitel. Nimm dir immer nur **eine** Aufgabe vor; die roten
Meldungen der anderen darfst du so lange ignorieren. Haengst du fest, helfen
die gestuften Hinweise in [`TIPPS.md`](TIPPS.md).

### Die Aufgaben

1. **`begruessung`** — Baue aus einem Namen den Satz `Hallo, Anna!`.
   Uebung im String-Verketten mit `+`.
2. **`celsiusZuFahrenheit`** — Formel: `F = C * 9/5 + 32`.
   Achtung: Wo lauert hier die Ganzzahldivision?
3. **`kreisFlaeche`** — `A = pi * r^2`. Nutze `Math.PI`. Wie quadrierst du sauber?
4. **`letzteZiffer`** — Die letzte Ziffer einer nicht-negativen Zahl (`0` ergibt `0`).
   Ein Operator genuegt.
5. **`zeitFormat`** — Aus `3661` Sekunden wird `"01:01:01"`. Die Eingabe liegt
   immer zwischen `0` und `86399` (ein Tag minus eine Sekunde).
   Ganzzahldivision und Modulo im Zusammenspiel, dann `String.format` mit `%02d`.
6. **`millisekundenProJahr`** — 365 Tage in Millisekunden.
   Hier passt das Ergebnis nicht in einen `int`. Der Rueckgabetyp ist `long` —
   aber das allein reicht nicht. Ueberlege, wo die Rechnung schiefgeht.

Zum Experimentieren gibt es [`src/Spielwiese.java`](src/Spielwiese.java):

```bash
./lerne.sh 01 -r Spielwiese
```

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
System.out.println(7 / 2 * 2.0);
```

<details><summary>Aufloesung</summary>

`6.0` — Von links nach rechts: `7 / 2` ist Ganzzahldivision und ergibt `3`, erst dann wird mit `2.0` multipliziert. Mit `7 / 2.0 * 2` kaeme `7.0` heraus.

</details>

**2.**

```java
System.out.println((int) 3.99 + (int) -3.99);
```

<details><summary>Aufloesung</summary>

`0` — `(int)` schneidet Richtung Null ab: `3 + (-3)`. Es rundet nie, auch nicht bei negativen Zahlen.

</details>

**3.**

```java
long x = 1000 * 1000 * 1000 * 10;
System.out.println(x);
```

<details><summary>Aufloesung</summary>

`1410065408` — Die Rechnung passiert komplett in `int` und laeuft ueber, *bevor* das Ergebnis in den `long` kommt. Der Zieltyp links hilft nicht. Richtig: `1000L * 1000 * 1000 * 10`. Genau das ist die Falle in Aufgabe 6.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

Kannst du diese Fragen ohne Nachschlagen beantworten?

- Warum ist `System.out.println(1/2)` gleich `0`?
- Was ist der Unterschied zwischen `javac Hallo.java` und `java Hallo`?
- Warum ist `var` keine dynamische Typisierung?
- Warum sollte man Geldbetraege nicht als `double` speichern?
- Was passiert bei `(int) -3.7`?
