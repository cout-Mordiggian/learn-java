/**
 * Kapitel 05, Aufgabe 3: ein Singleton.
 *
 * Im ganzen Programm soll es genau EINE Konfiguration geben. Im Moment kann
 * jeder beliebig viele erzeugen - und instanz() liefert jedes Mal eine neue,
 * frische. Was an einer Stelle eingestellt wird, sieht keine andere.
 *
 *   Konfiguration.instanz().setSprache("en");
 *   Konfiguration.instanz().getSprache()      -> "en"   (dasselbe Objekt!)
 *   new Konfiguration()                       -> Compilerfehler
 */
public class Konfiguration {

    // TODO: ein Feld, das die eine Instanz haelt (README 5.10).

    private String sprache = "de";
    private boolean farbig = false;

    // TODO: Niemand ausserhalb dieser Klasse darf "new Konfiguration()" schreiben.
    public Konfiguration() { }

    public static Konfiguration instanz() {
        // TODO: immer dieselbe Instanz zurueckgeben
        return new Konfiguration();
    }

    public String getSprache() {
        return sprache;
    }

    /** null, leer oder nur Leerzeichen -> IllegalArgumentException. */
    public void setSprache(String sprache) {
        // TODO: pruefen wie beim Inhaber von Konto, dann setzen
    }

    public boolean isFarbig() {
        return farbig;
    }

    public void setFarbig(boolean farbig) {
        // TODO
    }
}
