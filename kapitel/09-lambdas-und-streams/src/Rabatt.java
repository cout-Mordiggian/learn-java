/**
 * Kapitel 09 - schon fertig: die Schnittstelle fuer das Strategie-Muster.
 *
 * Eine Rabatt-Strategie bekommt einen Betrag in Cent und liefert den Betrag
 * NACH Abzug des Rabatts. Weil das Interface genau eine abstrakte Methode hat,
 * ist es funktional - jede Strategie kann auch ein Lambda sein:
 *
 *   Rabatt halbePreis = betrag -> betrag / 2;
 */
@FunctionalInterface
public interface Rabatt {

    long anwenden(long betragCent);
}
