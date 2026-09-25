import java.util.Objects;

/**
 * Kapitel 08 - Musterloesung: Werttyp mit natuerlicher Ordnung.
 */
public final class Person implements Comparable<Person> {

    private final String name;
    private final int alter;

    public Person(String name, int alter) {
        this.name = Objects.requireNonNull(name, "name");
        this.alter = alter;
    }

    public String getName() { return name; }
    public int getAlter()   { return alter; }

    @Override
    public int compareTo(Person andere) {
        // String implementiert Comparable selbst - wir reichen einfach durch.
        // Wichtig: NICHT this.alter - andere.alter fuer numerische Vergleiche
        // verwenden (Ueberlaufgefahr), sondern Integer.compare(a, b).
        return this.name.compareTo(andere.name);
    }

    @Override
    public String toString() {
        return name + "(" + alter + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person p)) return false;
        return alter == p.alter && name.equals(p.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, alter);
    }

    // Hinweis: compareTo (nur Name) und equals (Name + Alter) sind hier
    // INKONSISTENT. Fuer ein TreeSet waere das ein Problem - dort gaelten
    // zwei Personen mit gleichem Namen als dasselbe Element. Fuer HashSet
    // und Listen ist es unkritisch. In echtem Code sollte man das entweder
    // angleichen oder die Inkonsistenz bewusst dokumentieren.
}
