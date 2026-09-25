# Kapitel 08 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Paar.java: Felder, Getter, `getauscht` und `toString`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.8, Unterabschnitt "Eigene generische Typen" — die `Box<T>` dort ist
dasselbe Prinzip mit einem statt zwei Typparametern. Frag dich bei
`getauscht()`: Was verraet dir der Rueckgabetyp `Paar<B, A>` darueber, welcher
Wert an welche Stelle muss?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zwei Felder `private final A erstes` und `private final B zweites`, gesetzt im
Konstruktor, zurueckgegeben von den Gettern. Innerhalb der Klasse benutzt du `A`
und `B` wie ganz normale Typnamen.

`getauscht()` erzeugt ein **neues** Paar, dessen erste Position vom Typ `B` ist.
Du musst die Typen nicht hinschreiben: Mit dem Diamond `new Paar<>(...)` leitet
der Compiler sie aus dem Rueckgabetyp ab — und meldet einen Fehler, wenn du die
Argumente in der falschen Reihenfolge uebergibst. Der Compiler prueft hier also
fuer dich mit.

`toString` ergibt `(Anna, 34)`: runde Klammern, Komma **plus Leerzeichen**.
Die Fabrikmethode `von` ist schon fertig — schau dir nur ihre Signatur an.

</details>

## Paar.java: `equals` und `hashCode`

<details><summary>Tipp 1 — Richtung</summary>

Dasselbe Muster wie `Punkt` in Kapitel 5 (Abschnitt 5.6), mit zwei
Besonderheiten. Frag dich: Welchen Typ kannst du wegen der Typloeschung
(Abschnitt 8.8, "Type Erasure") zur Laufzeit ueberhaupt mit `instanceof` pruefen?
Und was passiert, wenn `erstes` `null` ist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `o instanceof Paar<A, B> p` kompiliert **nicht** ("Object cannot be safely
  cast to Paar<A,B>"), weil `A` und `B` zur Laufzeit nicht mehr existieren.
  Richtig ist das Wildcard-Muster `Paar<?, ?>`.
- Die Felder vergleichst du mit `Objects.equals(a, b)` statt mit `a.equals(b)`:
  Der Test "null-Inhalte sind erlaubt" baut `Paar.von(null, null)`, und
  `null.equals(...)` wuerde eine `NullPointerException` werfen.
- `hashCode`: `Objects.hash(...)` ueber genau die beiden Felder — das ist
  ebenfalls null-sicher.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Paar<?, ?> p)) return false;
    return Objects.equals(..., ...) && ...;
}
```

</details>

## Person.java: Konstruktor, Getter, `toString` und `compareTo`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.7 (`Comparable`). Frag dich: Wonach ist die **natuerliche** Ordnung
laut Aufgabe definiert — und kann `String` sich nicht schon selbst vergleichen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`name` und `alter` als `private final`, Getter wie gehabt, `toString` im Format
`Anna(34)` (keine Leerzeichen).

`compareTo` vergleicht **nur den Namen** und reicht dazu das Ergebnis von
`String.compareTo` durch. Negativ / 0 / positiv musst du nicht selbst basteln.
Der Test "gleicher Name -> 0" vergleicht Anna(34) mit Anna(99) — nimmst du das
Alter mit in `compareTo` auf, wird er rot.

Fuer numerische Vergleiche gilt allgemein: `Integer.compare(a, b)` statt `a - b`
(Ueberlaufgefahr).

</details>

## Person.java: `equals` und `hashCode`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.6 und 8.4 (warum `hashCode` fuer `HashSet` zaehlt). Frag dich:
Welche Felder entscheiden laut Tests, ob zwei Personen gleich sind?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Anders als `compareTo` beachtet `equals` **beide** Felder — der Test "equals
beachtet auch das Alter" prueft das. Also: gleiche Referenz, `instanceof Person p`,
dann `alter` mit `==` (bei `int` voellig in Ordnung) und `name` mit `equals`
(**nicht** `==`, Kapitel 3.2). `hashCode` mit `Objects.hash` ueber genau diese
beiden Felder, sonst erkennt das `HashSet` im Test das Duplikat nicht.

Gut zu wissen: `compareTo` und `equals` sind damit absichtlich **inkonsistent**
(Abschnitt 8.7). In einem `TreeSet` gaelten Anna(34) und Anna(99) als ein und
dasselbe Element.

</details>

## Aufgabe 1: `haeufigkeiten`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.5 (`merge`) und aus Kapitel 3 der Abschnitt 3.3 (`strip`, `split`).
Frag dich: Was liefert `split("\\s+")` fuer einen **leeren** String — und fuer
einen String, der mit Leerzeichen **beginnt**? Probier es in `jshell` aus.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die drei Randfaelle der Tests haengen alle an `split`:

- `"".split("\\s+")` liefert **nicht** ein leeres Array, sondern `[""]` — ohne
  Sonderbehandlung zaehlst du ein leeres Wort.
- `"  a b".split("\\s+")` liefert `["", "a", "b"]` — fuehrender Whitespace
  erzeugt vorne ein leeres Wort. (Leere Stuecke **am Ende** wirft `split` dagegen
  von selbst weg.)
- Deshalb: erst `strip()`, dann auf leer pruefen und ggf. die leere Map
  zurueckgeben, erst dann trennen.

Kleinschreiben mit `toLowerCase(Locale.ROOT)`. Zum Zaehlen `merge(wort, 1, Integer::sum)`:
Ist das Wort neu, wird 1 eingetragen, sonst wird die alte Zahl plus 1 gespeichert.
`\\s` erfasst uebrigens auch Zeilenumbrueche, deshalb klappt auch der Test mit `\n`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
Map<String, Integer> zaehler = new HashMap<>();
String s = text....;           // strip + toLowerCase(Locale.ROOT)
if (...) return zaehler;
for (String wort : s.split(...)) {
    zaehler.merge(...);
}
return zaehler;
```

