/**
 * Kapitel 06, Aufgabe 3.
 */
public class Kreis extends Figur implements Skalierbar {

    // TODO: private final double radius

    public Kreis(double radius) {
        // Vorgegeben, damit die Datei kompiliert - verstehe, warum:
        // Figur hat keinen parameterlosen Konstruktor, also MUSS hier
        // ein super(...) mit Namen stehen.
        super("Kreis");
        // TODO: negativen Radius mit IllegalArgumentException ablehnen,
        //       dann das Feld setzen.
    }

    public double getRadius() {
        // TODO
        return 0.0;
    }

    @Override
    public double flaeche() {
        // TODO: Math.PI * r * r
        return 0.0;
    }

    @Override
    public double umfang() {
        // TODO: 2 * Math.PI * r
        return 0.0;
    }

    @Override
    public Figur skaliert(double faktor) {
        // TODO: neuen Kreis mit skaliertem Radius zurueckgeben
        return new Kreis(0);
    }
}
