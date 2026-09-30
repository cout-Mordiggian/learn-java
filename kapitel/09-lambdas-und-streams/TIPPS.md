# Kapitel 09: Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf, jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `geradeQuadrate`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5 (Streams, Zwischenoperationen). Die Aufgabe hat zwei Schritte:
erst *auswählen*, dann *umwandeln*. Frag dich: Welche Zwischenoperation
wählt aus, welche wandelt um, und welche Terminaloperation macht daraus
wieder eine `List`?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`filter` bekommt ein `Predicate<Integer>`, `map` eine `Function<Integer, Integer>`,
am Ende `toList()`. "Gerade" prüfst du mit dem Rest-Operator `%`.

Die Tests prüfen auch `0` (ist gerade, `0 * 0 = 0`) und `-1` (ist ungerade).
Prüfe auf `== 0`. Wer stattdessen `!= 1` schreibt, hält `-1` fälschlich für
gerade, denn `-1 % 2` ist in Java `-1`, nicht `1`.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return zahlen.stream()
        .filter(z -> ...)
        .map(z -> ...)
        .toList();
```

</details>

## Aufgabe 2: `laengstesWort`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5 (Terminaloperationen `min`/`max`) und 9.6 (`Optional`).
`max` liefert schon ein `Optional`, genau den Rückgabetyp, den du brauchst.
Die eigentliche Frage: *Wonach* wird verglichen, und welches Element gewinnt
bei Gleichstand?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`max` braucht einen `Comparator<String>`. Den baust du dir mit
`Comparator.comparingInt(...)` aus einer Funktion, die jedem Wort eine Zahl
zuordnet.

Denkfalle Gleichstand (Test: `["abc", "xyz"]` muss `"abc"` liefern): Probier es
in `jshell` aus, statt zu raten:

```java
Stream.of("abc", "xyz").max((a, b) -> 0)     // Comparator, der alles fuer gleich haelt
```

Wenn du dich nicht auf dieses Verhalten verlassen willst, geht es auch explizit
mit `reduce((a, b) -> ...)`: Du behältst `a`, ausser `b` ist *echt* länger.

Leere Liste und Ein-Wort-Liste erledigt `max` von selbst.

</details>

<details><summary>Tipp 3: Gerüst</summary>

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

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.3 (Methodenreferenzen) und 9.5 (`map`, `sorted`). Du brauchst aus
jeder `Person` nur den Namen. Frag dich: In welcher Reihenfolge müssen
Umwandeln und Sortieren stehen, damit "anna" und "Bert" richtig einsortiert
werden?

</details>

<details><summary>Tipp 2: Ansatz</summary>

Erst `map(Person::getName)`, dann in Grossbuchstaben umwandeln, **dann**
`sorted()`. `toUpperCase(Locale.ROOT)` passt nicht als einfache
Methodenreferenz, weil es ein Argument braucht, nimm dort ein Lambda.

Denkfalle: Sortierst du *vor* dem Grossschreiben, gilt die natürliche
String-Ordnung, und da stehen alle Grossbuchstaben vor allen Kleinbuchstaben:
`["dora", "Bert", "anna"]` sortiert ergibt `[Bert, anna, dora]`. Der Test
"gemischte Schreibweise" fällt dann durch.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return personen.stream()
        .map(...)                         // Person -> Name
        .map(n -> ...)                    // Name -> NAME
        .sorted()
        .toList();
```

</details>

## Aufgabe 4: `alsListe`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5, Collectors. Suche den Collector, der Strings zu *einem* String
verbindet, und der ausser dem Trenner auch noch Anfang und Ende kennt.

</details>

<details><summary>Tipp 2: Ansatz</summary>

`Collectors.joining` gibt es mit einem, zwei oder drei Argumenten. Du brauchst
die Variante mit drei: Trenner, Präfix, Suffix. Achte auf das Leerzeichen im
Trenner (`"a, b"`, nicht `"a,b"`).

Randfall leere Liste: Der Test erwartet `"[]"`. Präfix und Suffix bleiben bei
`joining` auch dann stehen, wenn gar kein Element kommt, du brauchst also
keinen Sonderfall.

</details>

## Aufgabe 5: `durchschnittsalter`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5 (`mapToInt`, `average()`) und 9.6 (`Optional`). Frag dich: Was
ist der Durchschnitt von *keinen* Werten, und wie sagt Java dir das im Typ?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`mapToInt(Person::getAlter)` macht aus dem `Stream<Person>` einen `IntStream`.
Dessen `average()` liefert ein `OptionalDouble`, das bei leerem Stream leer ist.
Aus einem `OptionalDouble` holst du den Wert mit einem Standardwert heraus,
dieselbe Idee wie `orElse` bei `Optional`.

