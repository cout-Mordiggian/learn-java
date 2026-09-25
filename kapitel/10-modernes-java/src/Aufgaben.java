import java.util.List;
import java.util.Locale;

/**
 * Kapitel 10 - Modernes Java.
 * Pruefen:  ./lerne.sh 10
 */
public class Aufgaben {

    /**
     * Aufgabe 1: Flaeche per switch mit RECORD-MUSTERN.
     * Kein default - weil Form sealed ist, kennt der Compiler alle Faelle.
     * Dreieck nach Heron: s = (a+b+c)/2, A = sqrt(s(s-a)(s-b)(s-c))
     */
    public static double flaeche(Form form) {
        // TODO: return switch (form) {
        //           case Form.Kreis(double r) -> ...;
        //           case Form.Rechteck(double b, double h) -> ...;
        //           case Form.Dreieck(double a, double b, double c) -> ...;
        //       };
        return 0.0;
    }

    /**
     * Aufgabe 2: Benennung.
     * Kreis    -> "Kreis mit Radius 2.0"
     * Rechteck -> "Rechteck 3.0x4.0"
     * Dreieck  -> "Dreieck 3.0/4.0/5.0"
     * Nutze String.format(Locale.ROOT, "...%.1f...", ...)
     */
    public static String benenne(Form form) {
        // TODO
        return "";
    }

    /**
     * Aufgabe 3: Pattern Matching mit Bedingungen (guarded patterns).
     *   null            -> "nichts"
     *   Integer < 0     -> "negative Zahl"
     *   Integer == 0    -> "null"
     *   Integer > 0     -> "positive Zahl"
     *   leerer String   -> "leerer Text"
     *   String          -> "Text der Laenge 5"
     *   sonst           -> "unbekannt"
     */
    public static String beschreibe(Object o) {
        // TODO: case Integer i when i < 0 -> ...
        //       case null -> ...   (ja, das geht seit Java 21!)
        return "";
    }

    /**
     * Aufgabe 4: Steckbrief als Textblock.
     *
     * Artikel: Kaffee
     * Preis:   499 Cent
     * Menge:   3
     * Gesamt:  1497 Cent
     *
     * (mit abschliessendem Zeilenumbruch)
     */
    public static String steckbrief(Artikel a) {
        // TODO: Textblock mit """ ... """ und .formatted(...)
        return "";
    }

    /** Aufgabe 5: Wie viele Werktage sind in der Liste? */
    public static long werktageZaehlen(List<Wochentag> tage) {
        // TODO: Stream aus Kapitel 9
        return 0L;
    }
}
