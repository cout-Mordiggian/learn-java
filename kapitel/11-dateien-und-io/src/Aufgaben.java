import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Kapitel 11 - Dateien und IO.
 * Pruefen:  ./lerne.sh 11
 */
public class Aufgaben {

    /**
     * Aufgabe 1: Zeilen zaehlen.
     * Nutze Files.lines - und denke daran, den Stream zu schliessen.
     */
    public static long zeilenZaehlen(Path pfad) throws IOException {
        // TODO: try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) { ... }
        return 0L;
    }

    /**
     * Aufgabe 2: Zeilen schreiben, jede mit \n abgeschlossen, UTF-8.
     * Eine bestehende Datei wird ueberschrieben.
     */
    public static void schreibeZeilen(Path pfad, List<String> zeilen) throws IOException {
        // TODO: Files.write(...) oder Files.writeString(...)
    }

    /**
     * Aufgabe 3: alle Zeilen ohne leere/Whitespace-Zeilen, jede gestrippt.
     */
    public static List<String> nichtLeereZeilen(Path pfad) throws IOException {
        // TODO
        return List.of();
    }

    /**
     * Aufgabe 4: Datei lesen, aber ohne Exception nach aussen.
     * Fehlende oder unlesbare Datei -> Optional.empty()
     */
    public static Optional<String> sicherLesen(Path pfad) {
        // TODO: try/catch (IOException e) -> Optional.empty()
        return Optional.empty();
    }

    /**
     * Aufgabe 5: CSV im Format  sensor;wert  einlesen.
     * - erste Zeile ist die Kopfzeile und wird uebersprungen
     * - leere Zeilen werden uebersprungen
     * - leere Datei -> leere Liste
     * - kaputte Zeile -> IllegalArgumentException("Ungueltige Zeile 3: kaputt")
     *   (Zeilennummer in der Datei: 1-basiert, Kopf- und Leerzeilen zaehlen mit)
     * - auch "temp;21.5;" ist kaputt: drei Felder statt zwei
     */
    public static List<Messwert> csvLesen(Path pfad) throws IOException {
        // TODO: Files.readAllLines, dann mit Index durchlaufen.
        //       split(";", -1) und Double.parseDouble - beides kann schiefgehen.
        return List.of();
    }

    /**
     * Aufgabe 6: Durchschnittswert je Sensor.
     */
    public static Map<String, Double> durchschnittProSensor(List<Messwert> werte) {
        // TODO: Collectors.groupingBy(..., Collectors.averagingDouble(...))
        return Map.of();
    }

    /**
     * Aufgabe 7: Bericht schreiben, alphabetisch nach Sensorname.
     * Jede Zeile:  sensor=12.50\n
     */
    public static void schreibeBericht(Path pfad, Map<String, Double> durchschnitte)
            throws IOException {
        // TODO: TreeMap sortiert von selbst nach Schluessel.
        //       String.format(Locale.ROOT, "%s=%.2f", ...)
    }
}
