# Kapitel 09 — Lambdas und Streams

**Ziel:** Du schreibst Verhalten als Wert, baust Stream-Pipelines und weisst,
wann sie das Programm klarer machen — und wann nicht.

---

## 9.1 Lambdas: Verhalten als Wert

Vor Java 8 brauchte man fuer "ein Stueck Verhalten uebergeben" eine ganze Klasse:

```java
Collections.sort(namen, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
});
```

Mit Lambda:

```java
namen.sort((a, b) -> Integer.compare(a.length(), b.length()));
```

Syntax:

```java
() -> 42                        // keine Parameter
x -> x * 2                      // ein Parameter, Klammern optional
(x, y) -> x + y                 // mehrere Parameter
(String s) -> s.length()        // Typ explizit (selten noetig)
x -> {                          // Block: return ist dann Pflicht
    int y = x * 2;
    return y + 1;
}
```

Ein Lambda ist **kein** Objekt-Ersatz fuer beliebige Interfaces — es
funktioniert nur bei **funktionalen Interfaces**: Interfaces mit **genau einer**
abstrakten Methode.

```java
@FunctionalInterface           // optional, aber der Compiler prueft dann mit
interface Transformation {
    String anwenden(String eingabe);
}

Transformation gross = s -> s.toUpperCase();
gross.anwenden("hallo");       // "HALLO"
```

## 9.2 Die wichtigsten funktionalen Interfaces

Aus `java.util.function`:

| Interface | Methode | Bedeutung |
|-----------|---------|-----------|
| `Function<T,R>` | `R apply(T)` | wandelt um |
| `Predicate<T>` | `boolean test(T)` | prueft |
| `Consumer<T>` | `void accept(T)` | verbraucht |
| `Supplier<T>` | `T get()` | liefert |
| `UnaryOperator<T>` | `T apply(T)` | `Function<T,T>` |
| `BiFunction<T,U,R>` | `R apply(T,U)` | zwei Parameter |
| `BinaryOperator<T>` | `T apply(T,T)` | zwei gleiche Typen |

Fuer primitive Typen gibt es Sonderformen (`IntPredicate`, `ToIntFunction`, …),
die das Boxing vermeiden.

## 9.3 Methodenreferenzen

Wenn ein Lambda nichts anderes tut, als eine bestehende Methode aufzurufen:

```java
s -> s.toUpperCase()         ->   String::toUpperCase     // auf dem Parameter
s -> System.out.println(s)   ->   System.out::println     // auf einem Objekt
s -> Integer.parseInt(s)     ->   Integer::parseInt       // statisch
s -> new Person(s)           ->   Person::new             // Konstruktor
p -> p.getName()             ->   Person::getName
```

Vier Formen: statische Methode, Methode eines bestimmten Objekts, Methode des
Parameters, Konstruktor. Lesbarer — aber nur, wenn der Name fuer sich spricht.

## 9.4 Effektiv final

```java
int faktor = 3;
Function<Integer, Integer> f = x -> x * faktor;   // Compilerfehler - wegen der naechsten Zeile!
faktor = 4;                                        // macht faktor "nicht effektiv final"
```

Der Compiler meldet den Fehler **im Lambda** (`local variables referenced from a
lambda expression must be final or effectively final`), auch wenn die
eigentliche Ursache die spaetere Zuweisung ist. Nimm die Zeile `faktor = 4;`
weg, und alles kompiliert.

Ein Lambda darf lokale Variablen nur lesen, wenn sie **effektiv final** sind —
also nach der Initialisierung nicht mehr veraendert werden. Grund: Das Lambda
kann das Ende der Methode ueberleben; Java kopiert den Wert, statt eine
Referenz auf den Stack-Slot zu halten.

Instanzfelder duerfen sich dagegen aendern — die sind ueber `this` erreichbar.

## 9.5 Streams

Ein Stream ist **keine** Datenstruktur, sondern eine **Pipeline** ueber Daten.

```java
List<String> ergebnis = namen.stream()          // Quelle
        .filter(n -> n.length() > 3)            // Zwischenoperation
        .map(String::toUpperCase)               // Zwischenoperation
        .sorted()                               // Zwischenoperation
        .toList();                              // Terminaloperation
```

Drei Eigenschaften, die alles erklaeren:

1. **Faul**: Zwischenoperationen tun nichts, bis eine Terminaloperation kommt.
   Ohne `toList()` am Ende passiert gar nichts.
