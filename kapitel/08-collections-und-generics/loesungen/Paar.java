import java.util.Objects;

/**
 * Kapitel 08 - Musterloesung: generische Klasse.
 */
public final class Paar<A, B> {

    private final A erstes;
    private final B zweites;

    public Paar(A erstes, B zweites) {
        this.erstes = erstes;
        this.zweites = zweites;
    }

    /**
     * Die <A, B> vor dem Rueckgabetyp sind EIGENE Typparameter dieser Methode.
     * Sie verdecken zufaellig die gleichnamigen der Klasse - das ist erlaubt,
     * aber sie haben nichts miteinander zu tun. Eine statische Methode gehoert
     * keinem Objekt und kennt daher auch dessen Typargumente nicht.
     *
     * Der Nutzen: Paar.von("a", 1) statt new Paar<String, Integer>("a", 1) -
     * der Compiler leitet die Typen aus den Argumenten ab.
     */
    public static <A, B> Paar<A, B> von(A a, B b) {
        return new Paar<>(a, b);
    }

    public A getErstes()  { return erstes; }
    public B getZweites() { return zweites; }

    /**
     * Beachte den Rueckgabetyp: Paar<B, A>, nicht Paar<A, B>.
     * Der Compiler prueft das - ein vertauschtes new Paar<>(erstes, zweites)
     * waere hier ein Fehler.
     */
    public Paar<B, A> getauscht() {
        return new Paar<>(zweites, erstes);
    }

    @Override
    public String toString() {
        return "(" + erstes + ", " + zweites + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Bei Generics ist "instanceof Paar<?, ?>" das Maximum, was geht:
        // wegen Type Erasure existieren A und B zur Laufzeit nicht mehr.
        if (!(o instanceof Paar<?, ?> p)) return false;
        // Objects.equals statt erstes.equals(...): null-sicher, denn A und B
        // duerfen hier null sein.
        return Objects.equals(erstes, p.erstes) && Objects.equals(zweites, p.zweites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(erstes, zweites);
    }
}
