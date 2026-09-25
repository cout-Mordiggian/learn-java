/**
 * Kapitel 02 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static String fizzbuzz(int n) {
        // Der Fall "durch beide teilbar" MUSS zuerst geprueft werden.
        // Sonst greift schon der Fizz-Zweig und "FizzBuzz" wird nie erreicht.
        if (n % 3 == 0 && n % 5 == 0) return "FizzBuzz";
        if (n % 3 == 0) return "Fizz";
        if (n % 5 == 0) return "Buzz";
        // String.valueOf(n) sagt direkt, was passiert: Zahl -> Text.
        // "" + n funktioniert auch, wirkt aber wie ein Trick.
        return String.valueOf(n);
    }

    public static String notenText(int note) {
        // switch-Ausdruck: liefert einen Wert, kein break noetig, kein
        // versehentliches Durchfallen moeglich. Das Semikolon nach der
        // schliessenden Klammer ist Pflicht - es ist ein Ausdruck, keine Anweisung.
        return switch (note) {
            case 1 -> "sehr gut";
            case 2 -> "gut";
            case 3 -> "befriedigend";
            case 4 -> "ausreichend";
            case 5 -> "mangelhaft";
            case 6 -> "ungenuegend";
            default -> "ungueltig";
        };
    }

    public static boolean istPrimzahl(int n) {
        if (n < 2) return false;          // 0, 1 und negative Zahlen sind nie prim
        if (n == 2) return true;          // die einzige gerade Primzahl
        if (n % 2 == 0) return false;     // alle anderen geraden Zahlen raus

        // Warum reicht die Wurzel? Wenn n = a * b mit a <= b, dann ist
        // zwingend a <= sqrt(n). Einen Teiler oberhalb der Wurzel gibt es
        // also nie ohne Partner unterhalb - den haetten wir schon gefunden.
        // i * i <= n ist dasselbe wie i <= Math.sqrt(n), kommt aber ohne
        // Kommazahlen aus. Das (long) ist wichtig: Fuer n nahe Integer.MAX_VALUE
        // wuerde i * i als int ueberlaufen, negativ werden und die Schleife
        // nie korrekt beenden. In long passt das Produkt immer.
        // i += 2, weil gerade Teiler bereits ausgeschlossen sind.
        for (int i = 3; (long) i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static long fakultaet(int n) {
        // Der Akkumulator MUSS long sein. Waere er int, wuerde schon 13!
        // still ueberlaufen. Mit long ist bei 20! Schluss - 21! passt nicht mehr.
        long ergebnis = 1L;
        for (int i = 2; i <= n; i++) {
            ergebnis *= i;
        }
        return ergebnis;   // n <= 1 liefert korrekt 1, ohne Sonderfall
    }

    public static int quersumme(int n) {
        int summe = 0;
        int rest = n;                 // Parameter nicht veraendern - saubere Gewohnheit
        while (rest > 0) {
            summe += rest % 10;       // letzte Ziffer abholen
            rest /= 10;               // und abschneiden
        }
        return summe;
    }

    public static int summeVielfache(int grenze) {
        int summe = 0;
        for (int i = 1; i <= grenze; i++) {
            if (i % 3 != 0 && i % 5 != 0) continue;   // uninteressant -> naechster Durchlauf
            summe += i;
        }
        return summe;
    }

    public static String sternDreieck(int hoehe) {
        // StringBuilder statt String-Verkettung in der Schleife:
        // "s += x" in einer Schleife erzeugt bei jedem Durchlauf ein NEUES
        // String-Objekt (Strings sind unveraenderlich, Kapitel 3).
        // Bei grossen Schleifen ist das quadratischer Aufwand.
        StringBuilder sb = new StringBuilder();
        for (int zeile = 1; zeile <= hoehe; zeile++) {        // aeussere: welche Zeile
            for (int stern = 1; stern <= zeile; stern++) {    // innere: Sterne dieser Zeile
                sb.append('*');
            }
            sb.append('\n');                                  // Zeile abschliessen
        }
        return sb.toString();
        // Alternative ohne innere Schleife (seit Java 11):
        //     sb.append("*".repeat(zeile)).append('\n');
        // Kuerzer - aber hier geht es gerade ums Verschachteln.
    }
}
