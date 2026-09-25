import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kapitel 12 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static int zaehleMitZaehler(int threads, int proThread) throws InterruptedException {
        Zaehler zaehler = new Zaehler();
        List<Thread> alle = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < proThread; j++) {
                    zaehler.erhoehen();
                }
            });
            alle.add(t);
            t.start();
        }

        // Erst ALLE starten, dann ALLE joinen. Wuerde man direkt nach dem
        // start() joinen, liefe jeder Thread bis zum Ende, bevor der
        // naechste beginnt - also strikt nacheinander. Ein haeufiger und
        // gut versteckter Fehler: das Programm ist korrekt, aber nicht parallel.
        for (Thread t : alle) {
            t.join();
        }
        return zaehler.wert();
        // "zaehler" ist im Lambda effektiv final (Kapitel 9): die REFERENZ
        // aendert sich nie. Das Objekt dahinter darf sich sehr wohl aendern -
        // genau deshalb muss es selbst thread-sicher sein.
    }

    public static int zaehleMitAtomic(int threads, int proThread) throws InterruptedException {
        AtomicInteger zaehler = new AtomicInteger();
        List<Thread> alle = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < proThread; j++) {
                    // incrementAndGet ist EINE atomare Operation. Die JVM
                    // uebersetzt sie in einen atomaren Hardware-Befehl
                    // (auf x86 z. B. "lock xadd", sonst oft eine
                    // Compare-and-Swap-Schleife). Keine Sperre, kein Deadlock,
                    // meist weniger Overhead als synchronized. Zaehlen sehr
                    // viele Threads gleichzeitig, ist LongAdder noch schneller.
                    zaehler.incrementAndGet();
                }
            });
            alle.add(t);
            t.start();
        }
        for (Thread t : alle) t.join();
        return zaehler.get();
    }

    public static long summeParallel(long bis, int threads)
            throws InterruptedException, ExecutionException {
        if (bis <= 0) return 0L;

        // ExecutorService ist seit Java 19 AutoCloseable: close() wartet auf
        // alle laufenden Aufgaben und faehrt den Pool herunter. Frueher
        // brauchte es shutdown() + awaitTermination() in einem finally-Block.
        // Ohne Herunterfahren beendet sich das Programm NICHT - die
        // Pool-Threads sind keine Daemon-Threads.
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            List<Future<Long>> ergebnisse = new ArrayList<>();

            // Aufrunden statt abrunden: bei bis=10, threads=3 ergibt das
            // Abschnitte der Laenge 4 (1-4, 5-8, 9-10) - also hoechstens
            // "threads" Teilaufgaben. Mit bis / threads (=3) entstuende ein
            // vierter Rest-Abschnitt (1-3, 4-6, 7-9, 10).
            long proAbschnitt = (bis + threads - 1) / threads;
            for (long von = 1; von <= bis; von += proAbschnitt) {
                long start = von;
                long ende = Math.min(von + proAbschnitt - 1, bis);
                // start und ende sind lokale Kopien - sie sind effektiv final
                // und duerfen deshalb im Lambda benutzt werden. "von" direkt
                // waere ein Compilerfehler, weil die Schleife es veraendert.
                ergebnisse.add(pool.submit(() -> teilsumme(start, ende)));
            }

            long gesamt = 0;
            for (Future<Long> f : ergebnisse) {
                gesamt += f.get();   // blockiert, bis dieses Teilergebnis da ist
            }
            return gesamt;
        }
        // Anmerkung zur Ehrlichkeit: fuer eine Summe lohnt sich das nicht.
        // Thread-Erzeugung und Koordination kosten mehr als die Addition.
        // Parallelisierung zahlt sich erst bei teurer Arbeit pro Element aus -
        // und ob, das misst man, statt es zu vermuten.
    }

    private static long teilsumme(long von, long bis) {
        long summe = 0;
        for (long i = von; i <= bis; i++) summe += i;
        return summe;
    }

    public static List<Integer> laengenParallel(List<String> texte)
            throws InterruptedException, ExecutionException {
        if (texte.isEmpty()) return List.of();

        try (ExecutorService pool = Executors.newFixedThreadPool(
                Math.min(texte.size(), Runtime.getRuntime().availableProcessors()))) {

            List<Callable<Integer>> aufgaben = new ArrayList<>();
            for (String t : texte) {
                aufgaben.add(() -> t.length());   // Callable<Integer>, liefert ein Ergebnis
            }

            // invokeAll startet alle Aufgaben, wartet auf ALLE und liefert die
            // Futures in der REIHENFOLGE DER EINGABELISTE zurueck - unabhaengig
            // davon, welche zuerst fertig war. Genau das brauchen wir hier.
            List<Future<Integer>> futures = pool.invokeAll(aufgaben);

            List<Integer> ergebnis = new ArrayList<>();
            for (Future<Integer> f : futures) {
                ergebnis.add(f.get());
                // f.get() wirft ExecutionException, wenn die Aufgabe eine
                // Exception geworfen hat. Das Original steckt in getCause() -
                // wie das "Caused by" aus Kapitel 7.
            }
            return ergebnis;
        }
    }
}
