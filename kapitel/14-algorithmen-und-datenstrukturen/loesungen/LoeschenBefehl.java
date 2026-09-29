/**
 * Kapitel 14 - Musterloesung: Befehl "Text loeschen".
 */
public class LoeschenBefehl implements Befehl {

    private final Textpuffer puffer;
    private final int von;
    private final int laenge;
    private String geloescht;   // erst beim Ausfuehren bekannt, deshalb nicht final

    public LoeschenBefehl(Textpuffer puffer, int von, int laenge) {
        this.puffer = puffer;
        this.von = von;
        this.laenge = laenge;
        // NICHT hier schon den Text merken: Zwischen Erzeugen und Ausfuehren
        // kann sich der Puffer noch aendern. Rueckgaengig muss genau das
        // zurueckbringen, was beim Ausfuehren WIRKLICH geloescht wurde.
    }

    @Override
    public void ausfuehren() {
        geloescht = puffer.loeschen(von, laenge);
    }

    @Override
    public void rueckgaengig() {
        // Anders als beim Einfuegen reichen die Parameter allein nicht: Aus
        // "5 Zeichen ab Position 3" weiss niemand mehr, WELCHE 5 Zeichen.
        // Ein Befehl muss sich also manchmal Zustand von davor merken.
        puffer.einfuegen(von, geloescht);
    }
}
