import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Kapitel 09 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static List<Integer> geradeQuadrate(List<Integer> zahlen) {
        return zahlen.stream()
                .filter(z -> z % 2 == 0)      // Predicate<Integer>
                .map(z -> z * z)              // Function<Integer, Integer>
                .toList();                    // seit Java 16: unveraenderliche Liste
        // Vor Java 16: .collect(Collectors.toList()) - liefert eine
        // veraenderbare ArrayList. toList() ist heute die bessere Wahl.
    }

    public static Optional<String> laengstesWort(List<String> woerter) {
        return woerter.stream()
                .max(Comparator.comparingInt(String::length));
        // max() ist intern reduce(BinaryOperator.maxBy(cmp)), und maxBy ist
        //     (a, b) -> cmp.compare(a, b) >= 0 ? a : b
        // Bei Gleichstand (compare == 0) gewinnt also a - das zuerst gesehene.
        // Genau das wollten wir. Verlassen sollte man sich darauf trotzdem nur,
        // wenn man es geprueft hat: die Javadoc garantiert es nicht ausdruecklich.
        // Explizit und unabhaengig von diesem Detail waere:
        //     .reduce((a, b) -> b.length() > a.length() ? b : a)
    }

    public static List<String> grossgeschriebenSortiert(List<Person> personen) {
        return personen.stream()
                .map(Person::getName)         // Methodenreferenz statt p -> p.getName()
                .map(n -> n.toUpperCase(Locale.ROOT))   // Locale.ROOT: sonst Systemsprache
                .sorted()                     // natuerliche Ordnung von String
                .toList();
    }

    public static String alsListe(List<String> teile) {
        // joining(trenner, praefix, suffix) - bei leerem Stream bleiben
        // Praefix und Suffix trotzdem stehen, das Ergebnis ist also "[]".
        return teile.stream().collect(Collectors.joining(", ", "[", "]"));
    }

    public static double durchschnittsalter(List<Person> personen) {
        return personen.stream()
                .mapToInt(Person::getAlter)   // Stream<Person> -> IntStream (kein Boxing)
                .average()                    // OptionalDouble - leer bei leerem Stream
                .orElse(0.0);
        // average() gibt OptionalDouble zurueck, weil der Durchschnitt von
        // null Werten nicht definiert ist. orElse macht daraus unsere 0.0.
    }

    public static Map<String, List<String>> nachStadt(List<Person> personen) {
        return personen.stream()
                .collect(Collectors.groupingBy(
                        Person::getStadt,                            // Klassifizierer
                        Collectors.mapping(Person::getName,          // Downstream: erst
                                Collectors.toList())));              // umwandeln, dann sammeln
        // Ohne mapping bekaemest du Map<String, List<Person>>.
        // Der Downstream-Collector bestimmt, was PRO GRUPPE passiert -
        // das ist der Hebel, mit dem groupingBy so maechtig wird:
        //   counting()             -> Map<String, Long>
        //   summingInt(getAlter)   -> Map<String, Integer>
        //   averagingInt(getAlter) -> Map<String, Double>
    }

    public static Map<Boolean, List<Person>> volljaehrigkeit(List<Person> personen) {
        return personen.stream()
                .collect(Collectors.partitioningBy(p -> p.getAlter() >= 18));
        // Unterschied zu groupingBy(p -> p.getAlter() >= 18):
        // partitioningBy garantiert BEIDE Schluessel (true und false),
        // auch wenn eine Haelfte leer bleibt. groupingBy laesst leere
        // Gruppen einfach weg - das ist ein haeufiger NullPointer-Fallstrick.
    }

    public static Map<String, Integer> namenLaengen(List<Person> personen) {
        return personen.stream()
                .collect(Collectors.toMap(Person::getName, p -> p.getName().length()));
        // Laut Aufgabe sind die Namen eindeutig. Achtung: toMap wirft eine
        // IllegalStateException bei doppelten Schluesseln.
        // Wenn Duplikate moeglich sind, braucht es ein drittes Argument, die
        // Merge-Funktion:  toMap(k, v, (alt, neu) -> alt)
    }

    public static String alleAnwenden(List<Transformation> schritte, String eingabe) {
        // Die Idee: erst ALLE Schritte mit dann() aus Aufgabe 9 zu EINER
        // Transformation verketten, dann diese einmal anwenden.
        //   - s -> s ist das neutrale Element: "nichts tun". Bei leerer
        //     Liste kommt genau das heraus, also die Eingabe unveraendert.
        //   - dann() ist assoziativ: (a.dann(b)).dann(c) wirkt wie
        //     a.dann(b.dann(c)). Genau das verlangt reduce - deshalb waere
        //     das Ergebnis sogar mit parallelStream() korrekt.
        return schritte.stream()
                .reduce(s -> s, Transformation::dann)
                .anwenden(eingabe);
        // Verlockend, aber vertragswidrig waere
        //     reduce(eingabe, (text, t) -> t.anwenden(text), (a, b) -> b)
        // Sequentiell funktioniert das zufaellig, aber der Combiner (a, b) -> b
        // ist kein korrektes Zusammenfuegen - parallel kaeme Unsinn heraus.
        //
        // Ehrlicherweise ist die schlichte Schleife genauso gut:
        //     String ergebnis = eingabe;
        //     for (Transformation t : schritte) ergebnis = t.anwenden(ergebnis);
        //     return ergebnis;
        // Wer die Komposition oben nicht auf Anhieb durchschaut, nimmt die Schleife.
    }
}
