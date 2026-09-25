import java.util.Arrays;

/**
 * Kapitel 04 - Methoden.
 * Pruefen:  ./lerne.sh 04
 */
public class Aufgaben {

    /**
     * Aufgabe 1: Groesster gemeinsamer Teiler, rekursiv (Euklid).
     * ggT(48, 18) -> 6, ggT(7, 0) -> 7, ggT(0, 5) -> 5
     */
    public static int ggT(int a, int b) {
        // TODO: Basisfall b == 0, sonst ggT(b, a % b).
        return 0;
    }

    /** Aufgabe 2a: Maximum zweier int-Werte. */
    public static int max(int a, int b) {
        // TODO
        return 0;
    }

    /** Aufgabe 2b: Maximum dreier int-Werte. Nutze 2a! */
    public static int max(int a, int b, int c) {
        // TODO
        return 0;
    }

    /** Aufgabe 2c: Maximum zweier double-Werte. */
    public static double max(double a, double b) {
        // TODO
        return 0.0;
    }

    /**
     * Aufgabe 3a: Fibonacci rekursiv (die naive, langsame Variante).
     * fib(0)=0, fib(1)=1, fib(10)=55
     */
    public static long fibRekursiv(int n) {
        // TODO: Basisfall n <= 1.
        return 0L;
    }

    /**
     * Aufgabe 3b: Fibonacci iterativ. Muss auch fibIterativ(90) sofort liefern.
     */
    public static long fibIterativ(int n) {
        // TODO: Zwei Variablen mitfuehren und in jedem Schritt weiterschieben.
        return 0L;
    }

    /**
     * Aufgabe 4: Summe beliebig vieler Zahlen (Varargs).
     * summeAlle() -> 0, summeAlle(1,2,3) -> 6
     */
    public static int summeAlle(int... zahlen) {
        // TODO
        return 0;
    }

    /**
     * Aufgabe 5: Binaere Suche im aufsteigend sortierten Array.
     * Gibt den Index zurueck oder -1.
     * Schreibe dafuer eine private rekursive Hilfsmethode.
     */
    public static int binaereSuche(int[] sortiert, int gesucht) {
        // TODO: return suche(sortiert, gesucht, 0, sortiert.length - 1);
        return -1;
    }

    /**
     * Aufgabe 6: Verdoppelt jedes Element AN ORT UND STELLE.
     * Der Aufrufer sieht die Aenderung an seinem eigenen Array.
     */
    public static void verdoppleAlle(int[] werte) {
        // TODO
    }

    /**
     * Aufgabe 7: Neue Kopie mit zwei vertauschten Positionen.
     * Das Original bleibt unveraendert.
     * getauscht({1,2,3}, 0, 2) -> {3,2,1}
     */
    public static int[] getauscht(int[] werte, int i, int j) {
        // TODO: Erst kopieren, dann tauschen.
        return new int[0];
    }
}
