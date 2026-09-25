# Kapitel 03 — Strings und Arrays

**Ziel:** Du verstehst, warum `String` unveraenderlich ist, wann `==` bei Strings
zufaellig funktioniert (und warum du dich nie darauf verlassen darfst), und du
kannst mit ein- und zweidimensionalen Arrays arbeiten.

---

## 3.1 String ist unveraenderlich

Ein `String`-Objekt kann nach seiner Erzeugung **nie mehr geaendert** werden.
Jede Methode, die "aendert", gibt in Wahrheit ein **neues** Objekt zurueck.

```java
String s = "hallo";
s.toUpperCase();              // erzeugt "HALLO" - und wirft es weg!
System.out.println(s);        // immer noch "hallo"

s = s.toUpperCase();          // so ist es richtig
System.out.println(s);        // "HALLO"
```

Das ist der haeufigste Anfaengerfehler mit Strings. Merke: **Strings aendern
sich nicht, du zeigst nur auf einen neuen.**

Warum dieses Design? Unveraenderliche Objekte sind automatisch thread-sicher
(Kapitel 12), koennen ihren `hashCode` zwischenspeichern und sicher als
`Map`-Schluessel dienen (Kapitel 8).

## 3.2 `==` gegen `equals` — der wichtigste Absatz dieses Kapitels

```java
String a = "hallo";
String b = "hallo";
String c = new String("hallo");

a == b        // true  (!!)  - aber aus einem Grund, der dir nicht hilft, siehe unten
a == c        // false
a.equals(c)   // true  <- DAS willst du
```

`==` fragt bei Objekten: *"Sind das dieselben zwei Objekte im Speicher?"*
`equals` fragt: *"Haben sie denselben Inhalt?"*

Warum ist `a == b` dann `true`? Java haelt einen **String-Pool**: Literale, die
im Quelltext stehen, werden nur einmal angelegt und wiederverwendet. `a` und `b`
zeigen also tatsaechlich auf dasselbe Objekt. Sobald der String zur Laufzeit
entsteht — aus Nutzereingabe, aus einer Datei, per `new`, durch Verkettung mit
einer Variablen — gilt das nicht mehr. Ein Test mit zwei Literalen ist also
gruen, das echte Programm trotzdem falsch: die gemeinste Art von Fehler.

```java
String eingabe = liesVonTastatur();   // Nutzer tippt "hallo"
eingabe == "hallo"        // false !
eingabe.equals("hallo")   // true
```

**Regel ohne Ausnahme: Strings (und Objekte allgemein) mit `equals` vergleichen.**

Praktischer Kniff gegen `NullPointerException`:

```java
if ("ja".equals(antwort)) { ... }   // funktioniert auch wenn antwort null ist
if (antwort.equals("ja")) { ... }   // knallt bei antwort == null
```

Fuer Vergleiche ohne Gross-/Kleinschreibung: `equalsIgnoreCase`.

## 3.3 Wichtige String-Methoden

```java
String s = "  Hallo Welt  ";

s.length()                  // 14 (mit Leerzeichen)
s.trim()                    // "Hallo Welt"    (entfernt Whitespace aussen)
s.strip()                   // "Hallo Welt"    (wie trim, aber Unicode-korrekt)
s.isEmpty()                 // false           (Laenge == 0)
s.isBlank()                 // false           (leer oder nur Whitespace)

"Hallo".charAt(0)           // 'H'   (char, kein String!)
"Hallo".indexOf("ll")       // 2     (-1 wenn nicht gefunden)
"Hallo".contains("all")     // true
"Hallo".startsWith("Ha")    // true
"Hallo".substring(1, 3)     // "al"  (ab 1 bis VOR 3)
"Hallo".replace('l', 'L')   // "HaLLo"
"a,b,c".split(",")          // String[]{"a", "b", "c"}  (Achtung, Regex - siehe unten)
String.join("-", "a", "b")  // "a-b"
"ab".repeat(3)              // "ababab"
"Hallo".toUpperCase()       // "HALLO"
"5".compareTo("7")          // negativ (lexikografisch, nicht numerisch!)
```

`substring(von, bis)`: `von` ist **inklusiv**, `bis` ist **exklusiv**. Diese
Halboffenheit zieht sich durch die ganze Java-Bibliothek — sie sorgt dafuer,
dass `bis - von` immer die Laenge ergibt.

**`split` nimmt einen regulaeren Ausdruck**, keinen einfachen Text. Zeichen
wie `.` `|` `+` `*` `?` `(` `$` haben dort eine Sonderbedeutung:

```java
"1.2.3".split(".")      // leeres Array!  "." heisst "irgendein Zeichen"
"1.2.3".split("\\.")    // {"1", "2", "3"}  - Punkt mit \\ entwerten
"a|b".split("\\|")      // {"a", "b"}
"a  b".split("\\s+")    // {"a", "b"}  - "ein oder mehr Leerraumzeichen"
```

