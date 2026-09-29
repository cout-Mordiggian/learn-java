/**
 * Kapitel 6 - schon fertig: das "echte" Protokoll ganz innen.
 *
 * Es sammelt alle Zeilen, jede mit einem Zeilenumbruch dahinter. In einem
 * richtigen Programm stuende hier eine Datei oder die Konsole - fuer die
 * Tests ist ein String praktischer.
 */
public class TextProtokoll implements Protokoll {

    private final StringBuilder text = new StringBuilder();

    @Override
    public void schreibe(String zeile) {
        text.append(zeile).append('\n');
    }

    /** Alles bisher Geschriebene, z. B. "1: a\n2: b\n". */
    public String inhalt() {
        return text.toString();
    }
}
