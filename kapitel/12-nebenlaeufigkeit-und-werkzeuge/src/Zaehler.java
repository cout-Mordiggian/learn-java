/**
 * Kapitel 12, Aufgabe 1: ein thread-sicherer Zaehler.
 */
public class Zaehler {

    private int stand = 0;

    /** TODO: synchronized ergaenzen. */
    public void erhoehen() {
        // TODO
    }

    /** TODO: synchronized ergaenzen. */
    public void erhoeheUm(int betrag) {
        // TODO
    }

    /**
     * TODO: synchronized ergaenzen.
     *
     * Warum auch beim LESEN? Ohne Synchronisierung darf die JVM den Wert
     * in einem Register oder CPU-Cache halten. Wer wert() aufruft, saehe
     * dann moeglicherweise nie, was andere Threads geschrieben haben -
     * auch nach Minuten nicht. Das nennt man das Sichtbarkeitsproblem.
     * (Kein Test kann das zuverlaessig pruefen - siehe README.)
     */
    public int wert() {
        // TODO
        return stand;
    }
}
