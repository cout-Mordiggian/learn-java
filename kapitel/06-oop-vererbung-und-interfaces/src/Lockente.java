/**
 * Kapitel 6 - Aufgabe 8c: eine Holzente fuer Jaeger, sie fliegt nicht und ist stumm.
 *
 *   Ente e = new Lockente();
 *   e.anzeigen()   // "Ich bin eine Lockente"
 *   e.fliegen()    // "Ich kann nicht fliegen."
 *   e.quaken()     // "<< Stille >>"
 */
public class Lockente extends Ente {

    public Lockente() {
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
