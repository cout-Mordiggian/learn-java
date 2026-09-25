/**
 * Kapitel 09, Aufgabe 9: ein eigenes funktionales Interface.
 *
 * @FunctionalInterface ist optional - aber der Compiler prueft dann,
 * dass wirklich nur EINE abstrakte Methode da ist. Nimm die Annotation
 * immer, wenn das deine Absicht ist.
 */
@FunctionalInterface
public interface Transformation {

    String anwenden(String eingabe);

    /**
     * Komposition: erst diese Transformation, dann die naechste.
     * naechste.anwenden(this.anwenden(x))
     *
     * Gib ein neues Transformation-Lambda zurueck.
     */
    default Transformation dann(Transformation naechste) {
        // TODO: return eingabe -> ...
        return this;
    }
}
