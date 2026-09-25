import java.util.Arrays;
import java.util.Objects;

/**
 * Kapitel 14 - Musterloesung: eine eigene ArrayList.
 */
public class MeineListe<E> {

    private static final int ANFANGSKAPAZITAET = 4;

    // Object[] statt E[]: "new E[4]" verbietet der Compiler, weil E zur
    // Laufzeit geloescht ist (Type Erasure, Kapitel 8.8) - ein Array muss
    // aber wissen, welchen Elementtyp es hat. java.util.ArrayList macht es
    // intern genauso.
    private Object[] elemente = new Object[ANFANGSKAPAZITAET];

    // Invariante: Die Plaetze 0..groesse-1 sind belegt, alle Plaetze ab
    // "groesse" sind null. groesse <= elemente.length.
    private int groesse = 0;

    public void add(E element) {
        if (groesse == elemente.length) {
            wachsen();
        }
        elemente[groesse] = element;
        groesse++;
        // Aufwand: meistens O(1). Beim Wachsen O(n) fuer das Umkopieren - aber
        // danach ist wieder genauso viel Platz frei, wie gerade belegt ist.
        // Verteilt auf alle add-Aufrufe: amortisiert O(1).
    }

    private void wachsen() {
        // Verdoppeln, nicht "+1": Mit +1 muesste JEDES add umkopieren, n Aufrufe
        // kosteten dann 1 + 2 + ... + n = O(n^2). Mit Verdoppeln kosten n
        // Aufrufe insgesamt weniger als 3n Schritte.
        // Arrays.copyOf legt ein neues, groesseres Array an und kopiert hinein;
        // die neuen Plaetze sind null.
        elemente = Arrays.copyOf(elemente, elemente.length * 2);
    }

    // Der Cast (E) kann zur Laufzeit nicht geprueft werden - E ist ja geloescht.
    // Der Compiler warnt deshalb ("unchecked cast"). Wir wissen es besser: In
    // das Array gelangen nur Werte ueber add und set, und die nehmen nur E an.
    // @SuppressWarnings sagt dem Compiler genau das. So eng wie moeglich
    // setzen (an die Methode, nicht an die ganze Klasse).
    @SuppressWarnings("unchecked")
    public E get(int index) {
        pruefeIndex(index);
        return (E) elemente[index];
        // Aufwand: O(1) - der Platz im Array wird direkt berechnet.
    }

    public E set(int index, E element) {
        pruefeIndex(index);
        E alt = get(index);           // wie ArrayList: den alten Wert zurueckgeben
        elemente[index] = element;
        return alt;
    }

    public int size() {
        return groesse;
    }

    public E remove(int index) {
        pruefeIndex(index);
        E entfernt = get(index);
        // Alles rechts von index eine Stelle nach links ruecken, damit keine
        // Luecke entsteht (die Invariante verlangt: 0..groesse-1 belegt).
        // System.arraycopy(elemente, index + 1, elemente, index, groesse - index - 1)
        // taete dasselbe, nur schneller.
        for (int i = index; i < groesse - 1; i++) {
            elemente[i] = elemente[i + 1];
        }
        groesse--;
        // Den letzten Platz leeren. Sonst haelt das Array noch eine Referenz
        // auf ein Objekt, das logisch gar nicht mehr in der Liste ist - der
        // Garbage Collector koennte es nie wegraeumen.
        elemente[groesse] = null;
        return entfernt;
        // Aufwand: O(n - index). remove(0) ist also der teuerste Fall: O(n).
        // Das Array schrumpft nicht - ArrayList macht das auch nicht.
    }

    public boolean contains(Object o) {
        // Nur bis groesse suchen, nicht bis elemente.length - dahinter liegen
        // keine gueltigen Elemente.
        for (int i = 0; i < groesse; i++) {
            // equals, nicht == : "gleicher Inhalt", nicht "dasselbe Objekt".
            // Objects.equals ist null-sicher - null darf in der Liste stehen und
            // auch gesucht werden.
            if (Objects.equals(o, elemente[i])) return true;
        }
        return false;
        // Aufwand: O(n). Deshalb ist "contains in einer Schleife" bei Listen
        // heimlich O(n^2) - fuer so etwas gibt es HashSet.
    }

    /** Nur zum Beobachten: wie gross das innere Array gerade ist. */
    public int kapazitaet() {
        return elemente.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < groesse; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elemente[i]);   // append(Object) schreibt bei null "null"
        }
        return sb.append("]").toString();
    }

    private void pruefeIndex(int index) {
        // Nicht auf das Array verlassen! Bei groesse 1 und Kapazitaet 4 gibt es
        // elemente[1] durchaus - es ist nur kein gueltiges Element. Ohne diese
        // Pruefung lieferte get(1) stillschweigend null.
        if (index < 0 || index >= groesse) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " ungueltig bei Groesse " + groesse);
        }
    }
}
