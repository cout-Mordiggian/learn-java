/**
 * Kapitel 6 - schon fertig: die Strategie-Schnittstelle fuers Quaken (Aufgabe 8).
 *
 * Genau wie FlugVerhalten: Die Ente delegiert, die Klassen hinter dem
 * Interface entscheiden, welcher Laut herauskommt.
 */
public interface QuakVerhalten {

    /** Der Laut, z. B. "Quak". */
    String quaken();
}
