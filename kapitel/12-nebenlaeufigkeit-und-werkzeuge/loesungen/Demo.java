/**
 * Fertig vorgegeben: zeigt eine Race Condition live.
 *
 *     ./lerne.sh 12 -r Demo
 *
 * Fuehr das mehrmals aus - das Ergebnis der unsicheren Variante
 * ist jedes Mal ein anderes, und praktisch nie das richtige.
 */
public class Demo {

    // Bewusst UNGESCHUETZT - genau darum geht es.
    private static int unsicher = 0;

    public static void main(String[] args) throws InterruptedException {
        final int THREADS = 8;
        final int PRO_THREAD = 100_000;
        final int ERWARTET = THREADS * PRO_THREAD;

        Thread[] alle = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            alle[i] = new Thread(() -> {
                for (int j = 0; j < PRO_THREAD; j++) {
                    unsicher++;     // lesen, addieren, schreiben - drei Schritte
                }
            });
        }
        for (Thread t : alle) t.start();
        for (Thread t : alle) t.join();

        System.out.println();
        System.out.println("  Erwartet:  " + ERWARTET);
        System.out.println("  Bekommen:  " + unsicher);
        System.out.println("  Verloren:  " + (ERWARTET - unsicher)
                + "  (" + String.format(java.util.Locale.ROOT, "%.2f",
                        100.0 * (ERWARTET - unsicher) / ERWARTET) + " %)");
        System.out.println();
        System.out.println("  Kein Fehler, keine Exception - nur ein falsches Ergebnis.");
        System.out.println("  Genau das macht Race Conditions so gefaehrlich.");
        System.out.println("  Starte die Demo mehrmals: die Zahl ist jedes Mal anders.");
        System.out.println();
    }
}
