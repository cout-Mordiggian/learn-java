import java.util.Objects;

/**
 * Kapitel 09: Diese Klasse ist bereits fertig - du brauchst sie nur zu benutzen.
 * (In Kapitel 10 siehst du, wie aus so etwas ein einzeiliges record wird.)
 */
public final class Person {

    private final String name;
    private final int alter;
    private final String stadt;

    public Person(String name, int alter, String stadt) {
        this.name = name;
        this.alter = alter;
        this.stadt = stadt;
    }

    public String getName()  { return name; }
    public int getAlter()    { return alter; }
    public String getStadt() { return stadt; }

    @Override
    public String toString() { return name + "(" + alter + ", " + stadt + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person p)) return false;
        return alter == p.alter && name.equals(p.name) && stadt.equals(p.stadt);
    }

    @Override
    public int hashCode() { return Objects.hash(name, alter, stadt); }
}
