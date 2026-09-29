/**
 * Kapitel 09 - schon fertig: die Schnittstelle fuer das Beobachter-Muster.
 *
 * Wer wissen will, wann ein Artikel knapp wird, implementiert dieses Interface
 * und meldet sich beim Lager an. Funktional - ein Lambda genuegt:
 *
 *   lager.anmelden((artikel, bestand) -> System.out.println(artikel + " knapp: " + bestand));
 */
@FunctionalInterface
public interface LagerBeobachter {

    void knapp(String artikel, int bestand);
}
