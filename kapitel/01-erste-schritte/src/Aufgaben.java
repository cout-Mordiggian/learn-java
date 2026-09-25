/**
 * Kapitel 01 - Erste Schritte.
 *
 * Ersetze jedes TODO durch echten Code. Die Methoden geben aktuell
 * Platzhalterwerte zurueck, damit die Datei ueberhaupt kompiliert.
 *
 * Pruefen:  ./lerne.sh 01
 */
public class Aufgaben {

    /**
     * Aufgabe 1: Baue einen Begruessungssatz.
     * begruessung("Anna") -> "Hallo, Anna!"
     */
    public static String begruessung(String name) {
        // TODO: Setze den Satz mit dem +-Operator zusammen.
        return "";
    }

    /**
     * Aufgabe 2: Celsius in Fahrenheit umrechnen.
     * F = C * 9/5 + 32
     * celsiusZuFahrenheit(100.0) -> 212.0
     * celsiusZuFahrenheit(37.0)  -> 98.6
     */
    public static double celsiusZuFahrenheit(double celsius) {
        // TODO: Vorsicht - 9/5 ist in Java 1, nicht 1.8!
        return 0.0;
    }

    /**
     * Aufgabe 3: Flaeche eines Kreises.
     * A = pi * r^2, nutze Math.PI
     * kreisFlaeche(1.0) -> 3.14159...
     */
    public static double kreisFlaeche(double radius) {
        // TODO: radius * radius ist hier klarer als Math.pow - warum wohl?
        return 0.0;
    }

    /**
     * Aufgabe 4: Die letzte Ziffer einer nicht-negativen Zahl.
     * letzteZiffer(1234) -> 4
     * letzteZiffer(7)    -> 7
     * letzteZiffer(0)    -> 0
     */
    public static int letzteZiffer(int zahl) {
        // TODO: Ein einziger Operator reicht.
        return -1;
    }

    /**
     * Aufgabe 5: Sekunden als Uhrzeit formatieren.
     * Die Eingabe liegt immer zwischen 0 und 86399 (hoechstens "23:59:59").
     * zeitFormat(3661) -> "01:01:01"
     * zeitFormat(59)   -> "00:00:59"
     * zeitFormat(86399)-> "23:59:59"
     */
    public static String zeitFormat(int sekundenGesamt) {
        // TODO: Stunden, Minuten und Sekunden per / und % berechnen,
        //       dann mit String.format("%02d:%02d:%02d", ...) zusammensetzen.
        return "";
    }

    /**
     * Aufgabe 6: Millisekunden in einem Jahr (365 Tage).
     * Erwartet: 31536000000
     *
     * Der Rueckgabetyp ist long - aber das allein genuegt nicht.
     * Wenn du 365 * 24 * 60 * 60 * 1000 schreibst, rechnet Java die
     * ganze Multiplikation in int und laeuft dabei ueber.
     */
    public static long millisekundenProJahr() {
        // TODO: Sorge dafuer, dass die Rechnung selbst in long stattfindet.
        return 0L;
    }
}
