/**
 * Kapitel 05, Aufgabe 1: Ein gekapseltes Bankkonto.
 *
 * Betraege sind IMMER Cent-Betraege als long - nie double.
 *
 * Pruefen:  ./lerne.sh 05
 */
public class Konto {

    // TODO: statischer Zaehler fuer die fortlaufende Kontonummer
    // private static int anzahlKonten = 0;

    // TODO: Felder - nummer (final int), inhaber (final String), guthaben (long)

    /**
     * Erzeugt ein Konto und vergibt die naechste freie Nummer (beginnend bei 1).
     * Ein abgelehnter Aufruf verbraucht keine Nummer.
     *
     * @throws IllegalArgumentException wenn inhaber null, leer oder nur
     *                                  Leerzeichen ist oder startguthaben negativ ist
     */
    public Konto(String inhaber, long startguthaben) {
        // TODO: erst pruefen, dann Felder setzen, Zaehler hochzaehlen
    }

    public int getNummer() {
        // TODO
        return 0;
    }

    public String getInhaber() {
        // TODO
        return "";
    }

    public long getGuthaben() {
        // TODO
        return 0L;
    }

    /**
     * @throws IllegalArgumentException wenn betrag <= 0
     */
    public void einzahlen(long betrag) {
        // TODO
    }

    /**
     * Das komplette Guthaben abzuheben ist erlaubt.
     *
     * @return true wenn abgehoben wurde, false bei zu wenig Deckung
     * @throws IllegalArgumentException wenn betrag <= 0
     */
    public boolean abheben(long betrag) {
        // TODO
        return false;
    }

    /**
     * Ueberweist an ein anderes Konto. Nur wenn das Abheben klappt,
     * wird beim Ziel eingezahlt.
     *
     * @return true bei Erfolg
     * @throws NullPointerException wenn ziel null ist
     */
    public boolean ueberweiseAn(Konto ziel, long betrag) {
        // TODO
        return false;
    }

    /** Format: Konto[1, Anna, 5000 Cent] */
    @Override
    public String toString() {
        // TODO
        return "";
    }

    /** Wie viele Konten insgesamt erzeugt wurden. */
    public static int getAnzahlKonten() {
        // TODO
        return 0;
    }
}
