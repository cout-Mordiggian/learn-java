# Kapitel 14 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `insertionSort`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.4, das Kartenbild. Spiel es mit `{5, 3, 8, 1, 4}` auf Papier durch.
Frag dich: Welche Aussage gilt fuer den Teil links vom Strich vor jedem Schritt,
und was muss ein Schritt tun, damit sie danach auch fuer einen Platz mehr gilt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Aussen eine Schleife `i` von 1 bis `a.length - 1`. Merk dir `a[i]` in einer
Variablen, denn der Platz wird gleich ueberschrieben. Innen laeuft ein zweiter
Index `j` von `i - 1` nach **links**, solange `j >= 0` **und** `a[j]` groesser
als der gemerkte Wert ist. Dabei rueckt jedes dieser Elemente einen Platz nach
rechts. Danach steht die Luecke bei `j + 1`.

Die Reihenfolge der Bedingungen zaehlt: Erst `j >= 0` pruefen, dann `a[j]`
anfassen, sonst gibt es bei `j == -1` eine `ArrayIndexOutOfBoundsException`.
`&&` wertet von links aus und bricht frueh ab (Kapitel 2).

Kein `return` eines neuen Arrays: Der Test schaut auf das Array, das er dir
gegeben hat. `a = ...` in der Methode haette nach aussen keine Wirkung (4.2).

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
for (int i = 1; i < a.length; i++) {
    int aktuell = a[i];
    int j = i - 1;
    while (j >= 0 && ...) {
        a[j + 1] = ...;
        j--;
    }
    a[...] = aktuell;
}
```

</details>

## Aufgabe 2: `mergeSort` und `merge`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.4, das Merge-Sort-Bild, und Kapitel 4.4 (Basisfall plus Schritt,
der kleiner wird). Loese zuerst nur `merge` und teste sie gedanklich mit
`{1, 5}` und `{2, 3, 9}`. Frag dich: Welche drei Positionen musst du dir
waehrend des Mischens merken?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`mergeSort`: Ist `a.length <= 1`, gib `a.clone()` zurueck, nicht `a` selbst (der
Test prueft, dass ein **neues** Array kommt). Sonst `mitte = a.length / 2` und die
beiden Haelften mit `Arrays.copyOfRange(a, 0, mitte)` und
`Arrays.copyOfRange(a, mitte, a.length)` kopieren. Das `bis` ist dort exklusiv.
Beide Haelften rekursiv sortieren und das Ergebnis von `merge` zurueckgeben. Weil
du nur Kopien anfasst, bleibt das Original automatisch unveraendert.

`merge`: Ergebnis-Array mit Laenge `links.length + rechts.length`, drei Indizes
`i`, `j`, `k`, alle bei 0. Solange **beide** Seiten noch Elemente haben, das
kleinere uebernehmen und seinen Index sowie `k` erhoehen. Danach ist eine Seite
leer, der Rest der anderen wird angehaengt. Denk an **beide** Reste. Mit `<=`
statt `<` beim Vergleich wird es stabil.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static int[] mergeSort(int[] a) {
    if (a.length <= 1) return ...;
    int mitte = ...;
    int[] links = mergeSort(Arrays.copyOfRange(a, ..., ...));
    int[] rechts = mergeSort(Arrays.copyOfRange(a, ..., ...));
    return merge(links, rechts);
}

private static int[] merge(int[] links, int[] rechts) {
    int[] ergebnis = new int[...];
    int i = 0, j = 0, k = 0;
    while (i < links.length && j < rechts.length) {
        if (links[i] <= rechts[j]) ergebnis[k++] = links[i++];
        else ...;
    }
    while (...) ergebnis[k++] = links[i++];
    while (...) ...;
    return ergebnis;
}
```

</details>

## Aufgabe 3: `istSortiert`

<details><summary>Tipp 1 — Richtung</summary>

