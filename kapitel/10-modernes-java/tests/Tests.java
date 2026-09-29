import java.util.List;

public class Tests {
    public static void main(String[] args) {

        Pruef.abschnitt("Artikel: record");
        Artikel kaffee = new Artikel("Kaffee", 499, 3);
        Pruef.gleich("Kaffee", kaffee.name(), "Zugriffsmethode heisst name(), nicht getName()");
        Pruef.gleich(499L, kaffee.preisCent(), "preisCent()");
        Pruef.gleich(3, kaffee.menge(), "menge()");
        Pruef.gleich(1497L, kaffee.gesamtCent(), "gesamtCent()");
        Pruef.gleich("Artikel[name=Kaffee, preisCent=499, menge=3]", kaffee.toString(),
                "toString kommt vom Compiler");
        Pruef.wahr(kaffee.equals(new Artikel("Kaffee", 499, 3)), "equals kommt vom Compiler");
        Pruef.gleich(kaffee.hashCode(), new Artikel("Kaffee", 499, 3).hashCode(), "hashCode auch");

        Pruef.gleich("Kaffee", new Artikel("  Kaffee  ", 1, 1).name(), "Name wird gestrippt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Artikel("", 1, 1), "leerer Name abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Artikel("   ", 1, 1), "Name nur aus Leerzeichen abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Artikel(null, 1, 1), "null-Name abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Artikel("x", -1, 1), "negativer Preis abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Artikel("x", 1, -1), "negative Menge abgelehnt");

        Artikel einer = Artikel.einzeln("Tee", 250);
        Pruef.gleich(1, einer.menge(), "einzeln() setzt Menge 1");
        Artikel fuenf = einer.mitMenge(5);
        Pruef.gleich(5, fuenf.menge(), "mitMenge liefert neue Menge");
        Pruef.gleich(1, einer.menge(), "Original unveraendert");
        Pruef.gleich(1250L, fuenf.gesamtCent(), "Gesamtpreis der Kopie");

        Pruef.abschnitt("Wochentag: enum");
        Pruef.wahr(Wochentag.MONTAG.istWerktag(), "Montag ist Werktag");
        Pruef.falsch(Wochentag.SAMSTAG.istWerktag(), "Samstag nicht");
        Pruef.falsch(Wochentag.SONNTAG.istWerktag(), "Sonntag nicht");
        Pruef.gleich(7, Wochentag.values().length, "sieben Tage");
        Pruef.gleich(Wochentag.DIENSTAG, Wochentag.MONTAG.naechster(), "nach Montag Dienstag");
        Pruef.gleich(Wochentag.MONTAG, Wochentag.SONNTAG.naechster(), "nach Sonntag wieder Montag");
        Pruef.gleich(Wochentag.MONTAG, Wochentag.vonNummer(1), "1 -> Montag");
        Pruef.gleich(Wochentag.SONNTAG, Wochentag.vonNummer(7), "7 -> Sonntag");
        Pruef.wirft(IllegalArgumentException.class,
                () -> Wochentag.vonNummer(0), "0 abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> Wochentag.vonNummer(8), "8 abgelehnt");
        Pruef.gleich("MITTWOCH", Wochentag.MITTWOCH.name(), "name()");
        Pruef.gleich(Wochentag.FREITAG, Wochentag.valueOf("FREITAG"), "valueOf");

        Pruef.abschnitt("Form: sealed interface");
        Form k = new Form.Kreis(2);
        Form r = new Form.Rechteck(3, 4);
        Form d = new Form.Dreieck(3, 4, 5);
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Kreis(-1), "negativer Radius abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Rechteck(1, -1), "negative Hoehe abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Rechteck(-1, 1), "negative Breite abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Dreieck(-3, 4, 5), "Dreieck: negative Seite a abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Dreieck(3, -4, 5), "Dreieck: negative Seite b abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Form.Dreieck(3, 4, -5), "Dreieck: negative Seite c abgelehnt");

        Pruef.abschnitt("Aufgabe 1: flaeche (Record-Muster)");
        Pruef.fastGleich(Math.PI * 4, Aufgaben.flaeche(k), "Kreis r=2");
        Pruef.fastGleich(12.0, Aufgaben.flaeche(r), "Rechteck 3x4");
        Pruef.fastGleich(6.0, Aufgaben.flaeche(d), "Dreieck 3/4/5 (Heron)");
        Pruef.fastGleich(0.0, Aufgaben.flaeche(new Form.Kreis(0)), "Kreis r=0");

        Pruef.abschnitt("Aufgabe 2: benenne");
        Pruef.gleich("Kreis mit Radius 2.0", Aufgaben.benenne(k), "Kreis");
        Pruef.gleich("Rechteck 3.0x4.0", Aufgaben.benenne(r), "Rechteck");
        Pruef.gleich("Dreieck 3.0/4.0/5.0", Aufgaben.benenne(d), "Dreieck");

        Pruef.abschnitt("Aufgabe 3: beschreibe (guarded patterns)");
        Pruef.gleich("nichts", Aufgaben.beschreibe(null), "null");
        Pruef.gleich("negative Zahl", Aufgaben.beschreibe(-5), "negativ");
        Pruef.gleich("null", Aufgaben.beschreibe(0), "0");
        Pruef.gleich("positive Zahl", Aufgaben.beschreibe(42), "positiv");
        Pruef.gleich("leerer Text", Aufgaben.beschreibe(""), "leerer String");
        Pruef.gleich("Text der Laenge 5", Aufgaben.beschreibe("Hallo"), "String");
        Pruef.gleich("unbekannt", Aufgaben.beschreibe(3.14), "Double");

        Pruef.abschnitt("Aufgabe 4: steckbrief (Textblock)");
        Pruef.gleich("""
                Artikel: Kaffee
                Preis:   499 Cent
                Menge:   3
                Gesamt:  1497 Cent
                """, Aufgaben.steckbrief(kaffee), "vier Zeilen mit Umbruch am Ende");

        Pruef.abschnitt("Aufgabe 5: werktageZaehlen");
        Pruef.gleich(2L, Aufgaben.werktageZaehlen(
                List.of(Wochentag.MONTAG, Wochentag.SAMSTAG, Wochentag.FREITAG)), "2 von 3");
        Pruef.gleich(0L, Aufgaben.werktageZaehlen(List.of()), "leere Liste");
        Pruef.gleich(0L, Aufgaben.werktageZaehlen(
                List.of(Wochentag.SAMSTAG, Wochentag.SONNTAG)), "nur Wochenende");

        Pruef.abschnitt("Pizza: Builder (10.9)");
        // Diese Bloecke laufen ueber sicher(...): Wirft dein Builder unterwegs
        // eine Exception, wird das als FEHL gemeldet, und der Rest laeuft weiter.
        sicher("Pizza: ohne Extras", () -> {
            Pizza p = Pizza.builder(Pizza.Groesse.MITTEL).build();
            Pruef.gleich(Pizza.Groesse.MITTEL, p.groesse(), "Groesse kommt an");
            Pruef.gleich(List.of(), p.belaege(), "ohne belag(): leere Liste, nicht null");
            Pruef.falsch(p.hatExtraKaese(), "ohne extraKaese(): false");
            Pruef.gleich(1000L, p.preisCent(), "MITTEL ohne alles: 1000 Cent");
        });
        sicher("Pizza: mit Extras", () -> {
            Pizza p = Pizza.builder(Pizza.Groesse.KLEIN)
                    .belag("Salami")
                    .belag(" Pilze ")
                    .extraKaese()
                    .build();
            Pruef.gleich(List.of("Salami", "Pilze"), p.belaege(),
                    "Belaege in Reihenfolge, Leerzeichen am Rand entfernt");
            Pruef.wahr(p.hatExtraKaese(), "extraKaese() kommt an");
            Pruef.gleich(1300L, p.preisCent(), "KLEIN 800 + 2 x 150 + 200 = 1300");
            Pruef.gleich("Pizza KLEIN: Salami, Pilze + extra Kaese", p.toString(), "toString");
            Pizza gross = Pizza.builder(Pizza.Groesse.GROSS)
                    .belag("a").belag("b").belag("c").belag("d").belag("e").build();
            Pruef.gleich(1950L, gross.preisCent(), "GROSS mit 5 Belaegen: 1200 + 750 = 1950");
        });
        sicher("Pizza: fluent", () -> {
            Pizza.Builder b = Pizza.builder(Pizza.Groesse.KLEIN);
            Pruef.wahr(b.belag("Oliven") == b, "belag() gibt denselben Builder zurueck (return this)");
            Pruef.wahr(b.extraKaese() == b, "extraKaese() gibt denselben Builder zurueck");
        });
        sicher("Pizza: Pruefungen", () -> {
            Pruef.wirft(NullPointerException.class, () -> Pizza.builder(null),
                    "builder(null) -> NullPointerException");
            Pruef.wirft(IllegalArgumentException.class, () -> Pizza.builder(Pizza.Groesse.KLEIN).belag(""),
                    "belag(\"\") -> IllegalArgumentException");
            Pruef.wirft(IllegalArgumentException.class, () -> Pizza.builder(Pizza.Groesse.KLEIN).belag("   "),
                    "belag(\"   \") -> IllegalArgumentException");
            Pruef.wirft(IllegalArgumentException.class, () -> Pizza.builder(Pizza.Groesse.KLEIN).belag(null),
                    "belag(null) -> IllegalArgumentException");
            Pizza.Builder sechs = Pizza.builder(Pizza.Groesse.GROSS);
            for (int i = 1; i <= 6; i++) sechs.belag("Belag " + i);
            Pruef.wirft(IllegalStateException.class, sechs::build,
                    "6 Belaege -> build() wirft IllegalStateException");
        });
        sicher("Pizza: unveraenderlich", () -> {
            Pizza p = Pizza.builder(Pizza.Groesse.KLEIN).belag("Salami").build();
            Pruef.wirft(UnsupportedOperationException.class, () -> p.belaege().add("Ananas"),
                    "belaege() laesst sich von aussen nicht aendern");
            Pizza.Builder b = Pizza.builder(Pizza.Groesse.MITTEL).belag("Tomate");
            Pizza erste = b.build();
            b.belag("Mais");
            Pizza zweite = b.build();
            Pruef.gleich(List.of("Tomate"), erste.belaege(),
                    "Builder nach build() weiter benutzt: die erste Pizza bleibt, wie sie war");
            Pruef.gleich(List.of("Tomate", "Mais"), zweite.belaege(), "die zweite hat beide Belaege");
        });

        Pruef.bericht();
    }

    /**
     * Fuehrt einen Pruefblock aus. Wirft dein Code unterwegs eine Exception,
     * wird das als FEHL gemeldet - mit Datei und Zeile - und die restlichen
     * Pruefungen laufen trotzdem weiter.
     */
    private static void sicher(String was, Runnable block) {
        try {
            block.run();
        } catch (RuntimeException e) {
            String ort = "";
            for (StackTraceElement stelle : e.getStackTrace()) {
                String datei = stelle.getFileName();
                if (datei != null && !datei.equals("Tests.java") && !datei.equals("Pruef.java")
                        && !stelle.getClassName().startsWith("java.")) {
                    ort = "  bei " + datei + ":" + stelle.getLineNumber();
                    break;
                }
            }
            Pruef.gleich("keine Exception", e.getClass().getSimpleName() + ": " + e.getMessage() + ort, was);
        }
    }
}
