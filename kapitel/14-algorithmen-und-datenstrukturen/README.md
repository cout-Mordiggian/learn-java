# Kapitel 14 — Algorithmen und Datenstrukturen

**Ziel:** Du schätzt ab, wie der Aufwand eines Programms mit der Datenmenge
wächst, baust Such- und Sortierverfahren selbst und weisst, was in
`ArrayList`, `ArrayDeque` und `HashMap` wirklich passiert.

Binäre Suche und Rekursion kennst du aus Kapitel 4, O(1)/O(n)/O(log n) aus 8.1.

---

## 14.1 Algorithmus und Aufwand

Ein **Algorithmus** ist eine eindeutige, endliche Schrittfolge, die ein Problem
löst. Zwei korrekte Programme können für eine Million Datensätze eine
Millisekunde oder eine Viertelstunde brauchen. Den Unterschied macht nicht der
Rechner, sondern der Algorithmus. Man zählt deshalb **Schritte in Abhängigkeit
von n**, der Größe der Eingabe. Die **O-Notation** beschreibt, wie diese Zahl
*wächst*. Konstante Faktoren und kleine Zusatzglieder fallen weg: 3n + 5 ist
O(n), n^2/2 ist O(n^2).

| Aufwand | n = 10 | n = 1.000 | n = 1.000.000 | Beispiel |
|---------|-------:|----------:|--------------:|----------|
| O(1) konstant | 1 | 1 | 1 | `a[i]`, `list.get(i)` |
| O(log n) logarithmisch | 3 | 10 | 20 | binäre Suche |
| O(n) linear | 10 | 1.000 | 1.000.000 | lineare Suche, Summe |
| O(n log n) | 33 | 10.000 | 20.000.000 | Merge Sort, `Arrays.sort` |
| O(n^2) quadratisch | 100 | 1.000.000 | 1.000.000.000.000 | Insertion Sort, zwei geschachtelte Schleifen |

(Gerundet, Logarithmus zur Basis 2.) Bei grob einer Milliarde einfacher
Schritte pro Sekunde sortiert ein O(n log n)-Verfahren eine Million Zahlen in
etwa 0,02 s. Ein O(n^2)-Verfahren braucht rund 1.000 s, eine Viertelstunde.

**Faustregeln:** Eine Schleife über alle Elemente: O(n). Zwei geschachtelte:
O(n^2). Halbieren in jedem Schritt: O(log n). **Versteckte Schleifen
mitzählen!** `liste.contains(x)` ist bei einer `ArrayList` schon O(n), in einer
Schleife also O(n^2). Dasselbe gilt für `text += ...` (Kapitel 3).

Meist meint man den **schlimmsten Fall**. Auch Speicher ist Aufwand: Merge Sort
braucht O(n) zusätzlich, Insertion Sort nur O(1).

## 14.2 Selbst messen und die Fallen dabei

```java
long start = System.nanoTime();              // fuer Zeitdifferenzen, nicht fuer die Uhrzeit
tuWas();
long dauer = System.nanoTime() - start;      // Nanosekunden
```

Die Messung ist leicht. Gut messen ist schwer:

1. **JIT-Aufwärmen.** Die JVM startet im Interpreter und übersetzt erst häufig
   laufende Methoden in Maschinencode. Die ersten Läufe sind langsamer.
2. **Einmal messen lügt.** GC, andere Programme, Prozessortakt: miss mehrmals.
3. **Toter Code.** Ein nie benutztes Ergebnis darf der JIT wegoptimieren. Gib es aus.
4. **Zu kurz.** Unter Mikrosekunden misst du die Uhr. Viele Wiederholungen am Stück.

Profis nehmen das Werkzeug **JMH** (*Java Microbenchmark Harness*). Für ein
Gefühl reicht die fertige Demo [`src/Messung.java`](src/Messung.java). Sie
läuft auch vor der ersten gelösten Aufgabe und braucht wenige Sekunden:

```bash
bash lerne.sh 14 -r Messung
```

Teil 1 misst zehnmal dieselbe Arbeit und zeigt das Aufwärmen. Teil 2 und 3
vergleichen lineare mit binärer Suche und Selection Sort mit `Arrays.sort`.
Die absoluten Zahlen hängen vom Rechner ab, schau auf die Spalte **Faktor**:
Doppelt so viele Daten kosten bei O(n^2) etwa viermal so viel Zeit (x 3,9), bei
O(n log n) gut doppelt so viel (x 2,3). Genau das sagt die Tabelle aus 14.1.

