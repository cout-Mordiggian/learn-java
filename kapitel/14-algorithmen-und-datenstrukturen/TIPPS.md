# Kapitel 14 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `insertionSort`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.4, das Kartenbild. Spiel es mit `{5, 3, 8, 1, 4}` auf Papier durch.
Frag dich: Welche Aussage gilt für den Teil links vom Strich vor jedem Schritt,
und was muss ein Schritt tun, damit sie danach auch für einen Platz mehr gilt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Aussen eine Schleife `i` von 1 bis `a.length - 1`. Merk dir `a[i]` in einer
Variablen, denn der Platz wird gleich überschrieben. Innen läuft ein zweiter
Index `j` von `i - 1` nach **links**, solange `j >= 0` **und** `a[j]` größer
als der gemerkte Wert ist. Dabei rückt jedes dieser Elemente einen Platz nach
rechts. Danach steht die Lücke bei `j + 1`.

Die Reihenfolge der Bedingungen zählt: Erst `j >= 0` prüfen, dann `a[j]`
anfassen, sonst gibt es bei `j == -1` eine `ArrayIndexOutOfBoundsException`.
`&&` wertet von links aus und bricht früh ab (Kapitel 2).

Kein `return` eines neuen Arrays: Der Test schaut auf das Array, das er dir
gegeben hat. `a = ...` in der Methode hätte nach aussen keine Wirkung (4.2).

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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
der kleiner wird). Löse zuerst nur `merge` und teste sie gedanklich mit
`{1, 5}` und `{2, 3, 9}`. Frag dich: Welche drei Positionen musst du dir
während des Mischens merken?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`mergeSort`: Ist `a.length <= 1`, gib `a.clone()` zurück, nicht `a` selbst (der
Test prüft, dass ein **neues** Array kommt). Sonst `mitte = a.length / 2` und die
beiden Hälften mit `Arrays.copyOfRange(a, 0, mitte)` und
`Arrays.copyOfRange(a, mitte, a.length)` kopieren. Das `bis` ist dort exklusiv.
Beide Hälften rekursiv sortieren und das Ergebnis von `merge` zurückgeben. Weil
du nur Kopien anfasst, bleibt das Original automatisch unverändert.

`merge`: Ergebnis-Array mit Länge `links.length + rechts.length`, drei Indizes
`i`, `j`, `k`, alle bei 0. Solange **beide** Seiten noch Elemente haben, das
kleinere übernehmen und seinen Index sowie `k` erhöhen. Danach ist eine Seite
leer, der Rest der anderen wird angehängt. Denk an **beide** Reste. Mit `<=`
statt `<` beim Vergleich wird es stabil.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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

