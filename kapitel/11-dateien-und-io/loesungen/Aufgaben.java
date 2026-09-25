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
 * Kapitel 11 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static long zeilenZaehlen(Path pfad) throws IOException {
        // Files.lines haelt die Datei offen, solange der Stream lebt.
        // Ohne try-with-resources bleibt ein Dateideskriptor haengen -
        // in einer Schleife ueber tausende Dateien fuehrt das zu
        // "too many open files". Das ist DER Unterschied zu Streams
        // ueber Collections, die man einfach stehen lassen darf.
        try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) {
            return zeilen.count();
        }
    }

    public static void schreibeZeilen(Path pfad, List<String> zeilen) throws IOException {
        // Files.write(pfad, iterable) haengt an jede Zeile den
        // Plattform-Zeilentrenner an - unter Windows waere das \r\n.
        // Der Test erwartet \n, also bauen wir den String selbst.
        // Genau solche Details machen Textformate plattformabhaengig.
        StringBuilder sb = new StringBuilder();
        for (String z : zeilen) {
            sb.append(z).append('\n');
        }
        Files.writeString(pfad, sb.toString(), StandardCharsets.UTF_8);
        // writeString legt die Datei an oder ueberschreibt sie -
        // das ist das Standardverhalten (CREATE + TRUNCATE_EXISTING).
        // Zum Anhaengen braeuchte es StandardOpenOption.APPEND.
    }

    public static List<String> nichtLeereZeilen(Path pfad) throws IOException {
        try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) {
            return zeilen.map(String::strip)
                    .filter(z -> !z.isEmpty())
                    .toList();
            // Reihenfolge beachten: erst strippen, dann filtern.
            // Andersherum bliebe "   " uebrig, weil es nicht leer IST,
            // sondern erst nach dem Strippen leer WIRD.
        }
    }

    public static Optional<String> sicherLesen(Path pfad) {
        try {
            return Optional.of(Files.readString(pfad, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // Bewusste Uebersetzung: die checked Exception verschwindet,
            // die Information "kein Wert" wandert in den Rueckgabetyp.
            // Zulaessig, wenn der Grund den Aufrufer wirklich nicht
            // interessiert. Sonst waere Weiterreichen besser - ein
            // Optional.empty() kann "Datei fehlt" nicht von
            // "Festplatte defekt" unterscheiden.
            return Optional.empty();
        }
    }

    public static List<Messwert> csvLesen(Path pfad) throws IOException {
        List<String> zeilen = Files.readAllLines(pfad, StandardCharsets.UTF_8);
        List<Messwert> ergebnis = new ArrayList<>();

        // Index-Schleife statt for-each, weil wir die Zeilennummer fuer
        // die Fehlermeldung brauchen. Ein guter Parser sagt IMMER, WO
        // es geknallt hat - "Ungueltiges Format" allein ist wertlos.
        for (int i = 1; i < zeilen.size(); i++) {     // ab 1: Kopfzeile ueberspringen
            String zeile = zeilen.get(i);
            if (zeile.isBlank()) continue;

            String[] felder = zeile.split(";", -1);
            // Das -1 behaelt leere Felder am Ende. Ohne es waere
            // "temp;21.5;" nur zwei Felder statt drei - die kaputte Zeile
            // (ueberzaehliges Trennzeichen) wuerde still akzeptiert.
            if (felder.length != 2) {
                throw new IllegalArgumentException("Ungueltige Zeile " + (i + 1) + ": " + zeile);
            }
            try {
                double wert = Double.parseDouble(felder[1].strip());
                ergebnis.add(new Messwert(felder[0].strip(), wert));
            } catch (NumberFormatException e) {
                // Uebersetzen in unsere eigene Fehlersprache - mit Ursache (Kapitel 7).
                throw new IllegalArgumentException("Ungueltige Zeile " + (i + 1) + ": " + zeile, e);
            }
        }
        return ergebnis;
    }

    public static Map<String, Double> durchschnittProSensor(List<Messwert> werte) {
        return werte.stream()
                .collect(Collectors.groupingBy(
                        Messwert::sensor,                            // Klassifizierer
                        Collectors.averagingDouble(Messwert::wert))); // Downstream
        // averagingDouble liefert Double (nicht OptionalDouble) - fuer eine
        // leere Eingabe waere das Ergebnis laut Javadoc 0.0. Hier stoert das
        // nicht: groupingBy legt eine Gruppe erst mit dem ersten Element an.
    }

    public static void schreibeBericht(Path pfad, Map<String, Double> durchschnitte)
            throws IOException {
        // TreeMap sortiert beim Einfuegen nach der natuerlichen Ordnung
        // der Schluessel - hier alphabetisch. Alternative: entrySet in
        // eine Liste kopieren und sortieren.
        Map<String, Double> sortiert = new TreeMap<>(durchschnitte);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> e : sortiert.entrySet()) {
            sb.append(String.format(Locale.ROOT, "%s=%.2f", e.getKey(), e.getValue()))
              .append('\n');
        }
        Files.writeString(pfad, sb.toString(), StandardCharsets.UTF_8);
        // Bewusst '\n' statt %n: %n setzt den PLATTFORM-Zeilentrenner ein
        // (unter Windows \r\n). Fuer ein Dateiformat, das ueberall gleich
        // aussehen soll, ist das genau falsch. %n gehoert in Konsolenausgaben,
        // nicht in Daten.
    }
}
