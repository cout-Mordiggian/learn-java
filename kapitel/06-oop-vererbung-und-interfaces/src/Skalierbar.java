/**
 * Kapitel 06, Aufgabe 2: Faehigkeit "kann skaliert werden".
 *
 * Ein Interface beschreibt, was etwas KANN - nicht, was es IST.
 */
public interface Skalierbar {

    /** Gibt eine neue, um faktor skalierte Figur zurueck. */
    Figur skaliert(double faktor);

    /**
     * TODO: default-Methode, die skaliert(2.0) aufruft.
     * Sie braucht keine Implementierung in den Klassen - genau das ist der Sinn.
     */
    default Figur verdoppelt() {
        // TODO
        return skaliert(1.0);
    }
}
