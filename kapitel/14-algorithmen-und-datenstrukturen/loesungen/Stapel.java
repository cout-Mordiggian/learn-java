import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Kapitel 14 - Musterloesung: ein Stapel (LIFO) als verkettete Liste.
 */
public class Stapel<E> implements Iterable<E> {

    // private: gehoert zur Implementierung, niemand ausserhalb braucht sie.
    // static: ein Knoten braucht kein Stapel-Objekt und traegt deshalb keinen
    // versteckten Verweis darauf mit sich herum. Weil er statisch ist, sieht er
    // das E von Stapel nicht und deklariert ein eigenes (gleicher Name,
    // technisch ein anderer Typparameter).
    private static class Knoten<E> {
        final E wert;
        final Knoten<E> naechster;   // der Knoten darunter, oder null

        Knoten(E wert, Knoten<E> naechster) {
            this.wert = wert;
            this.naechster = naechster;
        }
    }

    // Invariante: oben zeigt auf den zuletzt hineingelegten Knoten, oder ist
    // null, wenn der Stapel leer ist. anzahl zaehlt die Knoten der Kette.
    private Knoten<E> oben;
    private int anzahl;

    public void push(E element) {
        // Der neue Knoten zeigt auf den bisherigen obersten und wird selbst
        // der oberste. Bei leerem Stapel ist "oben" null - das passt genau,
        // der neue Knoten hat dann niemanden unter sich.
        oben = new Knoten<>(element, oben);
        anzahl++;
        // Aufwand: O(1). Kein Umkopieren, keine Kapazitaet, die voll werden kann.
    }

    public E pop() {
        E wert = peek();          // wirft bei leerem Stapel - Pruefung nur einmal schreiben
        oben = oben.naechster;    // den obersten Knoten "aushaengen"
        anzahl--;
        return wert;
        // Aufwand: O(1). Der ausgehaengte Knoten ist unerreichbar und wird vom
        // Garbage Collector weggeraeumt.
    }

    public E peek() {
        if (oben == null) {
            // Wie Deque.pop()/element(): leer ist ein Fehler des Aufrufers.
            // Ein stilles null waere mehrdeutig - null koennte ja auch ein
            // gespeicherter Wert sein.
            throw new NoSuchElementException("Der Stapel ist leer");
        }
        return oben.wert;
    }

    public boolean istLeer() {
        return oben == null;
    }

    public int groesse() {
        // Mitgezaehlt statt jedes Mal die Kette abzulaufen: O(1) statt O(n).
        return anzahl;
    }

    // ------------------------------------------------------------ Aufgabe 9

    @Override
    public Iterator<E> iterator() {
        // Jeder Aufruf liefert einen NEUEN Iterator mit eigener Position.
        // Deshalb koennen zwei for-each-Schleifen unabhaengig (auch
        // verschachtelt) ueber denselben Stapel laufen.
        return new StapelIterator();
    }

    // Nicht static: Eine innere Klasse ohne static haelt einen versteckten
    // Verweis auf das Stapel-Objekt, das sie erzeugt hat. Genau den braucht
    // der Iterator: "oben" im Feld aktuell ist eigentlich Stapel.this.oben. Knoten
    // dagegen braucht keinen Stapel und ist deshalb static.
    // Weil die Klasse nicht static ist, sieht sie auch das E von Stapel und
    // braucht kein eigenes.
    private class StapelIterator implements Iterator<E> {

        private Knoten<E> aktuell = oben;   // der Knoten, den next() als naechstes liefert

        @Override
        public boolean hasNext() {
            return aktuell != null;
        }

        @Override
        public E next() {
            if (aktuell == null) {
                // So verlangt es der Vertrag von Iterator: Wer ueber das Ende
                // hinaus fragt, bekommt eine Exception, kein stilles null.
                throw new NoSuchElementException("Keine weiteren Elemente");
            }
            E wert = aktuell.wert;
            aktuell = aktuell.naechster;    // nur der Iterator rueckt weiter, der Stapel bleibt
            return wert;
        }
    }
}
