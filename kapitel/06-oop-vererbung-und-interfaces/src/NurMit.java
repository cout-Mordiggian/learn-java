/**
 * Kapitel 6 - Aufgabe 7c: ein Dekorierer, der nur passende Zeilen durchlaesst (6.9).
 * Pruefen:  ./lerne.sh 06
 *
 *   Protokoll p = new NurMit("FEHLER", basis);
 *   p.schreibe("INFO los");          // wird verschluckt
 *   p.schreibe("FEHLER kaputt");     // basis bekommt "FEHLER kaputt"
 *
 * Durch kommt jede Zeile, die teil enthaelt (Gross-/Kleinschreibung zaehlt).
 * teil oder innen null -> NullPointerException im Konstruktor.
 */
public class NurMit implements Protokoll {

    private final String teil;
    private final Protokoll innen;

    public NurMit(String teil, Protokoll innen) {
        // TODO: null ablehnen
        this.teil = teil;
        this.innen = innen;
    }

    @Override
    public void schreibe(String zeile) {
        // TODO
    }
}
