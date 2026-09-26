# Java-Syntax auf einen Blick

## Programmgerüst

```java
package de.firma.app;          // optional, muss erste Zeile sein
import java.util.List;

public class Start {
    public static void main(String[] args) {
        System.out.println("Hallo");
    }
}
```

## Variablen und Typen

```java
int i = 42;                    long l = 42L;
double d = 3.14;               float f = 3.14f;
char c = 'A';                  boolean b = true;
String s = "Text";             var x = new ArrayList<String>();
final double MWST = 0.19;      static final int MAX = 100;

int gross = 2_000_000_000;     // Unterstriche als Lesehilfe
int hex = 0xFF;  int bin = 0b1010;  int okt = 010;   // okt ist 8! fuehrende 0 = oktal
```

## Operatoren

```java
+  -  *  /  %              // % = Rest
++  --  +=  -=  *=  /=
==  !=  <  >  <=  >=
&&  ||  !                  // kurzschluessig
bedingung ? a : b          // ternaer
instanceof Typ variable    // Pattern Matching
```

## Kontrollfluss

```java
if (a) { } else if (b) { } else { }

String s = switch (x) {             // Ausdruck (bevorzugt) - liefert einen Wert
    case 1, 2 -> "wenig";
    case 3    -> "mittel";
    default   -> { yield "viel"; }
};                                  // Semikolon, weil es ein Ausdruck ist

switch (obj) {                      // Pattern Matching (Java 21)
    case Integer i when i > 0 -> ...;
    case String t             -> ...;
    case null, default        -> ...;
}

while (b) { }
do { } while (b);
for (int i = 0; i < n; i++) { }
for (var e : sammlung) { }
break;  continue;  return wert;
```

## Methoden

```java
public static int summe(int a, int b) { return a + b; }
public static int summeAlle(int... zahlen) { }        // Varargs
private void tuWas() { }
```

## Klassen

```java
public class Konto {
    private static int anzahl = 0;
    private final String inhaber;
    private long guthaben;

    public Konto(String inhaber) {
        this.inhaber = inhaber;
    }
    public Konto() { this("unbekannt"); }             // this(...) = anderer Konstruktor

    public long getGuthaben() { return guthaben; }
    public static int getAnzahl() { return anzahl; }

    @Override public String toString() { return "..."; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Konto k)) return false;
        return inhaber.equals(k.inhaber);
    }
    @Override public int hashCode() { return Objects.hash(inhaber); }
}
```

## Vererbung und Interfaces

```java
public abstract class Figur {
    protected Figur(String name) { }
    public abstract double flaeche();
}

public interface Skalierbar {
    Figur skaliert(double f);
    default Figur verdoppelt() { return skaliert(2); }
    static boolean gueltig(double f) { return f > 0; }
}

public class Kreis extends Figur implements Skalierbar {
    public Kreis() { super("Kreis"); }
    @Override public double flaeche() { return 0; }
    @Override public Figur skaliert(double f) { return null; }
}

public sealed interface Form permits Kreis2, Quadrat2 { }
```

| Modifier | Klasse | Paket | Unterklasse | überall |
|----------|:--:|:--:|:--:|:--:|
| `private` | X | | | |
| (nichts) | X | X | | |
| `protected` | X | X | X | |
| `public` | X | X | X | X |

## Records und Enums

```java
public record Punkt(double x, double y) {
    public Punkt {                          // kompakter Konstruktor
        if (x < 0) throw new IllegalArgumentException();
    }
    public double laenge() { return Math.hypot(x, y); }
    public static Punkt ursprung() { return new Punkt(0, 0); }
}
p.x();  p.equals(q);  p.toString();        // alles automatisch

public enum Ampel {
    ROT(30), GRUEN(45);
    private final int sekunden;
    Ampel(int sekunden) { this.sekunden = sekunden; }
    public int getSekunden() { return sekunden; }
}
Ampel.values();  Ampel.valueOf("ROT");  a.name();  a.ordinal();
```

## Exceptions

```java
try {
    riskant();
} catch (NumberFormatException | ArithmeticException e) {
    e.getMessage();
} catch (Exception e) {
    throw new IllegalStateException("Kontext", e);   // cause nicht vergessen
} finally {
    // laeuft immer
}

try (var r = Files.newBufferedReader(p)) { }         // schliesst automatisch

throw new IllegalArgumentException("Grund");
public void m() throws IOException { }

class MeinFehler extends Exception { }               // checked
class MeinBug extends RuntimeException { }           // unchecked
```

## Arrays

```java
int[] a = new int[5];
int[] b = {1, 2, 3};
int[][] m = new int[3][4];
a.length;  a[0];  a.clone();

Arrays.toString(a);  Arrays.sort(a);  Arrays.copyOf(a, 10);
Arrays.fill(a, 7);   Arrays.equals(a, b);  Arrays.stream(a).sum();
```

