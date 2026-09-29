import java.util.Objects;

/**
 * Kapitel 05, Aufgabe 2: Ein unveraenderlicher Punkt.
 *
 * "final class" verhindert Unterklassen - dazu mehr in Kapitel 6.
 */
public final class Punkt {

    // TODO: private final double x, y

    // TODO (Fabrikmethode ursprung): eine Konstante fuer den Punkt (0, 0)

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

    /**
     * Statische Fabrikmethode: der Punkt (0, 0) - bei jedem Aufruf DASSELBE Objekt.
     * (README 5.10: eine Fabrikmethode muss kein neues Objekt liefern.)
     */
    public static Punkt ursprung() {
        // TODO: die Konstante zurueckgeben statt jedes Mal ein neues Objekt
        return new Punkt(0, 0);
    }

    /**
     * Statische Fabrikmethode: Punkt aus Abstand zum Ursprung und Winkel in Grad.
     *   polar(2, 90) -> Punkt(0, 2)   (bis auf winzige Rundungsfehler)
     * x = radius * cos(w), y = radius * sin(w), w im Bogenmass: Math.toRadians(winkelGrad)
     */
    public static Punkt polar(double radius, double winkelGrad) {
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
