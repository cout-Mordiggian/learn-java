import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Kapitel 08 - Collections und Generics.
 * Pruefen:  ./lerne.sh 08
 */
public class Aufgaben {

    /**
     * Aufgabe 1: Wort -> Anzahl. Alles klein, Trennung an Whitespace.
     * "a b A" -> {a=2, b=1};  "" -> {}
     */
    public static Map<String, Integer> haeufigkeiten(String text) {
        // TODO: strip/isEmpty pruefen, toLowerCase(Locale.ROOT), split("\\s+"),
        //       dann map.merge(wort, 1, Integer::sum)
        return new HashMap<>();
    }

    /**
     * Aufgabe 2: Duplikate entfernen, Reihenfolge des ersten Auftretens erhalten.
     * [b, a, b, c] -> [b, a, c]
     */
    public static List<String> deduplizieren(List<String> eingabe) {
        // TODO: Welches Set merkt sich die Einfuegereihenfolge?
        return new ArrayList<>();
    }

    /**
     * Aufgabe 3: Die n haeufigsten Woerter.
     * Absteigend nach Anzahl, bei Gleichstand alphabetisch aufsteigend.
     * Weniger als n Eintraege -> entsprechend kuerzere Liste.
     */
    public static List<String> haeufigsteWoerter(Map<String, Integer> zaehler, int n) {
        // TODO: entrySet in eine Liste, mit einem Comparator sortieren,
        //       dann die ersten n Schluessel einsammeln.
        return new ArrayList<>();
    }

    /**
     * Aufgabe 4: Groesstes Element einer Liste, null bei leerer Liste.
     * Generische Methode mit Schranke.
     */
    public static <T extends Comparable<T>> T groesstes(List<T> liste) {
        // TODO
        return null;
    }

    /**
     * Aufgabe 5: Summe beliebiger Zahlen.
     * Der Wildcard-Typ erlaubt List<Integer>, List<Double>, List<Long> ...
     */
    public static double summe(List<? extends Number> zahlen) {
        // TODO: Number kennt doubleValue()
        return 0.0;
    }

    /**
     * Aufgabe 6: NEUE Liste, sortiert nach Alter, bei Gleichstand nach Name.
     * Die uebergebene Liste bleibt unveraendert.
     */
    public static List<Person> nachAlterDannName(List<Person> personen) {
        // TODO: Kopie anlegen, dann sortieren mit
        //       Comparator.comparingInt(Person::getAlter).thenComparing(Person::getName)
        return new ArrayList<>();
    }

    /**
     * Aufgabe 7: Woerter nach ihrem ersten Buchstaben gruppieren.
     * [Anna, Albert, Bert] -> {A=[Anna, Albert], B=[Bert]}
     * Reihenfolge der Schluessel: Einfuegereihenfolge. Leere Woerter ignorieren.
     */
    public static Map<Character, List<String>> gruppiereNachAnfangsbuchstabe(List<String> woerter) {
        // TODO: map.computeIfAbsent(buchstabe, k -> new ArrayList<>()).add(wort)
        return new LinkedHashMap<>();
    }
}
