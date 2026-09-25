/**
 * Kapitel 06, Aufgabe 4.
 */
public class Rechteck extends Figur implements Skalierbar {

    // TODO: private final double breite, hoehe

    /** Oeffentlicher Konstruktor: Name ist "Rechteck". */
    public Rechteck(double breite, double hoehe) {
        // Vorgegeben, damit die Datei kompiliert - verstehe, warum:
        // Statt die Pruefungen doppelt zu schreiben, delegiert dieser
        // Konstruktor mit this(...) an den allgemeineren darunter.
        this("Rechteck", breite, hoehe);
    }

    /**
     * Konstruktor fuer Unterklassen, die einen eigenen Namen tragen wollen.
     * protected: gedacht fuer Unterklassen wie Quadrat. (Im selben Paket ist
     * er allerdings auch fuer alle anderen Klassen sichtbar.)
     */
    protected Rechteck(String name, double breite, double hoehe) {
        super(name);   // vorgegeben, damit die Datei kompiliert
        // TODO: negative Breite oder Hoehe ablehnen, Felder setzen
    }

    public double getBreite() {
        // TODO
        return 0.0;
    }

    public double getHoehe() {
        // TODO
        return 0.0;
    }

    @Override
    public double flaeche() {
        // TODO
        return 0.0;
    }

    @Override
    public double umfang() {
        // TODO
        return 0.0;
    }

    @Override
    public Figur skaliert(double faktor) {
        // TODO
        return new Rechteck(0, 0);
    }
}
