import java.util.Locale;

/**
 * Kapitel 06 - Musterloesung: abstrakte Basisklasse.
 */
public abstract class Figur {

    // private, nicht protected: Unterklassen brauchen den Namen nicht direkt,
    // getName() reicht. Je weniger die Unterklasse sieht, desto lockerer die Kopplung.
    private final String name;

    // protected: gedacht fuer Unterklassen, die ihn ueber super(...) aufrufen.
    // Ein "new Figur(...)" ist ohnehin unmoeglich, weil Figur abstrakt ist.
    protected Figur(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Kein Rumpf, nur ein Semikolon: jede konkrete Unterklasse MUSS liefern.
    public abstract double flaeche();

    public abstract double umfang();

    /**
     * "Template Method": eine fertige Methode in der Basis, die Bausteine
     * aufruft, die erst die Unterklasse liefert. Figur weiss nicht, WIE
     * eine Flaeche berechnet wird - nur, DASS es geht.
     */
    public String beschreibung() {
        // Locale.ROOT erzwingt den Punkt als Dezimaltrennzeichen. Ohne diesen
        // Parameter nimmt Java die Systemeinstellung - auf einem deutschen
        // System kaeme "12,57" heraus und der Test schluege fehl. Das ist ein
        // echter, haeufiger Produktionsfehler bei Datenformaten und IDs.
        return String.format(Locale.ROOT, "%s: Flaeche=%.2f, Umfang=%.2f",
                name, flaeche(), umfang());
    }
}
