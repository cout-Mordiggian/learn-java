public class Tests {
    public static void main(String[] args) throws Exception {

        Pruef.abschnitt("Aufgabe 1: sicherTeilen");
        Pruef.gleich(5, Aufgaben.sicherTeilen(10, 2), "10 / 2");
        Pruef.gleich(0, Aufgaben.sicherTeilen(10, 0), "Division durch 0 -> 0");
        Pruef.gleich(-3, Aufgaben.sicherTeilen(-10, 3), "abgeschnitten Richtung 0");

        Pruef.abschnitt("Aufgabe 2: parseOderStandard");
        Pruef.gleich(42, Aufgaben.parseOderStandard("42", 0), "gueltige Zahl");
        Pruef.gleich(-1, Aufgaben.parseOderStandard("abc", -1), "keine Zahl");
        Pruef.gleich(7, Aufgaben.parseOderStandard(null, 7), "null");
        Pruef.gleich(7, Aufgaben.parseOderStandard("", 7), "leerer String");
        Pruef.gleich(-5, Aufgaben.parseOderStandard("-5", 0), "negative Zahl");

        Pruef.abschnitt("Aufgabe 3: auswerten (Multi-Catch)");
        String[] werte = {"1", "zwei", "3"};
        Pruef.gleich(1, Aufgaben.auswerten(werte, 0), "gueltiger Index, gueltige Zahl");
        Pruef.gleich(-1, Aufgaben.auswerten(werte, 1), "gueltiger Index, keine Zahl");
        Pruef.gleich(-1, Aufgaben.auswerten(werte, 99), "Index zu gross");
        Pruef.gleich(-1, Aufgaben.auswerten(werte, -1), "negativer Index");

        Pruef.abschnitt("Aufgabe 4: konfigWert (Exception Chaining)");
        Pruef.gleich(8080, Aufgaben.konfigWert("8080"), "gueltiger Wert");
        Pruef.wirft(IllegalStateException.class,
                () -> Aufgaben.konfigWert("abc"), "wirft IllegalStateException");
        try {
            Aufgaben.konfigWert("abc");
            Pruef.wahr(false, "haette werfen muessen");
        } catch (IllegalStateException e) {
            Pruef.gleich("Ungueltiger Konfigurationswert: abc", e.getMessage(), "Nachricht");
            Pruef.wahr(e.getCause() instanceof NumberFormatException,
                    "cause ist die urspruengliche NumberFormatException");
        }

        Pruef.abschnitt("Aufgabe 5: ablauf (finally)");
        Pruef.gleich("start|ok|ende", Aufgaben.ablauf(false), "ohne Fehler");
        Pruef.gleich("start|fehler|ende", Aufgaben.ablauf(true), "mit Fehler - ende kommt trotzdem");

        Pruef.abschnitt("Aufgabe 6: abheben (checked Exception)");
        Pruef.gleich(700L, Aufgaben.abheben(1000, 300), "genug Deckung");
        Pruef.gleich(0L, Aufgaben.abheben(500, 500), "exakt aufgebraucht");
        Pruef.wirft(IllegalArgumentException.class,
                () -> {
                    try { Aufgaben.abheben(1000, 0); }
                    catch (UnzureichendeDeckungException e) { throw new RuntimeException(e); }
                }, "Betrag 0 -> IllegalArgumentException");
        Pruef.wirft(IllegalArgumentException.class,
                () -> {
                    try { Aufgaben.abheben(1000, -5); }
                    catch (UnzureichendeDeckungException e) { throw new RuntimeException(e); }
                }, "negativer Betrag -> IllegalArgumentException");
        try {
            Aufgaben.abheben(100, 350);
            Pruef.wahr(false, "haette werfen muessen");
        } catch (UnzureichendeDeckungException e) {
            Pruef.gleich(250L, e.getFehlbetrag(), "Fehlbetrag steckt in der Exception");
            Pruef.gleich("Es fehlen 250 Cent", e.getMessage(), "Nachricht");
        }

        Pruef.abschnitt("Aufgabe 7: try-with-resources");
        Pruef.gleich("geoeffnet|benutzt|geschlossen", Aufgaben.protokoll(false), "normaler Ablauf");
        Pruef.gleich("geoeffnet|geschlossen", Aufgaben.protokoll(true),
                "close() laeuft auch bei Exception");

        Pruef.abschnitt("Tresor direkt");
        StringBuilder log = new StringBuilder();
        Tresor t = new Tresor(log);
        Pruef.wahr(t.istOffen(), "nach dem Konstruktor offen");
        t.close();
        Pruef.falsch(t.istOffen(), "nach close geschlossen");
        t.close();
        Pruef.gleich("geoeffnet|geschlossen", log.toString(), "zweites close() tut nichts (idempotent)");
        Pruef.wirft(IllegalStateException.class, t::benutzen, "geschlossener Tresor wehrt sich");

        Pruef.bericht();
    }
}
