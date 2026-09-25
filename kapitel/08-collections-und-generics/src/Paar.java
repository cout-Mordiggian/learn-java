import java.util.Objects;

/**
 * Kapitel 08: Eine eigene generische Klasse.
 *
 * A und B sind Typparameter - Platzhalter, die der Aufrufer festlegt.
 */
public final class Paar<A, B> {

    // TODO: private final A erstes; private final B zweites;

    public Paar(A erstes, B zweites) {
        // TODO
    }

    /**
     * Statische Fabrikmethode.
     *
     * Achtung: Die Typparameter der KLASSE gelten nicht fuer statische
     * Methoden - eine statische Methode gehoert ja keinem Objekt.
     * Deshalb braucht sie eigene: <A, B> vor dem Rueckgabetyp.
     */
    public static <A, B> Paar<A, B> von(A a, B b) {
        // Schon fertig - hier geht es um die Signatur, nicht um den Rumpf.
        return new Paar<>(a, b);
    }

    public A getErstes() {
        // TODO
        return null;
    }

    public B getZweites() {
        // TODO
        return null;
    }

    /** Vertauscht die beiden Positionen - beachte den Rueckgabetyp! */
    public Paar<B, A> getauscht() {
        // TODO
        return new Paar<>(null, null);
    }

    /** Format: (a, b) */
    @Override
    public String toString() {
        // TODO
        return "";
    }

    @Override
    public boolean equals(Object o) {
        // TODO: Objects.equals(...) ist hier praktisch, weil A und B null sein duerfen.
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }
}
