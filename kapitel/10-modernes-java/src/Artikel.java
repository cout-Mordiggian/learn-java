/**
 * Kapitel 10: ein record mit Validierung.
 *
 * Der Compiler erzeugt automatisch: Felder, Konstruktor,
 * name()/preisCent()/menge(), equals, hashCode, toString.
 */
public record Artikel(String name, long preisCent, int menge) {

    /**
     * Kompakter Konstruktor - beachte: KEINE Parameterliste.
     * Die Zuweisung an die Felder passiert danach automatisch.
     */
    public Artikel {
        // TODO: null-Name, leerer oder nur aus Leerzeichen bestehender Name,
        //       negativer Preis, negative Menge -> IllegalArgumentException
        // TODO: name = name.strip();   (Parameter darf man normalisieren)
    }

    public long gesamtCent() {
        // TODO
        return 0L;
    }

    /** Fabrikmethode fuer die Menge 1. */
    public static Artikel einzeln(String name, long preisCent) {
        // Schon fertig: eine Fabrikmethode ruft einfach den Konstruktor auf.
        return new Artikel(name, preisCent, 1);
    }

    /** Neues Objekt mit anderer Menge - dieses hier bleibt unveraendert. */
    public Artikel mitMenge(int neueMenge) {
        // TODO
        return this;
    }
}
