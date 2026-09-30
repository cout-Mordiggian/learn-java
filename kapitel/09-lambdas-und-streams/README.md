# Kapitel 09: Lambdas und Streams

**Ziel:** Du schreibst Verhalten als Wert, baust Stream-Pipelines und weisst,
wann sie das Programm klarer machen, und wann nicht.

---

## 9.1 Lambdas: Verhalten als Wert

Vor Java 8 brauchte man für "ein Stück Verhalten übergeben" eine ganze Klasse:

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

Ein Lambda ist **kein** Objekt-Ersatz für beliebige Interfaces, es
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
| `Predicate<T>` | `boolean test(T)` | prüft |
| `Consumer<T>` | `void accept(T)` | verbraucht |
| `Supplier<T>` | `T get()` | liefert |
| `UnaryOperator<T>` | `T apply(T)` | `Function<T,T>` |
| `BiFunction<T,U,R>` | `R apply(T,U)` | zwei Parameter |
| `BinaryOperator<T>` | `T apply(T,T)` | zwei gleiche Typen |

Für primitive Typen gibt es Sonderformen (`IntPredicate`, `ToIntFunction`, …),
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
Parameters, Konstruktor. Lesbarer, aber nur, wenn der Name für sich spricht.

## 9.4 Effektiv final

```java
int faktor = 3;
Function<Integer, Integer> f = x -> x * faktor;   // Compilerfehler - wegen der naechsten Zeile!
faktor = 4;                                        // macht faktor "nicht effektiv final"
```

Der Compiler meldet den Fehler **im Lambda** (`local variables referenced from a
lambda expression must be final or effectively final`), auch wenn die
eigentliche Ursache die spätere Zuweisung ist. Nimm die Zeile `faktor = 4;`
weg, und alles kompiliert.

Ein Lambda darf lokale Variablen nur lesen, wenn sie **effektiv final** sind,
also nach der Initialisierung nicht mehr verändert werden. Grund: Das Lambda
kann das Ende der Methode überleben; Java kopiert den Wert, statt eine
Referenz auf den Stack-Slot zu halten.

Instanzfelder dürfen sich dagegen ändern, die sind über `this` erreichbar.

## 9.5 Streams

Ein Stream ist **keine** Datenstruktur, sondern eine **Pipeline** über Daten.

```java
List<String> ergebnis = namen.stream()          // Quelle
        .filter(n -> n.length() > 3)            // Zwischenoperation
        .map(String::toUpperCase)               // Zwischenoperation
        .sorted()                               // Zwischenoperation
        .toList();                              // Terminaloperation
```

Drei Eigenschaften, die alles erklären:

1. **Faul**: Zwischenoperationen tun nichts, bis eine Terminaloperation kommt.
   Ohne `toList()` am Ende passiert gar nichts.
2. **Einmalig**: Ein Stream lässt sich nur einmal durchlaufen.
   Zweimal -> `IllegalStateException: stream has already been operated upon`.
3. **Nicht-verändernd**: Die Quelle bleibt unberührt.

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
mächtigste Werkzeug der ganzen API, damit baust du Auswertungen, die
sonst zwanzig Zeilen kosten. `mapping(f, toList())` wandelt dabei jedes Element
einer Gruppe um, bevor es gesammelt wird.

> **Falle bei `toMap`:** Kommt ein Schlüssel doppelt vor (zwei Personen namens
> "Anna"), wirft `toMap` eine `IllegalStateException: Duplicate key`. Wenn das
> passieren kann, gib als drittes Argument an, was dann gelten soll:
> `toMap(Person::getName, Person::getAlter, (alt, neu) -> alt)`.

## 9.6 `Optional`

`Optional<T>` sagt im Typ: "hier ist vielleicht kein Wert". Es ersetzt `null`
als **Rückgabewert**.

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

