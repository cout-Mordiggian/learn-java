/**
 * Kapitel 06 - Musterloesung: Interface.
 */
public interface Skalierbar {

    // Implizit public abstract - die Schluesselwoerter darf man weglassen
    // und laesst sie per Konvention auch weg.
    Figur skaliert(double faktor);

    /**
     * default-Methode: hat einen Rumpf, den jede implementierende Klasse
     * geschenkt bekommt. Eingefuehrt in Java 8, um bestehende Interfaces
     * erweitern zu koennen, ohne alle Implementierer zu brechen -
     * so kam z.B. forEach() nachtraeglich in Iterable.
     *
     * Sie kann andere Methoden des Interfaces aufrufen, aber keinen eigenen
     * Zustand haben - ein Interface hat keine Instanzfelder (nur Konstanten).
     */
    default Figur verdoppelt() {
        return skaliert(2.0);
    }
}
