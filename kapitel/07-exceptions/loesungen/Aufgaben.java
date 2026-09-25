/**
 * Kapitel 07 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static int sicherTeilen(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            // In echtem Code waere "if (b == 0) return 0;" besser:
            // Exceptions sind teuer (der Stacktrace wird aufgebaut) und
            // gehoeren nicht in den normalen Kontrollfluss.
            // Hier geht es um die Uebung des Konstrukts.
            return 0;
        }
        // Nebenbei: nur die GANZZAHL-Division wirft. 10.0 / 0 ergibt Infinity,
        // 0.0 / 0.0 ergibt NaN - Kommazahlen kennen keine Division-durch-null-Exception.
    }

    public static int parseOderStandard(String text, int standard) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Wichtig: Integer.parseInt(null) wirft ebenfalls eine
            // NumberFormatException ("Cannot parse null string"), keine
            // NullPointerException. Ein catch reicht also.
            return standard;
        }
    }

    public static int auswerten(String[] werte, int index) {
        try {
            return Integer.parseInt(werte[index]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            // Multi-Catch: EIN Block fuer mehrere Typen. Die Variable e hat
            // hier den naechsten gemeinsamen Obertyp (RuntimeException) und
            // ist implizit final - du darfst ihr nichts zuweisen.
            // Nur einsetzen, wenn die Behandlung wirklich identisch ist.
            return -1;
        }
    }

    public static int konfigWert(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Das zweite Argument ist die "cause". Ohne sie steht im Log nur
            // "Ungueltiger Konfigurationswert: abc" - mit ihr auch ein
            // "Caused by: NumberFormatException" samt Originalzeile.
            // Das ist der Unterschied zwischen 5 Minuten und 2 Stunden Suche.
            throw new IllegalStateException("Ungueltiger Konfigurationswert: " + text, e);
        }
    }

    public static String ablauf(boolean fehlerWerfen) {
        StringBuilder sb = new StringBuilder("start|");
        try {
            if (fehlerWerfen) {
                throw new RuntimeException("absichtlicher Fehler");
            }
            sb.append("ok|");
        } catch (RuntimeException e) {
            sb.append("fehler|");
        } finally {
            // Laeuft in JEDEM Fall: nach dem try, nach dem catch, sogar
            // nach einem return im try-Block. Der klassische Einsatzzweck
            // (Aufraeumen) ist heute meist von try-with-resources abgeloest.
            sb.append("ende");
        }
        return sb.toString();
    }

    public static long abheben(long guthaben, long betrag) throws UnzureichendeDeckungException {
        // Zwei Fehlerarten, zwei Typen - die Unterscheidung aus Kapitel 5:
        //  - betrag <= 0 ist ein Programmierfehler des Aufrufers -> unchecked
        //  - fehlende Deckung ist ein normaler Geschaeftsfall,
        //    auf den der Aufrufer reagieren kann und soll  -> checked
        if (betrag <= 0) {
            throw new IllegalArgumentException("Betrag muss positiv sein: " + betrag);
        }
        if (betrag > guthaben) {
            throw new UnzureichendeDeckungException(betrag - guthaben);
        }
        return guthaben - betrag;
    }

    public static String protokoll(boolean fehlerWerfen) {
        StringBuilder log = new StringBuilder();
        // Die Ressource wird in den Klammern deklariert. Am Ende des Blocks
        // ruft die JVM automatisch close() auf - egal ob normal beendet,
        // per return verlassen oder durch eine Exception abgebrochen.
        try (Tresor t = new Tresor(log)) {
            if (fehlerWerfen) {
                throw new RuntimeException("Panne");
            }
            t.benutzen();
        } catch (RuntimeException e) {
            // Bewusst geschluckt, damit der Test das Protokoll pruefen kann.
            // In echtem Code waere ein leerer catch-Block ein Fehler.
        }
        // Bei mehreren Ressourcen - try (A a = ...; B b = ...) - wird in
        // UMGEKEHRTER Reihenfolge geschlossen: erst b, dann a.
        return log.toString();
    }
}