Nicht selbst `summe / anzahl` rechnen: Bei leerer Liste ist das `0 / 0`, und bei
`int`-Division wäre ausserdem `26.25` zu `26` abgeschnitten.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return personen.stream()
        .mapToInt(...)
        .average()
        .orElse(...);
```

</details>

## Aufgabe 6: `nachStadt`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5, Collectors, besonders `groupingBy` mit Downstream-Collector.
Frag dich: Wonach wird gruppiert, und was soll *pro Gruppe* in der Liste
stehen: die ganze `Person` oder nur ihr Name?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`groupingBy(Person::getStadt)` allein liefert `Map<String, List<Person>>`,
der Compiler meckert dann über den Rückgabetyp. Das zweite Argument
(Downstream) bestimmt, was mit den Elementen einer Gruppe passiert:
`Collectors.mapping(umwandlung, sammler)` wandelt jedes Element um und sammelt
es dann.

Die Tests erwarten `["Anna", "Cem"]` in genau dieser Reihenfolge, die
Reihenfolge innerhalb einer Gruppe entspricht der Eingabe, das passt von
selbst. Leere Liste ergibt eine leere Map, auch das ohne Sonderfall.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return personen.stream()
        .collect(Collectors.groupingBy(
                ...,                                   // Klassifizierer: Stadt
                Collectors.mapping(..., ...)));        // Name, dann in eine Liste
```

</details>

## Aufgabe 7: `volljaehrigkeit`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5, Collectors: `partitioningBy`. Eine Aufteilung in genau zwei
Hälften nach einer Ja/Nein-Frage. Frag dich: Welches `Predicate<Person>`
beschreibt "volljährig"?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`Collectors.partitioningBy(praedikat)` liefert `Map<Boolean, List<Person>>`.
Zwei Randfälle aus den Tests:

- Eva ist **genau 18** und gilt als volljährig, also `>=`, nicht `>`.
- Bei leerer Eingabe müssen trotzdem beide Schlüssel `true` und `false` da
  sein. Genau das garantiert `partitioningBy`; `groupingBy` mit demselben
  Prädikat liefert dagegen eine leere Map, und der Test scheitert.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return personen.stream()
        .collect(Collectors.partitioningBy(p -> ...));
```

</details>

## Aufgabe 8: `namenLaengen`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5, Collectors: `toMap`. Pro Person entsteht genau ein Eintrag.
Frag dich: Welche Funktion liefert den **Schlüssel**, welche den **Wert**?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`Collectors.toMap(schluesselFunktion, wertFunktion)`: beide sind
`Function<Person, ...>`. Der Schlüssel geht als Methodenreferenz, für den
Wert brauchst du ein kleines Lambda, weil zwei Aufrufe hintereinander nötig
sind (erst der Name, dann dessen Länge).

Eine Merge-Funktion brauchst du laut Aufgabe nicht. Merk dir trotzdem:
Bei doppelten Schlüsseln wirft `toMap` eine `IllegalStateException`.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
return personen.stream()
        .collect(Collectors.toMap(..., p -> ...));
```

</details>

## Aufgabe 9: `Transformation.dann`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.1 (Lambdas, eigenes funktionales Interface). `dann` führt noch
nichts aus, sondern **liefert eine neue** `Transformation`. Frag dich: Was soll
diese neue Transformation mit ihrer Eingabe tun, und wer wird zuerst
angewendet, `this` oder `naechste`?

</details>

<details><summary>Tipp 2: Ansatz</summary>

Du gibst ein Lambda `eingabe -> ...` zurück. Darin rufst du zuerst
`this.anwenden(...)` auf und steckst dessen Ergebnis in `naechste.anwenden(...)`.
Das Lambda darf `this` und `naechste` benutzen, beide ändern sich nicht.

Die Tests prüfen die Reihenfolge: `ausrufen.dann(umgedreht)` muss `"!cba"`
ergeben, nicht `"cba!"`. Und `gross.dann(ausrufen).dann(umgedreht)` zeigt,
dass das Ergebnis von `dann` selbst wieder `dann` kann.

