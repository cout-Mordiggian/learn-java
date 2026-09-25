import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Kapitel 08 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static Map<String, Integer> haeufigkeiten(String text) {
        Map<String, Integer> zaehler = new LinkedHashMap<>();
        // Locale.ROOT wie in Kapitel 6: ohne Angabe gilt die Systemsprache,
        // und auf einem tuerkischen System wird aus "I" ein i OHNE Punkt.
        String s = text.strip().toLowerCase(Locale.ROOT);
        if (s.isEmpty()) return zaehler;      // sonst liefert split ein [""]
        for (String wort : s.split("\\s+")) {
            // merge(schluessel, wertWennNeu, funktionWennVorhanden)
            // Ersetzt die klassische Sequenz:
            //     Integer alt = map.get(wort);
            //     map.put(wort, alt == null ? 1 : alt + 1);
            // Integer::sum ist eine Methodenreferenz - Kapitel 9.
            zaehler.merge(wort, 1, Integer::sum);
        }
        return zaehler;
    }

    public static List<String> deduplizieren(List<String> eingabe) {
        // LinkedHashSet: Set-Semantik (keine Duplikate) PLUS Merken der
        // Einfuegereihenfolge. Ein HashSet wuerde die Reihenfolge verlieren,
        // ein TreeSet wuerde alphabetisch sortieren.
        Set<String> gesehen = new LinkedHashSet<>(eingabe);
        return new ArrayList<>(gesehen);
    }

    public static List<String> haeufigsteWoerter(Map<String, Integer> zaehler, int n) {
        List<Map.Entry<String, Integer>> eintraege = new ArrayList<>(zaehler.entrySet());
        eintraege.sort(
                // comparingByValue().reversed() -> absteigend nach Anzahl
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        // Gleichstand? Dann alphabetisch aufsteigend nach Schluessel.
                        // Ohne thenComparing waere die Reihenfolge bei Gleichstand
                        // zwar stabil, aber von der Map-Reihenfolge abhaengig -
                        // also nicht vorhersagbar.
                        .thenComparing(Map.Entry.comparingByKey()));

        List<String> ergebnis = new ArrayList<>();
        for (Map.Entry<String, Integer> e : eintraege) {
            if (ergebnis.size() >= n) break;
            ergebnis.add(e.getKey());
        }
        return ergebnis;
        // Mit Streams (Kapitel 9) waere das:
        //   zaehler.entrySet().stream()
        //          .sorted(...).limit(n).map(Map.Entry::getKey).toList();
    }

    public static <T extends Comparable<T>> T groesstes(List<T> liste) {
        // Die Schranke "extends Comparable<T>" ist noetig, damit compareTo
        // ueberhaupt aufgerufen werden darf. Ohne sie waere T nur ein Object.
        if (liste.isEmpty()) return null;
        T max = liste.get(0);
        for (T element : liste) {
            if (element.compareTo(max) > 0) max = element;
        }
        return max;
    }

    public static double summe(List<? extends Number> zahlen) {
        // "? extends Number" = eine Liste von IRGENDEINEM Untertyp von Number.
        // Damit passt List<Integer> genauso wie List<Double>.
        // Lesen ist erlaubt (alles ist eine Number), Hinzufuegen nicht -
        // der Compiler weiss ja nicht, welcher Untertyp genau erlaubt waere.
        // Merksatz PECS: Producer extends, Consumer super.
        double summe = 0;
        for (Number z : zahlen) {
            summe += z.doubleValue();
        }
        return summe;
    }

    public static List<Person> nachAlterDannName(List<Person> personen) {
        // Kopie, damit die Eingabeliste unangetastet bleibt. sort() arbeitet
        // in place - haetten wir direkt sortiert, waere das eine versteckte
        // Nebenwirkung (Kapitel 4).
        List<Person> kopie = new ArrayList<>(personen);
        kopie.sort(Comparator.comparingInt(Person::getAlter)
                             .thenComparing(Person::getName));
        return kopie;
        // comparingInt statt comparing: vermeidet das Boxing zu Integer.
        // Person::getAlter ist eine Methodenreferenz - Kapitel 9.
    }

    public static Map<Character, List<String>> gruppiereNachAnfangsbuchstabe(List<String> woerter) {
        Map<Character, List<String>> gruppen = new LinkedHashMap<>();
        for (String wort : woerter) {
            if (wort.isEmpty()) continue;
            // computeIfAbsent: "hol die Liste zu diesem Schluessel, und wenn
            // es noch keine gibt, lege eine an". Ersetzt:
            //     if (!gruppen.containsKey(c)) gruppen.put(c, new ArrayList<>());
            //     gruppen.get(c).add(wort);
            gruppen.computeIfAbsent(wort.charAt(0), k -> new ArrayList<>()).add(wort);
        }
        return gruppen;
        // Mit Streams: woerter.stream().collect(groupingBy(w -> w.charAt(0)))
    }
}
