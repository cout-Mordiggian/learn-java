import java.util.List;
import java.util.Locale;

/**
 * Kapitel 10 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static double flaeche(Form form) {
        // Record-Muster: "case Form.Kreis(double r)" prueft den Typ UND
        // zerlegt das Objekt in einem Zug. Kein Cast, kein k.radius().
        //
        // Kein default noetig: Form ist sealed, der Compiler kennt alle
        // drei Faelle. Kommt spaeter ein Fuenfeck dazu, meldet er GENAU
        // diese Stelle als unvollstaendig - das ist der eigentliche Gewinn.
        return switch (form) {
            case Form.Kreis(double r) -> Math.PI * r * r;
            case Form.Rechteck(double b, double h) -> b * h;
            case Form.Dreieck(double a, double b, double c) -> {
                double s = (a + b + c) / 2;                       // halber Umfang
                yield Math.sqrt(s * (s - a) * (s - b) * (s - c)); // Heron
            }
            // yield statt -> , weil dieser Zweig ein Block mit mehreren
            // Anweisungen ist. Bei einem einzelnen Ausdruck reicht der Pfeil.
        };
    }

    public static String benenne(Form form) {
        return switch (form) {
            case Form.Kreis(double r) ->
                    String.format(Locale.ROOT, "Kreis mit Radius %.1f", r);
            case Form.Rechteck(double b, double h) ->
                    String.format(Locale.ROOT, "Rechteck %.1fx%.1f", b, h);
            case Form.Dreieck(double a, double b, double c) ->
                    String.format(Locale.ROOT, "Dreieck %.1f/%.1f/%.1f", a, b, c);
        };
        // Locale.ROOT wie in Kapitel 6: sonst wuerde %.1f auf einem
        // deutschen System "2,0" statt "2.0" erzeugen.
    }

    public static String beschreibe(Object o) {
        return switch (o) {
            // "case null" ist seit Java 21 erlaubt. Ohne diesen Zweig
            // wirft ein switch ueber null eine NullPointerException - genau
            // wie der alte switch, der null gar nicht behandeln konnte.
            case null -> "nichts";

            // Guarded Patterns: Typ + Bedingung. Die REIHENFOLGE zaehlt,
            // das erste passende Muster gewinnt. Deshalb muessen die
            // eingeschraenkten Faelle vor dem allgemeinen "case Integer i" stehen -
            // andernfalls meldet der Compiler den spaeteren als unerreichbar.
            case Integer i when i < 0 -> "negative Zahl";
            case Integer i when i == 0 -> "null";
            case Integer i -> "positive Zahl";

            case String s when s.isEmpty() -> "leerer Text";
            case String s -> "Text der Laenge " + s.length();

            // default ist hier noetig: Object ist nicht sealed,
            // es gibt unendlich viele moegliche Typen.
            default -> "unbekannt";
        };
    }

    public static String steckbrief(Artikel a) {
        // Textblock: die gemeinsame Einrueckung wird abgeschnitten. Massgeblich
        // ist die am wenigsten eingerueckte Zeile; die schliessenden """ zaehlen
        // mit. Hier stehen Inhalt und """ gleich weit eingerueckt, also bleibt
        // links nichts stehen. Rueckt man die """ weiter nach links, wird der
        // Inhalt entsprechend eingerueckt.
        // formatted(...) ist die Kurzform von String.format(text, ...).
        return """
                Artikel: %s
                Preis:   %d Cent
                Menge:   %d
                Gesamt:  %d Cent
                """.formatted(a.name(), a.preisCent(), a.menge(), a.gesamtCent());
    }

    public static long werktageZaehlen(List<Wochentag> tage) {
        return tage.stream()
                .filter(Wochentag::istWerktag)   // Methodenreferenz auf den Parameter
                .count();
    }
}
