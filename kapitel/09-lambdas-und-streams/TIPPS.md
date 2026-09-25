# Kapitel 09 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `geradeQuadrate`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5 (Streams, Zwischenoperationen). Die Aufgabe hat zwei Schritte:
erst *auswaehlen*, dann *umwandeln*. Frag dich: Welche Zwischenoperation
waehlt aus, welche wandelt um — und welche Terminaloperation macht daraus
wieder eine `List`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`filter` bekommt ein `Predicate<Integer>`, `map` eine `Function<Integer, Integer>`,
am Ende `toList()`. "Gerade" pruefst du mit dem Rest-Operator `%`.

Die Tests pruefen auch `0` (ist gerade, `0 * 0 = 0`) und `-1` (ist ungerade).
Pruefe auf `== 0`. Wer stattdessen `!= 1` schreibt, haelt `-1` faelschlich fuer
gerade, denn `-1 % 2` ist in Java `-1`, nicht `1`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return zahlen.stream()
        .filter(z -> ...)
        .map(z -> ...)
        .toList();
```

</details>

## Aufgabe 2: `laengstesWort`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5 (Terminaloperationen `min`/`max`) und 9.6 (`Optional`).
`max` liefert schon ein `Optional` — genau den Rueckgabetyp, den du brauchst.
Die eigentliche Frage: *Wonach* wird verglichen, und welches Element gewinnt
bei Gleichstand?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`max` braucht einen `Comparator<String>`. Den baust du dir mit
`Comparator.comparingInt(...)` aus einer Funktion, die jedem Wort eine Zahl
zuordnet.

Denkfalle Gleichstand (Test: `["abc", "xyz"]` muss `"abc"` liefern): Probier es
in `jshell` aus, statt zu raten:

```java
Stream.of("abc", "xyz").max((a, b) -> 0)     // Comparator, der alles fuer gleich haelt
```

Wenn du dich nicht auf dieses Verhalten verlassen willst, geht es auch explizit
mit `reduce((a, b) -> ...)`: Du behaeltst `a`, ausser `b` ist *echt* laenger.

Leere Liste und Ein-Wort-Liste erledigt `max` von selbst.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return woerter.stream()
        .max(Comparator.comparingInt(...));
```

oder die explizite Variante:

```java
return woerter.stream()
        .reduce((a, b) -> ... ? b : a);
```

</details>

## Aufgabe 3: `grossgeschriebenSortiert`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.3 (Methodenreferenzen) und 9.5 (`map`, `sorted`). Du brauchst aus
jeder `Person` nur den Namen. Frag dich: In welcher Reihenfolge muessen
Umwandeln und Sortieren stehen, damit "anna" und "Bert" richtig einsortiert
werden?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Erst `map(Person::getName)`, dann in Grossbuchstaben umwandeln, **dann**
`sorted()`. `toUpperCase(Locale.ROOT)` passt nicht als einfache
Methodenreferenz, weil es ein Argument braucht — nimm dort ein Lambda.

Denkfalle: Sortierst du *vor* dem Grossschreiben, gilt die natuerliche
String-Ordnung, und da stehen alle Grossbuchstaben vor allen Kleinbuchstaben:
`["dora", "Bert", "anna"]` sortiert ergibt `[Bert, anna, dora]`. Der Test
"gemischte Schreibweise" faellt dann durch.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return personen.stream()
        .map(...)                         // Person -> Name
        .map(n -> ...)                    // Name -> NAME
        .sorted()
        .toList();
```

</details>

## Aufgabe 4: `alsListe`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5, Collectors. Suche den Collector, der Strings zu *einem* String
verbindet — und der ausser dem Trenner auch noch Anfang und Ende kennt.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Collectors.joining` gibt es mit einem, zwei oder drei Argumenten. Du brauchst
die Variante mit drei: Trenner, Praefix, Suffix. Achte auf das Leerzeichen im
Trenner (`"a, b"`, nicht `"a,b"`).

Randfall leere Liste: Der Test erwartet `"[]"`. Praefix und Suffix bleiben bei
`joining` auch dann stehen, wenn gar kein Element kommt — du brauchst also
keinen Sonderfall.

</details>

## Aufgabe 5: `durchschnittsalter`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5 (`mapToInt`, `average()`) und 9.6 (`Optional`). Frag dich: Was
ist der Durchschnitt von *keinen* Werten — und wie sagt Java dir das im Typ?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`mapToInt(Person::getAlter)` macht aus dem `Stream<Person>` einen `IntStream`.
Dessen `average()` liefert ein `OptionalDouble`, das bei leerem Stream leer ist.
Aus einem `OptionalDouble` holst du den Wert mit einem Standardwert heraus —
dieselbe Idee wie `orElse` bei `Optional`.

