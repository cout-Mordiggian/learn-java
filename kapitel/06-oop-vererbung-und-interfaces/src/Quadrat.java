/**
 * Kapitel 06, Aufgabe 5.
 *
 * "Ein Quadrat IST ein Rechteck" - hier geht die Vererbung gut, weil beide
 * unveraenderlich sind (warum das wichtig ist: siehe Musterloesung).
 */
public class Quadrat extends Rechteck {

    public Quadrat(double seite) {
        // Vorgegeben, damit die Datei kompiliert - verstehe, warum:
        // Rechteck hat keinen parameterlosen Konstruktor. Ueber den
        // protected-Konstruktor reicht Quadrat seinen eigenen Namen durch.
        super("Quadrat", seite, seite);
    }

    public double getSeite() {
        // TODO: Kannst du das aus dem Rechteck ableiten?
        return 0.0;
    }

    /**
     * Kovarianter Rueckgabetyp - hier steht Quadrat statt Figur.
     * Die Signatur ist vorgegeben, damit die Tests kompilieren (dort steht
     * "Quadrat q2 = q.skaliert(2);" ohne Cast) - verstehe, warum das beim
     * Ueberschreiben erlaubt ist.
     */
    @Override
    public Quadrat skaliert(double faktor) {
        // TODO
        return new Quadrat(0);
    }
}
