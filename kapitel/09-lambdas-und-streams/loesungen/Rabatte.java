import java.util.Locale;
import java.util.Objects;

/**
 * Kapitel 09 - Musterloesung: Strategien (Aufgabe 11) und eine Fabrik (Aufgabe 12).
 */
public final class Rabatte {

    private Rabatte() { }   // Utility-Klasse wie Pruef: nur statische Methoden

    // Diese Strategie hat keinen Zustand, eine einzige Instanz reicht fuer
    // alle. Genau das darf eine Fabrikmethode - ein Konstruktor nicht: Er
    // muss bei jedem "new" ein neues Objekt liefern.
    // Nebenbei: Auch ein schlichtes "return betrag -> betrag;" liefert auf der
    // ueblichen JVM (HotSpot) jedes Mal dasselbe Objekt, weil ein Lambda ohne
    // eingefangene Variablen dort wiederverwendet wird. Die Sprache garantiert
    // das aber nicht. Erst die Konstante macht es zu einer Zusage.
    private static final Rabatt KEINER = betrag -> betrag;

    public static Rabatt keiner() {
        return KEINER;
    }

    public static Rabatt prozent(int prozent) {
        // Pruefen, BEVOR die Strategie entsteht: Ein ungueltiger Rabatt soll
        // gar nicht erst existieren und nicht erst an der Kasse auffallen.
        if (prozent < 0 || prozent > 100) {
            throw new IllegalArgumentException("Prozent muss zwischen 0 und 100 liegen: " + prozent);
        }
        // Das Lambda "faengt" prozent ein (effektiv final, 9.4). Jede
        // Strategie merkt sich so ihren eigenen Satz.
        // Ganzzahlig: Der Abzug wird abgerundet, 10 % von 999 sind 99 Cent.
        return betrag -> betrag - betrag * prozent / 100;
    }

    public static Rabatt festbetrag(long abzugCent) {
        if (abzugCent < 0) {
            throw new IllegalArgumentException("Abzug darf nicht negativ sein: " + abzugCent);
        }
        return betrag -> Math.max(0, betrag - abzugCent);   // nie unter 0
    }

    public static Rabatt abMindestwert(long mindestCent, Rabatt rabatt) {
        Objects.requireNonNull(rabatt, "rabatt");
        // Eine Strategie, die eine andere einpackt und nur manchmal
        // weiterreicht: Das ist ein Dekorierer (Abschnitt 6.9).
        return betrag -> betrag >= mindestCent ? rabatt.anwenden(betrag) : betrag;
    }

    /**
     * Aufgabe 12: die Fabrik. Der Aufrufer nennt nur einen Code und bekommt
     * "irgendeinen Rabatt" - welche Strategie dahintersteckt, bleibt hier.
     * Kommt ein neuer Code dazu, aendert sich nur diese Methode.
     */
    public static Rabatt ausCode(String code) {
        Objects.requireNonNull(code, "code");
        // Locale.ROOT: Grossschreibung unabhaengig von der Spracheinstellung
        // des Rechners (auf tuerkischen Systemen wird aus "i" sonst ein "İ").
        String c = code.strip().toUpperCase(Locale.ROOT);

        if (c.equals("KEIN")) {
            return keiner();
        }
        if (c.startsWith("PROZENT")) {
            // "PROZENTabc" -> parseInt wirft NumberFormatException. Die ist eine
            // Unterklasse von IllegalArgumentException, passt also schon.
            // "PROZENT150" -> prozent() wirft selbst. Zahl auslesen und
            // pruefen - beides steckt schon in vorhandenen Methoden.
            return prozent(Integer.parseInt(c.substring("PROZENT".length())));
        }
        if (c.startsWith("MINUS")) {
            return festbetrag(Long.parseLong(c.substring("MINUS".length())));
        }
        throw new IllegalArgumentException("Unbekannter Rabattcode: " + code);
    }
}
