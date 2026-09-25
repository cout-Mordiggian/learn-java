/**
 * Kapitel 13 - Teil B: Fehlersuche.
 * Pruefen:  ./lerne.sh 13
 *
 * Jede Methode unten kompiliert - und jede enthaelt GENAU EINEN klassischen
 * Anfaengerfehler aus Abschnitt 13.7. Das Javadoc beschreibt jeweils, was
 * die Methode tun SOLL; die Tests pruefen dieses richtige Verhalten.
 *
 * Vorgehen (Abschnitt 13.6): roten Test lesen -> Fehler reproduzieren ->
 * eingrenzen -> Hypothese -> pruefen -> beheben. Aendere so wenig wie moeglich:
 * Meist reicht ein einziges Zeichen oder eine einzige Zeile.
 */
public class Fehlerhaft {

    /**
     * Durchschnitt aller Zahlen, mit Nachkommastellen.
     * durchschnitt({1, 2}) -> 1.5,  durchschnitt({2, 4, 6}) -> 4.0
     * Das Array enthaelt mindestens ein Element.
     */
    public static double durchschnitt(int[] zahlen) {
        int summe = 0;
        for (int z : zahlen) {
            summe += z;
        }
        return summe / zahlen.length;
    }

    /**
     * Zaehlt, wie viele Zahlen echt groesser als die Grenze sind.
     * zaehleGroesser({1, 5, 3, 8}, 4) -> 2,  zaehleGroesser({}, 0) -> 0
     */
    public static int zaehleGroesser(int[] zahlen, int grenze) {
        int anzahl = 0;
        for (int i = 0; i <= zahlen.length; i++) {
            if (zahlen[i] > grenze) {
                anzahl++;
            }
        }
        return anzahl;
    }

    /**
     * true, wenn der Benutzername genau "admin" lautet (Gross- und
     * Kleinschreibung zaehlt). Fuer null kommt false heraus.
     * istAdmin("admin") -> true,  istAdmin("Admin") -> false
     */
    public static boolean istAdmin(String name) {
        return name == "admin";
    }

    /**
     * Name des Wochentags: 1 -> "Montag" ... 7 -> "Sonntag",
     * jede andere Zahl -> "ungueltig".
     */
    public static String wochentag(int tag) {
        String name;
        switch (tag) {
            case 1:
                name = "Montag";
                break;
            case 2:
                name = "Dienstag";
                break;
            case 3:
                name = "Mittwoch";
                break;
            case 4:
                name = "Donnerstag";
                break;
            case 5:
                name = "Freitag";
            case 6:
                name = "Samstag";
                break;
            case 7:
                name = "Sonntag";
                break;
            default:
                name = "ungueltig";
        }
        return name;
    }

    /**
     * Entfernt Leerraum am Anfang und Ende und wandelt in Kleinbuchstaben um.
     * normalisiere("  Hallo Welt ") -> "hallo welt"
     */
    public static String normalisiere(String eingabe) {
        eingabe.strip();
        return eingabe.toLowerCase();
    }

    /**
     * Begrenzt einen Wert nach oben: der Wert selbst, hoechstens aber die Grenze.
     * hoechstens(3, 10) -> 3,  hoechstens(15, 10) -> 10
     */
    public static int hoechstens(int wert, int grenze) {
        int ergebnis = wert;
        if (wert > grenze); {
            ergebnis = grenze;
        }
        return ergebnis;
    }
}
