import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Kapitel 14 - Aufgabe 7: ein Stapel (LIFO: last in, first out) als
 * verkettete Liste.
 * Pruefen:  ./lerne.sh 14
 *
 *   push(1), push(2), push(3):   oben -> [3] -> [2] -> [1] -> null
 *   pop() liefert 3:             oben -> [2] -> [1] -> null
 *
 * Aufgabe 9 (erst nach Aufgabe 7): Der Stapel wird Iterable, dann laeuft
 * for-each von oben nach unten ueber ihn. Siehe README 14.9.
 */
public class Stapel<E> implements Iterable<E> {

    /**
     * Ein Glied der Kette - schon fertig.
     *
     * "private static class" ist eine statische innere Klasse: Sie gehoert zu
     * Stapel, niemand ausserhalb sieht sie (private), und ein Knoten braucht
     * kein Stapel-Objekt (static). Sie hat ihr eigenes E. Siehe README 14.6.
     */
    private static class Knoten<E> {
        final E wert;
        final Knoten<E> naechster;   // der Knoten darunter, oder null

        Knoten(E wert, Knoten<E> naechster) {
            this.wert = wert;
            this.naechster = naechster;
        }
    }

    private Knoten<E> oben;   // oberster Knoten, null bei leerem Stapel
    private int anzahl;       // mitzaehlen, damit groesse() nicht die Kette ablaufen muss

    /** Legt ein Element oben auf den Stapel. O(1). */
    public void push(E element) {
        // TODO: neuer Knoten, der auf den bisherigen obersten zeigt; er wird der neue oberste.
    }

    /**
     * Nimmt das oberste Element herunter und gibt es zurueck. O(1).
     * Leerer Stapel -> NoSuchElementException.
     */
    public E pop() {
        // TODO: bei leerem Stapel: throw new NoSuchElementException("...");
        //       sonst Wert merken, oben auf den naechsten Knoten setzen, anzahl verringern.
        return null;
    }

    /**
     * Liefert das oberste Element, OHNE es zu entfernen.
     * Leerer Stapel -> NoSuchElementException.
     */
    public E peek() {
        // TODO
        return null;
    }

    public boolean istLeer() {
        // TODO
        return true;
    }

    public int groesse() {
        // TODO
        return 0;
    }

    // ------------------------------------------------------------ Aufgabe 9

    /** Liefert einen Iterator, der von oben nach unten laeuft. Entfernt nichts. */
    @Override
    public Iterator<E> iterator() {
        return new StapelIterator();
    }

    /**
     * Bewusst OHNE static (anders als Knoten): Der Iterator braucht "seinen"
     * Stapel, denn er startet bei dessen oberstem Knoten. Siehe README 14.9.
     */
    private class StapelIterator implements Iterator<E> {

        // TODO: ein Feld fuer den Knoten, dessen Wert next() als naechstes liefert.
        //       Startwert: der oberste Knoten des Stapels.

        @Override
        public boolean hasNext() {
            // TODO
            return false;
        }

        @Override
        public E next() {
            // TODO: nichts mehr da -> NoSuchElementException.
            //       Sonst Wert merken, einen Knoten weiterruecken, Wert zurueckgeben.
            return null;
        }
    }
}
