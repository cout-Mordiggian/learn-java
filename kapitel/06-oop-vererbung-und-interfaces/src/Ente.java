/**
 * Kapitel 6 - Aufgabe 8a: die Ente als Kontext im Strategie-Muster (6.9).
 * Pruefen:  ./lerne.sh 06
 *
 * Was alle Enten gleich machen (schwimmen), steht hier fertig. Was sich
 * unterscheidet, steckt in zwei austauschbaren Objekten:
 *
 *   Ente e = new Stockente();
 *   e.fliegen()                                   // "Ich fliege!"
 *   e.setzeFlugVerhalten(new FliegtNicht());
 *   e.fliegen()                                   // "Ich kann nicht fliegen."
 *
 * null fuer ein Verhalten -> NullPointerException, im Konstruktor UND in
 * den beiden setze-Methoden (5.7).
 */
public abstract class Ente {

    // TODO: zwei private Felder, eins vom Typ FlugVerhalten, eins vom Typ QuakVerhalten

    protected Ente(FlugVerhalten flugVerhalten, QuakVerhalten quakVerhalten) {
        // TODO: null ablehnen, Felder setzen
    }

    // Vorgegeben, damit die Unterklassen kompilieren: Jede Ente schreibt
    // @Override an anzeigen(), also muss es die Methode hier geben.
    /** Z. B. "Ich bin eine Stockente". */
    public abstract String anzeigen();

    /** Fertig: Schwimmen koennen alle Enten gleich, das bleibt in der Oberklasse. */
    public String schwimmen() {
        return "Alle Enten schwimmen, sogar Lockenten.";
    }

    public String fliegen() {
        // TODO: an das FlugVerhalten weiterreichen
        return "";
    }

    public String quaken() {
        // TODO: an das QuakVerhalten weiterreichen
        return "";
    }

    public void setzeFlugVerhalten(FlugVerhalten flugVerhalten) {
        // TODO: null ablehnen, Feld ersetzen
    }

    public void setzeQuakVerhalten(QuakVerhalten quakVerhalten) {
        // TODO: null ablehnen, Feld ersetzen
    }
}
