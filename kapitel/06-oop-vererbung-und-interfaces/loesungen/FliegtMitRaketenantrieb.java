/**
 * Kapitel 6 - Musterloesung: ein neues FlugVerhalten.
 *
 * Diese Klasse kam nachtraeglich dazu, und weder Ente noch eine ihrer
 * Unterklassen musste dafuer geaendert werden (Open-Closed-Prinzip, 6.3).
 */
public class FliegtMitRaketenantrieb implements FlugVerhalten {

    @Override
    public String fliegen() {
        return "Ich fliege mit Raketenantrieb!";
    }
}
