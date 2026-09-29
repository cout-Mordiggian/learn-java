/**
 * Kapitel 14 - Musterloesung: Befehl "Text einfuegen".
 */
public class EinfuegenBefehl implements Befehl {

    // Alles, was zum Ausfuehren UND zum Rueckgaengigmachen noetig ist, steckt
    // im Befehl selbst. Deshalb kann der Verlauf ihn spaeter blind aufrufen.
    private final Textpuffer puffer;
    private final int pos;
    private final String text;

    public EinfuegenBefehl(Textpuffer puffer, int pos, String text) {
        this.puffer = puffer;
        this.pos = pos;
        this.text = text;
    }

    @Override
    public void ausfuehren() {
        puffer.einfuegen(pos, text);
    }

    @Override
    public void rueckgaengig() {
        // Das Gegenteil von "text bei pos einfuegen" ist "text.length()
        // Zeichen ab pos loeschen". Mehr muss man nicht wissen.
        puffer.loeschen(pos, text.length());
    }
}
