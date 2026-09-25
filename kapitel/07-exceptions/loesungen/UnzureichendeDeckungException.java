/**
 * Kapitel 07 - Musterloesung: eigene checked Exception.
 */
public class UnzureichendeDeckungException extends Exception {

    // Die Exception traegt DATEN, nicht nur Text. Der Aufrufer kann
    // getFehlbetrag() auswerten - eine Nachricht muesste er parsen.
    private final long fehlbetrag;

    public UnzureichendeDeckungException(long fehlbetrag) {
        // super(nachricht) fuellt getMessage(). Ohne diesen Aufruf waere
        // getMessage() null und der Stacktrace nahezu nutzlos.
        super("Es fehlen " + fehlbetrag + " Cent");
        this.fehlbetrag = fehlbetrag;
    }

    public long getFehlbetrag() {
        return fehlbetrag;
    }

    // Ueblich waere zusaetzlich ein Konstruktor mit cause:
    //   public UnzureichendeDeckungException(String msg, Throwable cause) {
    //       super(msg, cause); ...
    //   }
    // Hier nicht noetig, weil diese Exception keine andere uebersetzt.
}
