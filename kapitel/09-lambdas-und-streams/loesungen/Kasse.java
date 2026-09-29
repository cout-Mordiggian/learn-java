import java.util.Objects;

/**
 * Kapitel 09 - schon fertig: der "Kontext" im Strategie-Muster.
 *
 * Die Kasse weiss nicht, WIE ein Rabatt gerechnet wird. Sie kennt nur das
 * Interface Rabatt und ruft anwenden() auf. Neue Rabattarten brauchen deshalb
 * keine einzige Aenderung an dieser Klasse.
 */
public class Kasse {

    private long summeCent;
    private Rabatt rabatt = Rabatte.keiner();

    public void scanne(long preisCent) {
        summeCent += preisCent;
    }

    /** Die Strategie laesst sich jederzeit austauschen, auch mitten im Einkauf. */
    public void setzeRabatt(Rabatt rabatt) {
        this.rabatt = Objects.requireNonNull(rabatt, "rabatt");
    }

    public long zuZahlenCent() {
        return rabatt.anwenden(summeCent);
    }
}
