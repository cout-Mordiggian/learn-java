/**
 * Kapitel 07 - Exceptions.
 * Pruefen:  ./lerne.sh 07
 */
public class Aufgaben {

    /**
     * Aufgabe 1: a / b, aber 0 statt einer ArithmeticException.
     * Loese es hier bewusst mit try/catch.
     */
    public static int sicherTeilen(int a, int b) {
        // TODO
        return 0;
    }

    /**
     * Aufgabe 2: Text als Zahl, sonst der Standardwert.
     * parseOderStandard("42", 0)   -> 42
     * parseOderStandard("abc", -1) -> -1
     * parseOderStandard(null, 7)   -> 7
     */
    public static int parseOderStandard(String text, int standard) {
        // TODO: Welche Exception wirft Integer.parseInt(null)?
        //       Und welche bei "abc"? Probier beides aus - vielleicht
        //       reicht ein einziger catch-Block.
        return standard;
    }

    /**
     * Aufgabe 3: werte[index] als Zahl, oder -1.
     * Nutze EINEN catch-Block mit Multi-Catch (Typ1 | Typ2 e).
     */
    public static int auswerten(String[] werte, int index) {
        // TODO
        return -1;
    }

    /**
     * Aufgabe 4: parst text; bei Fehler
     *   IllegalStateException("Ungueltiger Konfigurationswert: " + text)
     * MIT der urspruenglichen Exception als cause.
     */
    public static int konfigWert(String text) {
        // TODO: throw new IllegalStateException(nachricht, e);
        return 0;
    }

    /**
     * Aufgabe 5: zeigt, dass finally immer laeuft.
     * ablauf(false) -> "start|ok|ende"
     * ablauf(true)  -> "start|fehler|ende"
     */
    public static String ablauf(boolean fehlerWerfen) {
        StringBuilder sb = new StringBuilder("start|");
        // TODO: try { wenn fehlerWerfen -> throw new RuntimeException("x");
        //             sonst "ok|" anhaengen }
        //       catch (RuntimeException e) { "fehler|" anhaengen }
        //       finally { "ende" anhaengen }
        return sb.toString();
    }

    /**
     * Aufgabe 6: Abheben mit checked Exception.
     *
     * @return das neue Guthaben
     * @throws IllegalArgumentException        wenn betrag <= 0
     * @throws UnzureichendeDeckungException   wenn betrag > guthaben
     */
    public static long abheben(long guthaben, long betrag) throws UnzureichendeDeckungException {
        // TODO
        return guthaben;
    }

    /**
     * Aufgabe 7: Tresor in try-with-resources benutzen.
     * protokoll(false) -> "geoeffnet|benutzt|geschlossen"
     * protokoll(true)  -> "geoeffnet|geschlossen"
     *   (die RuntimeException wird gefangen, close() laeuft trotzdem)
     */
    public static String protokoll(boolean fehlerWerfen) {
        StringBuilder log = new StringBuilder();
        // TODO: try (Tresor t = new Tresor(log)) {
        //           if (fehlerWerfen) throw new RuntimeException("Panne");
        //           t.benutzen();
        //       } catch (RuntimeException e) {
        //           // bewusst ignorieren - wir wollen nur sehen, dass close lief
        //       }
        return log.toString();
    }
}