- `Optional` als **Rückgabetyp**, nicht als Feld, nicht als Parameter
- Nie `optional.get()` ohne vorherige Prüfung; nimm `orElseThrow()`
- Nie `Optional<List<T>>`, eine leere Liste sagt dasselbe einfacher
- Nie `null` in ein `Optional`, dafür gibt es `Optional.ofNullable(x)`

Die Kette `map(...).filter(...).orElse(...)` ist der eigentliche Gewinn: Sie
ersetzt verschachtelte `null`-Prüfungen durch einen linearen Ausdruck.

## 9.7 Wann Streams, und wann nicht

**Gut:** Filtern, Umwandeln, Gruppieren, Aggregieren; Datenflüsse, die sich
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
und teuren, unabhängigen Operationen, und bringt alle Probleme der
Nebenläufigkeit mit (Kapitel 12). Miss nach, statt zu raten.

## 9.8 Strategie und Fabrik mit Lambdas

In 6.9 hast du das **Strategie-Muster** mit Klassen kennengelernt: Das
austauschbare Verhalten steckt hinter einem Interface, der **Kontext** ruft es
auf, ohne zu wissen, welche Implementierung dahintersteckt. Ist das Interface
funktional, ist jede Strategie einfach ein Lambda. Du benutzt das längst:

```java
personen.sort(Comparator.comparing(Person::getAlter));   // Comparator: Vergleichs-Strategie fuer sort
namen.stream().filter(n -> n.length() > 3)               // Predicate: Auswahl-Strategie fuer filter
     .map(String::toUpperCase);                          // Function: Umwandlungs-Strategie fuer map
```

`sort`, `filter` und `map` kennen den Ablauf, das Lambda liefert den variablen
Teil. Die funktionalen Interfaces aus 9.2 sind fertige Strategie-Schnittstellen.
Ein eigenes lohnt sich, wenn der Name etwas sagen soll:

```java
@FunctionalInterface
public interface Rabatt {
    long anwenden(long betragCent);        // Betrag nach Abzug
}

public class Kasse {                       // der Kontext
    private Rabatt rabatt = Rabatte.keiner();
    public void setzeRabatt(Rabatt r) { this.rabatt = r; }
    public long zuZahlenCent()        { return rabatt.anwenden(summeCent); }
}

kasse.setzeRabatt(betrag -> betrag / 2);   // eine Strategie als Lambda
```

Neue Rabattarten brauchen keine Änderung an der Kasse. Eine eigene Klasse pro
Strategie braucht man nur noch, wenn sie Zustand oder mehrere Methoden hat.

**Fabrikmethoden, die Lambdas liefern.** Statt die Lambdas überall hinzuschreiben,
sammelt man sie in statischen Fabrikmethoden (5.10). Das Lambda **fängt** dabei
den Parameter ein, er ist effektiv final (9.4). So merkt sich jede Strategie ihren
eigenen Wert:

```java
public static Rabatt prozent(int prozent) {
    if (prozent < 0 || prozent > 100) {                  // pruefen, BEVOR die Strategie entsteht
        throw new IllegalArgumentException("Prozent: " + prozent);
    }
    return betrag -> betrag - betrag * prozent / 100;    // prozent ist eingefangen
}

Rabatt zehn = Rabatte.prozent(10), zwanzig = Rabatte.prozent(20);   // zwei unabhaengige Strategien
```

Braucht eine Strategie keinen Parameter, reicht **eine** Instanz für alle.
Die legt man als Konstante ab und gibt immer sie zurück. Das kann eine
Fabrikmethode, ein Konstruktor nicht: `new` liefert jedes Mal ein neues Objekt (5.10).

```java
private static final Rabatt KEINER = betrag -> betrag;
public static Rabatt keiner() { return KEINER; }
```

**Die Fabrik wählt die Strategie.** Steht erst zur Laufzeit fest, welche
Strategie gebraucht wird, etwa weil sie als Text in einer Datei oder Eingabe
steht, gehört diese Entscheidung an **eine** Stelle:

