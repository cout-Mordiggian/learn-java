/**
 * Kapitel 02 - Operatoren und Kontrollfluss.
 * Pruefen:  ./lerne.sh 02
 */
public class Aufgaben {

    /**
     * Aufgabe 1: FizzBuzz fuer eine einzelne Zahl.
     * durch 3 teilbar        -> "Fizz"
     * durch 5 teilbar        -> "Buzz"
     * durch 3 UND 5 teilbar  -> "FizzBuzz"
     * sonst                  -> die Zahl als String, z.B. "7"
     * fizzbuzz(0) -> "FizzBuzz" (0 ist durch jede Zahl teilbar)
     */
    public static String fizzbuzz(int n) {
        // TODO: Reihenfolge der Bedingungen beachten!
        return "";
    }

    /**
     * Aufgabe 2: Schulnote als Text - mit switch-AUSDRUCK (Pfeilform).
     * 1 -> "sehr gut", 2 -> "gut", 3 -> "befriedigend",
     * 4 -> "ausreichend", 5 -> "mangelhaft", 6 -> "ungenuegend",
     * alles andere -> "ungueltig"
     */
    public static String notenText(int note) {
        // TODO: return switch (note) { case 1 -> ...; default -> ...; };
        return "";
    }

    /**
     * Aufgabe 3: Primzahltest.
     * istPrimzahl(2) -> true, istPrimzahl(9) -> false, istPrimzahl(1) -> false,
     * istPrimzahl(-7) -> false (negative Zahlen sind nie prim)
     */
    public static boolean istPrimzahl(int n) {
        // TODO: Sonderfaelle zuerst, dann Teiler suchen.
        return false;
    }

    /**
     * Aufgabe 4: Fakultaet, iterativ.
     * 0! = 1, 1! = 1, 5! = 120, 20! = 2432902008176640000
     */
    public static long fakultaet(int n) {
        // TODO: Schleife mit einem long-Akkumulator.
        return 0L;
    }

    /**
     * Aufgabe 5: Quersumme einer nicht-negativen Zahl.
     * quersumme(1234) -> 10, quersumme(0) -> 0
     */
    public static int quersumme(int n) {
        // TODO: while-Schleife, letzte Ziffer mit % 10 abholen und mit / 10 abschneiden.
        return 0;
    }

    /**
     * Aufgabe 6: Summe aller Zahlen von 1 bis grenze (einschliesslich),
     * die durch 3 oder 5 teilbar sind.
     * summeVielfache(10) -> 3+5+6+9+10 = 33
     */
    public static int summeVielfache(int grenze) {
        // TODO: for-Schleife, uninteressante Zahlen mit continue ueberspringen.
        return 0;
    }

    /**
     * Aufgabe 7: Sterndreieck als String, jede Zeile mit \n abgeschlossen.
     * sternDreieck(3) -> "*\n**\n***\n"
     * sternDreieck(0) -> ""
     */
    public static String sternDreieck(int hoehe) {
        // TODO: Zwei verschachtelte Schleifen - die aeussere fuer die Zeilen,
        //       die innere fuer die Sterne einer Zeile.
        //       Tipp: Ein StringBuilder sammelt das Ergebnis.
        return "";
    }
}
