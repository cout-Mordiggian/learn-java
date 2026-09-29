import java.util.Objects;

/**
 * Kapitel 6 - Musterloesung: Dekorierer, der jede Zeile nummeriert.
 */
public class MitZeilennummer implements Protokoll {

    private final Protokoll innen;   // das eingepackte Protokoll - "hat ein", nicht "ist ein"
    private int nummer;              // jeder Dekorierer zaehlt fuer sich

    public MitZeilennummer(Protokoll innen) {
        // Frueh scheitern (5.7): Ein null hier fiele sonst erst beim ersten
        // schreibe() auf, weit weg von der Stelle, an der es passiert ist.
        this.innen = Objects.requireNonNull(innen, "innen");
    }

    @Override
    public void schreibe(String zeile) {
        nummer++;
        innen.schreibe(nummer + ": " + zeile);   // veraendern, dann weiterreichen
    }
}
