# Kapitel 08 — Collections und Generics

**Ziel:** Du waehlst die passende Datenstruktur bewusst aus, kennst den
Zusammenhang zwischen `hashCode` und `HashMap` und schreibst eigene
generische Typen.

---

## 8.1 Die Landkarte

```
Iterable
└── Collection
    ├── List      geordnet, Duplikate erlaubt, Zugriff per Index
    │   ├── ArrayList     Array im Inneren  -> get O(1), add hinten O(1), Einfuegen vorn O(n)
    │   └── LinkedList    Kette im Inneren  -> get O(n), Einfuegen vorn O(1)
    ├── Set       keine Duplikate
    │   ├── HashSet          ungeordnet, O(1)
    │   ├── LinkedHashSet    Einfuegereihenfolge, O(1)
    │   └── TreeSet          sortiert, O(log n)
    └── Queue / Deque
        └── ArrayDeque       Stapel und Warteschlange

Map  (gehoert NICHT zu Collection!)
├── HashMap          ungeordnet, O(1)
├── LinkedHashMap    Einfuegereihenfolge
└── TreeMap          nach Schluessel sortiert, O(log n)
```

*O(1)* heisst "konstant, egal wie gross die Sammlung", *O(n)* "waechst mit der
Anzahl der Elemente", *O(log n)* "waechst sehr langsam" (bei einer Million
Elementen ca. 20 Schritte).

**Die Auswahlregel:**

| Du brauchst | Nimm |
|-------------|------|
| eine Liste in Reihenfolge | `ArrayList` |
| jedes Element hoechstens einmal | `HashSet` |
| Schluessel -> Wert | `HashMap` |
| sortiert | `TreeSet` / `TreeMap` |
| Einfuegereihenfolge erhalten | `LinkedHashSet` / `LinkedHashMap` |
| Stapel oder Warteschlange | `ArrayDeque` |

`ArrayList` ist in ueber 90 % der Faelle richtig. `LinkedList` wirkt in der
Theorie oft besser, ist in der Praxis wegen schlechter Cache-Nutzung fast immer
langsamer — nimm sie nur, wenn du wirklich staendig vorn einfuegst.

## 8.2 Gegen Interfaces programmieren

```java
List<String> namen = new ArrayList<>();      // gut
ArrayList<String> namen = new ArrayList<>(); // unnoetig festgelegt
```

Links der **allgemeinste** Typ, den du brauchst; rechts die konkrete
Implementierung. Dann kostet der Wechsel zu `LinkedList` genau ein Wort.

Das `<>` heisst *Diamond Operator*: Der Compiler liest den Typ von links ab.

## 8.3 `List`

```java
List<String> namen = new ArrayList<>();
namen.add("Anna");
namen.add("Bert");
namen.add(0, "Cem");         // an Position einfuegen

namen.get(0);                // "Cem"
namen.set(0, "Dora");        // ersetzen
namen.remove("Dora");        // nach Objekt entfernen
namen.remove(0);             // nach Index entfernen (!)
namen.size();
namen.contains("Anna");
namen.indexOf("Anna");
namen.isEmpty();

for (String n : namen) { ... }
```

> **Falle:** Bei `List<Integer>` bedeutet `liste.remove(1)` "Index 1", nicht
> "die Zahl 1". Fuer den Wert: `liste.remove(Integer.valueOf(1))`.

> **Noch eine Falle:** Aus einer Collection kommen `Integer`-**Objekte**, keine
> `int`s. Und bei Objekten vergleicht `==` die Referenz (Kapitel 3):
>
> ```java
> Integer a = 127, b = 127;
> a == b          // true  - Java haelt kleine Werte (-128..127) vorraetig
> Integer c = 128, d = 128;
> c == d          // false - zwei verschiedene Objekte!
> c.equals(d)     // true
> ```
>
> Genau wie beim String-Pool: Es funktioniert im kleinen Test und versagt mit
> echten Daten. `Integer` immer mit `equals` vergleichen — oder vorher in ein
> `int` auspacken (`int x = liste.get(0);`).

### Unveraenderliche Listen

```java
List<String> fest = List.of("a", "b", "c");   // seit Java 9
fest.add("d");                                // UnsupportedOperationException
```

