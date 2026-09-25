import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Tests {

    static Path ordner;

    public static void main(String[] args) throws IOException {
        ordner = Files.createTempDirectory("javakurs11");
        System.out.println("  (Arbeitsordner: " + ordner + ")");

        Pruef.abschnitt("Aufgabe 1: zeilenZaehlen");
        Path drei = schreibe("drei.txt", "a\nb\nc\n");
        Pruef.gleich(3L, Aufgaben.zeilenZaehlen(drei), "drei Zeilen");
        Pruef.gleich(0L, Aufgaben.zeilenZaehlen(schreibe("leer.txt", "")), "leere Datei");
        Pruef.gleich(1L, Aufgaben.zeilenZaehlen(schreibe("eine.txt", "nur eine")),
                "eine Zeile ohne Umbruch am Ende");

        Pruef.abschnitt("Aufgabe 2: schreibeZeilen");
        Path raus = ordner.resolve("raus.txt");
        Aufgaben.schreibeZeilen(raus, List.of("eins", "zwei"));
        Pruef.gleich("eins\nzwei\n", lies(raus), "Inhalt");
        Aufgaben.schreibeZeilen(raus, List.of("neu"));
        Pruef.gleich("neu\n", lies(raus), "ueberschreibt");
        Aufgaben.schreibeZeilen(raus, List.of());
        Pruef.gleich("", lies(raus), "leere Liste");
        // \u00fc = ue, \u00df = ss, \u00f6 = oe - als Escape, damit diese Datei ASCII bleibt.
        String umlaute = "Gr\u00fc\u00dfe aus K\u00f6ln";
        Aufgaben.schreibeZeilen(raus, List.of(umlaute));
        Pruef.gleich(umlaute + "\n", lies(raus), "UTF-8 hin und zurueck (Umlaute)");

        Pruef.abschnitt("Aufgabe 3: nichtLeereZeilen");
        Path gemischt = schreibe("gemischt.txt", "a\n\n  \n  b  \nc\n");
        Pruef.gleich(List.of("a", "b", "c"), Aufgaben.nichtLeereZeilen(gemischt), "gefiltert und gestrippt");
        Pruef.gleich(List.of(), Aufgaben.nichtLeereZeilen(schreibe("nurleer.txt", "\n \n")), "nur Leerzeilen");

        Pruef.abschnitt("Aufgabe 4: sicherLesen");
        Pruef.gleich(Optional.of("hallo"), Aufgaben.sicherLesen(schreibe("da.txt", "hallo")),
                "vorhandene Datei");
        Pruef.gleich(Optional.empty(), Aufgaben.sicherLesen(ordner.resolve("gibtsnicht.txt")),
                "fehlende Datei -> empty, keine Exception");

        Pruef.abschnitt("Aufgabe 5: csvLesen");
        Path csv = schreibe("werte.csv", """
                sensor;wert
                temp;21.5
                druck;1013.0

                temp;22.5
                """);
        List<Messwert> werte = Aufgaben.csvLesen(csv);
        Pruef.gleich(3, werte.size(), "drei Datensaetze (Kopf und Leerzeile uebersprungen)");
        Pruef.gleich(new Messwert("temp", 21.5), bei(werte, 0), "erster Datensatz");
        Pruef.gleich(new Messwert("temp", 22.5), bei(werte, 2), "letzter Datensatz");
        Pruef.gleich(List.of(), Aufgaben.csvLesen(schreibe("nurkopf.csv", "sensor;wert\n")),
                "nur Kopfzeile");
        Pruef.gleich(List.of(), Aufgaben.csvLesen(schreibe("ganzleer.csv", "")),
                "leere Datei");

        Path kaputt = schreibe("kaputt.csv", "sensor;wert\ntemp;21.5\nkaputt\n");
        try {
            Aufgaben.csvLesen(kaputt);
            Pruef.wahr(false, "haette werfen muessen");
        } catch (IllegalArgumentException e) {
            Pruef.gleich("Ungueltige Zeile 3: kaputt", e.getMessage(), "Nachricht nennt die Zeilennummer");
        }
        Path mitLeerzeile = schreibe("leerzeile.csv", "sensor;wert\ntemp;21.5\n\nkaputt\n");
        try {
            Aufgaben.csvLesen(mitLeerzeile);
            Pruef.wahr(false, "haette werfen muessen");
        } catch (IllegalArgumentException e) {
            Pruef.gleich("Ungueltige Zeile 4: kaputt", e.getMessage(),
                    "Leerzeilen zaehlen bei der Zeilennummer mit");
        }
        Path dreiFelder = schreibe("dreifelder.csv", "sensor;wert\ntemp;21.5;\n");
        try {
            Aufgaben.csvLesen(dreiFelder);
            Pruef.wahr(false, "haette werfen muessen");
        } catch (IllegalArgumentException e) {
            Pruef.gleich("Ungueltige Zeile 2: temp;21.5;", e.getMessage(),
                    "Trennzeichen am Ende = drei Felder (split mit -1)");
        }
        Path keineZahl = schreibe("keinezahl.csv", "sensor;wert\ntemp;abc\n");
        try {
            Aufgaben.csvLesen(keineZahl);
            Pruef.wahr(false, "haette werfen muessen");
        } catch (IllegalArgumentException e) {
            Pruef.gleich("Ungueltige Zeile 2: temp;abc", e.getMessage(), "unparsbare Zahl");
        }

        Pruef.abschnitt("Aufgabe 6: durchschnittProSensor");
        Map<String, Double> schnitt = Aufgaben.durchschnittProSensor(List.of(
                new Messwert("temp", 20), new Messwert("temp", 22), new Messwert("druck", 1000)));
        Pruef.gleich(2, schnitt.size(), "zwei Sensoren");
        Pruef.fastGleich(21.0, schnitt.getOrDefault("temp", -1.0), "Durchschnitt temp");
        Pruef.fastGleich(1000.0, schnitt.getOrDefault("druck", -1.0), "Durchschnitt druck");
        Pruef.gleich(Map.of(), Aufgaben.durchschnittProSensor(List.of()), "leere Liste");

        Pruef.abschnitt("Aufgabe 7: schreibeBericht");
        Path bericht = ordner.resolve("bericht.txt");
        Aufgaben.schreibeBericht(bericht, Map.of("temp", 21.0, "druck", 1013.456, "abc", 1.0));
        Pruef.gleich("abc=1.00\ndruck=1013.46\ntemp=21.00\n",
                lies(bericht),
                "alphabetisch, zwei Nachkommastellen, Punkt als Trenner");
        Aufgaben.schreibeBericht(bericht, Map.of());
        Pruef.gleich("", lies(bericht), "leere Map");

        aufraeumen(ordner);
        Pruef.bericht();
    }

    /** Loescht den temporaeren Arbeitsordner samt Inhalt (tiefste Eintraege zuerst). */
    private static void aufraeumen(Path wurzel) {
        try (Stream<Path> alle = Files.walk(wurzel)) {
            for (Path p : alle.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(p);
            }
        } catch (IOException e) {
            System.out.println("  (Konnte " + wurzel + " nicht aufraeumen: " + e.getMessage() + ")");
        }
    }

    /** Holt ein Element oder null - damit eine unfertige Loesung die Pruefung nicht abbricht. */
    private static <T> T bei(List<T> liste, int i) {
        return i < liste.size() ? liste.get(i) : null;
    }

    /** Liest eine Datei; gibt null zurueck, wenn es sie (noch) nicht gibt. */
    private static String lies(Path p) {
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    private static Path schreibe(String name, String inhalt) throws IOException {
        Path p = ordner.resolve(name);
        Files.writeString(p, inhalt, StandardCharsets.UTF_8);
        return p;
    }
}
