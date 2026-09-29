/**
 * Kapitel 6 - Aufgabe 7b: ein Dekorierer, der jede Zeile gross schreibt (6.9).
 * Pruefen:  ./lerne.sh 06
 *
 *   new Grossgeschrieben(basis).schreibe("Hallo")   // basis bekommt "HALLO"
 *
 * innen null -> NullPointerException im Konstruktor.
 */
public class Grossgeschrieben implements Protokoll {

    private final Protokoll innen;

    public Grossgeschrieben(Protokoll innen) {
        // TODO: null ablehnen
        this.innen = innen;
    }

    @Override
    public void schreibe(String zeile) {
        // TODO
    }
}