## 14.3 Suchen: linear oder binär

**Linear**: jedes Element anschauen, O(n), klappt immer. **Binär** (Kapitel 4,
Aufgabe 5): nur auf **sortierten** Daten, halbiert pro Schritt, O(log n):

```
Index:   0   1   2   3   4   5   6   7   8   9
Werte: [ 2   5   8  12  16  23  38  56  72  91 ]       gesucht: 23
von=0, bis=9, mitte=4 -> 16 < 23 -> rechts weiter: von=5
von=5, bis=9, mitte=7 -> 56 > 23 -> links weiter:  bis=6
von=5, bis=6, mitte=5 -> 23      -> gefunden
```

Sortieren kostet selbst O(n log n), das lohnt sich nur, wenn du **oft** suchst.
Bei Duplikaten findet die Suche aus Kapitel 4 *irgendeinen* Treffer. Oft
braucht man eine **Grenze**: den Index des *ersten* Elements, das mindestens so
gross ist wie der Suchwert (Aufgabe 4). Diesen Einfügepunkt meldet auch
`Arrays.binarySearch` bei fehlendem Wert, verpackt als `-(Einfuegepunkt) - 1`.
Gegen die Grenzfälle hilft kein Probieren mit `+1` und `-1`, sondern eine
**Invariante**, die vor jedem Durchlauf gilt: "Links von `von` ist alles zu
klein, ab `bis` ist alles gross genug."

## 14.4 Sortieren

**Selection Sort** sucht das Kleinste im Rest und tauscht es nach vorn:

```
[5  3  8  1  4]  ->  [1 |3  8  5  4]  ->  [1  3 |8  5  4]  ->  [1  3  4 |5  8]  ->  fertig
  1 nach vorn          3 bleibt             4 nach vorn          5 bleibt
```

Die Minimumsuche ist jedes Mal eine Schleife: immer ca. n^2/2 Vergleiche, auch
auf sortierten Daten. Diese Variante steckt in der Mess-Demo.

**Insertion Sort** arbeitet wie beim Kartenspiel: nächste Karte nehmen,
größere nach rechts rücken, in die Lücke stecken.

```
[5 |3  8  1  4]    3 nehmen: 5 rueckt              -> [3  5 |8  1  4]
[3  5 |8  1  4]    8 nehmen: passt schon           -> [3  5  8 |1  4]
[3  5  8 |1  4]    1 nehmen: 8, 5, 3 ruecken       -> [1  3  5  8 |4]
[1  3  5  8 |4]    4 nehmen: 8, 5 ruecken          -> [1  3  4  5  8]
```

Invariante: **Links vom Strich ist alles sortiert.** Umgekehrt sortierte Daten
kosten O(n^2), schon sortierte nur O(n). Für kleine oder fast sortierte Daten
ist das ideal. Die JDK-Verfahren schalten für kurze Stücke selbst darauf um.

**Merge Sort** folgt dem Muster **Teile und herrsche**: halbieren, beide Hälften
rekursiv sortieren (Kapitel 4.4), zusammenmischen. Basisfall: Länge 0 oder 1.

```
             [5 3 8 1 4 7]
       [5 3 8]           [1 4 7]         teilen
    [5]    [3 8]       [1]    [4 7]
          [3] [8]            [4] [7]     Basisfall: Laenge 1
    [5]    [3 8]       [1]    [4 7]      mischen
       [3 5 8]           [1 4 7]
             [1 3 4 5 7 8]
```

Beim **Mischen** zeigt je ein Zeiger auf den Anfang beider sortierter Hälften.
Das kleinere Element wandert ins Ergebnis, sein Zeiger rückt weiter. Ist eine
Seite leer, kommt der Rest der anderen dazu. Pro Ebene O(n), etwa log2(n) Ebenen:
**O(n log n) in jedem Fall**, bei O(n) Zusatzspeicher. Die Rekursionstiefe ist
nur log2(n), ein `StackOverflowError` droht nicht.

**Stabil** ist ein Verfahren, wenn gleiche Elemente ihre Reihenfolge behalten.
Bei `int` egal, bei Objekten wichtig: Sortierst du Personen nach Name und dann
stabil nach Alter, bleiben Gleichaltrige alphabetisch. Insertion und Merge Sort
sind stabil, wenn man nur *echt* Größere verschiebt bzw. bei Gleichstand links
nimmt. Selection Sort ist es nicht, das Tauschen lässt Elemente weit springen.

