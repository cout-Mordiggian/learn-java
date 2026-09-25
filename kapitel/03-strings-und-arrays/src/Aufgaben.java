import java.util.Arrays;

/**
 * Kapitel 03 - Strings und Arrays.
 * Pruefen:  ./lerne.sh 03
 */
public class Aufgaben {

    /**
     * Aufgabe 1: String umdrehen.
     * umdrehen("abc") -> "cba", umdrehen("") -> ""
     */
    public static String umdrehen(String text) {
        // TODO: StringBuilder hat eine Methode, die genau das kann.
        return "";
    }

    /**
     * Aufgabe 2: Palindromtest, unempfindlich gegen Gross-/Kleinschreibung
     * und gegen alles, was kein Buchstabe oder keine Ziffer ist.
     * "Ein Esel lese nie" -> true
     * "Hallo"             -> false
     * ""                  -> true (per Definition)
     */
    public static boolean istPalindrom(String text) {
        // TODO: 1. Nur Buchstaben/Ziffern uebernehmen, alles klein schreiben.
        //       2. Vergleichen - entweder mit umdrehen() oder mit zwei Indizes,
        //          die von aussen nach innen laufen.
        return false;
    }

    /**
     * Aufgabe 3: Woerter zaehlen.
     * "Hallo Welt"          -> 2
     * "  viel   Abstand  "  -> 2
     * ""                    -> 0
     * "   "                 -> 0
     */
    public static int wortAnzahl(String satz) {
        // TODO: strip() und split("\\s+"). Denke an den Sonderfall "leer".
        return 0;
    }

    /**
     * Aufgabe 4: Groesstes Element eines nicht-leeren Arrays.
     */
    public static int maximum(int[] werte) {
        // TODO: Starte NICHT mit 0 - was ist bei lauter negativen Zahlen?
        return 0;
    }

    /**
     * Aufgabe 5: Arithmetisches Mittel. Leeres Array -> 0.0
     * mittelwert(new int[]{1, 2}) -> 1.5
     * Muss auch fuer sehr grosse Werte stimmen, z.B. {Integer.MAX_VALUE, Integer.MAX_VALUE}.
     */
    public static double mittelwert(int[] werte) {
        // TODO: Achtung Ganzzahldivision! Und: In welchem Typ sammelst du die Summe?
        return 0.0;
    }

    /**
     * Aufgabe 6: Aufsteigend sortierte KOPIE. Das Original bleibt unveraendert.
     */
    public static int[] sortierteKopie(int[] werte) {
        // TODO: Erst kopieren (Arrays.copyOf oder clone), dann sortieren.
        return new int[0];
    }

    /**
     * Aufgabe 7: Matrix transponieren.
     * {{1,2,3},{4,5,6}} -> {{1,4},{2,5},{3,6}}
     * Leere Matrix -> leere Matrix
     */
    public static int[][] transponiere(int[][] matrix) {
        // TODO: Das Ergebnis hat so viele Zeilen, wie die Eingabe Spalten hat.
        return new int[0][0];
    }

    /**
     * Aufgabe 8: Teile mit einem Trenner verbinden (wie String.join, selbst gebaut).
     * ({"a","b","c"}, "-") -> "a-b-c"
     * ({"a"}, "-")         -> "a"
     * ({}, "-")            -> ""
     */
    public static String zusammenfuegen(String[] teile, String trenner) {
        // TODO: Kein Trenner am Anfang und keiner am Ende.
        //       Tipp: Trenner VOR jedem Element ausser dem ersten einfuegen.
        return "";
    }
}
