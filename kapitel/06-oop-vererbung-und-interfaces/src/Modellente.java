/**
 * Kapitel 6 - Aufgabe 8c: ein Modell, das (noch) nicht fliegt.
 *
 *   Ente e = new Modellente();
 *   e.anzeigen()   // "Ich bin eine Modellente"
 *   e.fliegen()    // "Ich kann nicht fliegen."
 *   e.quaken()     // "Quak"
 */
public class Modellente extends Ente {

    public Modellente() {
        // TODO: die richtigen Verhalten an Ente uebergeben.
        // Vorgegeben ist ein falscher Platzhalter, damit die Datei kompiliert.
        super(new FliegtMitFluegeln(), new Stumm());
    }

    @Override
    public String anzeigen() {
        // TODO
        return "";
    }
}
