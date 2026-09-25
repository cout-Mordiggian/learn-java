import java.util.Arrays;

/**
 * Kapitel 14 - Algorithmen und Datenstrukturen: Sortieren.
 * Pruefen:  ./lerne.sh 14
 *
 * Wichtig: Arrays.sort ist hier TABU. Die Tests wuerden es nicht merken -
 * aber dann haettest du nichts gelernt. Arrays.copyOfRange und clone sind
 * erlaubt, die sortieren nichts.
 */
public class Sortieren {

    /**
     * Aufgabe 1: Insertion Sort, AN ORT UND STELLE (in place).
     * Das uebergebene Array wird veraendert, es gibt keinen Rueckgabewert.
     *   int[] a = {5, 3, 8, 1};  insertionSort(a);  // a ist jetzt {1, 3, 5, 8}
     */
    public static void insertionSort(int[] a) {
        // TODO: Fuer i = 1 .. a.length-1: a[i] merken, alle groesseren
        //       Elemente links davon eine Stelle nach rechts ruecken, dann
        //       den gemerkten Wert in die Luecke legen.
        //       Invariante: links von i ist alles sortiert.
    }

    /**
     * Aufgabe 2: Merge Sort, rekursiv. Liefert ein NEUES sortiertes Array,
     * das Original bleibt unveraendert.
     *   mergeSort({5, 3, 8, 1}) -> {1, 3, 5, 8}
     */
    public static int[] mergeSort(int[] a) {
        // TODO: Basisfall: Laenge 0 oder 1 -> Kopie zurueckgeben (a.clone()).
        //       Sonst: in der Mitte teilen (Arrays.copyOfRange), beide Haelften
        //       rekursiv sortieren, mit merge zusammenfuehren.
        return new int[0];
    }

    /**
     * Hilfsmethode zu Aufgabe 2: fuegt zwei SORTIERTE Arrays zu einem
     * sortierten Array zusammen.
     *   merge({1, 5}, {2, 3, 9}) -> {1, 2, 3, 5, 9}
     */
    private static int[] merge(int[] links, int[] rechts) {
        // TODO: Ein Ergebnis-Array der Laenge links.length + rechts.length.
        //       Drei Indizes (links, rechts, Ergebnis). Solange beide Seiten
        //       noch Elemente haben: das kleinere nehmen. Dann den Rest anhaengen.
        return new int[0];
    }

    /**
     * Aufgabe 3: Ist das Array aufsteigend sortiert? Gleiche Nachbarn sind erlaubt.
     *   {1, 2, 2, 5} -> true,  {3, 1} -> false,  {} -> true
     */
    public static boolean istSortiert(int[] a) {
        // TODO: Nachbarpaare vergleichen.
        return false;
    }
}
