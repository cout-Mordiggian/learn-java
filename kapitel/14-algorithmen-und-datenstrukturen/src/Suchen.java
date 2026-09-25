/**
 * Kapitel 14 - Algorithmen und Datenstrukturen: Suchen.
 * Pruefen:  ./lerne.sh 14
 */
public class Suchen {

    /**
     * Aufgabe 4: Binaere Suche nach einer GRENZE.
     * Liefert den Index des ersten Elements, das >= wert ist.
     * Gibt es keins, ist das Ergebnis sortiert.length.
     *
     *   sortiert = {1, 3, 3, 3, 5}
     *   ersteGroesserGleich(sortiert, 3) -> 1    (erste 3, nicht irgendeine)
     *   ersteGroesserGleich(sortiert, 4) -> 4    (die 5)
     *   ersteGroesserGleich(sortiert, 0) -> 0
     *   ersteGroesserGleich(sortiert, 9) -> 5    (== length: nichts ist gross genug)
     *
     * Muss O(log n) sein - also KEINE Schleife ueber alle Elemente.
     */
    public static int ersteGroesserGleich(int[] sortiert, int wert) {
        // TODO: von = 0, bis = sortiert.length  (halboffen: bis gehoert nicht dazu).
        //       Solange von < bis: mitte berechnen (ueberlaufsicher wie in Kapitel 4)
        //       und je nach sortiert[mitte] < wert entweder von oder bis verschieben.
        //       Am Ende ist von == bis - und das ist die Antwort.
        return -1;
    }

    /**
     * Aufgabe 5: Wie oft kommt wert im sortierten Array vor? In O(log n).
     *   zaehleVorkommen({1, 3, 3, 3, 5}, 3) -> 3
     *   zaehleVorkommen({1, 3, 3, 3, 5}, 4) -> 0
     *
     * Denkfalle: Integer.MAX_VALUE + 1 ist keine groessere Zahl.
     */
    public static int zaehleVorkommen(int[] sortiert, int wert) {
        // TODO: Zweimal ersteGroesserGleich aufrufen. Wo beginnen die Kopien von
        //       wert, und wo beginnt das erste Element, das GROESSER ist?
        return -1;
    }
}
