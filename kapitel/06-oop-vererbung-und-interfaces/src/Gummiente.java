/**
 * Kapitel 6 - Aufgabe 8c: die Badewannen-Ente.
 *
 *   Ente e = new Gummiente();
 *   e.anzeigen()   // "Ich bin eine Gummiente"
 *   e.fliegen()    // "Ich kann nicht fliegen."
 *   e.quaken()     // "Quietsch"
 */
public class Gummiente extends Ente {

    public Gummiente() {
        // TODO: die richtigen Verhalten an Ente uebergeben.
        // Vorgegeben ist ein falscher Platzhalter, damit die Datei kompiliert.
        super(new FliegtMitFluegeln(), new Quaken());
    }

    @Override
    public String anzeigen() {
        // TODO
        return "";
    }
}