2. **Einmalig**: Ein Stream laesst sich nur einmal durchlaufen.
   Zweimal -> `IllegalStateException: stream has already been operated upon`.
3. **Nicht-veraendernd**: Die Quelle bleibt unberuehrt.

### Quellen

```java
liste.stream()
Arrays.stream(array)
Stream.of("a", "b", "c")
IntStream.range(0, 10)              // 0..9
IntStream.rangeClosed(1, 10)        // 1..10
Files.lines(pfad)                   // Kapitel 11
```

### Zwischenoperationen

```java
.filter(p -> p.getAlter() > 18)
.map(Person::getName)
.flatMap(List::stream)              // Liste von Listen flach klopfen
.distinct()
.sorted() / .sorted(comparator)
.limit(10) / .skip(5)
.peek(System.out::println)          // nur zum Debuggen!
.mapToInt(Person::getAlter)         // -> IntStream, kein Boxing
```

### Terminaloperationen

```java
.toList()                           // seit Java 16, unveraenderlich
.collect(Collectors.toList())       // aeltere, veraenderbare Variante
.forEach(System.out::println)
.count()
.anyMatch(p) / .allMatch(p) / .noneMatch(p)
.findFirst() / .findAny()           // -> Optional
.min(cmp) / .max(cmp)               // -> Optional
.reduce(0, Integer::sum)
.sum() / .average() / .max()        // nur auf IntStream/DoubleStream/...
```

### Collectors

```java
import static java.util.stream.Collectors.*;

.collect(toList())
.collect(toSet())
.collect(toMap(Person::getName, Person::getAlter))
.collect(joining(", ", "[", "]"))                    // Trenner, Praefix, Suffix
.collect(groupingBy(Person::getStadt))               // Map<String, List<Person>>
.collect(groupingBy(Person::getStadt, counting()))   // Map<String, Long>
.collect(groupingBy(Person::getStadt,
                    mapping(Person::getName, toList())))  // Map<String, List<String>>
.collect(partitioningBy(p -> p.getAlter() >= 18))    // Map<Boolean, List<Person>>
.collect(summingInt(Person::getAlter))
.collect(averagingInt(Person::getAlter))
```

`groupingBy` mit einem zweiten Collector ("Downstream") ist das
maechtigste Werkzeug der ganzen API — damit baust du Auswertungen, die
sonst zwanzig Zeilen kosten. `mapping(f, toList())` wandelt dabei jedes Element
einer Gruppe um, bevor es gesammelt wird.

> **Falle bei `toMap`:** Kommt ein Schluessel doppelt vor (zwei Personen namens
> "Anna"), wirft `toMap` eine `IllegalStateException: Duplicate key`. Wenn das
> passieren kann, gib als drittes Argument an, was dann gelten soll:
> `toMap(Person::getName, Person::getAlter, (alt, neu) -> alt)`.

## 9.6 `Optional`

`Optional<T>` sagt im Typ: "hier ist vielleicht kein Wert". Es ersetzt `null`
als **Rueckgabewert**.

```java
Optional<Person> gefunden = personen.stream()
        .filter(p -> p.getName().equals("Anna"))
        .findFirst();

gefunden.isPresent()                    // true/false
gefunden.get()                          // wirft, wenn leer - meiden!
gefunden.orElse(standardPerson)
gefunden.orElseGet(() -> teuerBauen())  // faul: nur bei Bedarf
gefunden.orElseThrow(() -> new NoSuchElementException("Anna fehlt"))
gefunden.map(Person::getName)           // Optional<String>
gefunden.filter(p -> p.getAlter() > 18)
gefunden.ifPresent(p -> System.out.println(p));
gefunden.ifPresentOrElse(p -> ..., () -> ...);
```

**Regeln:**

- `Optional` als **Rueckgabetyp** — nicht als Feld, nicht als Parameter
- Nie `optional.get()` ohne vorherige Pruefung; nimm `orElseThrow()`
- Nie `Optional<List<T>>` — eine leere Liste sagt dasselbe einfacher
- Nie `null` in ein `Optional` — dafuer gibt es `Optional.ofNullable(x)`

Die Kette `map(...).filter(...).orElse(...)` ist der eigentliche Gewinn: Sie
ersetzt verschachtelte `null`-Pruefungen durch einen linearen Ausdruck.

## 9.7 Wann Streams — und wann nicht

**Gut:** Filtern, Umwandeln, Gruppieren, Aggregieren; Datenfluesse, die sich
als Kette lesen lassen.

**Schlecht:**

