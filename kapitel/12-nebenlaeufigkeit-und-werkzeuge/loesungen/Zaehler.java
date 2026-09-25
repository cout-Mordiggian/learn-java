/**
 * Kapitel 12 - Musterloesung: thread-sicherer Zaehler.
 */
public class Zaehler {

    private int stand = 0;

    /**
     * synchronized auf einer Instanzmethode sperrt auf "this".
     * Alle synchronized-Methoden desselben Objekts schliessen sich
     * also gegenseitig aus.
     */
    public synchronized void erhoehen() {
        stand++;
    }

    public synchronized void erhoeheUm(int betrag) {
        stand += betrag;
    }

    /**
     * Auch der Lesezugriff ist synchronized - und das ist kein Zierrat.
     *
     * synchronized garantiert zwei Dinge:
     *   1. gegenseitigen Ausschluss (nur ein Thread zur Zeit)
     *   2. Sichtbarkeit: beim Betreten der Sperre werden die Aenderungen
     *      anderer Threads sichtbar, beim Verlassen die eigenen publiziert
     *      (das "happens-before"-Verhaeltnis des Java-Speichermodells)
     *
     * Ohne (2) duerfte die JVM stand in einem Register zwischenspeichern
     * und ein Leser saehe womoeglich dauerhaft einen veralteten Wert.
     *
     * Alternative fuer den reinen Lesefall: das Feld "volatile" machen -
     * das garantiert Sichtbarkeit, aber KEINEN Ausschluss. Fuer stand++
     * reicht volatile deshalb nicht.
     */
    public synchronized int wert() {
        return stand;
    }
}