Was muss für **jedes** Paar direkter Nachbarn gelten, damit ein Array
aufsteigend sortiert ist? Und wie viele solche Paare hat ein Array mit 0 oder 1
Element?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Eine Schleife über die Paare `(a[i - 1], a[i])` für `i` ab **1**. Ist das linke
Element größer als das rechte, steht fest: nicht sortiert, sofort `false`. Erst
nach der Schleife `true`. Gleiche Nachbarn sind erlaubt, also `>` und nicht `>=`.
Die Tests prüfen einen Fehler am Anfang, in der Mitte und am Ende, deine
Grenzen müssen also stimmen.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
for (int i = 1; i < ...; i++) {
    if (...) return false;
}
return true;
```

</details>

## Aufgabe 4: `ersteGroesserGleich`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.3, der Absatz über die Grenze und die Invariante. Frag dich: Wenn
`sortiert[mitte] < wert` ist, kann `mitte` dann die Antwort sein? Und wenn
`sortiert[mitte] >= wert` ist, kann `mitte` die Antwort sein, und kann es
eine Stelle weiter links noch eine bessere geben?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Halboffener Bereich: `von = 0`, `bis = sortiert.length`. Invariante: Links von
`von` ist alles `< wert`, ab `bis` alles `>= wert`. Solange `von < bis`:
`mitte = von + (bis - von) / 2`. Ist `sortiert[mitte] < wert`, gehört `mitte`
nach links, also `von = mitte + 1`. Sonst könnte `mitte` die Antwort sein, also
`bis = mitte` (**nicht** `mitte - 1`, sonst verlierst du sie).

Bei einem Treffer **nicht** abbrechen: Bei `{1, 3, 3, 3, 5}` trifft die erste
`mitte` die mittlere 3, gesucht ist aber die erste. Am Ende ist `von == bis`,
und das ist das Ergebnis, auch für das leere Array und "nichts ist gross genug".

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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
Frag dich: Wenn du weisst, wo der Block **beginnt** und wo der nächstgrößere
Wert beginnt, wie gross ist dann der Block?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`ersteGroesserGleich(sortiert, wert)` liefert den Anfang des Blocks.
`ersteGroesserGleich(sortiert, wert + 1)` liefert die erste Stelle **hinter** dem
Block, denn bei `int` gibt es zwischen `wert` und `wert + 1` nichts. Die
Differenz ist die Anzahl, auch 0, wenn der Wert fehlt.

Der Sonderfall: Ist `wert == Integer.MAX_VALUE`, läuft `wert + 1` über und wird
zu `Integer.MIN_VALUE` (Kapitel 1). Größer als `MAX_VALUE` kann aber nichts sein.
Wo endet der Block dann?

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
int anfang = ersteGroesserGleich(sortiert, wert);
int ende = (wert == Integer.MAX_VALUE) ? ... : ersteGroesserGleich(sortiert, ...);
return ...;
```

</details>

## Aufgabe 6: `MeineListe` — `add`, `get`, `size`, `toString`, Wachstum

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.5, das Bild mit `elemente` und `groesse`. Die zwei Felder sind schon
da. Frag dich für jede Methode: Was muss mit `groesse` passieren, und welche
Plätze des Arrays sind gerade gültig?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `size()` gibt `groesse` zurück, **nicht** `elemente.length`.
- `add`: Ist `groesse == elemente.length`, erst wachsen: ein neues Array der
  doppelten Länge anlegen, alle alten Elemente hineinkopieren und das Feld
  `elemente` auf das neue Array zeigen lassen. `Arrays.copyOf(elemente, neueLaenge)`
  erledigt die beiden ersten Schritte in einem. Dann `elemente[groesse] = element`
  und `groesse++`.
- `get`: Zuerst den Index prüfen. Gültig sind nur `0` bis `groesse - 1`. Das
  Array selbst hilft dir dabei nicht, denn bei `groesse` 1 und Kapazität 4 gibt es
  `elemente[1]` durchaus. Schreib eine private Methode `pruefeIndex`, die
  `throw new IndexOutOfBoundsException(...)` ausführt, und benutze sie auch in
  `set` und `remove`.
- `toString`: `StringBuilder`, `"["`, dann die Elemente `0..groesse-1` mit `", "`
  dazwischen, dann `"]"`. `append(Object)` schreibt für `null` von selbst "null".

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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
`b`. Frag dich: Welche Elemente müssen wohin, und was steht danach auf dem
letzten, jetzt ungültigen Platz? Für `contains` hilft Kapitel 3 (`==` gegen
`equals`) und 8.9 (`Objects.equals`).

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `set`: Index prüfen, alten Wert merken, überschreiben, alten Wert zurückgeben.
- `remove`: Index prüfen und das Element merken. Dann in einer Schleife von
  `index` bis `groesse - 2` jeweils `elemente[i] = elemente[i + 1]` setzen, also
  von **links nach rechts**, sonst überschreibst du, was du noch brauchst.
  Danach `groesse--` und `elemente[groesse] = null`, damit keine alte Referenz
  liegen bleibt.
- `contains`: nur bis `groesse` suchen, nicht bis `elemente.length`, dahinter
  stehen `null`s. Mit `==` schlägt der Test mit `new String("Anna")` fehl, und
  bei `Integer` über 127 ebenso (8.3). `o.equals(...)` scheitert an `o == null`.
  `Objects.equals(o, elemente[i])` erledigt beides.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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
