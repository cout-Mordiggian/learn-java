/**
 * Kapitel 07: Eine eigene CHECKED Exception.
 *
 * "extends Exception" macht sie checked - der Compiler zwingt Aufrufer,
 * sie zu fangen oder mit throws weiterzureichen.
 */
public class UnzureichendeDeckungException extends Exception {

    // TODO: private final long fehlbetrag

    public UnzureichendeDeckungException(long fehlbetrag) {
        // TODO: super("Es fehlen " + fehlbetrag + " Cent") und Feld setzen
        super("");
    }

    public long getFehlbetrag() {
        // TODO
        return 0L;
    }
}
