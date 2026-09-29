/**
 * Kapitel 6 - schon fertig: die gemeinsame Schnittstelle fuer das
 * Dekorierer-Muster (Abschnitt 6.9).
 *
 * Das echte Protokoll (TextProtokoll) und alle Dekorierer implementieren
 * dasselbe Interface. Deshalb kann man sie beliebig ineinander stecken:
 *
 *   Protokoll p = new MitZeilennummer(new Grossgeschrieben(new TextProtokoll()));
 */
public interface Protokoll {

    void schreibe(String zeile);
}
