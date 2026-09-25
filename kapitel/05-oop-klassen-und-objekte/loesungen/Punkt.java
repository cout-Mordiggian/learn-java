import java.util.Objects;

/**
 * Kapitel 05 - Musterloesung Punkt, mit Erklaerungen.
 *
 * Ein unveraenderlicher Werttyp. Die Zutaten:
 *   1. final class      -> niemand kann durch Vererbung Veraenderlichkeit einschleusen
 *   2. private final    -> Felder liegen nach dem Konstruktor fest
 *   3. keine Setter     -> "aendernde" Methoden geben neue Objekte zurueck
 */
public final class Punkt {

    private final double x;
    private final double y;

    public Punkt(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double abstand(Punkt anderer) {
        // Math.hypot(dx, dy) statt Math.sqrt(dx*dx + dy*dy):
        // rechnet dasselbe, vermeidet aber Ueber-/Unterlauf bei extremen Werten.
        return Math.hypot(anderer.x - this.x, anderer.y - this.y);
        // Beachte: wir greifen auf anderer.x zu, obwohl x private ist.
        // Das geht, weil "private" pro KLASSE gilt, nicht pro Objekt.
    }

    public double abstandZumUrsprung() {
        // Wiederverwendung statt zweiter Formel: eine Stelle, die stimmen muss.
        return abstand(new Punkt(0, 0));
    }

    public Punkt verschoben(double dx, double dy) {
        // Kein "this.x += dx" - das waere bei final gar nicht erlaubt und
        // wuerde die Unveraenderlichkeit brechen. Stattdessen: neues Objekt.
        return new Punkt(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return "Punkt(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        // Der Parameter MUSS Object sein. Mit "equals(Punkt p)" haettest du
        // ueberladen statt ueberschrieben - Collections wuerden weiter die
        // Referenzgleichheit von Object.equals verwenden. @Override verhindert das.
        if (this == o) return true;                 // Abkuerzung: identisches Objekt
        if (!(o instanceof Punkt p)) return false;  // deckt auch o == null ab!
        // Double.compare statt ==, weil == bei NaN und -0.0 ueberrascht:
        // - Double.NaN == Double.NaN ist false: zwei NaN-Punkte waeren nie gleich.
        // - 0.0 == -0.0 ist true, aber Objects.hash (ueber Double.hashCode)
        //   liefert fuer beide verschiedene Werte -> hashCode-Vertrag gebrochen.
        // Double.compare behandelt beide Faelle genau wie Double.hashCode,
        // deshalb passen equals und hashCode hier zusammen.
        return Double.compare(x, p.x) == 0 && Double.compare(y, p.y) == 0;
    }

    @Override
    public int hashCode() {
        // Der Vertrag: gleiche Objekte -> gleicher hashCode.
        // Deshalb genau die Felder verwenden, die auch equals vergleicht.
        // Objects.hash boxt die Werte und baut daraus einen kombinierten Hash.
        return Objects.hash(x, y);
    }
}
