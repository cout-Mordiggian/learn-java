/**
 * Kapitel 06 - Musterloesung: Rechteck.
 */
public class Rechteck extends Figur implements Skalierbar {

    private final double breite;
    private final double hoehe;

    /**
     * Der oeffentliche Konstruktor delegiert an den allgemeineren.
     * this(...) und super(...) schliessen sich aus: Ein Konstruktor darf
     * hoechstens EINEN anderen Konstruktor explizit aufrufen. Das super(...)
     * passiert dann im Konstruktor, an den this(...) delegiert.
     */
    public Rechteck(double breite, double hoehe) {
        this("Rechteck", breite, hoehe);
    }

    /**
     * protected, damit Quadrat einen eigenen Namen durchreichen kann.
     * Vorsicht: protected heisst "Unterklassen UND dasselbe Paket". Alle
     * Kursklassen liegen im selben (unbenannten) Paket - hier koennte also
     * jede Klasse beliebige Namen setzen. Echten Schutz gibt es erst mit
     * eigenen Paketen.
     */
    protected Rechteck(String name, double breite, double hoehe) {
        super(name);
        if (breite < 0 || hoehe < 0) {
            throw new IllegalArgumentException("Seiten duerfen nicht negativ sein");
        }
        this.breite = breite;
        this.hoehe = hoehe;
    }

    public double getBreite() { return breite; }
    public double getHoehe()  { return hoehe; }

    @Override
    public double flaeche() { return breite * hoehe; }

    @Override
    public double umfang()  { return 2 * (breite + hoehe); }

    @Override
    public Figur skaliert(double faktor) {
        return new Rechteck(breite * faktor, hoehe * faktor);
    }
}
