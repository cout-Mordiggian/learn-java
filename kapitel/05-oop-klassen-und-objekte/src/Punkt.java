import java.util.Objects;

/**
 * Kapitel 05, Aufgabe 2: Ein unveraenderlicher Punkt.
 *
 * "final class" verhindert Unterklassen - dazu mehr in Kapitel 6.
 */
public final class Punkt {

    // TODO: private final double x, y

    public Punkt(double x, double y) {
        // TODO
    }

    public double getX() {
        // TODO
        return 0.0;
    }

    public double getY() {
        // TODO
        return 0.0;
    }

    /** Euklidischer Abstand zu einem anderen Punkt. */
    public double abstand(Punkt anderer) {
        // TODO: Math.hypot(dx, dy) oder Math.sqrt(dx*dx + dy*dy)
        return 0.0;
    }

    public double abstandZumUrsprung() {
        // TODO: Kannst du dafuer abstand(...) wiederverwenden?
        return 0.0;
    }

    /** Gibt einen NEUEN, verschobenen Punkt zurueck. Dieser hier bleibt, wie er ist. */
    public Punkt verschoben(double dx, double dy) {
        // TODO
        return new Punkt(0, 0);
    }

    /** Format: Punkt(1.0, 2.0) */
    @Override
    public String toString() {
        // TODO
        return "";
    }

    @Override
    public boolean equals(Object o) {
        // TODO: 1. this == o?  2. instanceof mit Pattern?
        //       3. Felder vergleichen - bei double mit Double.compare, nicht mit ==
        //          (warum? siehe README 5.6).
        return false;
    }

    @Override
    public int hashCode() {
        // TODO: Objects.hash(...) - muss zu equals passen!
        return 0;
    }
}
