/**
 * Kapitel 06 - Musterloesung: Quadrat.
 */
public class Quadrat extends Rechteck {

    public Quadrat(double seite) {
        // Nutzt den protected Konstruktor der Oberklasse und gibt den
        // eigenen Namen mit. flaeche() und umfang() muessen nicht
        // ueberschrieben werden - die Rechteck-Formeln stimmen bereits.
        super("Quadrat", seite, seite);
    }

    public double getSeite() {
        // Kein eigenes Feld noetig: die Seite steckt schon im geerbten Zustand.
        // Ein zweites Feld waere redundant und koennte auseinanderlaufen.
        return getBreite();
    }

    /**
     * Kovarianter Rueckgabetyp: Rechteck.skaliert gibt Figur zurueck,
     * hier verengen wir auf Quadrat. Erlaubt, weil jedes Quadrat eine
     * Figur ist - der Vertrag der Oberklasse bleibt erfuellt.
     *
     * Der Nutzen: Aufrufer, die auf einer Quadrat-Variablen arbeiten,
     * brauchen keinen Cast.
     */
    @Override
    public Quadrat skaliert(double faktor) {
        return new Quadrat(getSeite() * faktor);
    }

    // Warum "Quadrat extends Rechteck" hier gut geht: beide sind
    // unveraenderlich. Haette Rechteck ein setBreite(), koennte man ein
    // Quadrat ueber eine Rechteck-Variable zu einem Nicht-Quadrat machen -
    // der klassische Verstoss gegen das Liskov-Prinzip ("eine Unterklasse
    // muss ueberall funktionieren, wo die Oberklasse erwartet wird").
    //
    // Hinweis zu equals: Wuerde man equals ergaenzen, duerften Quadrat und
    // Rechteck NICHT gegenseitig gleich sein koennen, sonst bricht die
    // Symmetrie des equals-Vertrags (Kapitel 5.6).
}
