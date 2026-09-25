import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Tests {
    public static void main(String[] args) throws Exception {

        Pruef.abschnitt("Zaehler");
        Zaehler z = new Zaehler();
        z.erhoehen();
        z.erhoehen();
        Pruef.gleich(2, z.wert(), "zweimal erhoeht");
        z.erhoeheUm(5);
        Pruef.gleich(7, z.wert(), "um 5 erhoeht");
        z.erhoeheUm(-3);
        Pruef.gleich(4, z.wert(), "negativer Schritt");

        // Die eigentliche Pruefung der Thread-Sicherheit - unabhaengig davon,
        // wie du zaehleMitZaehler baust. 8 Threads warten an einem Startsignal
        // und legen dann gleichzeitig los. Ohne synchronized gehen dabei
        // praktisch immer Erhoehungen verloren.
        Pruef.gleich(800_000, gleichzeitig(8, 100_000, Zaehler::erhoehen),
                "erhoehen() ist thread-sicher (8 Threads x 100.000)");
        Pruef.gleich(1_600_000, gleichzeitig(8, 100_000, zz -> zz.erhoeheUm(2)),
                "erhoeheUm() ist thread-sicher (8 Threads x 100.000 x 2)");

        Pruef.abschnitt("Aufgabe 1: zaehleMitZaehler (synchronized)");
        Pruef.gleich(0, Aufgaben.zaehleMitZaehler(0, 1000), "keine Threads");
        Pruef.gleich(1000, Aufgaben.zaehleMitZaehler(1, 1000), "ein Thread");
        // Der eigentliche Test: mit einem ungeschuetzten int++ schlaegt das
        // fast immer fehl. Zur Sicherheit mehrfach wiederholen - Race
        // Conditions treten nicht bei jedem Lauf auf.
        for (int versuch = 1; versuch <= 3; versuch++) {
            Pruef.gleich(80_000, Aufgaben.zaehleMitZaehler(8, 10_000),
                    "8 Threads x 10.000 (Versuch " + versuch + ")");
        }

        Pruef.abschnitt("Aufgabe 2: zaehleMitAtomic");
        Pruef.gleich(0, Aufgaben.zaehleMitAtomic(0, 1000), "keine Threads");
        for (int versuch = 1; versuch <= 3; versuch++) {
            Pruef.gleich(80_000, Aufgaben.zaehleMitAtomic(8, 10_000),
                    "8 Threads x 10.000 (Versuch " + versuch + ")");
        }

        Pruef.abschnitt("Aufgabe 3: summeParallel");
        Pruef.gleich(5050L, Aufgaben.summeParallel(100, 4), "1..100 auf 4 Threads");
        Pruef.gleich(1L, Aufgaben.summeParallel(1, 4), "1..1 - mehr Threads als Arbeit");
        Pruef.gleich(0L, Aufgaben.summeParallel(0, 4), "leerer Bereich");
        Pruef.gleich(55L, Aufgaben.summeParallel(10, 3), "1..10 auf 3 Threads (geht nicht auf)");
        long n = 1_000_000L;
        Pruef.gleich(n * (n + 1) / 2, Aufgaben.summeParallel(n, 8), "1..1.000.000 auf 8 Threads");

        Pruef.abschnitt("Aufgabe 4: laengenParallel");
        Pruef.gleich(List.of(5, 3, 8),
                Aufgaben.laengenParallel(List.of("Hallo", "abc", "Nebenlae")), "Reihenfolge erhalten");
        Pruef.gleich(List.of(), Aufgaben.laengenParallel(List.of()), "leere Liste");
        Pruef.gleich(List.of(0), Aufgaben.laengenParallel(List.of("")), "leerer String");
        // Bei nur drei Texten kaeme auch eine falsche Loesung (Ergebnisse in
        // Fertigstellungs-Reihenfolge einsammeln) fast immer zufaellig richtig
        // heraus. Bei 2000 Texten auf mehreren Threads nicht mehr.
        List<String> viele = IntStream.range(0, 2000).mapToObj(i -> "x".repeat(i % 97)).toList();
        Pruef.gleich(viele.stream().map(String::length).toList(), Aufgaben.laengenParallel(viele),
                "Reihenfolge erhalten (2000 Texte)");

        Pruef.bericht();
    }

    /**
     * Startet "threads" Threads, die nach einem gemeinsamen Startsignal je
     * "proThread"-mal "aktion" auf DEMSELBEN Zaehler ausfuehren.
     * Liefert den Endstand.
     */
    private static int gleichzeitig(int threads, int proThread, Consumer<Zaehler> aktion)
            throws InterruptedException {
        Zaehler zaehler = new Zaehler();
        CountDownLatch start = new CountDownLatch(1);
        List<Thread> alle = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for (int j = 0; j < proThread; j++) {
                    aktion.accept(zaehler);
                }
            });
            alle.add(t);
            t.start();
        }
        start.countDown();
        for (Thread t : alle) {
            t.join();
        }
        return zaehler.wert();
    }
}
