/**
 * Kapitel 14 - Aufgabe 6: eine eigene ArrayList.
 * Pruefen:  ./lerne.sh 14
 *
 * Im Inneren: ein Array plus ein Zaehler, wie viele Plaetze davon belegt sind.
 * Ist das Array voll, wird es durch ein doppelt so grosses ersetzt.
 *
 * Warum Object[] und nicht E[]? "new E[4]" verbietet der Compiler, weil der
 * Typparameter zur Laufzeit geloescht ist (Type Erasure, Kapitel 8.8).
 * Deshalb speichern wir Object und casten beim Herausgeben - so macht es
 * java.util.ArrayList auch. Siehe README, Abschnitt 14.5.
 */
public class MeineListe<E> {

    private static final int ANFANGSKAPAZITAET = 4;

    private Object[] elemente = new Object[ANFANGSKAPAZITAET];

    // Wie viele Plaetze von "elemente" belegt sind. Belegt sind immer genau
    // die Plaetze 0 bis groesse - 1.
    private int groesse = 0;

    /** Haengt ein Element hinten an. Ist das Array voll: vorher verdoppeln. */
    public void add(E element) {
        // TODO: Ist groesse == elemente.length? Dann ein doppelt so grosses Array
        //       anlegen und alles hinueberkopieren (Schleife oder Arrays.copyOf).
        //       Danach das Element auf Platz "groesse" legen und groesse erhoehen.
    }

    /**
     * Element an Position index. Ungueltiger Index (kleiner 0 oder >= size())
     * -> IndexOutOfBoundsException.
     */
    @SuppressWarnings("unchecked")   // der Cast unten ist ungeprueft - siehe README 14.5
    public E get(int index) {
        // TODO: Index pruefen, dann: return (E) elemente[index];
        return null;
    }

    /** Ersetzt das Element an Position index und gibt das ALTE zurueck. */
    public E set(int index, E element) {
        // TODO: wie get pruefen, alten Wert merken, ersetzen, alten Wert zurueckgeben.
        return null;
    }

    /** Anzahl der Elemente - NICHT die Laenge des Arrays. */
    public int size() {
        // TODO
        return 0;
    }

    /**
     * Entfernt das Element an Position index und gibt es zurueck.
     * Alles dahinter rueckt eine Stelle nach links, es bleibt keine Luecke.
     *   [a, b, c, d].remove(1) -> liefert b, Liste ist danach [a, c, d]
     */
    public E remove(int index) {
        // TODO: Index pruefen, Element merken, nach links ruecken, groesse
        //       verringern, den frei gewordenen letzten Platz auf null setzen.
        return null;
    }

    /**
     * Ist ein Element enthalten, das equals zu o ist? Auch null darf gesucht werden.
     */
    public boolean contains(Object o) {
        // TODO: nur die belegten Plaetze durchsuchen. Mit equals vergleichen,
        //       nicht mit == (Objects.equals ist null-sicher).
        return false;
    }

    /** Nur zum Beobachten (und fuer die Tests): die Laenge des inneren Arrays. Fertig. */
    public int kapazitaet() {
        return elemente.length;
    }

    /** Wie ArrayList: "[]", "[a]", "[a, b, c]". */
    @Override
    public String toString() {
        // TODO: StringBuilder, zwischen den Elementen ", ".
        return "";
    }

    // Tipp: Eine private Methode pruefeIndex(int index), die die Exception wirft,
    // erspart dir, dieselbe Pruefung in get, set und remove dreimal zu schreiben.
}