was sich bei `push(4)` und bei `pop()` an den Pfeilen ändert. Es ist jeweils
genau **ein** Pfeil, nämlich `oben`.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `push`: ein neuer `Knoten` mit dem Wert und dem **bisherigen** `oben` als
  `naechster`. Dann zeigt `oben` auf den neuen Knoten, `anzahl++`. Bei leerem
  Stapel ist `oben` gerade `null`, und das passt genau.
- `peek`: Ist `oben == null`, wirf `new NoSuchElementException("...")`. Sonst
  `oben.wert`.
- `pop`: Wert holen, dafür kannst du `peek()` wiederverwenden, dann wirft es
  schon bei leerem Stapel. Danach `oben = oben.naechster`, `anzahl--`, Wert zurück.
- `istLeer`: `oben == null`. `groesse`: `anzahl`.

Die Felder des Knotens sind `final`. Du änderst nie einen Knoten, nur den
Pfeil `oben`.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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

Warum reicht Zählen nicht? Schau dir `"([)]"` an: Welche Klammer muss
geschlossen werden, wenn das `)` kommt, und welche ist gerade die zuletzt
geöffnete? "Zuletzt geöffnet, zuerst geschlossen" ist genau LIFO (14.6).

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Einen `Stapel<Character>` anlegen und den String Zeichen für Zeichen mit
`charAt` durchlaufen:

- öffnende Klammer: `push`
- schliessende Klammer: Ist der Stapel leer, ist das Ergebnis `false` (`"))"`).
  Sonst `pop` und prüfen, ob die geholte öffnende Klammer zur schliessenden
  **passt**. Wenn nicht: `false`. Eine kleine private Methode `passen(char, char)`
  hält das lesbar.
- jedes andere Zeichen: ignorieren

Nach der Schleife ist das Ergebnis nur dann `true`, wenn nichts mehr offen ist
(`"(("`). `char` und `Character` wandeln sich automatisch ineinander um
(Autoboxing, 8.3).

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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
Stapel nicht leer ist, und jedes Zeichen an einen `StringBuilder` hängen. Kein
`ergebnis += ...` in der Schleife, das wäre O(n^2) (Kapitel 3, 14.1). Der leere
String funktioniert von selbst: nichts hinein, nichts heraus.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

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

## Aufgabe 9: `Stapel` als `Iterable`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.9 und das Bild mit dem Zeiger `aktuell`. Der Iterator ist eine
Art Lesezeichen in der Kette. Frag dich: Wo steht das Lesezeichen am Anfang,
woran erkennst du, dass es hinter dem letzten Knoten steht, und was muss
`next()` ausser "Wert liefern" noch tun?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`iterator()` ist schon fertig. In `StapelIterator` fehlt ein Feld
`private Knoten<E> aktuell = oben;`. Das darf direkt `oben` lesen, weil die
Klasse **nicht** `static` ist.

- `hasNext()`: Gibt es noch einen Knoten? Also `aktuell != null`. Nicht
  `aktuell.naechster != null`, dann fehlt immer das unterste Element.
- `next()`: Ist `aktuell` `null`, eine `NoSuchElementException` werfen. Sonst den
  Wert merken, `aktuell` einen Knoten weiterrücken, den Wert zurückgeben.

Nicht `pop()` benutzen! Der Iterator soll nur lesen, der Test prüft, dass der
Stapel nach der Schleife noch voll ist. Und die Position gehört in den
Iterator, nicht in den Stapel, sonst stören sich zwei Schleifen gegenseitig.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
private class StapelIterator implements Iterator<E> {
    private Knoten<E> aktuell = oben;

    @Override
    public boolean hasNext() {
        return ...;
    }

