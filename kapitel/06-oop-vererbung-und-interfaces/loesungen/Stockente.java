/**
 * Kapitel 6 - Musterloesung: die ganz normale Ente.
 */
public class Stockente extends Ente {

    public Stockente() {
        super(new FliegtMitFluegeln(), new Quaken());
    }

    @Override
    public String anzeigen() {
        return "Ich bin eine Stockente";
    }
}