Nicht selbst `summe / anzahl` rechnen: Bei leerer Liste ist das `0 / 0`, und bei
`int`-Division waere ausserdem `26.25` zu `26` abgeschnitten.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return personen.stream()
        .mapToInt(...)
        .average()
        .orElse(...);
```

</details>

## Aufgabe 6: `nachStadt`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5, Collectors, besonders `groupingBy` mit Downstream-Collector.
Frag dich: Wonach wird gruppiert — und was soll *pro Gruppe* in der Liste
stehen: die ganze `Person` oder nur ihr Name?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`groupingBy(Person::getStadt)` allein liefert `Map<String, List<Person>>` —
der Compiler meckert dann ueber den Rueckgabetyp. Das zweite Argument
(Downstream) bestimmt, was mit den Elementen einer Gruppe passiert:
`Collectors.mapping(umwandlung, sammler)` wandelt jedes Element um und sammelt
es dann.

Die Tests erwarten `["Anna", "Cem"]` in genau dieser Reihenfolge — die
Reihenfolge innerhalb einer Gruppe entspricht der Eingabe, das passt von
selbst. Leere Liste ergibt eine leere Map, auch das ohne Sonderfall.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return personen.stream()
        .collect(Collectors.groupingBy(
                ...,                                   // Klassifizierer: Stadt
                Collectors.mapping(..., ...)));        // Name, dann in eine Liste
```

</details>

## Aufgabe 7: `volljaehrigkeit`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5, Collectors: `partitioningBy`. Eine Aufteilung in genau zwei
Haelften nach einer Ja/Nein-Frage. Frag dich: Welches `Predicate<Person>`
beschreibt "volljaehrig"?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Collectors.partitioningBy(praedikat)` liefert `Map<Boolean, List<Person>>`.
Zwei Randfaelle aus den Tests:

- Eva ist **genau 18** und gilt als volljaehrig — also `>=`, nicht `>`.
- Bei leerer Eingabe muessen trotzdem beide Schluessel `true` und `false` da
  sein. Genau das garantiert `partitioningBy`; `groupingBy` mit demselben
  Praedikat liefert dagegen eine leere Map, und der Test scheitert.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return personen.stream()
        .collect(Collectors.partitioningBy(p -> ...));
```

</details>

## Aufgabe 8: `namenLaengen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5, Collectors: `toMap`. Pro Person entsteht genau ein Eintrag.
Frag dich: Welche Funktion liefert den **Schluessel**, welche den **Wert**?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Collectors.toMap(schluesselFunktion, wertFunktion)` — beide sind
`Function<Person, ...>`. Der Schluessel geht als Methodenreferenz, fuer den
Wert brauchst du ein kleines Lambda, weil zwei Aufrufe hintereinander noetig
sind (erst der Name, dann dessen Laenge).

Eine Merge-Funktion brauchst du laut Aufgabe nicht. Merk dir trotzdem:
Bei doppelten Schluesseln wirft `toMap` eine `IllegalStateException`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return personen.stream()
        .collect(Collectors.toMap(..., p -> ...));
```

</details>

## Aufgabe 9: `Transformation.dann`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.1 (Lambdas, eigenes funktionales Interface). `dann` fuehrt noch
nichts aus, sondern **liefert eine neue** `Transformation`. Frag dich: Was soll
diese neue Transformation mit ihrer Eingabe tun — und wer wird zuerst
angewendet, `this` oder `naechste`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Du gibst ein Lambda `eingabe -> ...` zurueck. Darin rufst du zuerst
`this.anwenden(...)` auf und steckst dessen Ergebnis in `naechste.anwenden(...)`.
Das Lambda darf `this` und `naechste` benutzen, beide aendern sich nicht.

Die Tests pruefen die Reihenfolge: `ausrufen.dann(umgedreht)` muss `"!cba"`
ergeben, nicht `"cba!"`. Und `gross.dann(ausrufen).dann(umgedreht)` zeigt,
dass das Ergebnis von `dann` selbst wieder `dann` kann.

