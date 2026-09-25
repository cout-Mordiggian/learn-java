import java.nio.file.Path;

/**
 * Abschlussprojekt - Einstiegspunkt.
 *
 * Starten:  ./starte.sh
 *
 * Aktuell ein Geruest, das bewusst schon kompiliert und startet: Es benutzt
 * noch keine der Klassen aus M2-M5 (Aufgabenliste, CsvSpeicher, Konsole) -
 * die stehen unten nur im Kommentar. Arbeite dich an den Meilensteinen aus
 * README.md entlang und ersetze diesen Rumpf Schritt fuer Schritt.
 */
public class Main {

    public static void main(String[] args) {
        // Meilenstein 6: Pfad aus args lesen, falls angegeben.
        Path datei = Path.of("aufgaben.csv");

        System.out.println();
        System.out.println("  Aufgabenverwaltung");
        System.out.println("  ------------------");
        System.out.println("  Datei: " + datei.toAbsolutePath());
        System.out.println();
        System.out.println("  Noch nichts gebaut. Fang mit Meilenstein 1 an:");
        System.out.println("  die TODOs in src/Aufgabe.java (record mit kompaktem Konstruktor).");
        System.out.println();

        // Ziel nach M5:
        //
        //   CsvSpeicher speicher = new CsvSpeicher();
        //   Aufgabenliste liste = new Aufgabenliste(speicher.laden(datei));
        //   new Konsole(liste).starten();
        //   speicher.speichern(datei, liste.alle());
        //
        // Achtung: laden() und speichern() werfen IOException (checked).
        // Entweder hier fangen und eine klare Meldung ausgeben, oder
        // main mit "throws IOException" deklarieren - siehe README, M5.
    }
}
