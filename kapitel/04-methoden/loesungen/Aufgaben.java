/**
 * Kapitel 04 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static int ggT(int a, int b) {
        // Euklid: der ggT aendert sich nicht, wenn man die groessere Zahl
        // durch den Rest der Division ersetzt. Der Rest wird streng kleiner,
        // also ist der Basisfall b == 0 garantiert erreichbar.
        // ggT(18, 48) -> ggT(48, 18) : der erste Schritt dreht das von selbst um,
        // weil 18 % 48 == 18.
        if (b == 0) return a;
        return ggT(b, a % b);
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int max(int a, int b, int c) {
        // Wiederverwendung statt Kopieren: die Zwei-Parameter-Version
        // kennt die Vergleichslogik schon. Aendert sie sich (z.B. bei
        // Gleichstand), bleibt alles konsistent.
        return max(max(a, b), c);
    }

    public static double max(double a, double b) {
        // Ueberladung: gleicher Name, andere Parametertypen. Der Compiler
        // waehlt anhand der Argumente aus - bei max(3.2, 5.5) diese hier.
        return a > b ? a : b;
    }

    public static long fibRekursiv(int n) {
        if (n <= 1) return n;                             // Basisfall
        return fibRekursiv(n - 1) + fibRekursiv(n - 2);   // zwei Aufrufe pro Ebene
        // Aufwand: O(2^n). fibRekursiv(50) berechnet fib(10) millionenfach neu.
        // Heilbar mit Memoisierung (Zwischenspeicher) - oder eben iterativ.
    }

    public static long fibIterativ(int n) {
        if (n <= 1) return n;
        long vorletzte = 0;   // fib(0)
        long letzte = 1;      // fib(1)
        for (int i = 2; i <= n; i++) {
            long naechste = vorletzte + letzte;
            vorletzte = letzte;      // beide Werte um eine Position weiterschieben
            letzte = naechste;
        }
        return letzte;
        // O(n) Zeit, O(1) Speicher. fib(92) ist der letzte Wert, der in long
        // passt - ab fib(93) laeuft die Rechnung still ueber.
    }

    public static int summeAlle(int... zahlen) {
        // Innerhalb der Methode ist "zahlen" ein ganz normales int[].
        // Bei summeAlle() uebergibt der Compiler ein leeres Array, nicht null.
        int summe = 0;
        for (int z : zahlen) summe += z;
        return summe;
    }

    public static int binaereSuche(int[] sortiert, int gesucht) {
        // Die oeffentliche Methode ist die bequeme Fassade; die Rekursion
        // braucht zusaetzliche Parameter, die den Aufrufer nicht interessieren.
        // Dieses Muster (public Fassade + private Rekursionsmethode) siehst du
        // in der Standardbibliothek staendig.
        return suche(sortiert, gesucht, 0, sortiert.length - 1);
    }

    private static int suche(int[] a, int gesucht, int von, int bis) {
        if (von > bis) return -1;              // Bereich leer -> nicht gefunden

        // NICHT (von + bis) / 2 : bei sehr grossen Indizes kann die Summe
        // den int-Bereich sprengen und negativ werden. Diese Form kann das nicht.
        int mitte = von + (bis - von) / 2;

        if (a[mitte] == gesucht) return mitte;
        if (a[mitte] < gesucht) return suche(a, gesucht, mitte + 1, bis);
        return suche(a, gesucht, von, mitte - 1);
        // Jeder Schritt halbiert den Bereich -> O(log n).
    }

    public static void verdoppleAlle(int[] werte) {
        // Wir veraendern das Objekt, auf das die Referenz zeigt.
        // Der Aufrufer sieht das, weil seine Variable auf dasselbe Array zeigt.
        for (int i = 0; i < werte.length; i++) {
            werte[i] *= 2;
        }
        // Achtung: "werte = new int[]{...}" haette KEINE Wirkung nach aussen -
        // das wuerde nur die lokale Kopie der Referenz umbiegen.
    }

    public static int[] getauscht(int[] werte, int i, int j) {
        // Der andere Vertrag: nichts veraendern, Ergebnis zurueckgeben.
        // Solche Methoden sind leichter zu testen und ohne Ueberraschungen
        // nebenlaeufig nutzbar (Kapitel 12).
        int[] kopie = werte.clone();
        int merker = kopie[i];
        kopie[i] = kopie[j];
        kopie[j] = merker;
        return kopie;
    }
}
