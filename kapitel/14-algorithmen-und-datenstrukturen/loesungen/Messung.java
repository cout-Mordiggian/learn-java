import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

/**
 * Kapitel 14 - fertige Mess-Demo. Hier ist nichts zu tun: starten und die
 * Spalte "Faktor" beobachten.
 *
 *   ./lerne.sh 14 -r Messung        (oder: bash lerne.sh 14 -r Messung)
 *
 * Die Demo benutzt NUR eigene Methoden aus dieser Datei und Arrays.sort /
 * Arrays.binarySearch. Sie laeuft also auch, solange deine Aufgaben noch
 * offen sind.
 *
 * Deine absoluten Zahlen haengen vom Rechner ab. Aussagekraeftig ist, wie sie
 * WACHSEN, wenn n sich verdoppelt bzw. verzehnfacht.
 */
public class Messung {

    /** Jede Messung wird so oft wiederholt; gezeigt wird der schnellste Lauf. */
    private static final int WIEDERHOLUNGEN = 3;

    /**
     * Alle Ergebnisse landen hier und werden am Ende ausgegeben. Wuerden wir
     * sie wegwerfen, duerfte der JIT-Compiler die ganze Rechnung als "tot"
     * erkennen und weglassen - dann messen wir nichts.
     */
    private static long senke = 0;

    public static void main(String[] args) {
        long gesamtStart = System.nanoTime();
        System.out.println();
        System.out.println("  Messung: Wie waechst die Laufzeit mit n?");
        System.out.println("  ========================================");

        kaltUndWarm();
        aufwaermen();
        suchen();
        sortieren();

        System.out.println();
        System.out.printf(Locale.GERMANY, "  (Pruefsumme %d - nur damit der JIT nichts wegoptimiert."
                + " Gesamtdauer %.1f s)%n", senke, (System.nanoTime() - gesamtStart) / 1e9);
        System.out.println();
    }

    // ------------------------------------------------------------ Teil 1

    /** Dieselbe Arbeit zehnmal hintereinander - die ersten Laeufe sind langsamer. */
    private static void kaltUndWarm() {
        System.out.println();
        System.out.println("  1) Dieselbe Arbeit zehnmal gemessen (Selection Sort, n = 3.000):");
        System.out.print("     ");
        int[] daten = zufallsArray(3_000, new Random(1));
        for (int lauf = 1; lauf <= 10; lauf++) {
            int[] kopie = daten.clone();
            long start = System.nanoTime();
            selectionSort(kopie);
            long dauer = System.nanoTime() - start;
            senke += kopie[0];
            System.out.printf(Locale.GERMANY, "%.2f ", dauer / 1e6);
        }
        System.out.println(" (ms)");
        System.out.println("     Die ersten Laeufe laufen im Interpreter, dann uebersetzt der JIT die");
        System.out.println("     Methode in Maschinencode. Eine einzelne Messung sagt deshalb wenig.");
    }

    /**
     * Vor den eigentlichen Messungen: alle gemessenen Methoden ein paar hundert
     * Mal laufen lassen, damit der JIT sie schon uebersetzt hat. Sonst misst die
     * erste Zeile der Tabellen den Interpreter und die spaeteren Zeilen den
     * Maschinencode - und die Faktoren waeren Unsinn.
     */
    private static void aufwaermen() {
        System.out.println();
        System.out.println("  Aufwaermen (die Ergebnisse davon werden nicht gezeigt) ...");
        Random zufall = new Random(4);
        int[] sortiert = new int[10_000];
        for (int i = 0; i < sortiert.length; i++) sortiert[i] = 2 * i;
        int[] anfragen = new int[2_000];
        for (int i = 0; i < anfragen.length; i++) anfragen[i] = 2 * zufall.nextInt(sortiert.length);
        for (int runde = 0; runde < 30; runde++) {
            zeitLinear(sortiert, anfragen);
            zeitBinaer(sortiert, anfragen);
            int[] daten = zufallsArray(20_000, zufall);
            zeitArraysSort(daten);
            zeitSelectionSort(Arrays.copyOf(daten, 1_000));
        }
    }

    // ------------------------------------------------------------ Teil 2

    private static void suchen() {
        System.out.println();
        System.out.println("  2) Suchen in einem sortierten Array (Zeit pro Suche, n verzehnfacht sich):");
        System.out.println();
        System.out.println("             n    linear (ns)   Faktor    binaer (ns)   Faktor");
        Random zufall = new Random(2);
        double vorherLinear = 0;
        double vorherBinaer = 0;
        for (int n = 1_000; n <= 1_000_000; n *= 10) {
            int[] daten = new int[n];
            for (int i = 0; i < n; i++) daten[i] = 2 * i;   // sortiert: 0, 2, 4, ...

            // Gesucht werden Werte, die sicher vorkommen. Bei kleinem n braucht
            // die lineare Suche mehr Anfragen, damit die Messung nicht zu kurz ist.
            int[] anfragenLinear = new int[Math.max(300, 30_000_000 / n)];
            for (int i = 0; i < anfragenLinear.length; i++) anfragenLinear[i] = 2 * zufall.nextInt(n);
            int[] anfragenBinaer = new int[200_000];
            for (int i = 0; i < anfragenBinaer.length; i++) anfragenBinaer[i] = 2 * zufall.nextInt(n);

            double linear = (double) zeitLinear(daten, anfragenLinear) / anfragenLinear.length;
            double binaer = (double) zeitBinaer(daten, anfragenBinaer) / anfragenBinaer.length;
            System.out.printf(Locale.GERMANY, "  %,12d  %13.1f   %6s  %13.1f   %6s%n",
                    n, linear, faktor(linear, vorherLinear), binaer, faktor(binaer, vorherBinaer));
            vorherLinear = linear;
            vorherBinaer = binaer;
        }
        System.out.println();
        System.out.println("     Linear: n mal 10 -> Zeit ungefaehr mal 10 (O(n)).");
        System.out.println("     Binaer: n mal 10 -> nur ca. 3 Schritte mehr (O(log n)). Dass die Zeit");
        System.out.println("     trotzdem etwas steigt, liegt am Prozessor-Cache: grosse Arrays passen nicht");
        System.out.println("     mehr hinein, jeder Sprung ins Array wird teurer.");
    }

