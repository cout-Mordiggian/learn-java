# Kapitel 03 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `umdrehen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.4, `StringBuilder` — die Liste der nützlichen Methoden am Ende.
Frag dich: Wie kommst du von einem `String` zu einem `StringBuilder` und
wieder zurück?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`String` selbst hat keine Umdreh-Methode (unveränderlich, Abschnitt 3.1), aber
`StringBuilder` hat `reverse()`. Ein `StringBuilder` lässt sich direkt mit einem
`String` im Konstruktor erzeugen. Denkfalle: `reverse()` gibt einen
`StringBuilder` zurück, keinen `String` — du brauchst am Ende noch `toString()`,
sonst kompiliert die Rückgabe nicht. Leerer String und ein einzelnes Zeichen
funktionieren ohne Sonderfall. Das Ganze passt in eine verkettete Zeile.

</details>

## Aufgabe 2: `istPalindrom`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.3 ("`char` ist eine Zahl", `Character`-Methoden) und Abschnitt 3.4
(`StringBuilder`). Die Aufgabe hat zwei Teile. Frag dich: Wie sieht
`"Ein Esel lese nie"` aus, nachdem du alles Unwichtige entfernt hast — und wie
prüfst du dann, ob es vorwärts und rückwärts gleich ist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Schritt 1: Laufe mit `for (char c : text.toCharArray())` über alle Zeichen,
behalte nur die, für die `Character.isLetterOrDigit(c)` gilt, und hänge sie
mit `Character.toLowerCase(c)` an einen `StringBuilder` an.

Schritt 2, zwei Wege: Entweder den sauberen Text mit deinem `umdrehen()`
vergleichen — dann mit `equals` auf zwei **Strings**. Achtung: `StringBuilder`
hat kein inhaltliches `equals`, und `sb.equals(sb.reverse())` ist immer `true`,
weil `reverse()` denselben Builder verändert und zurückgibt. Oder zwei Indizes
`links`/`rechts` von aussen nach innen laufen lassen und bei der ersten
Abweichung `false` liefern. Der Test `"abca"` stellt sicher, dass du nicht nur
die äußeren Zeichen vergleichst. Der leere String ist ein Palindrom.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static boolean istPalindrom(String text) {
    StringBuilder sauber = new StringBuilder();
    for (char c : text.toCharArray()) {
        if (...) {
            sauber.append(...);
        }
    }

    int links = 0;
    int rechts = ...;
    while (links < rechts) {
        if (...) return false;
        links++;
        rechts--;
    }
    return true;
}
```

</details>

## Aufgabe 3: `wortAnzahl`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.3: `strip()`, `isEmpty()` und `split` mit regulärem Ausdruck
(`"\\s+"`). Frag dich: Was liefert `split` eigentlich, wenn der Text leer ist
oder mit Leerraum **beginnt**?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`"\\s+"` heisst "ein oder mehr Leerraumzeichen" — das deckt Leerzeichen, Tab
und Zeilenumbruch ab (Test `"a\tb\nc"`) und fasst Mehrfachabstände zusammen.
Zwei Fallen: Beginnt der Text mit Leerraum, liefert `split` vorne ein **leeres**
Element (`"  a b".split("\\s+")` ergibt `["", "a", "b"]`) — deshalb zuerst
`strip()`. Und `"".split("\\s+")` liefert nicht ein leeres Array, sondern ein
Array mit **einem** leeren String, also Länge `1`. Den Fall "nach dem Strippen
leer" musst du deshalb vorher selbst mit `0` beantworten. Die Wortzahl ist dann
die `length` des Arrays.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static int wortAnzahl(String satz) {
    String s = ...;
    if (...) return 0;
    return ...;
}
```

</details>

