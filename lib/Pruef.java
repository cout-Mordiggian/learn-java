import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Ein winziges Pruef-Framework ("Mini-JUnit") fuer diesen Kurs.
 *
 * Bewusst simpel gehalten: statische Methoden, ein globaler Zaehler.
 * Ab Kapitel 12 lernst du das echte JUnit kennen - dann wirst du erkennen,
 * dass die Ideen dieselben sind (Assertion, Report, Exit-Code).
 */
public final class Pruef {

    private Pruef() { } // Utility-Klasse: keine Instanzen erlaubt

    private static final List<String> FEHLER = new ArrayList<>();
    private static int gesamt = 0;

    private static final String ESC   = String.valueOf((char) 27);
    private static final String GRUEN = ESC + "[32m";
    private static final String ROT   = ESC + "[31m";
    private static final String GRAU  = ESC + "[90m";
    private static final String GELB  = ESC + "[33m";
    private static final String RESET = ESC + "[0m";

    static {
        // Wirft dein Code eine Exception, bricht der ganze Testlauf ab. Statt eines
        // nackten Stacktraces gibt es dann eine Erklaerung und den Zwischenstand.
        Thread.setDefaultUncaughtExceptionHandler((thread, fehler) -> {
            System.out.flush();
            System.out.println();
            System.out.println(ROT + "  ABBRUCH: Dein Code hat eine Exception geworfen - die restlichen"
                    + " Pruefungen konnten nicht laufen." + RESET);
            System.out.println(GELB + "  Lies den Stacktrace unten von oben: Die erste Zeile mit einer deiner"
                    + " Dateien (z. B. Aufgaben.java:42) ist die Stelle in deinem Code." + RESET);
            System.out.println();
            fehler.printStackTrace(System.out);
            System.out.println();
            System.out.println(ROT + "  Bis zum Abbruch: " + (gesamt - FEHLER.size()) + " von " + gesamt
                    + " Pruefungen bestanden." + RESET);
            System.exit(1);
        });
    }

    /** Vergleicht zwei Werte. Arrays werden inhaltlich verglichen. */
    public static void gleich(Object erwartet, Object tatsaechlich, String was) {
        gesamt++;
        if (tiefGleich(erwartet, tatsaechlich)) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: " + zeige(erwartet) + "  |  bekommen: " + zeige(tatsaechlich));
        }
    }

    /** Fuer Kommazahlen: Vergleich mit Toleranz. */
    public static void fastGleich(double erwartet, double tatsaechlich, String was) {
        gesamt++;
        if (Math.abs(erwartet - tatsaechlich) < 1e-6) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: " + erwartet + "  |  bekommen: " + tatsaechlich);
        }
    }

    public static void wahr(boolean bedingung, String was) {
        gesamt++;
        if (bedingung) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: true  |  bekommen: false");
        }
    }

    public static void falsch(boolean bedingung, String was) {
        wahr(!bedingung, was);
    }

    /** Prueft, dass ein Codestueck eine bestimmte Exception wirft. */
    public static void wirft(Class<? extends Throwable> typ, Runnable code, String was) {
        gesamt++;
        try {
            code.run();
            fehlgeschlagen(was, "erwartet: " + typ.getSimpleName() + " geworfen  |  bekommen: keine Exception");
        } catch (Throwable t) {
            if (typ.isInstance(t)) {
                bestanden(was);
            } else {
                fehlgeschlagen(was, "erwartet: " + typ.getSimpleName()
                        + "  |  bekommen: " + t.getClass().getSimpleName() + " (" + t.getMessage() + ")");
            }
        }
    }

    /** Schliesst die Pruefung ab, gibt eine Zusammenfassung aus und beendet das Programm. */
    public static void bericht() {
        System.out.println();
        if (FEHLER.isEmpty()) {
            System.out.println(GRUEN + "  Alle " + gesamt + " Pruefungen bestanden. Weiter zum naechsten Kapitel!" + RESET);
            System.exit(0);
        } else {
            System.out.println(ROT + "  " + FEHLER.size() + " von " + gesamt + " Pruefungen fehlgeschlagen." + RESET);
            System.out.println(GELB + "  Tipp: Nimm dir die erste rote Pruefung vor, bearbeite das TODO in src/"
                    + " und starte erneut." + RESET);
            System.out.println(GELB + "  Haengst du fest: TIPPS.md im Kapitelordner hat gestufte Hinweise." + RESET);
            System.exit(1);
        }
    }

    /** Ueberschrift fuer einen Aufgabenblock. */
    public static void abschnitt(String titel) {
        System.out.println();
        System.out.println("  " + titel);
        System.out.println("  " + "-".repeat(Math.max(4, titel.length())));
    }

    // ---------------------------------------------------------------- intern

    private static void bestanden(String was) {
        System.out.println("  " + GRUEN + "OK  " + RESET + was);
    }

    private static void fehlgeschlagen(String was, String detail) {
        FEHLER.add(was);
        System.out.println("  " + ROT + "FEHL" + RESET + " " + was);
        System.out.println("       " + GRAU + detail + RESET);
    }

    private static boolean tiefGleich(Object a, Object b) {
        if (a instanceof Object[] x && b instanceof Object[] y)   return Arrays.deepEquals(x, y);
        if (a instanceof int[] x     && b instanceof int[] y)     return Arrays.equals(x, y);
        if (a instanceof long[] x    && b instanceof long[] y)    return Arrays.equals(x, y);
        if (a instanceof double[] x  && b instanceof double[] y)  return Arrays.equals(x, y);
        if (a instanceof char[] x    && b instanceof char[] y)    return Arrays.equals(x, y);
        if (a instanceof boolean[] x && b instanceof boolean[] y) return Arrays.equals(x, y);
        return Objects.deepEquals(a, b);
    }

    private static String zeige(Object o) {
        if (o == null) return "null";
        if (o instanceof Object[] x)  return Arrays.deepToString(x);
        if (o instanceof int[] x)     return Arrays.toString(x);
        if (o instanceof long[] x)    return Arrays.toString(x);
        if (o instanceof double[] x)  return Arrays.toString(x);
        if (o instanceof char[] x)    return Arrays.toString(x);
        if (o instanceof boolean[] x) return Arrays.toString(x);
        if (o instanceof String s)    return "\"" + s + "\"";
        return o.toString();
    }
}