Was muss fuer **jedes** Paar direkter Nachbarn gelten, damit ein Array
aufsteigend sortiert ist? Und wie viele solche Paare hat ein Array mit 0 oder 1
Element?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Eine Schleife ueber die Paare `(a[i - 1], a[i])` fuer `i` ab **1**. Ist das linke
Element groesser als das rechte, steht fest: nicht sortiert, sofort `false`. Erst
nach der Schleife `true`. Gleiche Nachbarn sind erlaubt, also `>` und nicht `>=`.
Die Tests pruefen einen Fehler am Anfang, in der Mitte und am Ende, deine
Grenzen muessen also stimmen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
for (int i = 1; i < ...; i++) {
    if (...) return false;
}
return true;
```

</details>

## Aufgabe 4: `ersteGroesserGleich`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.3, der Absatz ueber die Grenze und die Invariante. Frag dich: Wenn
`sortiert[mitte] < wert` ist, kann `mitte` dann die Antwort sein? Und wenn
`sortiert[mitte] >= wert` ist, kann `mitte` die Antwort sein, und kann es
eine Stelle weiter links noch eine bessere geben?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Halboffener Bereich: `von = 0`, `bis = sortiert.length`. Invariante: Links von
`von` ist alles `< wert`, ab `bis` alles `>= wert`. Solange `von < bis`:
`mitte = von + (bis - von) / 2`. Ist `sortiert[mitte] < wert`, gehoert `mitte`
nach links, also `von = mitte + 1`. Sonst koennte `mitte` die Antwort sein, also
`bis = mitte` (**nicht** `mitte - 1`, sonst verlierst du sie).

Bei einem Treffer **nicht** abbrechen: Bei `{1, 3, 3, 3, 5}` trifft die erste
`mitte` die mittlere 3, gesucht ist aber die erste. Am Ende ist `von == bis`,
und das ist das Ergebnis, auch fuer das leere Array und "nichts ist gross genug".

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
int von = 0;
int bis = sortiert.length;
while (von < bis) {
    int mitte = von + (bis - von) / 2;
    if (sortiert[mitte] < wert) {
        von = ...;
    } else {
        bis = ...;
    }
}
return ...;
```

</details>

## Aufgabe 5: `zaehleVorkommen`

<details><summary>Tipp 1 — Richtung</summary>

In einem sortierten Array stehen alle Kopien eines Werts direkt hintereinander.
Frag dich: Wenn du weisst, wo der Block **beginnt** und wo der naechstgroessere
Wert beginnt, wie gross ist dann der Block?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`ersteGroesserGleich(sortiert, wert)` liefert den Anfang des Blocks.
`ersteGroesserGleich(sortiert, wert + 1)` liefert die erste Stelle **hinter** dem
Block, denn bei `int` gibt es zwischen `wert` und `wert + 1` nichts. Die
Differenz ist die Anzahl, auch 0, wenn der Wert fehlt.

Der Sonderfall: Ist `wert == Integer.MAX_VALUE`, laeuft `wert + 1` ueber und wird
zu `Integer.MIN_VALUE` (Kapitel 1). Groesser als `MAX_VALUE` kann aber nichts sein.
Wo endet der Block dann?

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
int anfang = ersteGroesserGleich(sortiert, wert);
int ende = (wert == Integer.MAX_VALUE) ? ... : ersteGroesserGleich(sortiert, ...);
return ...;
```

</details>

## Aufgabe 6: `MeineListe` — `add`, `get`, `size`, `toString`, Wachstum

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.5, das Bild mit `elemente` und `groesse`. Die zwei Felder sind schon
da. Frag dich fuer jede Methode: Was muss mit `groesse` passieren, und welche
Plaetze des Arrays sind gerade gueltig?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `size()` gibt `groesse` zurueck, **nicht** `elemente.length`.
- `add`: Ist `groesse == elemente.length`, erst wachsen: ein neues Array der
  doppelten Laenge anlegen, alle alten Elemente hineinkopieren und das Feld
  `elemente` auf das neue Array zeigen lassen. `Arrays.copyOf(elemente, neueLaenge)`
  erledigt die beiden ersten Schritte in einem. Dann `elemente[groesse] = element`
  und `groesse++`.
- `get`: Zuerst den Index pruefen. Gueltig sind nur `0` bis `groesse - 1`. Das
  Array selbst hilft dir dabei nicht, denn bei `groesse` 1 und Kapazitaet 4 gibt es
  `elemente[1]` durchaus. Schreib eine private Methode `pruefeIndex`, die
  `throw new IndexOutOfBoundsException(...)` ausfuehrt, und benutze sie auch in
  `set` und `remove`.
- `toString`: `StringBuilder`, `"["`, dann die Elemente `0..groesse-1` mit `", "`
  dazwischen, dann `"]"`. `append(Object)` schreibt fuer `null` von selbst "null".

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public void add(E element) {
    if (groesse == elemente.length) {
        elemente = Arrays.copyOf(elemente, ...);   // import java.util.Arrays
    }
    elemente[...] = element;
    groesse++;
}

private void pruefeIndex(int index) {
    if (index < 0 || index >= ...) {
        throw new IndexOutOfBoundsException("Index " + index + ", Groesse " + groesse);
    }
}
```

</details>

