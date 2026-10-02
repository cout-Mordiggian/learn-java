/**
 * Kapitel 6 - schon fertig: die Strategie-Schnittstelle fuers Fliegen (Aufgabe 8).
 *
 * Eine Ente weiss nicht, WIE sie fliegt. Sie haelt ein FlugVerhalten und
 * fragt es. Jede Art zu fliegen ist eine eigene Klasse, die dieses Interface
 * implementiert, und laesst sich zur Laufzeit austauschen.
 */
public interface FlugVerhalten {

    /** Beschreibt, wie geflogen wird, z. B. "Ich fliege!". */
    String fliegen();
}