**In der Praxis** sortierst du mit der Bibliothek:

```java
Arrays.sort(zahlen);                              // int[]: Dual-Pivot-Quicksort, O(n log n)
Arrays.sort(woerter);                             // Objekte: TimSort, stabil
personen.sort(Comparator.comparing(Person::getName));   // List.sort: TimSort, stabil
```

TimSort ist ein Merge Sort, der kurze Stücke per Insertion Sort sortiert und
Vorsortiertes ausnutzt. Das ist schneller als alles Selbstgeschriebene. Selbst
bauen solltest du es trotzdem einmal: Erst dann weisst du, woher O(n log n) und
der Zusatzspeicher kommen, und die Muster (Invariante, zwei Zeiger, teile und
herrsche) begegnen dir immer wieder. **Deshalb ist `Arrays.sort` in den Aufgaben
tabu.** Die Tests merken es nicht, aber die Aufgabe wäre sinnlos.

## 14.5 Wie `ArrayList` innen funktioniert

```
elemente: [ "a" | "b" | "c" | null ]      groesse = 3, Kapazitaet = 4
             0     1     2     3
```

Ein Array plus ein Zähler, wie viele Plätze belegt sind. Daraus folgt alles:

- **`get(i)` ist O(1)**, die Adresse von Platz i wird direkt berechnet.
- **`add` am Ende** belegt Platz `groesse`. Ist das Array voll, wird ein doppelt
  so grosses angelegt und alles umkopiert: O(n).
- **Amortisiert O(1):** Nach dem Verdoppeln ist wieder so viel frei, wie belegt
  ist. Alle Kopien zusammen (1 + 2 + 4 + ... + n) sind weniger als 2n, im Schnitt
  O(1) pro `add`. Wächst das Array jedes Mal nur um einen Platz, sind es
  1 + 2 + ... + n, also O(n^2).
- **`remove(0)` ist O(n)**, alles dahinter rückt nach links, ebenso bei
  `add(0, x)`. Musst du oft vorn entfernen, nimm eine `ArrayDeque` (14.6).

Das echte `java.util.ArrayList` bekommt beim ersten `add` Kapazität 10 und
wächst um den Faktor 1,5. Für amortisiert O(1) reicht jeder feste Faktor
größer als 1. **Kapazität ist nicht Größe:** `new ArrayList<>(100)` hat
Platz für 100 Elemente, enthält aber keins.

**Das Problem mit `new E[]`.** Das verbietet der Compiler (*generic array
creation*), wegen der Typlöschung aus 8.8: Zur Laufzeit gibt es kein E, ein Array
muss seinen Elementtyp aber kennen. Der ehrliche Weg ist der von `ArrayList`:

```java
private Object[] elemente = new Object[4];     // intern: Object

@SuppressWarnings("unchecked")
public E get(int index) {
    return (E) elemente[index];                // beim Herausgeben: Cast
}
```

Die JVM kann `(E)` nicht prüfen, deshalb warnt der Compiler ("unchecked").
`@SuppressWarnings` heisst: "Ich habe nachgedacht, hinein kommt nur E über `add`
und `set`." Setz es so eng wie möglich, an die Methode statt an die Klasse.
`(E[]) new Object[4]` geht auch, solange das Array nie nach aussen gelangt.

## 14.6 Verkettete Strukturen: Stapel und Warteschlange

Statt in einem Array kann man Elemente in **Knoten** speichern, die auf den
nächsten zeigen (englisch meist `next`). Ein **Stapel** (*stack*, LIFO: last
in, first out) arbeitet nur oben:

```
push(1), push(2), push(3):     oben --> [3|o]--> [2|o]--> [1|null]
pop() liefert 3:               oben --> [2|o]--> [1|null]
```

`push` erzeugt einen Knoten, der auf den bisherigen obersten zeigt, und macht
ihn zum neuen obersten. `pop` biegt `oben` einen weiter. Beides ist O(1), ohne
Umkopieren und ohne Kapazität. Der Preis: Jeder Knoten ist ein eigenes Objekt,
verstreut im Speicher. Eine **Warteschlange** (*queue*, FIFO: first in, first
out) hängt hinten an, nimmt vorn weg und merkt sich dafür beide Enden:

```
offer(1), offer(2), offer(3):  kopf --> [1|o]--> [2|o]--> [3|null] <-- ende
poll() liefert 1:              kopf --> [2|o]--> [3|null] <-- ende
```

