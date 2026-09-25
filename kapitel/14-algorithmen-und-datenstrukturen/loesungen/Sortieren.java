import java.util.Arrays;

/**
 * Kapitel 14 - Musterloesung: Sortieren.
 */
public class Sortieren {

    public static void insertionSort(int[] a) {
        // Invariante: Vor jedem Durchlauf ist a[0..i-1] (alles LINKS von i)
        // sortiert. Am Anfang (i = 1) ist das trivial wahr - ein einzelnes
        // Element ist immer sortiert. Am Ende ist i == a.length, also ist
        // alles sortiert. Leeres Array und ein Element: die Schleife laeuft
        // gar nicht, fertig.
        for (int i = 1; i < a.length; i++) {
            int aktuell = a[i];          // die "neue Karte" aus der Hand nehmen
            int j = i - 1;
            // Alle groesseren Elemente eine Stelle nach rechts ruecken.
            // Nur STRENG groessere (>, nicht >=): gleiche Werte werden nicht
            // uebersprungen, ihre Reihenfolge bleibt - das macht es stabil.
            while (j >= 0 && a[j] > aktuell) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = aktuell;          // in die entstandene Luecke legen
        }
        // Aufwand: schlimmster Fall (umgekehrt sortiert) ca. n*n/2 Verschiebungen
        // -> O(n^2). Bester Fall (schon sortiert): die while-Schleife bricht
        // sofort ab -> O(n). Zusatzspeicher: O(1), alles passiert "in place".
    }

    public static int[] mergeSort(int[] a) {
        // Basisfall: 0 oder 1 Element ist sortiert. Trotzdem eine KOPIE
        // zurueckgeben - der Vertrag sagt "neues Array", und der Aufrufer
        // soll das Ergebnis aendern duerfen, ohne sein Original zu treffen.
        if (a.length <= 1) return a.clone();

        // Teilen: zwei Haelften als neue Arrays. copyOfRange(von, bis) ist
        // halboffen - "bis" ist nicht mehr dabei.
        int mitte = a.length / 2;
        int[] links = mergeSort(Arrays.copyOfRange(a, 0, mitte));
        int[] rechts = mergeSort(Arrays.copyOfRange(a, mitte, a.length));

        // Herrschen: zwei sortierte Haelften zu einem sortierten Ganzen mischen.
        return merge(links, rechts);

        // Aufwand: Jede Ebene der Rekursion fasst insgesamt alle n Elemente
        // einmal an (O(n)), und es gibt ca. log2(n) Ebenen, weil jede Ebene
        // halbiert -> O(n log n), und zwar IMMER, auch im schlimmsten Fall.
        // Preis: O(n) Zusatzspeicher fuer die Teil-Arrays.
        // Rekursionstiefe nur log2(n) - bei einer Million ca. 20. Ein
        // StackOverflowError (Kapitel 4.4) droht hier also nicht.
    }

    private static int[] merge(int[] links, int[] rechts) {
        int[] ergebnis = new int[links.length + rechts.length];
        int i = 0;   // naechstes unbenutztes Element in links
        int j = 0;   // naechstes unbenutztes Element in rechts
        int k = 0;   // naechste freie Stelle im Ergebnis
        // Invariante: ergebnis[0..k-1] ist sortiert und enthaelt genau die
        // kleinsten k Elemente beider Haelften.
        while (i < links.length && j < rechts.length) {
            // <= statt < : Bei Gleichstand gewinnt die LINKE Haelfte, deren
            // Elemente urspruenglich weiter vorn standen -> stabil.
            if (links[i] <= rechts[j]) {
                ergebnis[k++] = links[i++];
            } else {
                ergebnis[k++] = rechts[j++];
            }
        }
        // Eine Haelfte ist aufgebraucht, der Rest der anderen ist schon sortiert
        // und groesser als alles bisher - einfach anhaengen. Hoechstens eine
        // der beiden Schleifen laeuft ueberhaupt.
        while (i < links.length) ergebnis[k++] = links[i++];
        while (j < rechts.length) ergebnis[k++] = rechts[j++];
        return ergebnis;
        // Aufwand: jedes Element wird genau einmal kopiert -> O(n).
    }

    public static boolean istSortiert(int[] a) {
        // Sortiert heisst: kein Nachbarpaar steht falsch herum. Gleiche
        // Nachbarn sind erlaubt (Duplikate). Man muss also nur n-1 Paare
        // anschauen -> O(n). Bei 0 oder 1 Element gibt es kein Paar -> true.
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) return false;
        }
        return true;
    }
}