```java
public static Rabatt ausCode(String code) {             // "PROZENT15", "MINUS500", "KEIN"
    String c = code.strip().toUpperCase(Locale.ROOT);
    if (c.equals("KEIN"))        return keiner();
    if (c.startsWith("PROZENT")) return prozent(Integer.parseInt(c.substring("PROZENT".length())));
    ...
    throw new IllegalArgumentException("Unbekannter Rabattcode: " + code);
}
```

Der Rest des Programms kennt nur das Interface `Rabatt`. Kommt ein Code dazu,
ändert sich nur diese Methode. Und weil sie die vorhandenen Fabrikmethoden
aufruft, steht jede Regel (z. B. "höchstens 100 %") nur einmal im Code.

**`Supplier` als Fabrik.** Oft gibt man nicht das fertige Objekt mit, sondern
das Rezept, wie man eins erzeugt:

```java
TreeSet<String> s = namen.stream().collect(Collectors.toCollection(TreeSet::new));
gruppen.computeIfAbsent(stadt, k -> new ArrayList<>()).add(name);
```

`TreeSet::new` ist ein `Supplier<TreeSet<String>>`: "Wenn du eine Collection
brauchst, so erzeugst du sie." `computeIfAbsent` ruft die Fabrik nur auf, wenn
der Schlüssel noch fehlt.

Strategien lassen sich auch **kombinieren**: Eine Methode nimmt eine Strategie und
liefert eine neue, die sie einpackt. `Comparator.reversed()` und `thenComparing`
tun das, deine `Transformation.dann` aus Aufgabe 9 ebenso, und
`Rabatte.abMindestwert(5000, r)` in Aufgabe 11 wendet `r` nur ab einem Mindestbetrag
an. Das ist der Dekorierer aus 6.9 in Lambda-Form.

## 9.9 Beobachter und anonyme Klassen

**Problem:** Wird im Lager ein Artikel knapp, sollen der Einkauf, eine E-Mail und
eine Anzeige davon erfahren, nächsten Monat vielleicht noch mehr. Ruft das Lager
alle direkt auf, muss es jeden kennen, und jede Neuerung ändert das Lager.

**Lösung, das Beobachter-Muster** (*Observer*): Das Lager, das **Subjekt**, führt
eine Liste von Beobachtern, die nur ein kleines Interface erfüllen. Wer
informiert werden will, meldet sich an.

```
                          anmelden(b)
   Einkauf  ──┐          ┌──────────────────────┐
   Mail     ──┼────────> │ Lager                │
   Anzeige  ──┘          │  beobachter: [E,M,A] │
                         └──────────┬───────────┘
      Mehl faellt unter 5:          │ fuer jeden b: b.knapp("Mehl", 4)
                                    v
                   Einkauf.knapp(..)  Mail.knapp(..)  Anzeige.knapp(..)
```

```java
@FunctionalInterface
public interface LagerBeobachter {
    void knapp(String artikel, int bestand);
}

lager.anmelden((artikel, bestand) -> System.out.println(artikel + " nachbestellen!"));
```

Ist das Interface funktional, ist jeder Beobachter ein Lambda. Das Lager kennt nur
`LagerBeobachter`, wer dahintersteckt, weiss es nicht.

**Fallstricke:**

- **Abmelden während der Benachrichtigung.** Meldet sich ein Beobachter in
  `knapp()` ab, ändert er die Liste, über die das Lager gerade läuft. Das gibt
  die `ConcurrentModificationException` aus 8.6, oder, noch schlimmer, der
  nächste Beobachter wird still übersprungen. Abhilfe wie in 8.6: über eine
  **Kopie** laufen (`List.copyOf(beobachter)`). Das prüft Aufgabe 13.
- **Vergessenes Abmelden.** Die Liste hält jeden Beobachter am Leben. Wer sich
  nie abmeldet, wird nie vom Garbage Collector weggeräumt, ein Speicherleck.