`List.of` ist ideal fuer Konstanten und Rueckgabewerte, die niemand aendern soll.
Achtung: `List.of` erlaubt **kein** `null`. Ebenso gibt es `Set.of(...)` und
`Map.of("a", 1, "b", 2)`. Eine unveraenderliche **Kopie** einer bestehenden
Liste bekommst du mit `List.copyOf(liste)`.

Der alte Weg `Arrays.asList(...)` liefert etwas Halbgares: feste Groesse, aber
`set` erlaubt — und die Liste ist an das urspruengliche Array gekoppelt.

## 8.4 `Set` — und warum `hashCode` jetzt zaehlt

```java
Set<String> tags = new HashSet<>();
tags.add("java");
tags.add("java");            // wird ignoriert
tags.size();                 // 1
```

Wie erkennt das `HashSet` das Duplikat? In zwei Schritten:

1. `hashCode()` bestimmt den **Eimer** (Bucket), in dem gesucht wird.
2. Innerhalb des Eimers entscheidet `equals()`.

Deshalb der Vertrag aus Kapitel 5: **gleiche Objekte muessen gleichen hashCode
haben.** Sonst landen sie in verschiedenen Eimern, `equals` wird nie gefragt,
und dasselbe Objekt liegt zweimal in der Menge.

```java
class Person {                            // equals/hashCode NICHT ueberschrieben
    String name;
    Person(String name) { this.name = name; }
}
Set<Person> s = new HashSet<>();
s.add(new Person("Anna"));
s.add(new Person("Anna"));
s.size();                                 // 2 - der Bug
```

Noch heimtueckischer: Aenderst du ein Feld, nachdem das Objekt in der Menge
liegt, aendert sich sein `hashCode` — und das Objekt ist nicht mehr auffindbar,
obwohl es drinsteckt. **Nimm unveraenderliche Objekte als Schluessel.**

## 8.5 `Map`

```java
Map<String, Integer> alter = new HashMap<>();
alter.put("Anna", 34);
alter.get("Anna");                       // 34
alter.get("Unbekannt");                  // null  (nicht 0!)
alter.getOrDefault("Unbekannt", 0);      // 0
alter.containsKey("Anna");
alter.remove("Anna");

alter.putIfAbsent("Bert", 20);           // nur setzen, wenn noch nicht da

// Zaehlen: +1, oder auf 1 setzen, wenn der Schluessel neu ist
Map<String, Integer> anzahl = new HashMap<>();
anzahl.merge("java", 1, Integer::sum);

// Gruppieren: Liste anlegen, wenn noch keine da ist, dann hinzufuegen
Map<String, List<String>> nachStadt = new HashMap<>();
nachStadt.computeIfAbsent("Koeln", k -> new ArrayList<>()).add("Anna");

for (Map.Entry<String, Integer> e : alter.entrySet()) {
    System.out.println(e.getKey() + " ist " + e.getValue());
}
for (String k : alter.keySet()) { ... }
for (int v : alter.values()) { ... }
```

`merge` und `computeIfAbsent` ersetzen die klassische
"erst pruefen, dann einfuegen"-Sequenz durch eine Zeile — und du wirst sie in
diesem Kapitel brauchen.

Die Reihenfolge beim Durchlaufen einer `HashMap` ist **nicht festgelegt** und
kann sich zwischen Java-Versionen aendern. Verlass dich nie darauf — brauchst
du eine Ordnung, nimm `TreeMap` (sortiert) oder `LinkedHashMap` (Einfuegereihenfolge).

### Seit Java 21: erstes und letztes Element

Listen, `LinkedHashSet`, `TreeSet`, `ArrayDeque` & Co. haben gemeinsame
Methoden bekommen (*Sequenced Collections*):

```java
namen.getFirst();     // statt namen.get(0)
namen.getLast();      // statt namen.get(namen.size() - 1)
namen.reversed();     // umgekehrte Sicht, ohne Kopie
```

## 8.6 `ConcurrentModificationException`

```java
for (String n : namen) {
    if (n.startsWith("A")) namen.remove(n);   // knallt
}
```

Waehrend einer `for-each`-Schleife darf die Collection nicht strukturell
veraendert werden. Drei Auswege:

```java
namen.removeIf(n -> n.startsWith("A"));       // am besten

Iterator<String> it = namen.iterator();        // explizit
while (it.hasNext()) {
    if (it.next().startsWith("A")) it.remove();
}

for (String n : new ArrayList<>(namen)) { ... }  // ueber eine Kopie laufen
```

