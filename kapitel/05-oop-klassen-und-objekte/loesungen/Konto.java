import java.util.Objects;

/**
 * Kapitel 05 - Musterloesung Konto, mit Erklaerungen.
 */
public class Konto {

    // static = EIN Wert fuer die gesamte Klasse, nicht pro Objekt.
    // Er ueberlebt jedes einzelne Konto und zaehlt alle je erzeugten mit.
    private static int anzahlKonten = 0;

    // final: nach dem Konstruktor unveraenderlich. Eine Kontonummer, die sich
    // aendern kann, waere ein Widerspruch in sich - der Compiler haelt das fest.
    private final int nummer;
    private final String inhaber;

    // Nicht final: das Guthaben ist der einzige veraenderliche Zustand.
    // Genau deshalb ist es private und nur ueber Fachmethoden erreichbar.
    private long guthaben;

    public Konto(String inhaber, long startguthaben) {
        // Erst pruefen, dann zuweisen. Wirft der Konstruktor, existiert
        // gar kein halbfertiges Objekt - die Invariante bleibt heil.
        if (inhaber == null || inhaber.isBlank()) {
            throw new IllegalArgumentException("Inhaber darf nicht leer sein");
        }
        if (startguthaben < 0) {
            throw new IllegalArgumentException("Startguthaben darf nicht negativ sein: " + startguthaben);
        }
        // Erst NACH allen Pruefungen hochzaehlen - sonst wuerde ein
        // abgelehnter Konstruktoraufruf trotzdem eine Nummer verbrauchen.
        anzahlKonten++;
        this.nummer = anzahlKonten;
        this.inhaber = inhaber;      // this.inhaber = Feld, inhaber = Parameter
        this.guthaben = startguthaben;
    }

    public int getNummer()      { return nummer; }
    public String getInhaber()  { return inhaber; }
    public long getGuthaben()   { return guthaben; }
    // Bewusst KEIN setGuthaben: der Zustand aendert sich nur ueber
    // einzahlen/abheben - und die pruefen die Regeln.

    public void einzahlen(long betrag) {
        if (betrag <= 0) {
            throw new IllegalArgumentException("Einzahlung muss positiv sein: " + betrag);
        }
        guthaben += betrag;
    }

    public boolean abheben(long betrag) {
        // Zwei verschiedene Fehlerarten, zwei verschiedene Antworten:
        // - betrag <= 0 ist ein PROGRAMMIERFEHLER -> Exception
        // - zu wenig Deckung ist ein GESCHAEFTSFALL -> boolean
        // Diese Unterscheidung ist der Kern von Kapitel 7.
        if (betrag <= 0) {
            throw new IllegalArgumentException("Abhebung muss positiv sein: " + betrag);
        }
        if (betrag > guthaben) {
            return false;
        }
        guthaben -= betrag;
        return true;
    }

    public boolean ueberweiseAn(Konto ziel, long betrag) {
        // null ist hier ein Programmierfehler des Aufrufers. Die uebliche
        // Java-Antwort darauf ist eine NullPointerException mit klarer Meldung -
        // genau das liefert Objects.requireNonNull (mehr dazu in Kapitel 7).
        Objects.requireNonNull(ziel, "ziel");
        // Wiederverwendung statt Kopieren: abheben() prueft Betrag und Deckung
        // bereits. Nur wenn es geklappt hat, wird gutgeschrieben - sonst
        // waere Geld aus dem Nichts entstanden.
        if (!abheben(betrag)) {
            return false;
        }
        ziel.einzahlen(betrag);
        return true;
        // Anmerkung: Echte Ueberweisungen brauchen eine Transaktion. Zwischen
        // abheben und einzahlen darf niemand dazwischenfunken (Kapitel 12).
    }

    @Override
    public String toString() {
        // toString ist fuer Menschen: Logs, Debugger, Fehlermeldungen.
        // Ohne diese Methode saehe man nur "Konto@1b6d3586".
        return "Konto[" + nummer + ", " + inhaber + ", " + guthaben + " Cent]";
    }

    public static int getAnzahlKonten() {
        // static, weil die Antwort kein bestimmtes Konto braucht.
        // Zugriff auf "nummer" oder "guthaben" waere hier ein Compilerfehler.
        return anzahlKonten;
    }
}
