/**
 * Kapitel 14 - Aufgabe 10b: Rueckgaengig und Wiederholen.
 * Pruefen:  ./lerne.sh 14
 *
 *   Verlauf v = new Verlauf();
 *   v.ausfuehren(new EinfuegenBefehl(t, 0, "Hallo"));    // "Hallo"
 *   v.ausfuehren(new EinfuegenBefehl(t, 5, " Welt"));    // "Hallo Welt"
 *   v.rueckgaengig();                                     // "Hallo"
 *   v.wiederholen();                                      // "Hallo Welt"
 *
 * Regeln:
 *   - rueckgaengig() macht den JUENGSTEN noch nicht rueckgaengig gemachten
 *     Befehl rueckgaengig, wiederholen() den zuletzt rueckgaengig gemachten.
 *   - Beide liefern false und tun nichts, wenn es nichts zu tun gibt.
 *   - Ein neuer Befehl ueber ausfuehren() verwirft alles, was man noch haette
 *     wiederholen koennen - wie in jedem Editor.
 *
 * Tipp: zwei Stapel (README 14.6). ArrayDeque mit push/pop.
 */
public class Verlauf {

    // TODO: Felder

    /** Fuehrt den Befehl aus und merkt ihn sich. */
    public void ausfuehren(Befehl befehl) {
        // TODO
    }

    public boolean rueckgaengig() {
        // TODO
        return false;
    }

    public boolean wiederholen() {
        // TODO
        return false;
    }

    public boolean kannRueckgaengig() {
        // TODO
        return false;
    }

    public boolean kannWiederholen() {
        // TODO
        return false;
    }
}
