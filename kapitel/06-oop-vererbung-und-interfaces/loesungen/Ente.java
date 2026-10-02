import java.util.Objects;

/**
 * Kapitel 6 - Musterloesung: die Ente als Kontext im Strategie-Muster.
 *
 * Zwei Werkzeuge, jedes fuer seinen Zweck:
 *   extends Ente           -> "ist eine" Ente, mit gemeinsamem Code (schwimmen)
 *   FlugVerhalten-Feld     -> "hat ein" Verhalten, zur Laufzeit austauschbar (6.6)
 */
public abstract class Ente {

    // Gegen das Interface programmiert (6.5): Die Ente kennt keine einzige
    // konkrete Verhaltensklasse. Nicht final, weil die setze-Methoden sie ersetzen.
    private FlugVerhalten flugVerhalten;
    private QuakVerhalten quakVerhalten;

    protected Ente(FlugVerhalten flugVerhalten, QuakVerhalten quakVerhalten) {
        this.flugVerhalten = Objects.requireNonNull(flugVerhalten, "flugVerhalten");
        this.quakVerhalten = Objects.requireNonNull(quakVerhalten, "quakVerhalten");
    }

    public abstract String anzeigen();

    public String schwimmen() {
        return "Alle Enten schwimmen, sogar Lockenten.";
    }

    public String fliegen() {
        // Delegieren: Die Ente fragt ihr Verhalten. Ein "return fliegen();"
        // hier riefe sich selbst auf, bis zum StackOverflowError.
        return flugVerhalten.fliegen();
    }

    public String quaken() {
        return quakVerhalten.quaken();
    }

    public void setzeFlugVerhalten(FlugVerhalten flugVerhalten) {
        this.flugVerhalten = Objects.requireNonNull(flugVerhalten, "flugVerhalten");
    }

    public void setzeQuakVerhalten(QuakVerhalten quakVerhalten) {
        this.quakVerhalten = Objects.requireNonNull(quakVerhalten, "quakVerhalten");
    }
}
