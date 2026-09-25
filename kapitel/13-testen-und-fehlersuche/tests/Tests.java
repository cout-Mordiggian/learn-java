public class Tests {

    /**
     * Ein Stueck Code, das einen Wert liefert. In den Pruefungen unten steht
     * dafuer ein Lambda wie () -> Fehlerhaft.wochentag(5): "der Aufruf, den
     * wir gleich ausfuehren wollen". So kann pruefe() den Aufruf in ein
     * try/catch packen.
     */
    interface Aufruf {
        Object ausfuehren();
    }

    public static void main(String[] args) {

        // ------------------------------------------------------------ Teil A
        Pruef.abschnitt("Teil A: Deine Tests gegen die korrekte Implementierung");
        // Verglichen wird der Rueckgabewert von alleBestanden: true = alles bestanden.
        Pruef.gleich(true, laeuftDurch(new Kandidaten.Referenz()),
                "Deine Tests bestehen mit der korrekten Implementierung");

        Pruef.abschnitt("Teil A: Deine Tests gegen die Mutanten");
        Rabattrechner[] mutanten = {
            new Kandidaten.Mutant1(), new Kandidaten.Mutant2(), new Kandidaten.Mutant3(),
            new Kandidaten.Mutant4(), new Kandidaten.Mutant5(), new Kandidaten.Mutant6(),
            new Kandidaten.Mutant7()
        };
        for (int i = 0; i < mutanten.length; i++) {
            // Hier muss alleBestanden false liefern: Deine Tests haben den Fehler bemerkt.
            Pruef.gleich(false, laeuftDurch(mutanten[i]), "Deine Tests entlarven Mutant " + (i + 1));
        }

        // ------------------------------------------------------------ Teil B
        Pruef.abschnitt("Teil B: durchschnitt");
        pruefe(1.5, () -> Fehlerhaft.durchschnitt(new int[]{1, 2}), "{1, 2} -> 1.5");
        pruefe(4.0, () -> Fehlerhaft.durchschnitt(new int[]{2, 4, 6}), "{2, 4, 6} -> 4.0");
        pruefe(5.0 / 3, () -> Fehlerhaft.durchschnitt(new int[]{1, 2, 2}), "{1, 2, 2} -> 1.666...");
        pruefe(-2.5, () -> Fehlerhaft.durchschnitt(new int[]{-2, -3}), "negative Zahlen");

        Pruef.abschnitt("Teil B: zaehleGroesser");
        pruefe(2, () -> Fehlerhaft.zaehleGroesser(new int[]{1, 5, 3, 8}, 4), "{1, 5, 3, 8} groesser 4");
        pruefe(1, () -> Fehlerhaft.zaehleGroesser(new int[]{10, 20}, 10), "gleich der Grenze zaehlt nicht");
        pruefe(1, () -> Fehlerhaft.zaehleGroesser(new int[]{1, 2, 9}, 5), "letztes Element zaehlt mit");
        pruefe(0, () -> Fehlerhaft.zaehleGroesser(new int[]{}, 0), "leeres Array");

        Pruef.abschnitt("Teil B: istAdmin");
        pruefe(true, () -> Fehlerhaft.istAdmin("admin"), "Literal \"admin\"");
        pruefe(true, () -> Fehlerhaft.istAdmin(new String("admin")), "zur Laufzeit erzeugt: new String(\"admin\")");
        pruefe(true, () -> Fehlerhaft.istAdmin("xadmin".substring(1)), "zur Laufzeit erzeugt: substring");
        pruefe(false, () -> Fehlerhaft.istAdmin("Admin"), "Grossschreibung zaehlt");
        pruefe(false, () -> Fehlerhaft.istAdmin(null), "null -> false");

        Pruef.abschnitt("Teil B: wochentag");
        pruefe("Montag", () -> Fehlerhaft.wochentag(1), "1 -> Montag");
        pruefe("Donnerstag", () -> Fehlerhaft.wochentag(4), "4 -> Donnerstag");
        pruefe("Freitag", () -> Fehlerhaft.wochentag(5), "5 -> Freitag");
        pruefe("Samstag", () -> Fehlerhaft.wochentag(6), "6 -> Samstag");
        pruefe("Sonntag", () -> Fehlerhaft.wochentag(7), "7 -> Sonntag");
        pruefe("ungueltig", () -> Fehlerhaft.wochentag(0), "0 -> ungueltig");
        pruefe("ungueltig", () -> Fehlerhaft.wochentag(8), "8 -> ungueltig");

        Pruef.abschnitt("Teil B: normalisiere");
        pruefe("hallo welt", () -> Fehlerhaft.normalisiere("  Hallo Welt "), "Rand weg, klein");
        pruefe("abc", () -> Fehlerhaft.normalisiere("abc"), "schon normalisiert");
        pruefe("", () -> Fehlerhaft.normalisiere("   "), "nur Leerzeichen -> leer");

        Pruef.abschnitt("Teil B: hoechstens");
        pruefe(3, () -> Fehlerhaft.hoechstens(3, 10), "unter der Grenze: unveraendert");
        pruefe(10, () -> Fehlerhaft.hoechstens(10, 10), "genau an der Grenze");
        pruefe(10, () -> Fehlerhaft.hoechstens(15, 10), "ueber der Grenze: gekappt");
        pruefe(-7, () -> Fehlerhaft.hoechstens(-7, 0), "negativer Wert");

        Pruef.bericht();
    }

    // ------------------------------------------------------------- Helfer

    /**
     * Ruft deine Methode alleBestanden auf. Wirft sie selbst eine Exception
     * (etwa, weil ein Mutant an einer Stelle wirft, an der du keine erwartest),
     * gilt der Durchlauf als NICHT bestanden - genau wie in JUnit, wo eine
     * unerwartete Exception einen Test rot macht.
     */
    private static boolean laeuftDurch(Rabattrechner r) {
        try {
            return MeineTests.alleBestanden(r);
        } catch (RuntimeException e) {
            System.out.println("       [deine Pruefung] unerwartete Exception: " + beschreibe(e));
            return false;
        }
    }

    /**
     * Fuehrt den Aufruf aus und vergleicht das Ergebnis. Wirft der Aufruf eine
     * Exception, wird sie als FEHL gemeldet, statt den ganzen Testlauf
     * abzubrechen - so siehst du auch alle uebrigen Pruefungen.
     */
    private static void pruefe(Object erwartet, Aufruf aufruf, String was) {
        Object ergebnis;
        try {
            ergebnis = aufruf.ausfuehren();
        } catch (RuntimeException e) {
            Pruef.gleich(erwartet, beschreibe(e), was);
            return;
        }
        if (erwartet instanceof Double e && ergebnis instanceof Double t) {
            Pruef.fastGleich(e, t, was);
        } else {
            Pruef.gleich(erwartet, ergebnis, was);
        }
    }

    /** "ArrayIndexOutOfBoundsException (Index 4 ...) in Fehlerhaft.java:37" */
    private static String beschreibe(RuntimeException e) {
        String text = e.getClass().getSimpleName() + " (" + e.getMessage() + ")";
        // Die erste Stacktrace-Zeile aus deinem Code heraussuchen (Abschnitt 13.6)
        for (StackTraceElement zeile : e.getStackTrace()) {
            String datei = zeile.getFileName();
            if ("Fehlerhaft.java".equals(datei) || "MeineTests.java".equals(datei)) {
                return text + " in " + datei + ":" + zeile.getLineNumber();
            }
        }
        return text;
    }
}
