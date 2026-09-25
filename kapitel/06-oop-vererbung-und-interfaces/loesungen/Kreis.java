/**
 * Kapitel 06 - Musterloesung: Kreis.
 *
 * extends Figur          -> "ist eine" Figur (Herkunft)
 * implements Skalierbar  -> "kann" skaliert werden (Faehigkeit)
 */
public class Kreis extends Figur implements Skalierbar {

    private final double radius;

    public Kreis(double radius) {
        // In Java 21 MUSS super(...) die erste Anweisung sein, deshalb steht
        // die Pruefung hier danach. Seit Java 25 duerfte sie auch davor stehen,
        // solange sie das Objekt (this) noch nicht benutzt - dann wird ein
        // ungueltiger Radius abgelehnt, bevor Figur ueberhaupt initialisiert wird.
        super("Kreis");
        if (radius < 0) {
            throw new IllegalArgumentException("Radius darf nicht negativ sein: " + radius);
        }
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public double flaeche() {
        return Math.PI * radius * radius;
    }

    @Override
    public double umfang() {
        return 2 * Math.PI * radius;
    }

    @Override
    public Figur skaliert(double faktor) {
        // Neues Objekt statt Aenderung - dieselbe Idee wie bei Punkt in Kapitel 5.
        return new Kreis(radius * faktor);
    }
}