    private static long zeitLinear(int[] daten, int[] anfragen) {
        long beste = Long.MAX_VALUE;
        for (int w = 0; w < WIEDERHOLUNGEN; w++) {
            long start = System.nanoTime();
            for (int gesucht : anfragen) senke += lineareSuche(daten, gesucht);
            beste = Math.min(beste, System.nanoTime() - start);
        }
        return beste;
    }

    private static long zeitBinaer(int[] daten, int[] anfragen) {
        long beste = Long.MAX_VALUE;
        for (int w = 0; w < WIEDERHOLUNGEN; w++) {
            long start = System.nanoTime();
            for (int gesucht : anfragen) senke += Arrays.binarySearch(daten, gesucht);
            beste = Math.min(beste, System.nanoTime() - start);
        }
        return beste;
    }

    // ------------------------------------------------------------ Teil 3

    private static void sortieren() {
        System.out.println();
        System.out.println("  3) Sortieren eines Zufalls-Arrays (n verdoppelt sich):");
        System.out.println();
        System.out.println("             n    Selection Sort (ms)   Faktor    Arrays.sort (ms)   Faktor");
        Random zufall = new Random(3);
        double vorherSelection = 0;
        double vorherArrays = 0;
        for (int n = 1_000; n <= 32_000; n *= 2) {
            int[] daten = zufallsArray(n, zufall);
            double selection = zeitSelectionSort(daten) / 1e6;
            double arrays = zeitArraysSort(daten) / 1e6;
            System.out.printf(Locale.GERMANY, "  %,12d  %19.2f   %6s  %17.3f   %6s%n",
                    n, selection, faktor(selection, vorherSelection), arrays, faktor(arrays, vorherArrays));
            vorherSelection = selection;
            vorherArrays = arrays;
        }
        System.out.println();
        System.out.println("     Selection Sort: n mal 2 -> Zeit ungefaehr mal 4 (O(n^2)).");
        System.out.println("     Arrays.sort:    n mal 2 -> Zeit gut mal 2 (O(n log n)). Bei kleinen n");
        System.out.println("     schwanken die Werte stark - die Messungen sind dort sehr kurz.");
    }

    private static long zeitSelectionSort(int[] daten) {
        long beste = Long.MAX_VALUE;
        for (int w = 0; w < WIEDERHOLUNGEN; w++) {
            int[] kopie = daten.clone();          // Kopieren gehoert nicht zur Messung
            long start = System.nanoTime();
            selectionSort(kopie);
            beste = Math.min(beste, System.nanoTime() - start);
            senke += kopie[kopie.length / 2];
        }
        return beste;
    }

    private static long zeitArraysSort(int[] daten) {
        long beste = Long.MAX_VALUE;
        // Arrays.sort ist so schnell, dass wir oefter messen koennen.
        for (int w = 0; w < 5 * WIEDERHOLUNGEN; w++) {
            int[] kopie = daten.clone();
            long start = System.nanoTime();
            Arrays.sort(kopie);
            beste = Math.min(beste, System.nanoTime() - start);
            senke += kopie[kopie.length / 2];
        }
        return beste;
    }

    // ------------------------------------------------------------ Algorithmen

    /** Lineare Suche: jedes Element der Reihe nach anschauen. O(n). */
    private static int lineareSuche(int[] a, int gesucht) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == gesucht) return i;
        }
        return -1;
    }

    /**
     * Selection Sort: das kleinste Element des unsortierten Rests suchen und
     * nach vorn tauschen. Immer ca. n*n/2 Vergleiche -> O(n^2).
     * (Nicht zu verwechseln mit Insertion Sort - das ist deine Aufgabe.)
     */
    private static void selectionSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int kleinster = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[kleinster]) kleinster = j;
            }
            int merker = a[i];
            a[i] = a[kleinster];
            a[kleinster] = merker;
        }
    }

    // ------------------------------------------------------------ Hilfen

    private static int[] zufallsArray(int n, Random zufall) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = zufall.nextInt(1_000_000);
        return a;
    }

    /** "x 4,1" - wie viel mal so lange wie in der Zeile davor. */
    private static String faktor(double jetzt, double vorher) {
        if (vorher <= 0) return "-";
        return String.format(Locale.GERMANY, "x %.1f", jetzt / vorher);
    }
}
