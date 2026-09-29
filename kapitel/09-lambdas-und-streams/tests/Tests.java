import java.util.ArrayList;
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

        // ------------------------------------------------------------ Strategie
        Pruef.abschnitt("Aufgabe 11: Rabatt-Strategien");
        sicher("Rabatte.keiner", () -> {
            Pruef.gleich(1234L, Rabatte.keiner().anwenden(1234), "keiner: Betrag bleibt");
            Pruef.wahr(Rabatte.keiner() == Rabatte.keiner(),
                    "keiner() liefert immer dieselbe Instanz (eine Fabrikmethode darf cachen)");
        });
        sicher("Rabatte.prozent", () -> {
            Pruef.gleich(900L, Rabatte.prozent(10).anwenden(1000), "10 % von 1000 -> 900");
            Pruef.gleich(900L, Rabatte.prozent(10).anwenden(999), "10 % von 999: Abzug 99 (abgerundet) -> 900");
            Pruef.gleich(750L, Rabatte.prozent(25).anwenden(1000), "25 % von 1000 -> 750");
            Pruef.gleich(500L, Rabatte.prozent(0).anwenden(500), "0 % -> unveraendert");
            Pruef.gleich(0L, Rabatte.prozent(100).anwenden(500), "100 % -> 0");
            Pruef.wirft(IllegalArgumentException.class, () -> Rabatte.prozent(-1), "prozent(-1) -> Exception");
            Pruef.wirft(IllegalArgumentException.class, () -> Rabatte.prozent(101), "prozent(101) -> Exception");
            Rabatt zehn = Rabatte.prozent(10);
            Rabatt zwanzig = Rabatte.prozent(20);
            Pruef.gleich(900L, zehn.anwenden(1000), "zwei Strategien nebeneinander: jede merkt sich ihren Satz (10 %)");
            Pruef.gleich(800L, zwanzig.anwenden(1000), "zwei Strategien nebeneinander: jede merkt sich ihren Satz (20 %)");
        });
        sicher("Rabatte.festbetrag", () -> {
            Pruef.gleich(1500L, Rabatte.festbetrag(500).anwenden(2000), "2000 - 500 -> 1500");
            Pruef.gleich(0L, Rabatte.festbetrag(500).anwenden(300), "300 - 500 -> 0, nicht negativ");
            Pruef.gleich(700L, Rabatte.festbetrag(0).anwenden(700), "festbetrag(0) -> unveraendert");
            Pruef.wirft(IllegalArgumentException.class, () -> Rabatte.festbetrag(-1), "festbetrag(-1) -> Exception");
        });
        sicher("Rabatte.abMindestwert", () -> {
            Rabatt r = Rabatte.abMindestwert(5000, Rabatte.prozent(10));
            Pruef.gleich(4999L, r.anwenden(4999), "unter dem Mindestwert: kein Rabatt");
            Pruef.gleich(4500L, r.anwenden(5000), "genau der Mindestwert: Rabatt gilt");
            Pruef.gleich(9000L, r.anwenden(10000), "darueber: Rabatt gilt");
            Pruef.gleich(100L, Rabatte.abMindestwert(0, betrag -> betrag / 2).anwenden(200),
                    "funktioniert mit jeder Strategie, auch einem Lambda");
            Pruef.wirft(NullPointerException.class, () -> Rabatte.abMindestwert(10, null),
                    "abMindestwert(10, null) -> NullPointerException");
        });
        sicher("Kasse: Strategie austauschen", () -> {
            Kasse k = new Kasse();
            k.scanne(1000);
            k.scanne(2500);
            Pruef.gleich(3500L, k.zuZahlenCent(), "Kasse ohne Rabatt");
            k.setzeRabatt(Rabatte.prozent(10));
            Pruef.gleich(3150L, k.zuZahlenCent(), "Kasse mit 10 %");
            k.setzeRabatt(Rabatte.festbetrag(500));
            Pruef.gleich(3000L, k.zuZahlenCent(), "Kasse mit 5 Euro Abzug");
            k.setzeRabatt(betrag -> betrag / 2);
            Pruef.gleich(1750L, k.zuZahlenCent(), "Kasse mit einem Lambda als Strategie");
        });

        // ------------------------------------------------------------ Fabrik
        Pruef.abschnitt("Aufgabe 12: Rabatte.ausCode (Fabrik)");
        sicher("ausCode: gueltige Codes", () -> {
            Pruef.wahr(Rabatte.keiner() != null && Rabatte.ausCode("KEIN") == Rabatte.keiner(), "KEIN -> dieselbe Instanz wie keiner()");
            Pruef.gleich(900L, Rabatte.ausCode("PROZENT10").anwenden(1000), "PROZENT10 auf 1000 -> 900");
            Pruef.gleich(750L, Rabatte.ausCode("prozent25").anwenden(1000), "prozent25: Gross-/Kleinschreibung egal");
            Pruef.gleich(700L, Rabatte.ausCode("  MINUS300 ").anwenden(1000), "\"  MINUS300 \": Leerzeichen egal");
            Pruef.gleich(0L, Rabatte.ausCode("MINUS5000").anwenden(1000), "MINUS5000 auf 1000 -> 0");
            Pruef.gleich(1000L, Rabatte.ausCode("PROZENT0").anwenden(1000), "PROZENT0 -> unveraendert");
        });
        sicher("ausCode: ungueltige Codes", () -> {
            for (String code : new String[]{"GRATIS", "", "PROZENT", "PROZENTabc", "PROZENT150", "MINUS", "MINUS-5",
                    "PROZENT 10", "10PROZENT"}) {
                Pruef.wirft(IllegalArgumentException.class, () -> Rabatte.ausCode(code),
                        "\"" + code + "\" -> IllegalArgumentException");
            }
            Pruef.wirft(NullPointerException.class, () -> Rabatte.ausCode(null), "null -> NullPointerException");
        });

        // ------------------------------------------------------------ Beobachter
        Pruef.abschnitt("Aufgabe 13: Lager (Beobachter)");
        sicher("Lager: Bestand", () -> {
            Lager l = new Lager(5);
            Pruef.gleich(0, l.bestand("Mehl"), "unbekannter Artikel: Bestand 0");
            l.einlagern("Mehl", 10);
            l.einlagern("Mehl", 5);
            Pruef.gleich(15, l.bestand("Mehl"), "zweimal einlagern addiert");
            l.entnehmen("Mehl", 4);
            Pruef.gleich(11, l.bestand("Mehl"), "entnehmen zieht ab");
            Pruef.wirft(IllegalStateException.class, () -> l.entnehmen("Mehl", 12), "mehr entnehmen als da ist");
            Pruef.gleich(11, l.bestand("Mehl"), "nach dem Fehlversuch: Bestand unveraendert");
            Pruef.wirft(IllegalStateException.class, () -> l.entnehmen("Zucker", 1), "nicht vorhandenen Artikel entnehmen");
            Pruef.wirft(IllegalArgumentException.class, () -> l.einlagern("Mehl", 0), "einlagern(0)");
            Pruef.wirft(IllegalArgumentException.class, () -> l.entnehmen("Mehl", -3), "entnehmen(-3)");
            Pruef.gleich(11, l.bestand("Mehl"), "nach allen Fehlversuchen: Bestand unveraendert");
        });
        sicher("Lager: wann gemeldet wird", () -> {
            Lager l = new Lager(5);
            List<String> meldungen = new ArrayList<>();
            l.anmelden((artikel, bestand) -> meldungen.add(artikel + ":" + bestand));
            l.einlagern("Mehl", 10);
            l.entnehmen("Mehl", 3);
            Pruef.gleich(List.of(), meldungen, "10 -> 7: ueber dem Meldebestand, keine Meldung");
            l.entnehmen("Mehl", 3);
            Pruef.gleich(List.of("Mehl:4"), meldungen, "7 -> 4: unter den Meldebestand 5 gefallen -> Meldung");
            l.entnehmen("Mehl", 2);
            Pruef.gleich(List.of("Mehl:4"), meldungen, "4 -> 2: war schon darunter, KEINE neue Meldung");
            l.einlagern("Mehl", 10);
            l.entnehmen("Mehl", 7);
            Pruef.gleich(List.of("Mehl:4"), meldungen, "12 -> 5: genau der Meldebestand ist noch nicht knapp");
            l.entnehmen("Mehl", 5);
            Pruef.gleich(List.of("Mehl:4", "Mehl:0"), meldungen, "5 -> 0: wieder darunter gefallen -> Meldung");
            l.einlagern("Zucker", 5);
            l.entnehmen("Zucker", 1);
            Pruef.gleich(List.of("Mehl:4", "Mehl:0", "Zucker:4"), meldungen, "jeder Artikel fuer sich");
        });
        sicher("Lager: mehrere Beobachter", () -> {
            Lager l = new Lager(3);
            List<String> meldungen = new ArrayList<>();
            LagerBeobachter a = (artikel, bestand) -> meldungen.add("A");
            LagerBeobachter b = (artikel, bestand) -> meldungen.add("B");
            l.anmelden(a);
            l.anmelden(b);
            l.anmelden(a);
            l.einlagern("Ei", 3);
            l.entnehmen("Ei", 1);
            Pruef.gleich(List.of("A", "B"), meldungen,
                    "alle benachrichtigt, in Anmelde-Reihenfolge, doppelt angemeldet zaehlt einmal");
            l.abmelden(a);
            l.abmelden((artikel, bestand) -> { });   // nie angemeldet: darf nichts tun
            l.einlagern("Ei", 5);
            l.entnehmen("Ei", 5);
            Pruef.gleich(List.of("A", "B", "B"), meldungen, "nach abmelden(a) nur noch B");
            Pruef.wirft(NullPointerException.class, () -> l.anmelden(null), "anmelden(null) -> NullPointerException");
        });
        sicher("Lager: Abmelden waehrend der Meldung", () -> {
            Lager l = new Lager(3);
            List<String> meldungen = new ArrayList<>();
            // Eine anonyme Klasse statt eines Lambdas, weil sie sich mit "this"
            // selbst abmelden kann. Das tut sie mitten in der Benachrichtigung.
            l.anmelden(new LagerBeobachter() {
                @Override
                public void knapp(String artikel, int bestand) {
                    meldungen.add("einmalig");
                    l.abmelden(this);
                }
            });
            l.anmelden((artikel, bestand) -> meldungen.add("B"));
            l.anmelden((artikel, bestand) -> meldungen.add("C"));
            l.einlagern("Salz", 3);
            l.entnehmen("Salz", 1);
            Pruef.gleich(List.of("einmalig", "B", "C"), meldungen,
                    "ein Beobachter meldet sich in knapp() ab: die anderen bekommen die Meldung trotzdem");
            l.einlagern("Salz", 5);
            l.entnehmen("Salz", 5);
            Pruef.gleich(List.of("einmalig", "B", "C", "B", "C"), meldungen, "beim naechsten Mal ist er weg");
        });

        Pruef.bericht();
    }

    /**
     * Fuehrt einen Pruefblock aus. Wirft dein Code unterwegs eine Exception,
     * wird das als FEHL gemeldet - mit Datei und Zeile - und die restlichen
     * Pruefungen laufen trotzdem weiter.
     */
    private static void sicher(String was, Runnable block) {
        try {
            block.run();
        } catch (RuntimeException | StackOverflowError e) {
            Pruef.gleich("keine Exception", beschreibe(e), was);
        }
    }

    private static String beschreibe(Throwable e) {
        String ort = "";
        for (StackTraceElement stelle : e.getStackTrace()) {
            String klasse = stelle.getClassName();
            String datei = stelle.getFileName();
            if (datei != null && !datei.equals("Tests.java") && !datei.equals("Pruef.java")
                    && !klasse.startsWith("java.") && !klasse.startsWith("jdk.")) {
                ort = "  bei " + datei + ":" + stelle.getLineNumber();
                break;
            }
        }
        return e.getClass().getSimpleName() + ": " + e.getMessage() + ort;
    }

    /** null-sichere Helfer, damit ein unfertiges src/ nicht die ganze Pruefung abbricht. */
    private static int groesse(List<?> liste) {
        return liste == null ? -1 : liste.size();
    }

    private static String ersterName(List<Person> liste) {
        return liste == null || liste.isEmpty() ? null : liste.get(0).getName();
    }
}