- **Exceptions.** Wirft ein Beobachter, erfahren die restlichen nichts mehr.
  Robuste Subjekte fangen deshalb pro Beobachter.

Im JDK: `java.util.Observer` ist seit Java 9 veraltet. Heute schreibt man ein
eigenes kleines Interface wie hier. Grafische Oberflächen bestehen aus
Beobachtern (`button.addActionListener(...)`).

**Neu: die anonyme Klasse.** Der erste Codeblock in 9.1 hatte schon eine:
`new Comparator<String>() { ... }`. Das ist eine Klasse ohne Namen, die direkt bei
`new` definiert und sofort instanziiert wird. Ein Lambda kann nicht auf sich selbst
zeigen. Soll sich ein Beobachter selbst abmelden, braucht man deshalb eine
anonyme Klasse, denn darin ist `this` das Objekt selbst:

```java
lager.anmelden(new LagerBeobachter() {          // "eine Klasse, die LagerBeobachter implementiert"
    @Override
    public void knapp(String artikel, int bestand) {
        System.out.println("Einmalige Warnung: " + artikel);
        lager.abmelden(this);                   // this = dieser Beobachter
    }
});
```

Heute nimmst du ein Lambda, ausser du brauchst `this`, eigene Felder oder mehrere
Methoden (dann ist das Interface ohnehin nicht funktional). Die Tests von
Aufgabe 13 benutzen genau so eine Klasse. In einem Lambda heisst `this` dagegen
dasselbe wie in der umgebenden Methode.

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md): erst Tipp 1, dann wieder selbst probieren.

Prüfen mit `./lerne.sh 09`. `Person.java` ist bereits fertig, für die
Aufgaben 1 bis 10 arbeitest du in `Aufgaben.java` und `Transformation.java`, für
11 bis 13 in `Rabatte.java` und `Lager.java`. Die Interfaces `Rabatt` und
`LagerBeobachter` sowie die `Kasse` sind fertig. Lies sie trotzdem, sie gehören
zum Muster.

1. **`geradeQuadrate(List<Integer>)`**: gerade Zahlen filtern, quadrieren.
2. **`laengstesWort(List<String>)`** -> `Optional<String>`.
   Bei Gleichstand das **erste**. *Denkfalle:* Was macht `max` bei Gleichstand
   überhaupt? Die Javadoc sagt es nicht deutlich, schreib dir zwei Zeilen
   und probier es aus, bevor du dich auf eine Annahme verlässt.
3. **`grossgeschriebenSortiert(List<Person>)`** -> `List<String>`,
   Namen in Grossbuchstaben (`toUpperCase(Locale.ROOT)`), alphabetisch.
   Die Eingabe ist nicht sortiert.
4. **`alsListe(List<String>)`** -> `"[a, b, c]"` mit `Collectors.joining`.
5. **`durchschnittsalter(List<Person>)`** -> `double`, leere Liste -> `0.0`.
   Tipp: `mapToInt(...).average()` liefert ein `OptionalDouble`.
6. **`nachStadt(List<Person>)`** -> `Map<String, List<String>>`:
   Stadt -> Namen der dortigen Personen. `groupingBy` mit Downstream-Collector.
7. **`volljaehrigkeit(List<Person>)`** -> `Map<Boolean, List<Person>>`
   mit `partitioningBy`. Volljährig heisst `alter >= 18`.
8. **`namenLaengen(List<Person>)`** -> `Map<String, Integer>` mit `toMap`.
   Du darfst annehmen, dass die Namen eindeutig sind, eine Merge-Funktion
   für doppelte Schlüssel ist nicht nötig.
9. **`Transformation.dann(...)`**: eine `default`-Methode, die zwei
   Transformationen hintereinanderschaltet (Komposition).
   `gross.dann(umgedreht).anwenden("abc")` -> `"CBA"`.
10. **`alleAnwenden(List<Transformation>, String)`**: alle der Reihe nach
    anwenden. Tipp: `reduce`, oder eine schlichte Schleife.
