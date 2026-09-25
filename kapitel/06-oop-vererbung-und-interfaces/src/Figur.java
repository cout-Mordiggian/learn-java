import java.util.Locale;

/**
 * Kapitel 06, Aufgabe 1: Abstrakte Basisklasse fuer geometrische Figuren.
 */
public abstract class Figur {

    // TODO: private final String name

    protected Figur(String name) {
        // TODO
    }

    public String getName() {
        // TODO
        return "";
    }

    // Vorgegeben, damit die Tests kompilieren - verstehe, warum:
    // Kreis und Rechteck schreiben @Override an flaeche() und umfang().
    // Gaebe es die beiden hier nicht, haetten sie nichts zum Ueberschreiben,
    // und der Compiler wuerde abbrechen.
    public abstract double flaeche();

    public abstract double umfang();

    /**
     * Format (Kreis mit Radius 1): "Kreis: Flaeche=3.14, Umfang=6.28"
     * Locale.ROOT ist Pflicht - sonst kaeme auf einem deutschen System 3,14 heraus.
     */
    public String beschreibung() {
        // TODO: String.format(Locale.ROOT, "%s: Flaeche=%.2f, Umfang=%.2f", ...)
        return "";
    }
}
