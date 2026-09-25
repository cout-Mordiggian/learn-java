import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kapitel 12 - Nebenlaeufigkeit.
 * Pruefen:  ./lerne.sh 12
 */
public class Aufgaben {

    /**
     * Aufgabe 1: threads Threads, jeder erhoeht proThread-mal einen Zaehler.
     * Ergebnis muss EXAKT threads * proThread sein.
     */
    public static int zaehleMitZaehler(int threads, int proThread) throws InterruptedException {
        Zaehler zaehler = new Zaehler();
        // TODO: Threads anlegen, starten, per join() auf alle warten,
        //       dann zaehler.wert() zurueckgeben.
        //       Achtung: erst ALLE starten, dann ALLE joinen -
        //       sonst laeuft alles nacheinander statt parallel.
        return zaehler.wert();
    }

    /**
     * Aufgabe 2: dasselbe mit AtomicInteger statt synchronized.
     */
    public static int zaehleMitAtomic(int threads, int proThread) throws InterruptedException {
        AtomicInteger zaehler = new AtomicInteger();
        // TODO: incrementAndGet()
        return zaehler.get();
    }

    /**
     * Aufgabe 3: Summe 1..bis, aufgeteilt auf hoechstens threads Teilaufgaben,
     * ueber einen ExecutorService.
     * throws: InterruptedException (Warten unterbrochen) und
     * ExecutionException (eine Teilaufgabe ist gescheitert) - beides von Future.get().
     * summeParallel(100, 4) -> 5050
     */
    public static long summeParallel(long bis, int threads)
            throws InterruptedException, ExecutionException {
        // TODO: try (ExecutorService pool = Executors.newFixedThreadPool(threads)) { ... }
        //       Bereich in threads Abschnitte teilen, je ein Callable<Long>
        //       submitten, dann die Futures einsammeln und aufaddieren.
        return 0L;
    }

    /**
     * Aufgabe 4: Laenge jedes Strings, jeweils in einer eigenen Aufgabe.
     * Die REIHENFOLGE muss der Eingabe entsprechen.
     */
    public static List<Integer> laengenParallel(List<String> texte)
            throws InterruptedException, ExecutionException {
        // TODO: List<Callable<Integer>> bauen, pool.invokeAll(...) aufrufen,
        //       dann die Futures der Reihe nach auslesen.
        return List.of();
    }
}