Typischer Fehler: `this.anwenden(...)` sofort in `dann` aufrufen. Dort gibt es
aber noch gar keine Eingabe, die kommt erst, wenn jemand die neue
Transformation anwendet.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
default Transformation dann(Transformation naechste) {
    return eingabe -> ...;     // erst this auf eingabe, dann naechste auf das Zwischenergebnis
}
```

</details>

## Aufgabe 10: `alleAnwenden`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.5 (`reduce`), oder einfach eine Schleife (Abschnitt 9.7 sagt
selbst, dass die manchmal klarer ist). Frag dich: Was soll bei einer **leeren**
Liste herauskommen, und wie kommst du bei der Schleife von einem Schritt zum
nächsten?

</details>

<details><summary>Tipp 2: Ansatz</summary>

**Weg A, Schleife:** Eine Variable mit der Eingabe starten, jede
`Transformation` darauf anwenden und das Ergebnis wieder in dieselbe Variable
schreiben. Leere Liste: Die Schleife läuft nie, die Eingabe kommt unverändert
zurück.

**Weg B, `reduce`:** Mit `dann` aus Aufgabe 9 alle Schritte zu *einer*
Transformation verketten und diese einmal anwenden. `reduce(startwert, verknuepfung)`
braucht dafür ein neutrales Element, eine Transformation, die nichts verändert.
Die Verknüpfung ist genau `dann` (als Methodenreferenz).

Vorsicht bei der Variante `reduce(eingabe, (text, t) -> t.anwenden(text), ...)`:
Sie braucht einen dritten Parameter (Combiner), der hier nicht sinnvoll zu
schreiben ist. Nimm lieber Weg A oder B.

</details>

<details><summary>Tipp 3: Gerüst</summary>

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

## Aufgabe 11: Rabatt-Strategien

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.8 und 9.1. `Rabatt` hat genau eine Methode
`long anwenden(long betragCent)`, also ist jedes Lambda `betrag -> ...` ein
Rabatt. Jede der vier Methoden prüft ihre Parameter und gibt dann ein Lambda
zurück. Frag dich: Welche Prüfung gehört **vor** das `return`, und was
rechnet das Lambda **selbst**?

</details>

<details><summary>Tipp 2: Ansatz</summary>

- `keiner`: `betrag -> betrag`. "Immer dieselbe Instanz" bekommst du sicher mit
  einer Konstanten: `private static final Rabatt KEINER = betrag -> betrag;` und
  `return KEINER;`.
- `prozent`: erst `prozent < 0 || prozent > 100` -> `IllegalArgumentException`,
  dann `return betrag -> betrag - betrag * prozent / 100;`. Ganzzahlige Division
  rundet den **Abzug** ab, genau wie verlangt. Das Lambda darf `prozent` benutzen,
  weil der Parameter effektiv final ist (9.4). So merkt sich jede Strategie ihren
  eigenen Satz.
- `festbetrag`: negativ -> Exception. Das Lambda nimmt `Math.max(0, ...)`.
- `abMindestwert`: `Objects.requireNonNull(rabatt, "rabatt")` **vor** dem Lambda,
  sonst fällt `null` erst beim Anwenden auf. Im Lambda: Ist der Betrag
  mindestens `mindestCent`, `rabatt.anwenden(betrag)`, sonst `betrag`.

Warum besteht `keiner()` den Test auch ohne Konstante? Ein Lambda, das keine
Variablen von aussen benutzt, erzeugt die übliche JVM (HotSpot) nur einmal und
gibt danach immer dasselbe Objekt heraus. Das ist ein Implementierungsdetail, die
Sprachspezifikation verspricht es nicht. Erst die Konstante macht es zu einer
Zusage, auf die sich Aufrufer verlassen dürfen. Bei `prozent(10)` klappt der
Trick nicht, das Lambda fängt `prozent` ein, deshalb entsteht jedes Mal ein
neues Objekt.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
private static final Rabatt KEINER = ...;

public static Rabatt prozent(int prozent) {
    if (...) {
        throw new IllegalArgumentException("...");
    }
    return betrag -> ...;
}

public static Rabatt abMindestwert(long mindestCent, Rabatt rabatt) {
    Objects.requireNonNull(rabatt, "rabatt");
    return betrag -> betrag >= mindestCent ? ... : ...;
}
```

</details>

## Aufgabe 12: `Rabatte.ausCode`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.8, "Die Fabrik wählt die Strategie". Erst den Code vereinheitlichen, dann anhand des
Anfangs entscheiden, welche deiner Methoden aus Aufgabe 11 zuständig ist. Frag
dich: Wie viele der ungültigen Fälle aus dem Test behandeln `prozent` und
`festbetrag` schon von selbst?

</details>

<details><summary>Tipp 2: Ansatz</summary>

