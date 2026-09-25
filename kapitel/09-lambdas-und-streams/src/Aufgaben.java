import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Kapitel 09 - Lambdas und Streams.
 * Pruefen:  ./lerne.sh 09
 *
 * Loese die Aufgaben 1-8 moeglichst mit Streams - darum geht es hier.
 */
public class Aufgaben {

    /**
     * Aufgabe 1: gerade Zahlen herausfiltern und quadrieren.
     * [1,2,3,4] -> [4,16]
     */
    public static List<Integer> geradeQuadrate(List<Integer> zahlen) {
        // TODO: stream().filter(...).map(...).toList()
        return List.of();
    }

    /**
     * Aufgabe 2: laengstes Wort, bei Gleichstand das erste.
     * Leere Liste -> Optional.empty()
     */
    public static Optional<String> laengstesWort(List<String> woerter) {
        // TODO: Welches Element liefert max() bei Gleichstand?
        //       Rate nicht - probier es mit zwei gleich langen Woertern aus.
        return Optional.empty();
    }

    /**
     * Aufgabe 3: Namen in Grossbuchstaben, alphabetisch sortiert.
     */
    public static List<String> grossgeschriebenSortiert(List<Person> personen) {
        // TODO: toUpperCase(Locale.ROOT) - ohne Locale haengt das Ergebnis
        //       von der Systemsprache ab (Stichwort: tuerkisches i).
        return List.of();
    }

    /**
     * Aufgabe 4: ["a","b","c"] -> "[a, b, c]",  [] -> "[]"
     */
    public static String alsListe(List<String> teile) {
        // TODO: Collectors.joining(trenner, praefix, suffix)
        return "";
    }

    /**
     * Aufgabe 5: Durchschnittsalter, leere Liste -> 0.0
     */
    public static double durchschnittsalter(List<Person> personen) {
        // TODO: mapToInt(...).average() gibt OptionalDouble zurueck.
        return 0.0;
    }

    /**
     * Aufgabe 6: Stadt -> Namen der dort wohnenden Personen.
     * Tipp: groupingBy mit mapping(...) als Downstream-Collector.
     */
    public static Map<String, List<String>> nachStadt(List<Person> personen) {
        // TODO
        return Map.of();
    }

    /**
     * Aufgabe 7: aufteilen in volljaehrig (>= 18) und nicht.
     * partitioningBy liefert IMMER beide Schluessel - auch wenn eine Seite leer ist.
     */
    public static Map<Boolean, List<Person>> volljaehrigkeit(List<Person> personen) {
        // TODO
        return Map.of();
    }

    /**
     * Aufgabe 8: Name -> Laenge des Namens.
     * Die Namen sind eindeutig - doppelte Schluessel musst du nicht behandeln.
     */
    public static Map<String, Integer> namenLaengen(List<Person> personen) {
        // TODO: Collectors.toMap(schluesselFunktion, wertFunktion)
        return Map.of();
    }

    /**
     * Aufgabe 10: alle Transformationen der Reihe nach anwenden.
     * Leere Liste -> Eingabe unveraendert.
     */
    public static String alleAnwenden(List<Transformation> schritte, String eingabe) {
        // TODO
        return eingabe;
    }
}