## Aufgabe 6: `MeineListe` — `set`, `remove`, `contains`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.5, `remove(0)` ist O(n). Zeichne `[a | b | c | d]` auf und streiche
`b`. Frag dich: Welche Elemente muessen wohin, und was steht danach auf dem
letzten, jetzt ungueltigen Platz? Fuer `contains` hilft Kapitel 3 (`==` gegen
`equals`) und 8.9 (`Objects.equals`).

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `set`: Index pruefen, alten Wert merken, ueberschreiben, alten Wert zurueckgeben.
- `remove`: Index pruefen und das Element merken. Dann in einer Schleife von
  `index` bis `groesse - 2` jeweils `elemente[i] = elemente[i + 1]` setzen, also
  von **links nach rechts**, sonst ueberschreibst du, was du noch brauchst.
  Danach `groesse--` und `elemente[groesse] = null`, damit keine alte Referenz
  liegen bleibt.
- `contains`: nur bis `groesse` suchen, nicht bis `elemente.length`, dahinter
  stehen `null`s. Mit `==` schlaegt der Test mit `new String("Anna")` fehl, und
  bei `Integer` ueber 127 ebenso (8.3). `o.equals(...)` scheitert an `o == null`.
  `Objects.equals(o, elemente[i])` erledigt beides.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public E remove(int index) {
    pruefeIndex(index);
    E entfernt = get(index);
    for (int i = index; i < groesse - 1; i++) {
        elemente[i] = ...;
    }
    groesse--;
    elemente[...] = null;
    return entfernt;
}

public boolean contains(Object o) {
    for (int i = 0; i < ...; i++) {
        if (Objects.equals(..., ...)) return true;   // import java.util.Objects
    }
    return false;
}
```

</details>

## Aufgabe 7: `Stapel`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.6, das Bild mit `oben --> [3|o]--> [2|o]--> [1|null]`. Zeichne auf,
was sich bei `push(4)` und bei `pop()` an den Pfeilen aendert. Es ist jeweils
genau **ein** Pfeil, naemlich `oben`.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `push`: ein neuer `Knoten` mit dem Wert und dem **bisherigen** `oben` als
  `naechster`. Dann zeigt `oben` auf den neuen Knoten, `anzahl++`. Bei leerem
  Stapel ist `oben` gerade `null`, und das passt genau.
- `peek`: Ist `oben == null`, wirf `new NoSuchElementException("...")`. Sonst
  `oben.wert`.
- `pop`: Wert holen, dafuer kannst du `peek()` wiederverwenden, dann wirft es
  schon bei leerem Stapel. Danach `oben = oben.naechster`, `anzahl--`, Wert zurueck.
- `istLeer`: `oben == null`. `groesse`: `anzahl`.

Die Felder des Knotens sind `final`. Du aenderst nie einen Knoten, nur den
Pfeil `oben`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public void push(E element) {
    oben = new Knoten<>(element, ...);
    anzahl++;
}

public E pop() {
    E wert = peek();
    oben = ...;
    anzahl--;
    return wert;
}
```

</details>

## Aufgabe 8a: `klammernKorrekt`

<details><summary>Tipp 1 — Richtung</summary>

Warum reicht Zaehlen nicht? Schau dir `"([)]"` an: Welche Klammer muss
geschlossen werden, wenn das `)` kommt, und welche ist gerade die zuletzt
geoeffnete? "Zuletzt geoeffnet, zuerst geschlossen" ist genau LIFO (14.6).

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Einen `Stapel<Character>` anlegen und den String Zeichen fuer Zeichen mit
`charAt` durchlaufen:

- oeffnende Klammer: `push`
- schliessende Klammer: Ist der Stapel leer, ist das Ergebnis `false` (`"))"`).
  Sonst `pop` und pruefen, ob die geholte oeffnende Klammer zur schliessenden
  **passt**. Wenn nicht: `false`. Eine kleine private Methode `passen(char, char)`
  haelt das lesbar.
- jedes andere Zeichen: ignorieren

Nach der Schleife ist das Ergebnis nur dann `true`, wenn nichts mehr offen ist
(`"(("`). `char` und `Character` wandeln sich automatisch ineinander um
(Autoboxing, 8.3).

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
Stapel<Character> offen = new Stapel<>();
for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);
    if (c == '(' || c == '[' || c == '{') {
        offen.push(c);
    } else if (c == ')' || c == ']' || c == '}') {
        if (...) return false;
        char auf = offen.pop();
        if (!passen(auf, c)) return false;
    }
}
return ...;
```

</details>

## Aufgabe 8b: `umkehren`

<details><summary>Tipp 1 — Richtung</summary>

Was kommt aus einem Stapel zuerst wieder heraus, das erste oder das letzte
Element, das hineingelegt wurde?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Erst alle Zeichen des Strings der Reihe nach pushen. Dann poppen, solange der
Stapel nicht leer ist, und jedes Zeichen an einen `StringBuilder` haengen. Kein
`ergebnis += ...` in der Schleife, das waere O(n^2) (Kapitel 3, 14.1). Der leere
String funktioniert von selbst: nichts hinein, nichts heraus.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
Stapel<Character> stapel = new Stapel<>();
for (int i = 0; i < s.length(); i++) {
    stapel.push(...);
}
StringBuilder sb = new StringBuilder();
while (...) {
    sb.append(...);
}
return sb.toString();
```

