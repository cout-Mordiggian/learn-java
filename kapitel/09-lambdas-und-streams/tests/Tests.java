import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Tests {

    static final List<Person> LEUTE = List.of(
            new Person("Anna", 34, "Koeln"),
            new Person("Bert", 17, "Bonn"),
            new Person("Cem", 42, "Koeln"),
            new Person("Dora", 12, "Bonn"));

    public static void main(String[] args) {

        Pruef.abschnitt("Aufgabe 1: geradeQuadrate");
        Pruef.gleich(List.of(4, 16), Aufgaben.geradeQuadrate(List.of(1, 2, 3, 4)), "1..4");
        Pruef.gleich(List.of(), Aufgaben.geradeQuadrate(List.of(1, 3)), "nur ungerade");
        Pruef.gleich(List.of(), Aufgaben.geradeQuadrate(List.of()), "leere Liste");
        Pruef.gleich(List.of(0, 4), Aufgaben.geradeQuadrate(List.of(0, -1, 2)), "0 ist gerade");

        Pruef.abschnitt("Aufgabe 2: laengstesWort");
        Pruef.gleich(Optional.of("Banane"),
                Aufgaben.laengstesWort(List.of("Apfel", "Banane", "Kiwi")), "klarer Sieger");
        Pruef.gleich(Optional.of("abc"),
                Aufgaben.laengstesWort(List.of("abc", "xyz")), "Gleichstand -> das erste");
        Pruef.gleich(Optional.empty(), Aufgaben.laengstesWort(List.of()), "leere Liste");
        Pruef.gleich(Optional.of("a"), Aufgaben.laengstesWort(List.of("a")), "ein Wort");

        Pruef.abschnitt("Aufgabe 3: grossgeschriebenSortiert");
        Pruef.gleich(List.of("ANNA", "BERT", "CEM", "DORA"),
                Aufgaben.grossgeschriebenSortiert(LEUTE), "vier Namen");
        Pruef.gleich(List.of(), Aufgaben.grossgeschriebenSortiert(List.of()), "leer");
        Pruef.gleich(List.of("ANNA", "BERT", "DORA"), Aufgaben.grossgeschriebenSortiert(List.of(
                        new Person("dora", 1, "x"), new Person("Bert", 1, "x"), new Person("anna", 1, "x"))),
                "unsortierte Eingabe, gemischte Schreibweise");

        Pruef.abschnitt("Aufgabe 4: alsListe");
        Pruef.gleich("[a, b, c]", Aufgaben.alsListe(List.of("a", "b", "c")), "drei Teile");
        Pruef.gleich("[a]", Aufgaben.alsListe(List.of("a")), "ein Teil");
        Pruef.gleich("[]", Aufgaben.alsListe(List.of()), "leer");

        Pruef.abschnitt("Aufgabe 5: durchschnittsalter");
        Pruef.fastGleich(26.25, Aufgaben.durchschnittsalter(LEUTE), "vier Personen");
        Pruef.fastGleich(0.0, Aufgaben.durchschnittsalter(List.of()), "leere Liste");

        Pruef.abschnitt("Aufgabe 6: nachStadt");
        Map<String, List<String>> staedte = Aufgaben.nachStadt(LEUTE);
        Pruef.gleich(2, staedte.size(), "zwei Staedte");
        Pruef.gleich(List.of("Anna", "Cem"), staedte.get("Koeln"), "Koeln");
        Pruef.gleich(List.of("Bert", "Dora"), staedte.get("Bonn"), "Bonn");
        Pruef.gleich(Map.of(), Aufgaben.nachStadt(List.of()), "leere Liste");

        Pruef.abschnitt("Aufgabe 7: volljaehrigkeit");
        Map<Boolean, List<Person>> teile = Aufgaben.volljaehrigkeit(LEUTE);
        Pruef.gleich(2, groesse(teile.get(true)), "zwei volljaehrig");
        Pruef.gleich(2, groesse(teile.get(false)), "zwei minderjaehrig");
        Pruef.gleich("Anna", ersterName(teile.get(true)), "Reihenfolge bleibt erhalten");
        Map<Boolean, List<Person>> grenze = Aufgaben.volljaehrigkeit(List.of(new Person("Eva", 18, "Bonn")));
        Pruef.gleich(1, groesse(grenze.get(true)), "mit genau 18 volljaehrig");
        Map<Boolean, List<Person>> leer = Aufgaben.volljaehrigkeit(List.of());
        Pruef.wahr(leer.containsKey(true) && leer.containsKey(false),
                "partitioningBy liefert immer beide Schluessel");

        Pruef.abschnitt("Aufgabe 8: namenLaengen");
        Pruef.gleich(Map.of("Anna", 4, "Bert", 4, "Cem", 3, "Dora", 4),
                Aufgaben.namenLaengen(LEUTE), "Name -> Laenge");

        Pruef.abschnitt("Aufgabe 9: Transformation.dann");
        Transformation gross = s -> s.toUpperCase();
        Transformation umgedreht = s -> new StringBuilder(s).reverse().toString();
        Transformation ausrufen = s -> s + "!";
        Pruef.gleich("CBA", gross.dann(umgedreht).anwenden("abc"), "gross, dann umgedreht");
        Pruef.gleich("CBA", umgedreht.dann(gross).anwenden("abc"), "umgedreht, dann gross");
        Pruef.gleich("ABC!", gross.dann(ausrufen).anwenden("abc"), "gross, dann ausrufen");
        Pruef.gleich("!cba", ausrufen.dann(umgedreht).anwenden("abc"),
                "Reihenfolge zaehlt: erst ausrufen, dann umgedreht");
        Pruef.gleich("!CBA", gross.dann(ausrufen).dann(umgedreht).anwenden("abc"), "dreifach");

        Pruef.abschnitt("Aufgabe 10: alleAnwenden");
        Pruef.gleich("CBA!", Aufgaben.alleAnwenden(List.of(gross, umgedreht, ausrufen), "abc"),
                "drei Schritte");
        Pruef.gleich("abc", Aufgaben.alleAnwenden(List.of(), "abc"), "keine Schritte");
        Pruef.gleich("ABC", Aufgaben.alleAnwenden(List.of(gross), "abc"), "ein Schritt");

        Pruef.bericht();
    }

    /** null-sichere Helfer, damit ein unfertiges src/ nicht die ganze Pruefung abbricht. */
    private static int groesse(List<?> liste) {
        return liste == null ? -1 : liste.size();
    }

    private static String ersterName(List<Person> liste) {
        return liste == null || liste.isEmpty() ? null : liste.get(0).getName();
    }
}
