/**
 * Kapitel 14 - Aufgabe 10, schon fertig: der Text, den die Befehle bearbeiten.
 *
 * Im Befehl-Muster heisst das der "Empfaenger": Er kann die eigentliche
 * Arbeit, weiss aber nichts von Befehlen oder Rueckgaengig.
 */
public class Textpuffer {

    private final StringBuilder text = new StringBuilder();

    /** Fuegt text an Position pos ein. 0 = ganz vorn, laenge() = ganz hinten. */
    public void einfuegen(int pos, String text) {
        this.text.insert(pos, text);
    }

    /** Loescht laenge Zeichen ab Position von und gibt sie zurueck. */
    public String loeschen(int von, int laenge) {
        String weg = text.substring(von, von + laenge);
        text.delete(von, von + laenge);
        return weg;
    }

    public String text() {
        return text.toString();
    }

    public int laenge() {
        return text.length();
    }
}
