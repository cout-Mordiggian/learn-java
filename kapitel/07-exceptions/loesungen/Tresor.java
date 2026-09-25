/**
 * Kapitel 07 - Musterloesung: AutoCloseable-Ressource.
 */
public class Tresor implements AutoCloseable {

    private final StringBuilder protokoll;
    private boolean offen;

    public Tresor(StringBuilder protokoll) {
        this.protokoll = protokoll;
        this.offen = true;
        protokoll.append("geoeffnet|");
    }

    public void benutzen() {
        // IllegalStateException heisst: "Das Objekt ist im falschen Zustand
        // fuer diese Operation." Nicht IllegalArgumentException - es liegt
        // ja nicht am Argument, es gibt gar keines.
        if (!offen) {
            throw new IllegalStateException("Tresor ist geschlossen");
        }
        protokoll.append("benutzt|");
    }

    public boolean istOffen() {
        return offen;
    }

    @Override
    public void close() {
        // AutoCloseable.close() darf Exception werfen. Wir deklarieren hier
        // KEIN throws - eine Ueberschreibung darf die geworfenen Exceptions
        // einschraenken. Das erspart jedem Aufrufer einen catch-Block.
        //
        // close() sollte ausserdem idempotent sein: ein zweiter Aufruf darf
        // weder knallen noch noch einmal etwas bewirken. Deshalb:
        if (!offen) {
            return;
        }
        offen = false;
        protokoll.append("geschlossen");
    }
}