## 8.7 Sortieren: `Comparable` und `Comparator`

**`Comparable`** — die *natuerliche* Ordnung, in der Klasse selbst:

```java
public class Person implements Comparable<Person> {
    @Override
    public int compareTo(Person andere) {
        return this.name.compareTo(andere.name);
    }
}
Collections.sort(personen);      // oder personen.sort(null)
```

Der Rueckgabewert: **negativ** wenn `this` kleiner, **0** bei Gleichheit,
**positiv** wenn groesser. Nicht `-1/0/1` selbst basteln — nimm
`Integer.compare(a, b)`. (`a - b` kann ueberlaufen!)

**`Comparator`** — beliebig viele alternative Ordnungen, ausserhalb der Klasse:

```java
personen.sort(Comparator.comparing(Person::getName));
personen.sort(Comparator.comparingInt(Person::getAlter).reversed());

personen.sort(Comparator
        .comparingInt(Person::getAlter)
        .thenComparing(Person::getName));      // Gleichstand nach Name
```

Nur eine natuerliche Ordnung pro Klasse, beliebig viele Comparatoren. Wenn du
schwankst, welche Ordnung "die richtige" ist, ist es ein `Comparator`.

Sinnvoll: `compareTo` sollte mit `equals` konsistent sein
(`a.compareTo(b) == 0` genau dann, wenn `a.equals(b)`). `TreeSet` benutzt
naemlich `compareTo` statt `equals` — bei Inkonsistenz verschwinden Elemente.

## 8.8 Generics

### Warum?

```java
List liste = new ArrayList();          // roher Typ - vor Java 5
liste.add("text");
Integer i = (Integer) liste.get(0);    // kompiliert, knallt zur Laufzeit

List<String> liste = new ArrayList<>();
Integer i = liste.get(0);              // Compilerfehler - viel besser
```

Generics verlagern Typfehler von der Laufzeit in die Compile-Zeit.

### Eigene generische Typen

```java
public class Box<T> {
    private final T inhalt;
    public Box(T inhalt) { this.inhalt = inhalt; }
    public T get() { return inhalt; }
}

Box<String> b = new Box<>("hallo");
String s = b.get();                    // kein Cast noetig
```

Konvention fuer Typparameter: `T` (Type), `E` (Element), `K`/`V` (Key/Value),
`R` (Result).

### Generische Methoden

```java
public static <T extends Comparable<T>> T groesstes(List<T> liste) { ... }
//            ^^^^^^^^^^^^^^^^^^^^^^^^ vor dem Rueckgabetyp deklariert
```

`extends Comparable<T>` ist eine **Schranke**: Nur Typen, die sich vergleichen
lassen. Erst dadurch darfst du im Rumpf `compareTo` aufrufen.

### Wildcards und PECS

```java
double summe(List<? extends Number> zahlen)   // lesen: Producer Extends
void fuelle(List<? super Integer> ziel)       // schreiben: Consumer Super
```

**PECS: Producer `extends`, Consumer `super`.**

Der Grund: `List<Integer>` ist **kein** `List<Number>` — sonst koennte man
ueber die `Number`-Sicht ein `Double` in die `Integer`-Liste legen. Mit
`? extends Number` darfst du lesen (alles ist eine `Number`), aber nichts
hineinlegen.

### Typloeschung (Type Erasure)

Generics existieren nur zur Compile-Zeit. Zur Laufzeit ist aus `List<String>`
ein schlichtes `List` geworden. Folgen:

```java
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
a.getClass() == b.getClass()      // true!

new T()                           // geht nicht
new T[10]                         // geht nicht
if (obj instanceof List<String>)  // geht nicht (obj vom Typ Object)
```

Das ist der Preis dafuer, dass Generics 2004 rueckwaertskompatibel eingefuehrt
wurden. Man arrangiert sich damit.

## 8.9 `Collections` und `Objects`

```java
Collections.sort(liste);
Collections.reverse(liste);
Collections.shuffle(liste);
Collections.max(liste);
Collections.unmodifiableList(liste);   // Nur-Lese-Sicht
Collections.emptyList();

Objects.equals(a, b);            // null-sicher
Objects.hash(a, b, c);
Objects.requireNonNull(x, "x");
Objects.toString(x, "-");
```

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Drei Dateien in [`src/`](src/) — pruefen mit `./lerne.sh 08`.

