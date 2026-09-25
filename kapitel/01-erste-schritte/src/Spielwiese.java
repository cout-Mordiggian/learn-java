/**
 * Freie Spielwiese. Aendere hier, was du willst, und starte mit:
 *
 *     ./lerne.sh 01 -r Spielwiese
 */
public class Spielwiese {
    public static void main(String[] args) {
        System.out.println("1 / 2        = " + (1 / 2));
        System.out.println("1.0 / 2      = " + (1.0 / 2));
        System.out.println("0.1 + 0.2    = " + (0.1 + 0.2));
        System.out.println("(int) -3.7   = " + (int) -3.7);
        System.out.println("Math.round(-3.7) = " + Math.round(-3.7));

        int gross = 2_000_000_000;
        System.out.println("int-Ueberlauf: " + (gross + gross));
        System.out.println("mit long:      " + (2_000_000_000L + 2_000_000_000L));

        System.out.printf("Formatiert: %s hat %d Punkte (%.1f%%)%n", "Anna", 42, 87.5);
    }
}