    @Override
    public E next() {
        if (...) {
            throw new NoSuchElementException("Keine weiteren Elemente");
        }
        E wert = ...;
        aktuell = ...;
        return wert;
    }
}
```

</details>

## Aufgabe 10: Befehle und Verlauf

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 14.10 und das Bild mit den zwei Stapeln. Erst die beiden Befehle, dann
den Verlauf. Frag dich bei jedem Befehl: Welche Angaben brauche ich, um die
Aktion **rückgängig** zu machen, und wann kenne ich sie?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `EinfuegenBefehl`: Die drei Konstruktorparameter in `final`-Felder.
  `ausfuehren`: `puffer.einfuegen(pos, text)`. `rueckgaengig`: dasselbe Stück
  wieder löschen, also `puffer.loeschen(pos, text.length())`.
- `LoeschenBefehl`: `puffer`, `von`, `laenge` in Felder, dazu ein Feld
  `String geloescht` **ohne** `final`. `ausfuehren` merkt sich den Rückgabewert
  von `puffer.loeschen(von, laenge)`. `rueckgaengig` fügt ihn bei `von` wieder
  ein. Merkst du dir den Text im Konstruktor, fällt der Test "merkt sich beim
  Ausführen" durch, denn dort ändert sich der Puffer zwischen Erzeugen und
  Ausführen.
- `Verlauf`: zwei `Deque<Befehl>` als `ArrayDeque`.
  - `ausfuehren(b)`: `b.ausfuehren()`, auf den Rückgängig-Stapel pushen,
    Wiederholen-Stapel leeren (`clear()`).
  - `rueckgaengig()`: leer -> `false`. Sonst poppen, `rueckgaengig()` aufrufen,
    auf den Wiederholen-Stapel pushen, `true`.
  - `wiederholen()`: spiegelbildlich. Aber **nicht** `ausfuehren(b)` des
    Verlaufs benutzen, das würde den Wiederholen-Stapel leeren! Direkt
    `b.ausfuehren()` und auf den Rückgängig-Stapel pushen.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
private final Deque<Befehl> rueckgaengigStapel = new ArrayDeque<>();
private final Deque<Befehl> wiederholenStapel = new ArrayDeque<>();

public void ausfuehren(Befehl befehl) {
    befehl.ausfuehren();
    rueckgaengigStapel.push(befehl);
    ...
}

public boolean rueckgaengig() {
    if (rueckgaengigStapel.isEmpty()) {
        return false;
    }
    Befehl b = rueckgaengigStapel.pop();
    b.......();
    wiederholenStapel.push(b);
    return true;
}
```

</details>

## Selbstcheck — Antworten

<details><summary>Warum ist O(n^2) bei einer Million Elementen ein Problem, O(n log n) aber nicht?</summary>

Bei n = 1.000.000 sind n^2 = 10^12 Schritte, n log n dagegen nur rund 2 * 10^7.
Bei grob 10^9 einfachen Schritten pro Sekunde sind das etwa 1.000 Sekunden (eine
Viertelstunde) gegen 0,02 Sekunden. Der Unterschied wächst mit n immer weiter:
Verdoppelst du die Daten, vervierfacht sich die Zeit bei O(n^2), bei O(n log n)
verdoppelt sie sich nur knapp. Kein schnellerer Rechner gleicht das aus, nur ein
besserer Algorithmus.

</details>

<details><summary>Warum sagt eine einzelne Messung mit <code>System.nanoTime()</code> wenig aus?</summary>

Erstens läuft Code anfangs im Interpreter. Erst nach dem Aufwärmen hat der
JIT-Compiler ihn in Maschinencode übersetzt, die ersten Läufe sind also zu
langsam. Zweitens stören Garbage Collector, andere Programme und die
Taktanpassung der CPU einzelne Messungen zufällig. Drittens kann der JIT
Rechnungen streichen, deren Ergebnis nie benutzt wird. Und sehr kurze Messungen
bestehen vor allem aus der Ungenauigkeit der Uhr. Deshalb: aufwärmen, mehrfach
messen, Minimum oder Median nehmen, Ergebnisse verwenden, genug Arbeit pro
Messung. Oder gleich JMH benutzen.

</details>