</details>

## Aufgabe 2: `deduplizieren`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.1, die Landkarte und die Auswahlregel. Frag dich: Welche
`Set`-Implementierung verhindert Duplikate **und** merkt sich die
Einfuegereihenfolge?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`HashSet` verliert die Reihenfolge, `TreeSet` sortiert alphabetisch — die Tests
erwarten aber `[b, a, c]` aus `[b, a, b, c, a]`. Die richtige Implementierung
steht in der Landkarte direkt zwischen den beiden. Fast alle Collections haben
einen Konstruktor, der eine andere Collection entgegennimmt und alle Elemente
uebernimmt — damit brauchst du nicht einmal eine Schleife. Der Rueckgabetyp ist
aber `List`, du musst also am Ende wieder eine Liste daraus machen.

</details>

## Aufgabe 3: `haeufigsteWoerter`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.5 (`entrySet`) und 8.7 (`Comparator`, `reversed`, `thenComparing`).
Frag dich: Eine Map kann man nicht sortieren — aber was kann man stattdessen
sortieren? Und wie drueckst du "erst nach Anzahl absteigend, dann nach Wort
aufsteigend" als **einen** Comparator aus?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Kopiere `zaehler.entrySet()` in eine `ArrayList<Map.Entry<String, Integer>>`,
sortiere sie, und sammle die Schluessel der ersten `n` Eintraege ein.

Zum Sortieren gibt es fertige Comparatoren: `Map.Entry.comparingByValue()` und
`Map.Entry.comparingByKey()`. **Die Falle:** Haengst du direkt `.reversed()` an,
kann der Compiler die Typen nicht mehr ableiten und meldet
`incompatible types: Comparator<Entry<Object,V>> ...`. Abhilfe ist ein
Typzeuge: `Map.Entry.<String, Integer>comparingByValue().reversed()`. Alternativ
schreibst du den Comparator als Lambda `(x, y) -> ...` und vergleichst selbst mit
`Integer.compare` und `String.compareTo`.

Randfaelle der Tests: `n` groesser als die Map (dann eben alle), leere Map (dann
leere Liste). Ein `subList(0, n)` ohne Begrenzung wuerde bei `n = 99` eine
`IndexOutOfBoundsException` werfen — begrenze mit `Math.min` oder brich die
Schleife ab, sobald `n` Eintraege gesammelt sind.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
List<Map.Entry<String, Integer>> eintraege = new ArrayList<>(zaehler.entrySet());
eintraege.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
        .thenComparing(...));

