import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Kapitel 09 - Musterloesung zu Aufgabe 13: Beobachter (Observer).
 */
public class Lager {

    private final int meldebestand;
    private final Map<String, Integer> bestaende = new HashMap<>();
    // List statt Set: Die Reihenfolge der Anmeldung bleibt erhalten. Doppelte
    // verhindert anmelden() selbst.
    private final List<LagerBeobachter> beobachter = new ArrayList<>();

    public Lager(int meldebestand) {
        if (meldebestand < 0) {
            throw new IllegalArgumentException("Meldebestand darf nicht negativ sein");
        }
        this.meldebestand = meldebestand;
    }

    public void anmelden(LagerBeobachter b) {
        Objects.requireNonNull(b, "beobachter");
        // Doppelt angemeldet hiesse doppelt benachrichtigt - fast nie gewollt.
        // contains vergleicht mit equals; Lambdas haben kein eigenes equals,
        // also zaehlt hier dasselbe Objekt (==).
        if (!beobachter.contains(b)) {
            beobachter.add(b);
        }
    }

    public void abmelden(LagerBeobachter b) {
        beobachter.remove(b);   // nicht angemeldet: remove tut einfach nichts
    }

    public void einlagern(String artikel, int menge) {
        pruefeMenge(menge);
        bestaende.merge(artikel, menge, Integer::sum);
    }

    public void entnehmen(String artikel, int menge) {
        pruefeMenge(menge);
        int vorher = bestand(artikel);
        if (menge > vorher) {
            // Pruefen, BEVOR etwas geaendert wird: Bei einem Fehler bleibt
            // der Bestand, wie er war.
            throw new IllegalStateException("Nur " + vorher + "x " + artikel + " da, gewuenscht: " + menge);
        }
        int nachher = vorher - menge;
        bestaende.put(artikel, nachher);

        // Nur beim UEBERSCHREITEN der Schwelle melden, nicht bei jeder Entnahme
        // darunter - sonst ertrinkt der Einkauf in Meldungen.
        if (vorher >= meldebestand && nachher < meldebestand) {
            benachrichtige(artikel, nachher);
        }
    }

    public int bestand(String artikel) {
        return bestaende.getOrDefault(artikel, 0);
    }

    private void benachrichtige(String artikel, int bestand) {
        // Ueber eine KOPIE laufen: Meldet sich ein Beobachter in knapp() ab,
        // aenderte er sonst die Liste, ueber die wir gerade laufen, und es
        // gaebe eine ConcurrentModificationException (Kapitel 8.6). List.copyOf
        // ist ein Schnappschuss: Wer waehrend der Meldung an- oder abmeldet,
        // wirkt erst bei der naechsten.
        for (LagerBeobachter b : List.copyOf(beobachter)) {
            b.knapp(artikel, bestand);
        }
    }

    private static void pruefeMenge(int menge) {
        if (menge <= 0) {
            throw new IllegalArgumentException("Menge muss positiv sein: " + menge);
        }
    }
}
