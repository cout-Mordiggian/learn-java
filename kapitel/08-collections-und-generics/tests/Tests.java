import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tests {
    public static void main(String[] args) {

        Pruef.abschnitt("Paar: generische Klasse");
        Paar<String, Integer> p = Paar.von("Anna", 34);
        Pruef.gleich("Anna", p.getErstes(), "getErstes");
        Pruef.gleich(34, p.getZweites(), "getZweites");
        Pruef.gleich("(Anna, 34)", p.toString(), "toString");
        Paar<Integer, String> g = p.getauscht();
        Pruef.gleich(34, g.getErstes(), "getauscht: erstes");
        Pruef.gleich("Anna", g.getZweites(), "getauscht: zweites");
        Pruef.wahr(p.equals(Paar.von("Anna", 34)), "equals bei gleichem Inhalt");
        Pruef.falsch(p.equals(Paar.von("Anna", 35)), "equals bei anderem zweiten Wert");
        Pruef.falsch(p.equals(Paar.von("Bert", 34)), "equals bei anderem ersten Wert");
        Pruef.falsch(p.equals(null), "equals(null) ist false");
        Pruef.falsch(p.equals("(Anna, 34)"), "equals mit anderem Typ ist false");
        Pruef.gleich(p.hashCode(), Paar.von("Anna", 34).hashCode(), "hashCode passt zu equals");
        Pruef.wahr(Paar.von(null, null).equals(Paar.von(null, null)), "null-Inhalte sind erlaubt");

        Pruef.abschnitt("Person: Comparable, equals, toString");
        Person anna = new Person("Anna", 34);
        Pruef.gleich("Anna", anna.getName(), "getName");
        Pruef.gleich(34, anna.getAlter(), "getAlter");
        Pruef.gleich("Anna(34)", anna.toString(), "toString");
        Pruef.wahr(anna.compareTo(new Person("Bert", 20)) < 0, "Anna vor Bert");
        Pruef.wahr(new Person("Cem", 1).compareTo(anna) > 0, "Cem nach Anna");
        Pruef.gleich(0, anna.compareTo(new Person("Anna", 99)), "gleicher Name -> 0");
        Pruef.wahr(anna.equals(new Person("Anna", 34)), "equals");
        Pruef.falsch(anna.equals(new Person("Anna", 35)), "equals beachtet auch das Alter");
        Set<Person> menge = new HashSet<>(List.of(new Person("Anna", 34), new Person("Anna", 34)));
        Pruef.gleich(1, menge.size(), "HashSet erkennt Duplikat");

        Pruef.abschnitt("Aufgabe 1: haeufigkeiten");
        Pruef.gleich(Map.of(), Aufgaben.haeufigkeiten(""), "leerer Text");
        Pruef.gleich(Map.of(), Aufgaben.haeufigkeiten("   "), "nur Whitespace");
        Pruef.gleich(Map.of("a", 2, "b", 1), Aufgaben.haeufigkeiten("a b A"), "Gross/Klein egal");
        Pruef.gleich(Map.of("hallo", 2, "welt", 1),
                Aufgaben.haeufigkeiten("hallo   welt\nhallo"), "mehrfacher Whitespace");
        Pruef.gleich(Map.of("a", 1, "b", 1), Aufgaben.haeufigkeiten("  a b  "),
                "fuehrender/abschliessender Whitespace erzeugt kein leeres Wort");

        Pruef.abschnitt("Aufgabe 2: deduplizieren");
        Pruef.gleich(List.of("b", "a", "c"),
                Aufgaben.deduplizieren(List.of("b", "a", "b", "c", "a")), "Reihenfolge erhalten");
        Pruef.gleich(List.of(), Aufgaben.deduplizieren(List.of()), "leere Liste");
        Pruef.gleich(List.of("x"), Aufgaben.deduplizieren(List.of("x", "x", "x")), "alles gleich");

        Pruef.abschnitt("Aufgabe 3: haeufigsteWoerter");
        Map<String, Integer> z = new LinkedHashMap<>();
        z.put("b", 3); z.put("a", 5); z.put("d", 3); z.put("c", 1);
        Pruef.gleich(List.of("a", "b", "d"), Aufgaben.haeufigsteWoerter(z, 3),
                "nach Anzahl, bei Gleichstand alphabetisch");
        Pruef.gleich(List.of("a"), Aufgaben.haeufigsteWoerter(z, 1), "nur das haeufigste");
        Pruef.gleich(List.of("a", "b", "d", "c"), Aufgaben.haeufigsteWoerter(z, 99),
                "n groesser als die Map");
        Pruef.gleich(List.of(), Aufgaben.haeufigsteWoerter(Map.of(), 3), "leere Map");

        Pruef.abschnitt("Aufgabe 4: groesstes (generische Methode)");
        Pruef.gleich(9, Aufgaben.groesstes(List.of(3, 9, 2)), "Integer");
        Pruef.gleich(9, Aufgaben.groesstes(List.of(9, 3, 2)), "Maximum an erster Stelle");
        Pruef.gleich("c", Aufgaben.groesstes(List.of("a", "c", "b")), "String");
        Pruef.gleich(new Person("Cem", 1), Aufgaben.groesstes(
                List.of(new Person("Anna", 1), new Person("Cem", 1))), "Person (Comparable)");
        Pruef.gleich(null, Aufgaben.groesstes(new ArrayList<String>()), "leere Liste -> null");

        Pruef.abschnitt("Aufgabe 5: summe (Wildcard)");
        Pruef.fastGleich(6.0, Aufgaben.summe(List.of(1, 2, 3)), "List<Integer>");
        Pruef.fastGleich(4.5, Aufgaben.summe(List.of(1.5, 3.0)), "List<Double>");
        Pruef.fastGleich(0.0, Aufgaben.summe(List.of()), "leere Liste");

        Pruef.abschnitt("Aufgabe 6: nachAlterDannName");
        List<Person> leute = new ArrayList<>(List.of(
                new Person("Cem", 30), new Person("Anna", 30), new Person("Bert", 20)));
        List<Person> sortiert = Aufgaben.nachAlterDannName(leute);
        Pruef.gleich(List.of(new Person("Bert", 20), new Person("Anna", 30), new Person("Cem", 30)),
                sortiert, "Alter, dann Name");
        Pruef.gleich("Cem", leute.get(0).getName(), "Eingabeliste unveraendert");

        Pruef.abschnitt("Aufgabe 7: gruppiereNachAnfangsbuchstabe");
        Map<Character, List<String>> gruppen =
                Aufgaben.gruppiereNachAnfangsbuchstabe(List.of("Anna", "Albert", "Bert", ""));
        Pruef.gleich(2, gruppen.size(), "zwei Gruppen, leeres Wort ignoriert");
        Pruef.gleich(List.of("Anna", "Albert"), gruppen.get('A'), "Gruppe A");
        Pruef.gleich(List.of("Bert"), gruppen.get('B'), "Gruppe B");
        Pruef.gleich(Map.of(), Aufgaben.gruppiereNachAnfangsbuchstabe(List.of()), "leere Eingabe");
        Map<Character, List<String>> umgekehrt =
                Aufgaben.gruppiereNachAnfangsbuchstabe(List.of("Bert", "Anna", "Cem"));
        Pruef.gleich(List.of('B', 'A', 'C'), new ArrayList<>(umgekehrt.keySet()),
                "Schluessel in Einfuegereihenfolge");

        Pruef.bericht();
    }
}
