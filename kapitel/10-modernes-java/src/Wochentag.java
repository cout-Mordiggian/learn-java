/**
 * Kapitel 10: ein enum mit Zustand und Verhalten.
 */
public enum Wochentag {

    // Vorgegeben: die sieben Konstanten mit dem Werktag-Flag.
    // Die Argumente in Klammern gehen an den Konstruktor unten.
    MONTAG(true), DIENSTAG(true), MITTWOCH(true), DONNERSTAG(true),
    FREITAG(true), SAMSTAG(false), SONNTAG(false);

    // TODO: private final boolean werktag;

    /**
     * Enum-Konstruktoren sind implizit private. "private" hinzuschreiben ist
     * erlaubt (aber unueblich), "public" oder "protected" waere ein Compilerfehler.
     */
    Wochentag(boolean werktag) {
        // TODO
    }

    public boolean istWerktag() {
        // TODO
        return false;
    }

    /** Der naechste Tag; nach SONNTAG kommt wieder MONTAG. */
    public Wochentag naechster() {
        // TODO: values() liefert alle Konstanten, ordinal() die Position.
        //       Der Modulo-Operator sorgt fuer den Umlauf.
        return this;
    }

    /**
     * 1 = MONTAG ... 7 = SONNTAG
     * @throws IllegalArgumentException bei allem anderen
     */
    public static Wochentag vonNummer(int n) {
        // TODO
        return MONTAG;
    }
}