</details>

## Selbstcheck — Antworten

<details><summary>Warum ist O(n^2) bei einer Million Elementen ein Problem, O(n log n) aber nicht?</summary>

Bei n = 1.000.000 sind n^2 = 10^12 Schritte, n log n dagegen nur rund 2 * 10^7.
Bei grob 10^9 einfachen Schritten pro Sekunde sind das etwa 1.000 Sekunden (eine
Viertelstunde) gegen 0,02 Sekunden. Der Unterschied waechst mit n immer weiter:
Verdoppelst du die Daten, vervierfacht sich die Zeit bei O(n^2), bei O(n log n)
verdoppelt sie sich nur knapp. Kein schnellerer Rechner gleicht das aus, nur ein
besserer Algorithmus.

</details>

<details><summary>Warum sagt eine einzelne Messung mit <code>System.nanoTime()</code> wenig aus?</summary>

Erstens laeuft Code anfangs im Interpreter. Erst nach dem Aufwaermen hat der
JIT-Compiler ihn in Maschinencode uebersetzt, die ersten Laeufe sind also zu
langsam. Zweitens stoeren Garbage Collector, andere Programme und die
Taktanpassung der CPU einzelne Messungen zufaellig. Drittens kann der JIT
Rechnungen streichen, deren Ergebnis nie benutzt wird. Und sehr kurze Messungen
bestehen vor allem aus der Ungenauigkeit der Uhr. Deshalb: aufwaermen, mehrfach
messen, Minimum oder Median nehmen, Ergebnisse verwenden, genug Arbeit pro
Messung. Oder gleich JMH benutzen.

</details>

<details><summary>Warum ist <code>add</code> am Ende einer <code>ArrayList</code> amortisiert O(1), obwohl das Wachsen O(n) kostet?</summary>

Das teure Umkopieren passiert selten, und nach jedem Wachsen ist wieder so viel
Platz frei, wie schon belegt war. Beim Verdoppeln kopierst du bis n Elemente
insgesamt 1 + 2 + 4 + ... + n < 2n Elemente um. Auf n Aufrufe verteilt sind das
weniger als 2 Kopien pro `add`, also im Schnitt konstant. "Amortisiert" heisst
genau das: Einzelne Aufrufe koennen teuer sein, der Durchschnitt ueber viele ist
O(1). Das funktioniert mit jedem festen Wachstumsfaktor groesser als 1 (die echte
`ArrayList` nimmt 1,5). Wuerde das Array jedes Mal um einen festen Betrag wachsen,
waeren es O(n^2) Kopien insgesamt.

</details>

<details><summary>Was bedeutet "stabil" beim Sortieren, und wann spielt es eine Rolle?</summary>

Ein stabiles Verfahren laesst gleich bewertete Elemente in ihrer urspruenglichen
Reihenfolge. Bei `int` sieht man das nicht, zwei gleiche Zahlen sind
ununterscheidbar. Bei Objekten, die nur nach **einem** Merkmal sortiert werden,
zaehlt es: Sind Personen schon nach Name sortiert und du sortierst sie stabil
nach Alter, stehen Gleichaltrige weiterhin alphabetisch. Merge Sort und Insertion
Sort sind stabil (bei Gleichstand links nehmen bzw. nur echt Groessere
verschieben), Selection Sort ist es nicht. `List.sort` und `Arrays.sort` fuer
Objekte (TimSort) sind garantiert stabil.

</details>

<details><summary>Stapel oder Warteschlange: Was passt zur Klammerpruefung, was zu Druckauftraegen, und warum?</summary>

Klammerpruefung: **Stapel** (LIFO). Die zuletzt geoeffnete Klammer muss als erste
geschlossen werden, also wird immer das juengste Element gebraucht.
Druckauftraege: **Warteschlange** (FIFO). Wer zuerst kommt, wird zuerst gedruckt,
also wird immer das aelteste Element gebraucht. In Java nimmst du fuer beides
`ArrayDeque`: `push`/`pop` fuer den Stapel, `offer`/`poll` fuer die Schlange.

</details>