## Strings

```java
s.length()  s.charAt(0)  s.substring(1, 3)  s.indexOf("x")
s.contains("x")  s.startsWith("x")  s.endsWith("x")
s.strip()  s.isBlank()  s.isEmpty()  s.repeat(3)
s.toUpperCase()  s.replace("a", "b")  s.split(";", -1)
s.equals(t)  s.equalsIgnoreCase(t)  s.compareTo(t)
String.join("-", liste)  String.format(Locale.ROOT, "%.2f", d)
"...".formatted(a, b)

new StringBuilder().append(x).reverse().toString()

String t = """
        Textblock
        """;
```

Platzhalter: `%s` `%d` `%f` `%.2f` `%02d` `%-10s` `%n` `%%`

## Collections

```java
List<String> l = new ArrayList<>();       l.add(x); l.get(0); l.remove(0);
Set<String> s = new HashSet<>();          s.add(x); s.contains(x);
Map<String, Integer> m = new HashMap<>(); m.put(k, v); m.get(k);
Deque<String> d = new ArrayDeque<>();     d.push(x); d.pop();

m.getOrDefault(k, 0);
m.merge(k, 1, Integer::sum);
m.computeIfAbsent(k, x -> new ArrayList<>()).add(v);
m.putIfAbsent(k, v);
for (var e : m.entrySet()) { e.getKey(); e.getValue(); }

List.of(1, 2, 3);   Set.of("a");   Map.of("k", 1);   // unveraenderlich
List.copyOf(liste);                                    // unveraenderliche Kopie
liste.getFirst();   liste.getLast();   liste.reversed();   // seit Java 21
liste.removeIf(x -> x.isEmpty());
liste.sort(Comparator.comparing(Person::getName).thenComparing(...).reversed());
```

## Lambdas und Streams

```java
Runnable r = () -> tuWas();
Function<String, Integer> f = String::length;
Predicate<String> p = s -> s.isEmpty();

liste.stream()
     .filter(x -> x > 0)
     .map(Object::toString)
     .sorted()
     .distinct()
     .limit(10)
     .toList();

.mapToInt(Person::getAlter).sum() / .average().orElse(0)
.anyMatch(p)  .allMatch(p)  .count()  .findFirst()  .reduce(0, Integer::sum)

.collect(Collectors.joining(", ", "[", "]"))
.collect(Collectors.groupingBy(Person::getStadt))
.collect(Collectors.groupingBy(Person::getStadt, Collectors.counting()))
.collect(Collectors.partitioningBy(p -> p.getAlter() >= 18))
.collect(Collectors.groupingBy(Person::getStadt,
         Collectors.mapping(Person::getName, Collectors.toList())))
.collect(Collectors.toMap(Person::getName, Person::getAlter))   // doppelter Schluessel -> Exception

optional.orElse(x)  .orElseGet(() -> x)  .orElseThrow()
        .map(f)  .filter(p)  .ifPresent(c)  .isPresent()
```

## Dateien

```java
Path p = Path.of("ordner", "datei.txt");
Files.readString(p, UTF_8);        Files.readAllLines(p, UTF_8);
Files.writeString(p, text, UTF_8); Files.write(p, zeilen);
Files.exists(p);  Files.size(p);  Files.createDirectories(p);
try (Stream<String> z = Files.lines(p, UTF_8)) { }    // MUSS geschlossen werden
```

## Nebenläufigkeit

```java
Thread t = new Thread(() -> tuWas());  t.start();  t.join();

try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
    Future<Long> f = pool.submit(() -> berechne());
    long e = f.get();
    List<Future<Integer>> alle = pool.invokeAll(aufgaben);
}

private final AtomicInteger z = new AtomicInteger();  z.incrementAndGet();
private volatile boolean stopp;                        // Sichtbarkeit fuer Flags
public synchronized void m() { }
Map<K, V> m = new ConcurrentHashMap<>();
```

## Nützliche Statik

```java
Math.max/min/abs/pow/sqrt/hypot/round/floor/ceil/random
Math.PI  Math.E
Integer.parseInt(s)  Integer.compare(a, b)  Integer.MAX_VALUE
Double.parseDouble(s)  Double.compare(a, b)
String.valueOf(x)
Objects.equals(a, b)  Objects.hash(a, b)  Objects.requireNonNull(x)
Character.isDigit/isLetter/isLetterOrDigit/toLowerCase
Math.addExact(a, b)  Math.floorMod(a, b)             // Ueberlauf-Exception / Rest >= 0
System.currentTimeMillis()  System.nanoTime()
```
