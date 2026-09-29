/**
 * Kapitel 05 - Musterloesung Konfiguration: ein klassischer Singleton.
 *
 * Die drei Zutaten:
 *   1. private Konstruktor   -> niemand ausserhalb kann "new" sagen
 *   2. private static final  -> die eine Instanz, beim Laden der Klasse erzeugt
 *   3. static instanz()      -> der einzige Weg an das Objekt heran
 */
public final class Konfiguration {

    // Die JVM legt dieses Objekt genau einmal an, wenn die Klasse zum ersten
    // Mal benutzt wird. Das ist sogar bei mehreren Threads sicher - warum die
    // "faule" Variante mit if (instanz == null) das nicht ist, zeigt 12.3.
    private static final Konfiguration INSTANZ = new Konfiguration();

    private String sprache = "de";
    private boolean farbig = false;

    // private: nur diese Klasse selbst darf den Konstruktor aufrufen - und tut
    // es genau einmal, in der Zeile mit INSTANZ. Ganz weglassen geht nicht:
    // Ohne eigenen Konstruktor erzeugt der Compiler einen oeffentlichen.
    // "final" an der Klasse: keine Unterklassen. Die koennten wegen des privaten
    // Konstruktors ohnehin nicht entstehen, so steht die Absicht aber im Code.
    private Konfiguration() { }

    public static Konfiguration instanz() {
        return INSTANZ;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        // Auch ein Singleton hat Invarianten: "sprache ist nie leer".
        if (sprache == null || sprache.isBlank()) {
            throw new IllegalArgumentException("Sprache darf nicht leer sein");
        }
        this.sprache = sprache;
    }

    public boolean isFarbig() {
        return farbig;
    }

    public void setFarbig(boolean farbig) {
        this.farbig = farbig;
    }

    // Der Preis dieses Musters: Jeder Code, der Konfiguration.instanz() aufruft,
    // haengt an diesem einen globalen Objekt, und Tests koennen es nicht
    // zuruecksetzen. Siehe 13.8.
}
