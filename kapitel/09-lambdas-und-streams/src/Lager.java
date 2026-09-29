import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kapitel 09 - Aufgabe 13: der Beobachter (Observer), siehe README 9.9.
 * Pruefen:  ./lerne.sh 09
 *
 * Das Lager verwaltet Bestaende. Wer wissen will, wann etwas knapp wird,
 * meldet einen LagerBeobachter an (Interface, schon fertig):
 *
 *   Lager lager = new Lager(5);          // Meldebestand 5
 *   lager.anmelden((artikel, bestand) -> System.out.println(artikel + " knapp: " + bestand));
 *   lager.einlagern("Mehl", 10);
 *   lager.entnehmen("Mehl", 6);          // 10 -> 4: gemeldet "Mehl knapp: 4"
 *   lager.entnehmen("Mehl", 1);          //  4 -> 3: war schon knapp, KEINE neue Meldung
 *
 * Regeln:
 *   - Gemeldet wird, wenn eine Entnahme den Bestand UNTER den Meldebestand
 *     bringt, der vorher noch mindestens so gross war. "Knapp" heisst: kleiner
 *     als der Meldebestand. 5 bei Meldebestand 5 ist noch nicht knapp.
 *   - Alle Beobachter werden benachrichtigt, in der Reihenfolge der Anmeldung.
 *     Wer doppelt angemeldet wird, bekommt trotzdem nur eine Meldung.
 *   - Ein Beobachter darf sich waehrend knapp() selbst abmelden. Die anderen
 *     muessen die Meldung trotzdem bekommen, und es darf keine Exception geben.
 *   - menge <= 0 -> IllegalArgumentException.
 *     Mehr entnehmen als da ist -> IllegalStateException, Bestand bleibt.
 */
public class Lager {

    private final int meldebestand;
    private final Map<String, Integer> bestaende = new HashMap<>();
    private final List<LagerBeobachter> beobachter = new ArrayList<>();

    public Lager(int meldebestand) {
        this.meldebestand = meldebestand;
    }

    /** null -> NullPointerException. Schon angemeldet -> nichts tun. */
    public void anmelden(LagerBeobachter b) {
        // TODO
    }

    /** Nicht angemeldet -> nichts tun. */
    public void abmelden(LagerBeobachter b) {
        // TODO
    }

    public void einlagern(String artikel, int menge) {
        // TODO: menge pruefen, Bestand erhoehen (Map.merge oder getOrDefault, Kapitel 8)
    }

    public void entnehmen(String artikel, int menge) {
        // TODO: pruefen, Bestand senken, bei Bedarf alle benachrichtigen.
        //       Tipp: Merk dir den Bestand vorher UND nachher.
    }

    /** Bestand des Artikels, 0 fuer unbekannte Artikel. */
    public int bestand(String artikel) {
        // TODO
        return 0;
    }
}
