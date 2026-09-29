/**
 * Kapitel 6 - Aufgabe 7a: ein Dekorierer, der jede Zeile nummeriert (6.9).
 * Pruefen:  ./lerne.sh 06
 *
 *   TextProtokoll basis = new TextProtokoll();
 *   Protokoll p = new MitZeilennummer(basis);
 *   p.schreibe("a");                  // basis bekommt "1: a"
 *   p.schreibe("b");                  // basis bekommt "2: b"
 *   basis.inhalt()                    // "1: a\n2: b\n"
 *
 * Jeder MitZeilennummer zaehlt fuer sich, ab 1.
 * innen null -> NullPointerException, und zwar schon im Konstruktor (5.7).
 */
public class MitZeilennummer implements Protokoll {

    private final Protokoll innen;
    // TODO: ein Zaehler

    public MitZeilennummer(Protokoll innen) {
        // TODO: null ablehnen
        this.innen = innen;
    }

    @Override
    public void schreibe(String zeile) {
        // TODO: Nummer davorsetzen und an das innere Protokoll weiterreichen
    }
}