```java
// Einfache Schleife mit Seiteneffekt - als Stream nur umstaendlicher:
namen.stream().forEach(n -> System.out.println(n));   // -> for (String n : namen)

// Zustand ausserhalb aendern - verfehlt den Zweck komplett:
List<String> ergebnis = new ArrayList<>();
namen.stream().filter(...).forEach(ergebnis::add);    // -> .filter(...).toList()

// Indizes gebraucht, mehrere Sammlungen parallel - Schleife ist klarer.
```

Und `.parallelStream()`: fast nie. Es lohnt erst bei sehr grossen Datenmengen
und teuren, unabhaengigen Operationen — und bringt alle Probleme der
Nebenlaeufigkeit mit (Kapitel 12). Miss nach, statt zu raten.

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Zwei Dateien in [`src/`](src/) — pruefen mit `./lerne.sh 09`.
`Person.java` ist bereits fertig, du arbeitest in `Aufgaben.java` und
`Transformation.java`.

1. **`geradeQuadrate(List<Integer>)`** — gerade Zahlen filtern, quadrieren.
2. **`laengstesWort(List<String>)`** -> `Optional<String>`.
   Bei Gleichstand das **erste**. *Denkfalle:* Was macht `max` bei Gleichstand
   ueberhaupt? Die Javadoc sagt es nicht deutlich — schreib dir zwei Zeilen
   und probier es aus, bevor du dich auf eine Annahme verlaesst.
3. **`grossgeschriebenSortiert(List<Person>)`** -> `List<String>`,
   Namen in Grossbuchstaben (`toUpperCase(Locale.ROOT)`), alphabetisch.
   Die Eingabe ist nicht sortiert.
4. **`alsListe(List<String>)`** -> `"[a, b, c]"` mit `Collectors.joining`.
5. **`durchschnittsalter(List<Person>)`** -> `double`, leere Liste -> `0.0`.
   Tipp: `mapToInt(...).average()` liefert ein `OptionalDouble`.
6. **`nachStadt(List<Person>)`** -> `Map<String, List<String>>`:
   Stadt -> Namen der dortigen Personen. `groupingBy` mit Downstream-Collector.
7. **`volljaehrigkeit(List<Person>)`** -> `Map<Boolean, List<Person>>`
   mit `partitioningBy`. Volljaehrig heisst `alter >= 18`.
8. **`namenLaengen(List<Person>)`** -> `Map<String, Integer>` mit `toMap`.
   Du darfst annehmen, dass die Namen eindeutig sind — eine Merge-Funktion
   fuer doppelte Schluessel ist nicht noetig.
9. **`Transformation.dann(...)`** — eine `default`-Methode, die zwei
   Transformationen hintereinanderschaltet (Komposition).
   `gross.dann(umgedreht).anwenden("abc")` -> `"CBA"`.
10. **`alleAnwenden(List<Transformation>, String)`** — alle der Reihe nach
    anwenden. Tipp: `reduce` — oder eine schlichte Schleife.

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
Stream<String> s = Stream.of("a", "b", "c")
        .peek(x -> System.out.print(x));
System.out.println("fertig");
```

<details><summary>Aufloesung</summary>

`fertig` — Ohne Terminaloperation passiert gar nichts, auch das `peek` nicht. Der Stream ist nur ein Bauplan.

</details>

**2.**

```java
Stream.of("a", "b", "c")
      .peek(System.out::print)
      .filter(x -> !x.equals("b"))
      .map(String::toUpperCase)
      .forEach(System.out::print);
```

<details><summary>Aufloesung</summary>

`aAbcC` — Streams arbeiten **Element fuer Element** die ganze Kette ab, nicht Stufe fuer Stufe. `a` laeuft komplett durch (`a`, dann `A`), `b` bleibt im Filter haengen, dann folgt `c`.

</details>

**3.**

```java
System.out.println(Stream.of(1, 2, 3, 4)
        .filter(n -> n > 5)
        .findFirst());
```

<details><summary>Aufloesung</summary>

`Optional.empty` — `findFirst` gibt kein `null` und keine Exception zurueck, sondern ein leeres `Optional`. Deshalb der Rueckgabetyp.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Was macht ein Interface "funktional"?
- Warum muessen von Lambdas benutzte lokale Variablen effektiv final sein?
- Warum passiert ohne Terminaloperation gar nichts?
- Wann `orElse`, wann `orElseGet`?
- Nenne zwei Faelle, in denen eine Schleife besser ist als ein Stream.