Ausserdem wirft `split` leere Felder **am Ende** stillschweigend weg — mehr
dazu in Kapitel 11 (`split(";", -1)`).

### Zwischen Text und Zahl umwandeln

```java
int n       = Integer.parseInt("42");       // Text -> int  (NumberFormatException bei "4x2")
double d    = Double.parseDouble("3.5");    // Punkt als Dezimaltrenner, nie Komma
String s1   = String.valueOf(42);           // Zahl -> Text
String s2   = "" + 42;                      // geht auch, ist aber ein Trick
char c      = "Hallo".charAt(1);            // 'a'
String s3   = String.valueOf(c);            // char -> String
```

### `char` ist eine Zahl

Ein `char` ist intern eine 16-Bit-Zahl (der Unicode-Wert). Deshalb geht:

```java
char c = 'a';
c + 1                  // 98   (int!)  - Rechnen macht aus char einen int
(char) (c + 1)         // 'b'
'7' - '0'              // 7    - Ziffernzeichen in Zahlwert umrechnen
Character.isDigit('7')  Character.isLetter('x')  Character.toUpperCase('x')
```

Beachte die Anfuehrungszeichen: `'a'` (einfach) ist ein `char`, `"a"` (doppelt)
ein `String`. `'ab'` ist ein Compilerfehler.

## 3.4 `StringBuilder` — wenn du oft aenderst

```java
// Schlecht bei vielen Durchlaeufen:
String s = "";
for (int i = 0; i < 10000; i++) {
    s += i;        // erzeugt 10000 neue String-Objekte -> quadratischer Aufwand
}

// Gut:
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);  // aendert einen internen Puffer -> linearer Aufwand
}
String s = sb.toString();
```

Nuetzlich: `append`, `insert`, `reverse`, `setLength(0)` (leeren), `length`.

Fuer **einzelne** Verkettungen ausserhalb von Schleifen ist `+` voellig in
Ordnung — der Compiler optimiert sie selbst. Nur in Schleifen hilft er dir nicht.

## 3.5 Textbloecke (seit Java 15)

```java
String json = """
        {
          "name": "Anna",
          "alter": 34
        }
        """;
```

Kein Escapen von Anfuehrungszeichen, keine `\n`-Kette. Die gemeinsame
Einrueckung aller Zeilen wird automatisch entfernt — die Position der
schliessenden `"""` bestimmt, wie viel.

## 3.6 Arrays

Ein Array ist eine Folge fester Laenge mit **einem** Elementtyp.

```java
int[] zahlen = new int[5];              // 5 Elemente, alle 0
int[] primzahlen = {2, 3, 5, 7, 11};    // direkt gefuellt
String[] namen = new String[3];         // 3 Elemente, alle null

zahlen[0] = 42;
int erstes = zahlen[0];
int anzahl = zahlen.length;             // Feld, keine Methode - kein ()!
```

Die Laenge steht bei der Erzeugung fest und aendert sich **nie**. Brauchst du
eine wachsende Liste, nimm `ArrayList` (Kapitel 8).

Die **Standardwerte** sind nicht zufaellig: `0` fuer Zahlen, `false` fuer
`boolean`, `null` fuer Objekte, das Nullzeichen `'\u0000'` fuer `char`.

Index von `0` bis `length - 1`. Alles andere:

```
ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
```

### Nuetzliche Helfer in `java.util.Arrays`

```java
import java.util.Arrays;

Arrays.toString(zahlen)        // "[2, 3, 5]"  <- zum Ausgeben!
Arrays.sort(zahlen)            // sortiert AN ORT UND STELLE (veraendert das Array)
Arrays.copyOf(zahlen, 10)      // Kopie mit neuer Laenge, Rest mit 0 aufgefuellt
Arrays.copyOfRange(z, 1, 3)    // Teilkopie [1, 3)
Arrays.fill(zahlen, 7)         // alles auf 7
Arrays.equals(a, b)            // inhaltlicher Vergleich
Arrays.stream(zahlen).sum()    // Kapitel 9
```

`System.out.println(zahlen)` gibt etwas wie `[I@6d06d69c` aus — das ist die
Standard-`toString` von `Object` (Typkennung + Hashcode). Fuer Arrays immer
`Arrays.toString(...)` verwenden.

### Arrays sind Referenztypen

```java
int[] a = {1, 2, 3};
int[] b = a;         // KEINE Kopie - b zeigt auf dasselbe Array
b[0] = 99;
System.out.println(a[0]);   // 99

int[] c = a.clone();        // jetzt eine echte (flache) Kopie
```

So sieht das im Speicher aus:

```
   Variablen (Stack)                  Objekte (Heap)

   a  [ o-]-----------+
                      +------->  int[] { 99, 2, 3 }
   b  [ o-]-----------+

   c  [ o-]------------------->  int[] {  1, 2, 3 }   (eigene Kopie von vorher)
```