11. **`Rabatte`: Strategien** (9.8): `keiner()`, `prozent(p)`, `festbetrag(cent)`,
    `abMindestwert(mindest, rabatt)`. Jede liefert einen `Rabatt`, am besten als
    Lambda. Ungültige Werte scheitern **sofort**, nicht erst beim Anwenden.
    `keiner()` liefert bei jedem Aufruf dieselbe Instanz.
12. **`Rabatte.ausCode`: Fabrik** (9.8): aus `"KEIN"`, `"PROZENT15"`, `"MINUS500"`
    die passende Strategie, Gross-/Kleinschreibung und Leerzeichen am Rand egal.
    Benutze deine Methoden aus Aufgabe 11, statt die Rechnungen zu wiederholen.
13. **`Lager`: Beobachter** (9.9): Bestände verwalten, Beobachter an- und
    abmelden, benachrichtigen, wenn ein Artikel **unter** den Meldebestand fällt.
    Ein Beobachter darf sich während der Meldung selbst abmelden.

Wirft dein Code in den Aufgaben 11 bis 13 eine Exception, melden die Tests sie als FEHL
mit Datei und Zeile und laufen weiter. Ehrlicher Hinweis: `keiner()` besteht den
Test "dieselbe Instanz" auch ohne Konstante. Warum, steht in `TIPPS.md`.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
Stream<String> s = Stream.of("a", "b", "c")
        .peek(x -> System.out.print(x));
System.out.println("fertig");
```

<details><summary>Auflösung</summary>

`fertig`: Ohne Terminaloperation passiert gar nichts, auch das `peek` nicht. Der Stream ist nur ein Bauplan.

</details>

**2.**

```java
Stream.of("a", "b", "c")
      .peek(System.out::print)
      .filter(x -> !x.equals("b"))
      .map(String::toUpperCase)
      .forEach(System.out::print);
```

<details><summary>Auflösung</summary>

`aAbcC`: Streams arbeiten **Element für Element** die ganze Kette ab, nicht Stufe für Stufe. `a` läuft komplett durch (`a`, dann `A`), `b` bleibt im Filter hängen, dann folgt `c`.

</details>

**3.**

```java
System.out.println(Stream.of(1, 2, 3, 4)
        .filter(n -> n > 5)
        .findFirst());
```

<details><summary>Auflösung</summary>

`Optional.empty`: `findFirst` gibt kein `null` und keine Exception zurück, sondern ein leeres `Optional`. Deshalb der Rückgabetyp.

</details>

**4.**

```java
List<Rabatt> rabatte = new ArrayList<>();
for (int p = 10; p <= 30; p += 10) {
    int satz = p;
    rabatte.add(betrag -> betrag - betrag * satz / 100);
}
for (Rabatt r : rabatte) System.out.print(r.anwenden(1000) + " ");
```

<details><summary>Auflösung</summary>

`900 800 700 `: Jedes Lambda fängt **seine** Kopie von `satz` ein (9.4), deshalb merkt sich jede Strategie ihren eigenen Satz, obwohl alle an derselben Stelle entstehen. `satz` ist in jedem Durchlauf eine neue Variable und effektiv final. `p` selbst dagegen dürfte das Lambda nicht benutzen, `p += 10` ändert es: Compilerfehler. Genau so arbeitet `Rabatte.prozent` in Aufgabe 11.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Was macht ein Interface "funktional"?
- Warum müssen von Lambdas benutzte lokale Variablen effektiv final sein?
- Warum passiert ohne Terminaloperation gar nichts?
- Wann `orElse`, wann `orElseGet`?
- Nenne zwei Fälle, in denen eine Schleife besser ist als ein Stream.
- Warum braucht man für eine Strategie wie `Comparator` heute selten eine eigene Klasse?
- Warum läuft `Lager` beim Benachrichtigen über eine Kopie der Beobachterliste?
