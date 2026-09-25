import java.util.Objects;

/**
 * Kapitel 08: Ein Werttyp mit natuerlicher Ordnung.
 *
 * Comparable<Person> heisst: "Personen lassen sich von Natur aus vergleichen."
 * Hier: nach Name.
 */
public final class Person implements Comparable<Person> {

    // TODO: private final String name; private final int alter;

    public Person(String name, int alter) {
        // TODO
    }

    public String getName() {
        // TODO
        return "";
    }

    public int getAlter() {
        // TODO
        return 0;
    }

    /**
     * Natuerliche Ordnung: nach Name.
     * Negativ / 0 / positiv - String kann das schon selbst.
     */
    @Override
    public int compareTo(Person andere) {
        // TODO
        return 0;
    }

    /** Format: Anna(34) */
    @Override
    public String toString() {
        // TODO
        return "";
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }
}