`a` und `b` enthalten denselben **Pfeil**, nicht zwei Arrays. Wer ueber einen
Pfeil etwas aendert, aendert es fuer alle, die auf dasselbe Objekt zeigen.

Das gilt fuer alle Objekte in Java. Eine Zuweisung kopiert nie das Objekt,
nur den Verweis darauf. Male dir bei Unklarheit genau so ein Bild — das ist
kein Anfaengertrick, das machen erfahrene Entwickler auch.

### Zweidimensionale Arrays

```java
int[][] matrix = new int[3][4];        // 3 Zeilen, 4 Spalten
int[][] fest = {
    {1, 2, 3},
    {4, 5, 6}
};

matrix[1][2] = 7;                      // Zeile 1, Spalte 2
int zeilen = fest.length;              // 2
int spalten = fest[0].length;          // 3
```

Genau genommen ist `int[][]` ein "Array von Arrays" — die Zeilen duerfen
unterschiedlich lang sein (*jagged array*). Deshalb `fest[0].length` und nicht
`fest.length[1]`.

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Datei: [`src/Aufgaben.java`](src/Aufgaben.java) — pruefen mit `./lerne.sh 03`.

1. **`umdrehen(String)`** — `"abc"` -> `"cba"`. `StringBuilder` kennt eine Methode dafuer.
2. **`istPalindrom(String)`** — Gross-/Kleinschreibung und alles ausser
   Buchstaben und Ziffern ignorieren. `"Ein Esel lese nie"` -> `true`.
   Tipp: `Character.isLetterOrDigit(c)`.
3. **`wortAnzahl(String)`** — Woerter zaehlen. Mehrfache Leerzeichen und
   Rand-Whitespace duerfen nicht mitzaehlen; ein leerer Text hat 0 Woerter.
   Tipp: `strip()` und `split("\\s+")`.
4. **`maximum(int[])`** — Groesstes Element. Das Array ist garantiert nicht leer.
   *Denkfalle:* Womit initialisierst du deinen Startwert?
5. **`mittelwert(int[])`** — Arithmetisches Mittel als `double`, leeres Array -> `0.0`.
   *Denkfalle:* Ganzzahldivision (Kapitel 1) lauert hier schon wieder.
   *Zweite Denkfalle:* Die Summe zweier grosser `int`-Werte passt nicht mehr in einen `int`.
6. **`sortierteKopie(int[])`** — Aufsteigend sortiertes **neues** Array.
   Das Original muss unveraendert bleiben — genau das prueft der Test.
7. **`transponiere(int[][])`** — Zeilen und Spalten tauschen.
   `{{1,2,3},{4,5,6}}` -> `{{1,4},{2,5},{3,6}}`. Achte auf die Groesse des Ergebnisses.
8. **`zusammenfuegen(String[], String)`** — Wie `String.join`, aber selbst gebaut:
   `({"a","b","c"}, "-")` -> `"a-b-c"`. Kein Trenner am Ende!

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
String s = "Java";
s.concat("21");
System.out.println(s);
```

<details><summary>Aufloesung</summary>

`Java` — `concat` liefert einen **neuen** String und laesst `s` unveraendert. Das Ergebnis wird weggeworfen. Richtig: `s = s.concat("21");`

</details>

**2.**

```java
String a = "hal";
String b = a + "lo";
System.out.println(b == "hallo");
System.out.println(b.equals("hallo"));
```

<details><summary>Aufloesung</summary>

`false`, dann `true` — `b` entsteht zur Laufzeit aus einer Variablen und ist ein neues Objekt, nicht das Literal aus dem Pool. (Waere `a` als `final String a = "hal"` deklariert, rechnete der Compiler `a + "lo"` schon vorher aus, und `==` waere `true`. Noch ein Grund, sich nie auf `==` zu verlassen.)

</details>

**3.**

```java
int[] a = {1, 2, 3};
int[] b = a;
b[0] = 9;
a = new int[]{4, 5, 6};
System.out.println(b[0] + " " + a[0]);
```

<details><summary>Aufloesung</summary>

`9 4` — `b[0] = 9` aendert das gemeinsame Array. Danach zeigt `a` auf ein neues Array, `b` aber weiter auf das alte. Male die Pfeile aus Abschnitt 3.6 dazu.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum aendert `s.toUpperCase();` (ohne Zuweisung) nichts?
- Warum ist `a == b` bei zwei Literalen `true`, bei Nutzereingabe aber `false`?
- Warum gibt `System.out.println(intArray)` Kauderwelsch aus?
- Was ist der Unterschied zwischen `int[] b = a;` und `int[] b = a.clone();`?
- Warum ist `array.length` ohne Klammern, `string.length()` aber mit?