`code.strip().toUpperCase(Locale.ROOT)` macht aus `"  prozent15 "` ein
`"PROZENT15"`. Ist `code` `null`, wirft schon `strip()` die verlangte
`NullPointerException`. Deutlicher ist ein `Objects.requireNonNull` am Anfang.

Dann: `equals("KEIN")` -> `keiner()`. `startsWith("PROZENT")` -> den Rest mit
`substring("PROZENT".length())` abschneiden, mit `Integer.parseInt` umwandeln
und an `prozent(...)` geben. Genauso `MINUS` mit `Long.parseLong` und
`festbetrag`. Alles andere -> `IllegalArgumentException`.

Die ungültigen Codes erledigen sich dann fast von selbst:
`"PROZENT"`, `"PROZENTabc"` und `"PROZENT 10"` lassen `parseInt` eine
`NumberFormatException` werfen, und die **ist** eine `IllegalArgumentException`
(Unterklasse, Kapitel 7). `"PROZENT150"` und `"MINUS-5"` lehnen `prozent` bzw.
`festbetrag` ab. Das ist der Gewinn, wenn die Fabrik vorhandene Methoden benutzt,
statt selbst zu rechnen: Jede Regel steht nur an einer Stelle.

`Locale.ROOT`: Ohne Angabe richtet sich `toUpperCase` nach der Sprache des
Rechners. Auf einem türkischen System wird aus `"i"` ein `"İ"`, und `"minus"`
passt nicht mehr zu `"MINUS"`.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
public static Rabatt ausCode(String code) {
    Objects.requireNonNull(code, "code");
    String c = code.strip().toUpperCase(Locale.ROOT);
    if (c.equals("KEIN")) {
        return ...;
    }
    if (c.startsWith("PROZENT")) {
        return prozent(Integer.parseInt(c.substring(...)));
    }
    if (...) {
        ...
    }
    throw new IllegalArgumentException("Unbekannter Rabattcode: " + code);
}
```

</details>

## Aufgabe 13: `Lager`

<details><summary>Tipp 1: Richtung</summary>

Abschnitt 9.9. Zuerst nur die Bestände (`einlagern`, `entnehmen`, `bestand`),
bis der erste Testblock grün ist. Dann `anmelden`/`abmelden` und die Meldung.
Frag dich bei der Meldung: Welche **zwei** Bestände musst du vergleichen, damit
nur das Unterschreiten meldet und nicht jede Entnahme darunter?

</details>

<details><summary>Tipp 2: Ansatz</summary>

- `bestand`: `bestaende.getOrDefault(artikel, 0)`.
- `einlagern`: `menge <= 0` -> `IllegalArgumentException`, sonst
  `bestaende.merge(artikel, menge, Integer::sum)` (addiert oder legt neu an).
- `entnehmen`: Menge prüfen. `vorher = bestand(artikel)`. `menge > vorher` ->
  `IllegalStateException`, **bevor** du etwas änderst. Dann
  `nachher = vorher - menge` speichern. Melden, wenn
  `vorher >= meldebestand && nachher < meldebestand`.
- `anmelden`: `Objects.requireNonNull`, dann nur hinzufügen, wenn
  `!beobachter.contains(b)`.
- `abmelden`: `beobachter.remove(b)` tut bei Unbekannten einfach nichts.
- Benachrichtigen: `for (LagerBeobachter b : List.copyOf(beobachter))`. Ohne die
  Kopie meldet sich der erste Beobachter im Test ab, während du über die Liste
  läufst. Dann wirft der Iterator eine `ConcurrentModificationException` (8.6), oder
  bei nur zwei Beobachtern wird der zweite still übersprungen. Probier es aus.

</details>

<details><summary>Tipp 3: Gerüst</summary>

```java
public void entnehmen(String artikel, int menge) {
    pruefeMenge(menge);                  // eigene kleine private Methode
    int vorher = bestand(artikel);
    if (menge > vorher) {
        throw new IllegalStateException("...");
    }
    int nachher = ...;
    bestaende.put(artikel, nachher);
    if (vorher >= meldebestand && ...) {
        for (LagerBeobachter b : List.copyOf(beobachter)) {
            b.knapp(..., ...);
        }
    }
}
```

</details>

---

## Selbstcheck: Antworten

<details><summary>Was macht ein Interface "funktional"?</summary>

Es hat **genau eine abstrakte Methode**. `default`- und `static`-Methoden zählen
nicht mit, deshalb darf `Transformation` neben `anwenden` auch `dann` haben.
Nur für solche Interfaces kann der Compiler ein Lambda einsetzen: Er weiss
dann, welche Methode das Lambda implementiert. `@FunctionalInterface` ist
optional, lässt den Compiler aber prüfen, dass es bei einer abstrakten
Methode bleibt.

</details>

<details><summary>Warum müssen von Lambdas benutzte lokale Variablen effektiv final sein?</summary>

Ein Lambda kann länger leben als die Methode, in der es entsteht (z. B. `dann`
gibt eines zurück). Die lokale Variable liegt aber auf dem Stack und ist nach
dem Methodenende weg. Java **kopiert** deshalb ihren Wert ins Lambda. Wäre die
Variable danach noch änderbar, gäbe es zwei verschiedene Werte, das Original
und die Kopie. Die Regel "effektiv final" verhindert diese Verwirrung (und
nebenbei Datenrennen, wenn das Lambda in einem anderen Thread läuft,
Kapitel 12).

</details>

<details><summary>Warum passiert ohne Terminaloperation gar nichts?</summary>

Streams sind **faul**: `filter`, `map` und Co. beschreiben nur die Pipeline
und liefern einen neuen Stream zurück. Erst die Terminaloperation (`toList`,
`count`, `forEach`, …) zieht die Elemente einzeln durch alle Stufen. Das spart
Arbeit, z. B. kann `findFirst` nach dem ersten Treffer aufhören.

```java
Stream.of("a", "b").peek(System.out::println).map(String::toUpperCase);   // gibt nichts aus
Stream.of("a", "b").peek(System.out::println).toList();                   // gibt a und b aus
```

</details>

<details><summary>Wann `orElse`, wann `orElseGet`?</summary>

Das Argument von `orElse(wert)` wird **immer** ausgewertet, bevor die Methode
aufgerufen wird, auch wenn das `Optional` einen Wert hat. `orElseGet(supplier)`
ruft den `Supplier` nur auf, wenn das `Optional` leer ist. Also: `orElse` für
fertige, billige Werte (`orElse(0.0)`, `orElse("")`), `orElseGet`, wenn der
Standardwert teuer ist oder Seiteneffekte hat (`orElseGet(() -> ladeAusDatenbank())`).

</details>

<details><summary>Nenne zwei Fälle, in denen eine Schleife besser ist als ein Stream.</summary>

1. Wenn du **Indizes** brauchst oder mehrere Sammlungen gleichzeitig
   durchläufst, z. B. `a[i]` mit `b[i]` vergleichen.
2. Wenn es eigentlich nur um einen **Seiteneffekt** geht (ausgeben,
   in eine bestehende Liste schreiben), `for (String n : namen)` ist dann
   klarer als `namen.stream().forEach(...)`.

Weitere: Zustand, der über mehrere Elemente mitgeführt wird (Kapitel 11.5),
frühes Abbrechen mit komplizierter Bedingung, oder checked Exceptions im
Schleifenrumpf, die lassen sich in Lambdas nicht einfach weiterwerfen.

</details>

<details><summary>Warum braucht man für eine Strategie wie `Comparator` heute selten eine eigene Klasse?</summary>

`List.sort` ist der Kontext. Es kennt den Ablauf des Sortierens, nicht aber, wie
zwei Elemente verglichen werden. Diesen austauschbaren Teil bekommt es als
Objekt, das ein Interface erfüllt (`Comparator`). Das ist genau das
Strategie-Muster. Weil `Comparator` ein funktionales Interface ist, genügt ein
Lambda oder eine Fabrikmethode wie `Comparator.comparing(Person::getAlter)`.
Früher brauchte jede Sortierreihenfolge eine eigene Klasse. Eine Klasse lohnt
sich heute nur noch, wenn die Strategie Zustand oder mehrere Methoden hat.

</details>

<details><summary>Warum läuft `Lager` beim Benachrichtigen über eine Kopie der Beobachterliste?</summary>

Weil ein Beobachter in `knapp()` beliebigen Code ausführen darf, auch
`lager.abmelden(this)` oder `lager.anmelden(...)`. Das ändert die Liste, über
die das Lager gerade läuft. Der Iterator von `ArrayList` bemerkt das und wirft
eine `ConcurrentModificationException`. In einem Sonderfall (Abmelden beim
vorletzten Element) merkt er es nicht und beendet die Schleife still, dann
bekommt der letzte Beobachter keine Meldung. Beides fällt nur bei bestimmten
Beobachtern auf, also spät. Mit `List.copyOf(beobachter)` läuft die Schleife
über einen Schnappschuss. Änderungen wirken erst bei der nächsten Meldung.
Alternative: `CopyOnWriteArrayList` (12.6) kopiert bei jedem Schreiben selbst.

</details>