List<String> ergebnis = new ArrayList<>();
for (Map.Entry<String, Integer> e : eintraege) {
    if (...) break;
    ergebnis.add(...);
}
return ergebnis;
```

</details>

## Aufgabe 4: `groesstes`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.8, Unterabschnitt "Generische Methoden". Die Signatur ist schon
vorgegeben. Frag dich: Was darfst du mit einem `T` nur deshalb tun, weil dort
`T extends Comparable<T>` steht?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Operatoren `<` und `>` funktionieren nur fuer primitive Zahlen, nicht fuer
`T` — du vergleichst mit `a.compareTo(b)`, und "groesser" heisst "Ergebnis `> 0`".
Das Vorgehen ist dieselbe Maximumsuche wie bei `Figuren.groesste` in Kapitel 6:
leere Liste -> `null`, sonst mit dem ersten Element starten und bei jedem
groesseren Element ersetzen. Die Tests pruefen das Maximum vorne und in der
Mitte.

`Collections.max(liste)` wuerde auch funktionieren, wirft aber bei leerer Liste
eine `NoSuchElementException` — und der Uebungszweck ist gerade das eigene
`compareTo`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
if (liste.isEmpty()) return null;
T max = ...;
for (T element : liste) {
    if (element.compareTo(max) ...) {
        max = element;
    }
}
return max;
```

</details>

## Aufgabe 5: `summe`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.8, Unterabschnitt "Wildcards und PECS". Frag dich: Als welchen Typ
kannst du die Elemente einer `List<? extends Number>` sicher lesen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Jedes Element ist garantiert eine `Number` — mehr weiss der Compiler nicht.
Deine Schleifenvariable ist also vom Typ `Number` (nicht `Integer`, denn es
koennte ja eine `List<Double>` sein). Mit einer `Number` kannst du nicht direkt
rechnen: `summe += z` kompiliert nicht, weil `Number` kein Wrapper eines
bestimmten primitiven Typs ist. Aber jede `Number` kann sich selbst als
`double` liefern — der Kommentar im TODO nennt die Methode. Eine leere Liste
ergibt von selbst `0.0`.

</details>

## Aufgabe 6: `nachAlterDannName`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.7 (`Comparator`) und Abschnitt 4.2 (versteckte Nebenwirkungen).
Frag dich: Was passiert mit der **uebergebenen** Liste, wenn du direkt darauf
`sort` aufrufst?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`sort` arbeitet **in place**: Sortierst du die Eingabe direkt, ist sie hinterher
veraendert, und der Test "Eingabeliste unveraendert" wird rot. Waere die Eingabe
ein `List.of(...)`, gaebe es sogar eine `UnsupportedOperationException`. Also:
erst eine Kopie mit `new ArrayList<>(personen)`, dann die Kopie sortieren und
zurueckgeben.