<details><summary>Warum ist <code>add</code> am Ende einer <code>ArrayList</code> amortisiert O(1), obwohl das Wachsen O(n) kostet?</summary>

Das teure Umkopieren passiert selten, und nach jedem Wachsen ist wieder so viel
Platz frei, wie schon belegt war. Beim Verdoppeln kopierst du bis n Elemente
insgesamt 1 + 2 + 4 + ... + n < 2n Elemente um. Auf n Aufrufe verteilt sind das
weniger als 2 Kopien pro `add`, also im Schnitt konstant. "Amortisiert" heisst
genau das: Einzelne Aufrufe können teuer sein, der Durchschnitt über viele ist
O(1). Das funktioniert mit jedem festen Wachstumsfaktor größer als 1 (die echte
`ArrayList` nimmt 1,5). Würde das Array jedes Mal um einen festen Betrag wachsen,
wären es O(n^2) Kopien insgesamt.

</details>

<details><summary>Was bedeutet "stabil" beim Sortieren, und wann spielt es eine Rolle?</summary>

Ein stabiles Verfahren lässt gleich bewertete Elemente in ihrer ursprünglichen
Reihenfolge. Bei `int` sieht man das nicht, zwei gleiche Zahlen sind
ununterscheidbar. Bei Objekten, die nur nach **einem** Merkmal sortiert werden,
zählt es: Sind Personen schon nach Name sortiert und du sortierst sie stabil
nach Alter, stehen Gleichaltrige weiterhin alphabetisch. Merge Sort und Insertion
Sort sind stabil (bei Gleichstand links nehmen bzw. nur echt Größere
verschieben), Selection Sort ist es nicht. `List.sort` und `Arrays.sort` für
Objekte (TimSort) sind garantiert stabil.

</details>

<details><summary>Stapel oder Warteschlange: Was passt zur Klammerprüfung, was zu Druckaufträgen, und warum?</summary>

Klammerprüfung: **Stapel** (LIFO). Die zuletzt geöffnete Klammer muss als erste
geschlossen werden, also wird immer das jüngste Element gebraucht.
Druckaufträge: **Warteschlange** (FIFO). Wer zuerst kommt, wird zuerst gedruckt,
also wird immer das älteste Element gebraucht. In Java nimmst du für beides
`ArrayDeque`: `push`/`pop` für den Stapel, `offer`/`poll` für die Schlange.

</details>

<details><summary>Warum ist <code>Knoten</code> eine <code>static</code> innere Klasse, <code>StapelIterator</code> aber nicht?</summary>

Eine innere Klasse ohne `static` trägt einen versteckten Verweis auf das
äußere Objekt, das sie erzeugt hat, und darf dessen Felder direkt benutzen.
Das lohnt sich nur, wenn sie dieses Objekt wirklich braucht. Ein Knoten braucht
keinen Stapel: Er hält nur einen Wert und den nächsten Knoten. Ohne `static`
trüge jeder der vielleicht Millionen Knoten einen überflüssigen Verweis mit
sich. Der Iterator dagegen gehört zu genau einem Stapel und startet bei dessen
`oben`, hier ist der Verweis genau richtig. Deshalb gilt die Faustregel aus
14.6: `static`, ausser man braucht das äußere Objekt.

</details>

<details><summary>Warum muss sich <code>LoeschenBefehl</code> den gelöschten Text beim Ausführen merken und nicht im Konstruktor?</summary>

Zwischen Erzeugen und Ausführen kann Zeit vergehen, und der Text kann sich
ändern, etwa durch andere Befehle oder weil der Befehl erst beim Wiederholen
erneut läuft. Rückgängig muss genau das wiederherstellen, was beim Ausführen
**tatsächlich** gelöscht wurde. Das weiss man erst in diesem Moment:
`Textpuffer.loeschen` liefert es als Rückgabewert. Beim Einfügen gibt es das
Problem nicht, denn der eingefügte Text steht schon im Befehl. Allgemein:
Ein Befehl muss sich den Zustand merken, den er zerstört, und zwar dann, wenn er
ihn zerstört.

</details>
