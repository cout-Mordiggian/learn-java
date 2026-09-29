import java.util.ArrayList;
import java.util.List;

/**
 * Kapitel 10: der Builder (README 10.9).
 * Pruefen:  ./lerne.sh 10
 *
 *   Pizza p = Pizza.builder(Pizza.Groesse.MITTEL)
 *           .belag("Salami")
 *           .belag("Pilze")
 *           .extraKaese()
 *           .build();
 *
 * Regeln:
 *   - Groesse ist Pflicht:  builder(null) -> NullPointerException
 *   - belag(x): x null oder nur Leerzeichen -> IllegalArgumentException,
 *               sonst x ohne Leerzeichen am Rand (strip) anhaengen
 *   - build(): mehr als MAX_BELAEGE Belaege -> IllegalStateException
 *   - Preis: Grundpreis der Groesse + PREIS_BELAG je Belag
 *            + PREIS_EXTRA_KAESE, falls extraKaese()
 *   - Eine fertige Pizza ist unveraenderlich - auch dann, wenn jemand den
 *     Builder danach weiter benutzt.
 */
public final class Pizza {

    /** Schon fertig: die Groessen mit ihrem Grundpreis (enum mit Feld, Kapitel 10.3). */
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

    // Privat: Eine Pizza entsteht nur ueber den Builder.
    private Pizza(Builder b) {
        // TODO: Werte aus dem Builder uebernehmen.
        //       Vorsicht bei der Liste: Was passiert, wenn der Builder danach
        //       noch belag() bekommt? Und darf p.belaege().add(..) klappen?
        this.groesse = null;
        this.belaege = null;
        this.extraKaese = false;
    }

    public static Builder builder(Groesse groesse) {
        return new Builder(groesse);
    }

    public Groesse groesse()       { return groesse; }
    public List<String> belaege()  { return belaege; }
    public boolean hatExtraKaese() { return extraKaese; }

    public long preisCent() {
        // TODO
        return 0;
    }

    /** Schon fertig. */
    @Override
    public String toString() {
        String rest = belaege.isEmpty() ? "ohne Belag" : String.join(", ", belaege);
        return "Pizza " + groesse + ": " + rest + (extraKaese ? " + extra Kaese" : "");
    }

    /**
     * Der Builder: eine statische innere Klasse (wie Knoten in Kapitel 14.6).
     * Er sammelt die Angaben, build() macht daraus die fertige Pizza.
     */
    public static final class Builder {
        private final Groesse groesse;
        private final List<String> belaege = new ArrayList<>();
        private boolean extraKaese;

        private Builder(Groesse groesse) {
            // TODO: null ablehnen (Objects.requireNonNull)
            this.groesse = groesse;
        }

        public Builder belag(String belag) {
            // TODO: pruefen, anhaengen - und was gibst du zurueck, damit man
            //       .belag(..).belag(..) verketten kann?
            return null;
        }

        public Builder extraKaese() {
            // TODO
            return null;
        }

        public Pizza build() {
            // TODO: Regel "hoechstens MAX_BELAEGE" pruefen, dann die Pizza bauen
            return new Pizza(this);
        }
    }
}
