import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Kapitel 10 - Musterloesung: Builder (README 10.9).
 */
public final class Pizza {

    public enum Groesse {
        KLEIN(800), MITTEL(1000), GROSS(1200);

        private final long grundpreisCent;

        Groesse(long grundpreisCent) {
            this.grundpreisCent = grundpreisCent;
        }

        public long grundpreisCent() {
            return grundpreisCent;
        }
    }

    public static final int MAX_BELAEGE = 5;
    public static final long PREIS_BELAG = 150;
    public static final long PREIS_EXTRA_KAESE = 200;

    private final Groesse groesse;
    private final List<String> belaege;
    private final boolean extraKaese;

    // Privat: Eine Pizza entsteht NUR ueber den Builder. So kann es keine
    // halb fertige oder ungueltige Pizza geben.
    private Pizza(Builder b) {
        this.groesse = b.groesse;
        // Kopie! Ohne sie teilten sich Builder und Pizza dieselbe Liste: Ein
        // weiteres belag() am Builder wuerde die fertige Pizza nachtraeglich
        // aendern. List.copyOf ist zugleich unveraenderlich.
        this.belaege = List.copyOf(b.belaege);
        this.extraKaese = b.extraKaese;
    }

    /** Statische Fabrikmethode, die den Builder liefert - Pflichtangaben als Parameter. */
    public static Builder builder(Groesse groesse) {
        return new Builder(groesse);
    }

    public Groesse groesse()       { return groesse; }
    public List<String> belaege()  { return belaege; }
    public boolean hatExtraKaese() { return extraKaese; }

    public long preisCent() {
        return groesse.grundpreisCent()
                + PREIS_BELAG * belaege.size()
                + (extraKaese ? PREIS_EXTRA_KAESE : 0);
    }

    @Override
    public String toString() {
        String rest = belaege.isEmpty() ? "ohne Belag" : String.join(", ", belaege);
        return "Pizza " + groesse + ": " + rest + (extraKaese ? " + extra Kaese" : "");
    }

    /**
     * Static, weil ein Builder vor der Pizza existiert und kein Pizza-Objekt
     * braucht (vgl. Knoten in Kapitel 14.6). Als innere Klasse darf er auf
     * den privaten Konstruktor von Pizza zugreifen - und umgekehrt Pizza auf
     * seine privaten Felder.
     */
    public static final class Builder {
        private final Groesse groesse;
        private final List<String> belaege = new ArrayList<>();
        private boolean extraKaese;

        private Builder(Groesse groesse) {
            // Pflichtangaben sofort pruefen: Der Fehler zeigt dann auf die
            // Zeile, in der er gemacht wurde, nicht erst auf build().
            this.groesse = Objects.requireNonNull(groesse, "groesse");
        }

        public Builder belag(String belag) {
            if (belag == null || belag.isBlank()) {
                throw new IllegalArgumentException("Belag darf nicht leer sein");
            }
            belaege.add(belag.strip());
            return this;            // "fluent": erlaubt .belag(..).belag(..).build()
        }

        public Builder extraKaese() {
            extraKaese = true;
            return this;
        }

        public Pizza build() {
            // Regeln, die das GANZE Objekt betreffen, prueft build(). Hier
            // koennte man auch schon in belag() pruefen - build() ist aber
            // die Stelle, an der man alle Regeln an einem Ort findet.
            if (belaege.size() > MAX_BELAEGE) {
                throw new IllegalStateException("hoechstens " + MAX_BELAEGE + " Belaege, gewuenscht: "
                        + belaege.size());
            }
            return new Pizza(this);
        }
    }
}