Stapel: Undo, Klammerprüfung (Aufgabe 8), der Aufrufstapel der JVM (4.4).
Warteschlange: Druckaufträge, alles, was der Reihe nach drankommt.

**Neu: die statische innere Klasse.** In `Stapel.java` steht die Knotenklasse
*innerhalb* von `Stapel`, als `private static class Knoten<E> { ... }`:

- **Innen und `private`**: ein Bauteil des Stapels, draussen unsichtbar. Beide
  Klassen dürfen gegenseitig auf ihre privaten Member zugreifen.
- **`static`**: Ein Knoten braucht kein Stapel-Objekt. Ohne `static` trüge jeder
  Knoten einen versteckten Verweis auf "seinen" Stapel mit sich. Faustregel:
  innere Klassen `static` machen, ausser man braucht das äußere Objekt.
- Deshalb sieht `Knoten` das `E` von `Stapel` nicht und deklariert ein eigenes.

**In der Praxis:** `ArrayDeque`, ein Ringpuffer auf Array-Basis ohne Knoten,
fast immer schneller als `LinkedList`. Kein `null` als Element erlaubt.
`java.util.Stack` ist veraltet.

```java
Deque<Integer> stapel = new ArrayDeque<>();     // push, pop (leer: Exception), peek (leer: null)
Queue<String> schlange = new ArrayDeque<>();    // offer hinten, poll vorn (leer: null)
```

## 14.7 Wie `HashMap` im Prinzip funktioniert

Eine `HashMap` ist ein Array von **Eimern** (*buckets*). Den Eimer bestimmt der
`hashCode` des Schlüssels, im Prinzip `hashCode % n` bei n Eimern. Mit 8 Eimern:

```
"Anna".hashCode() = 2045632 -> Eimer 0      Eimer 0: ("Anna", 34)
"Bert".hashCode() = 2066917 -> Eimer 5      Eimer 3: ("Cem", 51) -> ("Emil", 27)
"Cem".hashCode()  =   67627 -> Eimer 3      Eimer 5: ("Bert", 20)
"Emil".hashCode() = 2163691 -> Eimer 3      alle anderen: leer
```

`get("Emil")` berechnet den Eimer (O(1)) und vergleicht **nur darin** per
`equals`. Zwei Schlüssel in einem Eimer sind eine **Kollision**, völlig normal,
der Eimer hält dann eine kurze Kette. Hier greift der Vertrag aus 8.4: Haben
`equals`-gleiche Objekte verschiedene `hashCode`s, sucht `get` im falschen Eimer.
Ebenso, wenn sich ein Schlüssel ändert, während er in der Map liegt.

Bei guter Verteilung sind `get` und `put` im Mittel O(1). Die echte `HashMap`
startet mit 16 Eimern und verdoppelt bei drei Vierteln Füllung (*load factor*
0,75), amortisiert O(1) wie `ArrayList`. Statt `%` nimmt sie eine Bitmaske, so
gibt es auch bei negativem `hashCode` keinen negativen Index (vgl. `-7 % 3` in
Kapitel 2). Landet alles in einem Eimer, wird die Suche O(n), seit Java 8 werden
volle Eimer deshalb zum Baum (O(log n)). Die "zufällige" Reihenfolge beim
Durchlaufen (8.5) ist einfach die Reihenfolge der Eimer.

## 14.8 Welche Struktur wofür

| Struktur | stark bei | schwach bei |
|----------|-----------|-------------|
| Array / `ArrayList` | `get(i)` O(1), hinten anfügen amortisiert O(1) | vorn einfügen/entfernen O(n), `contains` O(n) |
| sortiertes Array | Suchen O(log n) | Einfügen O(n) |
| `ArrayDeque` | Stapel und Warteschlange, beide Enden O(1) | kein Index-Zugriff |
| verkettete Liste | Einfügen an bekannter Stelle O(1) | `get(i)` O(n), schlechte Cache-Nutzung |
| `HashSet` / `HashMap` | enthalten? bzw. Wert zum Schlüssel: O(1) im Mittel | keine Ordnung |
| `TreeSet` / `TreeMap` | sortiert, kleinstes/größtes: O(log n) | langsamer als Hash |

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md). Erst Tipp 1 lesen, dann wieder selbst probieren.