## Aufgabe 4: `maximum`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.6 (Zugriff per Index, `length`) und die `for`-Schleife aus Kapitel 2,
Abschnitt 2.4. Frag dich: Welchen Startwert hat dein "bisher größter Wert",
wenn im Array nur negative Zahlen stehen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Denkfalle: Startest du mit `0`, liefert `maximum({-5, -2, -9})` fälschlich `0`
— eine Zahl, die gar nicht im Array steht. Starte stattdessen mit einem Wert,
der garantiert dazugehört: dem ersten Element (die Aufgabe sichert zu, dass das
Array nicht leer ist). Dann läufst du über die restlichen Elemente und
ersetzt den Kandidaten, sobald du einen größeren findest. Die Tests prüfen
das Maximum am Anfang, am Ende und bei nur einem Element. (`Integer.MIN_VALUE`
als Startwert ginge auch.)

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static int maximum(int[] werte) {
    int max = ...;
    for (int i = ...; i < werte.length; i++) {
        if (...) max = ...;
    }
    return max;
}
```

</details>

## Aufgabe 5: `mittelwert`

<details><summary>Tipp 1 — Richtung</summary>

Kapitel 1, Abschnitt 1.4: "Ganzzahldivision" **und** "Überlauf ohne Warnung" —
beide Fallen schlagen hier zu. Frag dich: In welchem Typ sammelst du die Summe,
und in welchem Typ findet die Division statt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Summe sammeln, durch `werte.length` teilen. Drei Fallen, alle vom Test geprüft:

- **Leeres Array:** Ohne Sonderfall teilst du `0` durch `0` — je nach Typen gibt
  das eine `ArithmeticException` oder `NaN`. Erwartet ist `0.0`, also vorher
  selbst zurückgeben.
- **Ganzzahldivision:** `summe / werte.length` mit zwei Ganzzahlen ergibt bei
  `{1, 2}` den Wert `1.0` statt `1.5`. Auch `(double) (summe / laenge)` ist zu
  spät — der Cast muss auf einen **Operanden**, nicht auf das Ergebnis.
- **Überlauf:** `Integer.MAX_VALUE + Integer.MAX_VALUE` passt nicht in `int`
  (ergibt `-2`). Sammle die Summe deshalb in einem `long`.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static double mittelwert(int[] werte) {
    if (...) return 0.0;
    long summe = 0;
    for (int w : werte) ...;
    return ... / werte.length;   // wo gehoert der Cast hin?
}
```

</details>

## Aufgabe 6: `sortierteKopie`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.6: "Nützliche Helfer in `java.util.Arrays`" und "Arrays sind
Referenztypen". Frag dich: Verändert `Arrays.sort` das Array, das du ihm gibst
— und was bedeutet das für das Original des Aufrufers?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Arrays.sort` sortiert **an Ort und Stelle**. Deshalb zuerst eine echte Kopie
anlegen — mit `Arrays.copyOf(werte, werte.length)` oder `werte.clone()` — und
nur die Kopie sortieren. Denkfalle: `int[] kopie = werte;` ist **keine** Kopie,
sondern ein zweiter Verweis auf dasselbe Array; dann sortierst du doch das
Original, und der Test "Original ist unverändert" schlägt fehl. Zweite Falle:
`Arrays.sort` gibt nichts zurück (`void`), `return Arrays.sort(kopie);`
kompiliert also nicht.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static int[] sortierteKopie(int[] werte) {
    int[] kopie = ...;
    Arrays.sort(...);
    return ...;
}
```

</details>

## Aufgabe 7: `transponiere`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.6, "Zweidimensionale Arrays": `matrix.length` ist die Zeilenzahl,
`matrix[0].length` die Spaltenzahl. Frag dich: Welche Größe hat das Ergebnis
für eine 2x3-Matrix — und wo landet das Element aus Zeile `i`, Spalte `j`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Das Ergebnis hat **vertauschte Dimensionen**: so viele Zeilen, wie die Eingabe
Spalten hat, und umgekehrt. Legst du es mit denselben Dimensionen wie die
Eingabe an, bekommst du eine `ArrayIndexOutOfBoundsException`. Dann zwei
verschachtelte Schleifen über die Eingabe; beim Schreiben ins Ergebnis tauschst
du einfach die beiden Indizes. Randfall "leere Matrix": `matrix[0]` gibt es
dann nicht — der Zugriff wirft eine Exception. Prüfe `matrix.length == 0`
vorher und gib ein leeres `int[][]` zurück (z. B. `new int[0][]`).

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static int[][] transponiere(int[][] matrix) {
    if (...) return new int[0][];
    int zeilen = matrix.length;
    int spalten = ...;
    int[][] ergebnis = new int[...][...];
    for (int i = 0; i < zeilen; i++) {
        for (int j = 0; j < spalten; j++) {
            ergebnis[...][...] = matrix[i][j];
        }
    }
    return ergebnis;
}
```

</details>

## Aufgabe 8: `zusammenfuegen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 3.4 (`StringBuilder`) und die klassische `for`-Schleife mit Index
(Kapitel 2, Abschnitt 2.4). Frag dich: Bei drei Teilen gibt es wie viele
Trenner — und woran erkennst du in der Schleife, ob du gerade beim ersten
Element bist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Bei `n` Teilen gibt es `n - 1` Trenner. Am saubersten: Hänge den Trenner
**vor** jedem Element an, ausser vor dem ersten (Index `0`). Dafür brauchst du
den Index, also keine `for-each`-Schleife. Die Alternative "immer Trenner
anhängen und am Ende das letzte Zeichen abschneiden" hat zwei Fallen: Bei
leerem Array gibt es nichts abzuschneiden, und beim mehrzeichigen Trenner
`", "` entfernst du nur eines der beiden Zeichen — genau das prüft der Test.
`String.join` selbst ist hier natürlich tabu.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static String zusammenfuegen(String[] teile, String trenner) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < teile.length; i++) {
        if (...) sb.append(...);
        sb.append(...);
    }
    return sb.toString();
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum ändert `s.toUpperCase();` (ohne Zuweisung) nichts?</summary>

