/**
 * Kapitel 6 - Musterloesung: nicht fliegen koennen.
 */
public class FliegtNicht implements FlugVerhalten {

    @Override
    public String fliegen() {
        return "Ich kann nicht fliegen.";
    }
}
