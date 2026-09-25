import java.util.Arrays;

/**
 * Kapitel 03 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static String umdrehen(String text) {
        // StringBuilder.reverse() arbeitet auf dem internen Puffer und gibt
        // den Builder selbst zurueck - daher die Kette mit toString().
        return new StringBuilder(text).reverse().toString();
    }

    public static boolean istPalindrom(String text) {
        // Schritt 1: normalisieren. Wir bauen einen "sauberen" String,
        // der nur Kleinbuchstaben und Ziffern enthaelt.
        StringBuilder sauber = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sauber.append(Character.toLowerCase(c));
            }
        }

        // Schritt 2: Zwei Indizes von aussen nach innen laufen lassen.
        // Das ist sparsamer als reverse() + equals(), weil es beim ersten
        // Unterschied abbricht und keinen zweiten String erzeugt.
        int links = 0;
        int rechts = sauber.length() - 1;
        while (links < rechts) {
            if (sauber.charAt(links) != sauber.charAt(rechts)) return false;
            links++;
            rechts--;
        }
        return true;
    }

    public static int wortAnzahl(String satz) {
        String s = satz.strip();
        // Ohne diesen Sonderfall liefert "".split("\\s+") ein Array
        // mit EINEM leeren String - also faelschlich 1 statt 0.
        if (s.isEmpty()) return 0;
        // \\s+ ist ein regulaerer Ausdruck: ein oder mehr Whitespace-Zeichen
        // (Leerzeichen, Tab, Zeilenumbruch). Das + fasst Mehrfachtrenner zusammen.
        return s.split("\\s+").length;
    }

    public static int maximum(int[] werte) {
        // Startwert ist das ERSTE Element, nicht 0. Mit 0 als Startwert
        // wuerde maximum({-5,-2,-9}) faelschlich 0 liefern.
        // Alternative: Integer.MIN_VALUE als Startwert - funktioniert auch.
        // Bei einem leeren Array verhalten sich beide aber verschieden:
        // werte[0] wirft sofort eine Exception (Fehler faellt auf), MIN_VALUE
        // liefert still einen falschen Wert (Fehler bleibt verborgen).
        int max = werte[0];
        for (int i = 1; i < werte.length; i++) {
            if (werte[i] > max) max = werte[i];
        }
        return max;
    }

    public static double mittelwert(int[] werte) {
        if (werte.length == 0) return 0.0;
        // Summe in long: Schon zwei grosse int-Werte (z.B. 2 * Integer.MAX_VALUE)
        // sprengen den int-Bereich. In long passt die Summe von bis zu
        // rund 4 Milliarden int-Werten sicher.
        long summe = 0;
        for (int w : werte) summe += w;
        // (double) erzwingt Kommazahl-Division. Ohne den Cast waere
        // mittelwert({1,2}) gleich 1.0 statt 1.5.
        return (double) summe / werte.length;
    }

    public static int[] sortierteKopie(int[] werte) {
        // Arrays.sort sortiert IN PLACE - es veraendert das uebergebene Array.
        // Deshalb erst kopieren. clone() taete es auch; copyOf ist die
        // gebraeuchlichere Form, weil sie zusaetzlich die Laenge aendern kann.
        int[] kopie = Arrays.copyOf(werte, werte.length);
        Arrays.sort(kopie);
        return kopie;
    }

    public static int[][] transponiere(int[][] matrix) {
        if (matrix.length == 0) return new int[0][];
        int zeilen = matrix.length;
        int spalten = matrix[0].length;
        // Das Ergebnis hat vertauschte Dimensionen: spalten x zeilen.
        int[][] ergebnis = new int[spalten][zeilen];
        for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                ergebnis[j][i] = matrix[i][j];   // der eigentliche Trick: Indizes tauschen
            }
        }
        return ergebnis;
    }

    public static String zusammenfuegen(String[] teile, String trenner) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < teile.length; i++) {
            // Trenner VOR jedem Element ausser dem ersten. Das vermeidet
            // das nachtraegliche Abschneiden eines ueberzaehligen Trenners
            // und funktioniert auch bei leerem Array ohne Sonderfall.
            if (i > 0) sb.append(trenner);
            sb.append(teile[i]);
        }
        return sb.toString();
        // In echtem Code: String.join(trenner, teile) - eine Zeile.
    }
}
