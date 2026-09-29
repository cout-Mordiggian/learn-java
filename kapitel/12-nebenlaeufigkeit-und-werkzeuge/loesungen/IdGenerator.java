import java.util.concurrent.atomic.AtomicLong;

/**
 * Kapitel 12 - Musterloesung: ein Singleton fuer viele Threads.
 */
public enum IdGenerator {

    // Genau eine Konstante = genau eine Instanz. Die JVM garantiert, dass es
    // keine zweite geben kann: kein Konstruktoraufruf von aussen, kein Trick
    // mit Serialisierung oder Reflection. Joshua Bloch nennt das in
    // "Effective Java" den besten Weg, ein Singleton zu bauen.
    INSTANZ;

    // AtomicLong statt long: "zaehler++" ist Lesen, Rechnen, Schreiben -
    // drei Schritte, zwischen denen ein anderer Thread dazwischenkommen kann
    // (12.2). incrementAndGet erledigt alles in einem unteilbaren
    // Schritt. Ein Singleton wird fast immer von vielen Stellen benutzt, also
    // oft auch von vielen Threads.
    private final AtomicLong zaehler = new AtomicLong();

    public long naechsteId() {
        return zaehler.incrementAndGet();   // erhoehen, dann den neuen Wert liefern: 1, 2, 3, ...
    }
}