`String` ist unveränderlich: `toUpperCase()` verändert das vorhandene Objekt
nicht, sondern erzeugt ein **neues** `String`-Objekt mit dem Ergebnis und gibt
es zurück. Ohne Zuweisung wird dieses neue Objekt einfach weggeworfen, und `s`
zeigt weiter auf den alten Text. Richtig ist `s = s.toUpperCase();` — dann zeigt
`s` auf den neuen String. Das gilt für alle "ändernden" String-Methoden wie
`trim`, `replace` oder `strip`.

</details>

<details><summary>Warum ist `a == b` bei zwei Literalen `true`, bei Nutzereingabe aber `false`?</summary>

`==` vergleicht bei Objekten, ob beide Variablen auf **dasselbe Objekt** zeigen.
String-Literale aus dem Quelltext legt Java im String-Pool nur einmal an, also
zeigen `a = "hallo"` und `b = "hallo"` tatsächlich auf dasselbe Objekt. Ein
String, der zur Laufzeit entsteht (Eingabe, Datei, `new`, Verkettung mit einer
Variablen), ist ein eigenes, neues Objekt — gleicher Inhalt, andere Adresse,
also `false`. Deshalb immer `equals` für den Inhalt.

</details>

<details><summary>Warum gibt `System.out.println(intArray)` Kauderwelsch aus?</summary>

Arrays überschreiben `toString()` nicht, also wird die Standardversion aus
`Object` verwendet. Die liefert Typkennung plus `@` plus Hashcode in
Hexadezimal, z. B. `[I@46fbb2c1` — `[I` bedeutet "Array von `int`". Den Inhalt
bekommst du mit `Arrays.toString(intArray)`, bei zweidimensionalen Arrays mit
`Arrays.deepToString(matrix)`.

```java
System.out.println(Arrays.toString(new int[]{1, 2}));   // [1, 2]
```

</details>

<details><summary>Was ist der Unterschied zwischen `int[] b = a;` und `int[] b = a.clone();`?</summary>

`int[] b = a;` kopiert nur die **Referenz**: `a` und `b` zeigen auf dasselbe
Array, eine Änderung über `b[0] = 99` sieht man auch in `a[0]`.
`a.clone()` legt ein **neues** Array mit denselben Werten an; danach sind beide
unabhängig, und `a == b` ist `false`. Die Kopie ist flach: Bei einem Array von
Objekten (oder bei `int[][]`) werden nur die Verweise kopiert, nicht die
Objekte bzw. inneren Arrays selbst.

</details>

<details><summary>Warum ist `array.length` ohne Klammern, `string.length()` aber mit?</summary>

Bei einem Array ist `length` ein **Feld** — ein fester Wert, der bei der
Erzeugung gesetzt wird und sich nie ändert; Felder liest man ohne Klammern.
`String` ist dagegen eine normale Klasse, und `length()` ist eine **Methode**,
die die Länge aus den internen Daten des Strings ermittelt; Methoden ruft man
immer mit `()` auf. Merkhilfe: Arrays sind ein Sprachbaustein mit eigener
Syntax, `String` ist "nur" eine Klasse der Bibliothek.

</details>
