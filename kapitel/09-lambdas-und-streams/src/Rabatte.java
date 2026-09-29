/**
 * Kapitel 09 - Aufgabe 11 (Strategien) und Aufgabe 12 (Fabrik), siehe README 9.8.
 * Pruefen:  ./lerne.sh 09
 *
 * Jede Methode liefert eine Rabatt-Strategie (Interface Rabatt, schon fertig).
 * Am einfachsten als Lambda:  return betrag -> ...;
 * Wie die Kasse (Kasse.java, fertig) die Strategien benutzt, zeigt README 9.8.
 */
public final class Rabatte {

    private Rabatte() { }   // nur statische Methoden, keine Instanzen

    /**
     * Aufgabe 11a: kein Rabatt, der Betrag bleibt.
     * Soll bei JEDEM Aufruf dieselbe Instanz liefern (keiner() == keiner()).
     */
    public static Rabatt keiner() {
        // TODO
        return null;
    }

    /**
     * Aufgabe 11b: prozent Prozent Abzug, der Abzug wird abgerundet.
     *   prozent(10) auf 999 -> Abzug 99 -> 900
     * prozent ausserhalb 0..100 -> IllegalArgumentException (sofort, nicht erst beim Anwenden)
     */
    public static Rabatt prozent(int prozent) {
        // TODO
        return null;
    }

    /**
     * Aufgabe 11c: fester Abzug in Cent, das Ergebnis wird nie negativ.
     *   festbetrag(500) auf 2000 -> 1500,   auf 300 -> 0
     * abzugCent negativ -> IllegalArgumentException
     */
    public static Rabatt festbetrag(long abzugCent) {
        // TODO
        return null;
    }

    /**
     * Aufgabe 11d: rabatt gilt nur, wenn der Betrag mindestens mindestCent ist.
     *   abMindestwert(5000, prozent(10)) auf 4999 -> 4999,  auf 5000 -> 4500
     * rabatt null -> NullPointerException
     */
    public static Rabatt abMindestwert(long mindestCent, Rabatt rabatt) {
        // TODO
        return null;
    }

    /**
     * Aufgabe 12: die Fabrik. Erzeugt aus einem Rabattcode die passende Strategie.
     *
     *   "KEIN"       -> keiner()
     *   "PROZENT15"  -> prozent(15)
     *   "MINUS500"   -> festbetrag(500)
     *
     * Leerzeichen am Rand und Gross-/Kleinschreibung sind egal ("  prozent15 ").
     * Alles andere -> IllegalArgumentException, z. B. "GRATIS", "PROZENT",
     * "PROZENTabc", "PROZENT150", "MINUS-5".  null -> NullPointerException.
     */
    public static Rabatt ausCode(String code) {
        // TODO
        return keiner();
    }
}
