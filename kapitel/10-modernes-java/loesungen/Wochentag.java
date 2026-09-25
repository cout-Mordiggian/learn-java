/**
 * Kapitel 10 - Musterloesung: enum mit Zustand und Verhalten.
 */
public enum Wochentag {

    // Die Argumente in Klammern gehen an den Konstruktor.
    // Das Semikolon nach der letzten Konstante ist Pflicht, sobald
    // danach noch Felder oder Methoden folgen.
    MONTAG(true),
    DIENSTAG(true),
    MITTWOCH(true),
    DONNERSTAG(true),
    FREITAG(true),
    SAMSTAG(false),
    SONNTAG(false);

    private final boolean werktag;

    // Implizit private. "private" hinzuschreiben ist erlaubt, aber unueblich;
    // "public" waere ein Compilerfehler - Enum-Konstanten sind abschliessend.
    Wochentag(boolean werktag) {
        this.werktag = werktag;
    }

    public boolean istWerktag() {
        return werktag;
    }

    public Wochentag naechster() {
        // values() liefert bei JEDEM Aufruf eine neue Kopie des Arrays
        // (damit niemand die Konstanten ueberschreibt). In heissen Schleifen
        // lohnt es sich, das Ergebnis einmalig in ein static final Feld zu legen.
        Wochentag[] alle = values();
        return alle[(ordinal() + 1) % alle.length];   // Modulo sorgt fuer den Umlauf
    }

    public static Wochentag vonNummer(int n) {
        if (n < 1 || n > 7) {
            throw new IllegalArgumentException("Tag muss zwischen 1 und 7 liegen: " + n);
        }
        return values()[n - 1];
        // Diese Methode haengt an der DEKLARATIONSREIHENFOLGE. Genau deshalb
        // sollte man ordinal() nie persistieren: Wer die Konstanten umsortiert,
        // aendert still die Bedeutung aller gespeicherten Werte.
        // Zum Speichern immer name() verwenden.
    }
}
