/**
 * Kapitel 09 - Musterloesung: funktionales Interface mit Komposition.
 */
@FunctionalInterface
public interface Transformation {

    String anwenden(String eingabe);

    /**
     * Gibt eine NEUE Transformation zurueck - ausgefuehrt wird noch nichts.
     * Das Lambda faengt "this" und "naechste" ein und wendet sie erst an,
     * wenn jemand anwenden() aufruft.
     *
     * Dasselbe Muster steckt in Function.andThen und Predicate.and.
     */
    default Transformation dann(Transformation naechste) {
        return eingabe -> naechste.anwenden(this.anwenden(eingabe));
        //     ^ Reihenfolge beachten: erst this, dann naechste.
        //       naechste.anwenden(this.anwenden(x)) - von innen nach aussen lesen.
    }
}
