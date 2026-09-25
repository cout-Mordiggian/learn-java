/**
 * Kapitel 07: Eine Ressource fuer try-with-resources.
 *
 * AutoCloseable ist die einzige Voraussetzung dafuer, dass ein Objekt
 * in den Klammern von try(...) stehen darf.
 */
public class Tresor implements AutoCloseable {

    // TODO: private final StringBuilder protokoll
    // TODO: private boolean offen

    public Tresor(StringBuilder protokoll) {
        // TODO: Feld setzen, offen = true, "geoeffnet|" anhaengen
    }

    /**
     * @throws IllegalStateException wenn der Tresor schon geschlossen ist
     */
    public void benutzen() {
        // TODO: pruefen, dann "benutzt|" anhaengen
    }

    public boolean istOffen() {
        // TODO
        return false;
    }

    @Override
    public void close() {
        // TODO: war der Tresor schon zu? Dann nichts tun (idempotent).
        //       Sonst offen = false, "geschlossen" anhaengen.
        // Beachte: close() darf hier ohne throws deklariert werden.
        // AutoCloseable.close() erlaubt Exception, aber eine Ueberschreibung
        // darf weniger werfen - und das macht Aufrufern das Leben leichter.
    }
}