### `Paar.java` — eine eigene generische Klasse

`Paar<A, B>` mit `getErstes()`, `getZweites()`, `toString()` -> `(a, b)`,
`equals`/`hashCode` und:

```java
public Paar<B, A> getauscht()          // Typen tauschen!
public static <A, B> Paar<A, B> von(A a, B b)   // statische Fabrikmethode
```

Die statische Fabrikmethode braucht eigene Typparameter — die der Klasse
gelten nur fuer Instanzmethoden. Das ist der Klassiker, an dem Generics
"klick" machen.

### `Person.java`

`name` (String), `alter` (int), `implements Comparable<Person>` (nach Name),
plus `equals`/`hashCode`/`toString` -> `Anna(34)`.

### `Aufgaben.java`

1. **`haeufigkeiten(String text)`** -> `Map<String, Integer>`.
   Kleinschreibung mit `toLowerCase(Locale.ROOT)`, an Whitespace trennen
   (auch fuehrender Whitespace darf kein leeres Wort erzeugen), leerer Text ->
   leere Map. Tipp: `merge(wort, 1, Integer::sum)`.
2. **`deduplizieren(List<String>)`** — Duplikate raus, **Reihenfolge erhalten**.
   Welche `Set`-Implementierung kann das?
3. **`haeufigsteWoerter(Map<String,Integer>, int n)`** -> `List<String>`.
   Absteigend nach Anzahl; bei Gleichstand alphabetisch aufsteigend.
4. **`groesstes(List<T>)`** — generische Methode mit Schranke,
   `null` bei leerer Liste.
5. **`summe(List<? extends Number>)`** -> `double`. Wildcard.
6. **`nachAlterDannName(List<Person>)`** — **neue** sortierte Liste,
   aufsteigend nach Alter, bei Gleichstand nach Name.
   Nutze `Comparator.comparingInt(...).thenComparing(...)`.
7. **`gruppiereNachAnfangsbuchstabe(List<String>)`** ->
   `Map<Character, List<String>>`, Schluessel in der Reihenfolge des ersten
   Auftretens, leere Woerter ignorieren. Tipp: `computeIfAbsent`.

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
List<Integer> l = new ArrayList<>(List.of(10, 20, 30));
l.remove(1);
System.out.println(l);
```

<details><summary>Aufloesung</summary>

`[10, 30]` — `remove(1)` mit einem `int` ist `remove(int index)`, nicht `remove(Object)`. Entfernt wird das Element an **Index** 1, nicht die Zahl 1.

</details>

**2.**

```java
Set<String> s = new TreeSet<>(List.of("b", "C", "a"));
System.out.println(s);
```

<details><summary>Aufloesung</summary>

`[C, a, b]` — `TreeSet` sortiert nach `String.compareTo`, und das vergleicht Unicode-Werte: Grossbuchstaben (`C` = 67) kommen vor Kleinbuchstaben (`a` = 97). Fuer eine "menschliche" Ordnung: `new TreeSet<>(String.CASE_INSENSITIVE_ORDER)`.

</details>

**3.**

```java
Map<String, Integer> m = new HashMap<>();
m.put("a", 1);
m.put("a", 2);
System.out.println(m.size() + " " + m.get("a") + " " + m.get("b"));
```

<details><summary>Aufloesung</summary>

`1 2 null` — Ein Schluessel kommt hoechstens einmal vor, das zweite `put` **ersetzt** den Wert. Ein fehlender Schluessel liefert `null`, keine Exception und keine `0`.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Wann `ArrayList`, wann `HashSet`, wann `TreeMap`?
- Was passiert, wenn du `equals` ueberschreibst und `hashCode` vergisst?
- Warum wirft `liste.remove(x)` in einer for-each-Schleife?
- `Comparable` oder `Comparator` — woran machst du das fest?
- Warum ist `List<Integer>` kein `List<Number>`?

---

**Wie geht es weiter?** Empfohlen ist jetzt [Kapitel 14 — Algorithmen und Datenstrukturen](../14-algorithmen-und-datenstrukturen/README.md):
Dort baust du `ArrayList` und einen Stapel selbst nach und siehst, warum die Auswahlregel aus 8.1 stimmt. Danach geht es mit Kapitel 9 weiter.