Der Comparator steht schon im TODO. **Die Falle:** Schreibst du ihn mit Lambdas
statt Methodenreferenzen, also `comparingInt(p -> p.getAlter()).thenComparing(p -> p.getName())`,
kann der Compiler den Typ von `p` nicht ableiten (fuer ihn ist `p` ein `Object`)
und meldet `cannot find symbol`. Mit `Person::getAlter` und `Person::getName`
klappt es.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
List<Person> kopie = ...;
kopie.sort(Comparator.comparingInt(...).thenComparing(...));
return kopie;
```

</details>

## Aufgabe 7: `gruppiereNachAnfangsbuchstabe`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 8.5 (`computeIfAbsent`, "Gruppieren") und die Auswahlregel in 8.1.
Frag dich: Welche `Map`-Implementierung behaelt die Einfuegereihenfolge der
Schluessel — und was passiert bei `"".charAt(0)`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Nimm eine `LinkedHashMap`. Mit einer `HashMap` kommen die Schluessel des Tests
  `[Bert, Anna, Cem]` als `[A, B, C]` statt `[B, A, C]` heraus.
- Leere Woerter ueberspringst du (`continue`), bevor du `charAt(0)` aufrufst —
  auf einem leeren String wirft das eine `StringIndexOutOfBoundsException`.
- Der `char` aus `charAt(0)` wird automatisch zu `Character` geboxt, passend zum
  Schluesseltyp.
- `computeIfAbsent(schluessel, k -> new ArrayList<>())` liefert die Liste zu
  diesem Buchstaben und legt sie vorher an, falls es noch keine gibt. An das
  Ergebnis kannst du direkt `.add(wort)` haengen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
Map<Character, List<String>> gruppen = new LinkedHashMap<>();
for (String wort : woerter) {
    if (...) continue;
    gruppen.computeIfAbsent(..., k -> new ArrayList<>()).add(...);
}
return gruppen;
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Wann <code>ArrayList</code>, wann <code>HashSet</code>, wann <code>TreeMap</code>?</summary>

`ArrayList`, wenn du eine geordnete Folge mit Duplikaten und Zugriff per Index
brauchst — das ist der Normalfall. `HashSet`, wenn jedes Element hoechstens
einmal vorkommen soll und du vor allem schnell (O(1)) fragen willst, ob etwas
enthalten ist; eine Reihenfolge garantiert es nicht. `TreeMap`, wenn du
Schluessel-Wert-Paare brauchst und die Schluessel dabei **sortiert** sein sollen
(O(log n)); ohne Sortierbedarf reicht die schnellere `HashMap`.

</details>

<details><summary>Was passiert, wenn du <code>equals</code> ueberschreibst und <code>hashCode</code> vergisst?</summary>

Dann haben zwei `equals`-gleiche Objekte in aller Regel verschiedene Hashwerte,
denn der geerbte `Object.hashCode` unterscheidet einzelne Objekte. `HashSet` und
`HashMap` waehlen per `hashCode` den Eimer und fragen `equals` nur innerhalb
dieses Eimers — so wird `equals` gar nicht erst gefragt. Folge: Duplikate im
`HashSet`, und `map.get(neuerGleicherSchluessel)` liefert `null`, obwohl der
Eintrag drinsteckt. Der Fehler kompiliert problemlos und faellt erst zur
Laufzeit auf.

</details>

<details><summary>Warum wirft <code>liste.remove(x)</code> in einer for-each-Schleife?</summary>

Die for-each-Schleife benutzt intern einen `Iterator`. Die Liste zaehlt jede
strukturelle Aenderung mit, und der Iterator prueft bei jedem `next()`, ob sich
dieser Zaehler seit seiner Erzeugung geaendert hat. Hat er das, wirft er eine
`ConcurrentModificationException` (fail-fast), statt mit einem inkonsistenten
Stand weiterzulaufen. (Entfernst du zufaellig das vorletzte Element, endet die
Schleife ohne Exception, ueberspringt aber das letzte — auch das ist ein Bug.)
Auswege: `removeIf`, `Iterator.remove()` oder ueber eine Kopie laufen.

</details>

<details><summary><code>Comparable</code> oder <code>Comparator</code> — woran machst du das fest?</summary>

`Comparable` definiert die **eine natuerliche** Ordnung einer Klasse und steckt in
der Klasse selbst (`compareTo`) — wie Namen alphabetisch oder Zahlen der Groesse
nach. Einen `Comparator` nimmst du fuer jede weitere oder situationsabhaengige
Ordnung und fuer Klassen, die du nicht aendern kannst; davon kann es beliebig
viele geben. Faustregel aus dem README: Wenn du schwankst, welche Ordnung "die
richtige" ist, ist es ein `Comparator`.

```java
personen.sort(Comparator.comparingInt(Person::getAlter));
```

</details>

<details><summary>Warum ist <code>List&lt;Integer&gt;</code> kein <code>List&lt;Number&gt;</code>?</summary>

Waere es eines, koenntest du ueber die `Number`-Sicht etwas Falsches hineinlegen:

```java
List<Integer> ganz = new ArrayList<>();
List<Number> zahlen = ganz;   // Compilerfehler - und das ist gut so
zahlen.add(3.14);             // sonst laege ein Double in der Integer-Liste
```

Generics sind deshalb *invariant*, obwohl `Integer` ein Untertyp von `Number`
ist. Willst du nur lesen, nimm `List<? extends Number>` — damit darfst du jede
Liste von Zahlen uebergeben, aber nichts hineinlegen (PECS).

</details>
