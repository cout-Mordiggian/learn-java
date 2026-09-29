import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Kapitel 14 - Musterloesung: Rueckgaengig und Wiederholen mit zwei Stapeln.
 */
public class Verlauf {

    // Zwei Stapel (README 14.6). Oben liegt jeweils der juengste Befehl:
    // Rueckgaengig nimmt immer den zuletzt ausgefuehrten - LIFO.
    private final Deque<Befehl> rueckgaengigStapel = new ArrayDeque<>();
    private final Deque<Befehl> wiederholenStapel = new ArrayDeque<>();

    public void ausfuehren(Befehl befehl) {
        befehl.ausfuehren();
        rueckgaengigStapel.push(befehl);
        // Ein neuer Befehl beginnt eine neue "Zeitlinie". Die rueckgaengig
        // gemachten Befehle passen nicht mehr zum Text und verfallen - so
        // verhaelt sich jeder Editor.
        wiederholenStapel.clear();
    }

    public boolean rueckgaengig() {
        if (rueckgaengigStapel.isEmpty()) {
            return false;
        }
        Befehl b = rueckgaengigStapel.pop();
        b.rueckgaengig();
        wiederholenStapel.push(b);   // derselbe Befehl kann spaeter nochmal laufen
        return true;
    }

    public boolean wiederholen() {
        if (wiederholenStapel.isEmpty()) {
            return false;
        }
        Befehl b = wiederholenStapel.pop();
        b.ausfuehren();
        rueckgaengigStapel.push(b);  // NICHT ausfuehren(b) - das wuerde den Wiederholen-Stapel leeren
        return true;
    }

    public boolean kannRueckgaengig() {
        return !rueckgaengigStapel.isEmpty();
    }

    public boolean kannWiederholen() {
        return !wiederholenStapel.isEmpty();
    }
}