Fünf Dateien in [`src/`](src/), prüfen mit `./lerne.sh 14` (`Messung.java` ist
fertig). Wirft dein Code eine Exception, melden die Tests sie als FEHL mit Datei
und Zeile und laufen weiter. Die Sortier-Tests nutzen Randfälle (leer, ein
Element, sortiert, umgekehrt, Duplikate, negativ) und Zufallsdaten gegen `Arrays.sort`.

1. **`Sortieren.insertionSort`** sortiert **an Ort und Stelle** (wie `verdoppleAlle`, Kapitel 4).
2. **`Sortieren.mergeSort`**: rekursiv, **neues** Array, Original unverändert, Mischen in `merge`.
3. **`Sortieren.istSortiert`**: aufsteigend, gleiche Nachbarn erlaubt.
4. **`Suchen.ersteGroesserGleich(sortiert, wert)`**: Index des ersten Elements
   `>= wert`, sonst `sortiert.length`. Binär, O(log n).
5. **`Suchen.zaehleVorkommen(sortiert, wert)`** in O(log n) mit Aufgabe 4. Eine
   lineare Lösung besteht die Tests auch, ob du halbierst, prüfst du selbst.
6. **`MeineListe<E>`**: `add`, `get`, `set` (liefert den alten Wert), `size`,
   `remove(int)` (rückt nach links), `contains` (mit `equals`, `null` erlaubt),
   `toString` wie `[a, b]`. Kapazität 4, **Verdoppeln** beim Wachsen. Ungültiger
   Index: `IndexOutOfBoundsException`, auch wenn das Array dort noch Platz hätte.
7. **`Stapel<E>`**: `push`, `pop`, `peek`, `istLeer`, `groesse`. `pop` und `peek`
   werfen bei leerem Stapel `NoSuchElementException`. Die Knotenklasse ist fertig.
8. **`Anwendungen.klammernKorrekt`** für `()[]{}` und **`Anwendungen.umkehren`**,
   beide mit **deinem** Stapel. Zählen reicht nicht: `"([)]"` ist falsch.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen.

**1.**

```java
int[] a = {10, 20, 30, 40};
System.out.println(Arrays.binarySearch(a, 30) + " " + Arrays.binarySearch(a, 25));
```

<details><summary>Auflösung</summary>

`2 -3`: Die 30 steht an Index 2. Die 25 fehlt, also liefert `binarySearch` `-(Einfuegepunkt) - 1`. Eingefügt würde sie an Index 2 (vor der 30), das ergibt `-2 - 1 = -3`. Das `- 1` sorgt dafür, dass auch Einfügepunkt 0 negativ wird. Der Einfügepunkt ist genau das, was `ersteGroesserGleich` in Aufgabe 4 liefert.

</details>

**2.**

```java
List<String> l = new ArrayList<>(100);
System.out.println(l.size());
l.set(0, "a");
```

<details><summary>Auflösung</summary>

Erst `0`, dann `IndexOutOfBoundsException: Index 0 out of bounds for length 0`. Die 100 ist die **Kapazität** des inneren Arrays, nicht die Größe. Die Liste ist leer, und `set` darf nur vorhandene Elemente ersetzen. Genau diese Prüfung baust du in `MeineListe` ein.

</details>

**3.**

```java
Deque<Integer> d = new ArrayDeque<>();
d.push(1);
d.push(2);
d.offer(3);
System.out.println(d.pop() + " " + d.pollLast() + " " + d);
```

<details><summary>Auflösung</summary>

`2 3 [1]`: `push` legt vorn an (Stapel-Sicht), `offer` hängt hinten an (Warteschlangen-Sicht), die Deque ist also `[2, 1, 3]`. `pop` nimmt vorn die 2, `pollLast` hinten die 3, übrig bleibt `[1]`. Eine *double-ended queue* ist an beiden Enden O(1) und taugt deshalb für beides. Beide Sichten zu mischen macht Code aber schwer lesbar.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum ist O(n^2) bei einer Million Elementen ein Problem, O(n log n) aber nicht?
- Warum sagt eine einzelne Messung mit `System.nanoTime()` wenig aus?
- Warum ist `add` am Ende einer `ArrayList` amortisiert O(1), obwohl das Wachsen O(n) kostet?
- Was bedeutet "stabil" beim Sortieren, und wann spielt es eine Rolle?
- Stapel oder Warteschlange: Was passt zur Klammerprüfung, was zu Druckaufträgen, und warum?

---

**Wie geht es weiter?** Zurück in die Hauptreihe mit
[Kapitel 9 — Lambdas und Streams](../09-lambdas-und-streams/README.md).