Typischer Fehler: `this.anwenden(...)` sofort in `dann` aufrufen. Dort gibt es
aber noch gar keine Eingabe — die kommt erst, wenn jemand die neue
Transformation anwendet.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
default Transformation dann(Transformation naechste) {
    return eingabe -> ...;     // erst this auf eingabe, dann naechste auf das Zwischenergebnis
}
```

</details>

## Aufgabe 10: `alleAnwenden`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 9.5 (`reduce`) — oder einfach eine Schleife (Abschnitt 9.7 sagt
selbst, dass die manchmal klarer ist). Frag dich: Was soll bei einer **leeren**
Liste herauskommen, und wie kommst du bei der Schleife von einem Schritt zum
naechsten?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

**Weg A, Schleife:** Eine Variable mit der Eingabe starten, jede
`Transformation` darauf anwenden und das Ergebnis wieder in dieselbe Variable
schreiben. Leere Liste: Die Schleife laeuft nie, die Eingabe kommt unveraendert
zurueck.

**Weg B, `reduce`:** Mit `dann` aus Aufgabe 9 alle Schritte zu *einer*
Transformation verketten und diese einmal anwenden. `reduce(startwert, verknuepfung)`
braucht dafuer ein neutrales Element — eine Transformation, die nichts veraendert.
Die Verknuepfung ist genau `dann` (als Methodenreferenz).

Vorsicht bei der Variante `reduce(eingabe, (text, t) -> t.anwenden(text), ...)`:
Sie braucht einen dritten Parameter (Combiner), der hier nicht sinnvoll zu
schreiben ist. Nimm lieber Weg A oder B.

</details>

<details><summary>Tipp 3 — Geruest</summary>

Weg A:

```java
String ergebnis = eingabe;
for (Transformation t : schritte) {
    ergebnis = ...;
}
return ergebnis;
```

Weg B:

```java
return schritte.stream()
        .reduce(s -> ..., ...)       // neutrales Element, Verknuepfung
        .anwenden(eingabe);
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Was macht ein Interface "funktional"?</summary>

Es hat **genau eine abstrakte Methode**. `default`- und `static`-Methoden zaehlen
nicht mit — deshalb darf `Transformation` neben `anwenden` auch `dann` haben.
Nur fuer solche Interfaces kann der Compiler ein Lambda einsetzen: Er weiss
dann, welche Methode das Lambda implementiert. `@FunctionalInterface` ist
optional, laesst den Compiler aber pruefen, dass es bei einer abstrakten
Methode bleibt.

</details>

<details><summary>Warum muessen von Lambdas benutzte lokale Variablen effektiv final sein?</summary>

Ein Lambda kann laenger leben als die Methode, in der es entsteht (z. B. `dann`
gibt eines zurueck). Die lokale Variable liegt aber auf dem Stack und ist nach
dem Methodenende weg. Java **kopiert** deshalb ihren Wert ins Lambda. Waere die
Variable danach noch aenderbar, gaebe es zwei verschiedene Werte — das Original
und die Kopie. Die Regel "effektiv final" verhindert diese Verwirrung (und
nebenbei Datenrennen, wenn das Lambda in einem anderen Thread laeuft,
Kapitel 12).

</details>

<details><summary>Warum passiert ohne Terminaloperation gar nichts?</summary>

Streams sind **faul**: `filter`, `map` und Co. beschreiben nur die Pipeline
und liefern einen neuen Stream zurueck. Erst die Terminaloperation (`toList`,
`count`, `forEach`, …) zieht die Elemente einzeln durch alle Stufen. Das spart
Arbeit, z. B. kann `findFirst` nach dem ersten Treffer aufhoeren.

```java
Stream.of("a", "b").peek(System.out::println).map(String::toUpperCase);   // gibt nichts aus
Stream.of("a", "b").peek(System.out::println).toList();                   // gibt a und b aus
```

</details>

<details><summary>Wann `orElse`, wann `orElseGet`?</summary>

Das Argument von `orElse(wert)` wird **immer** ausgewertet, bevor die Methode
aufgerufen wird — auch wenn das `Optional` einen Wert hat. `orElseGet(supplier)`
ruft den `Supplier` nur auf, wenn das `Optional` leer ist. Also: `orElse` fuer
fertige, billige Werte (`orElse(0.0)`, `orElse("")`), `orElseGet`, wenn der
Standardwert teuer ist oder Seiteneffekte hat (`orElseGet(() -> ladeAusDatenbank())`).

</details>

<details><summary>Nenne zwei Faelle, in denen eine Schleife besser ist als ein Stream.</summary>

1. Wenn du **Indizes** brauchst oder mehrere Sammlungen gleichzeitig
   durchlaeufst — z. B. `a[i]` mit `b[i]` vergleichen.
2. Wenn es eigentlich nur um einen **Seiteneffekt** geht (ausgeben,
   in eine bestehende Liste schreiben) — `for (String n : namen)` ist dann
   klarer als `namen.stream().forEach(...)`.

Weitere: Zustand, der ueber mehrere Elemente mitgefuehrt wird (Kapitel 11.5),
fruehes Abbrechen mit komplizierter Bedingung, oder checked Exceptions im
Schleifenrumpf — die lassen sich in Lambdas nicht einfach weiterwerfen.

</details>
