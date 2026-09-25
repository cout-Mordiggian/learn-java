/**
 * Kapitel 14 - Musterloesung: Anwendungen des Stapels.
 */
public class Anwendungen {

    public static boolean klammernKorrekt(String s) {
        // Die Idee: Die zuletzt geoeffnete Klammer muss als erste geschlossen
        // werden - "zuletzt rein, zuerst raus" ist genau ein Stapel.
        // Nur zu zaehlen reicht NICHT: "([)]" hat von jeder Sorte gleich viele
        // offene und schliessende Klammern und ist trotzdem falsch.
        Stapel<Character> offen = new Stapel<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                offen.push(c);                        // Autoboxing char -> Character
            } else if (c == ')' || c == ']' || c == '}') {
                if (offen.istLeer()) return false;    // schliessend ohne passende offene
                char zuletztOffen = offen.pop();      // Auto-Unboxing
                if (!passen(zuletztOffen, c)) return false;
            }
            // alle anderen Zeichen interessieren nicht
        }
        // Am Ende darf nichts mehr offen sein: "((" ist nicht korrekt.
        return offen.istLeer();
        // Aufwand: O(n) Zeit, schlimmstenfalls O(n) Speicher ("((((...").
    }

    private static boolean passen(char auf, char zu) {
        return (auf == '(' && zu == ')')
                || (auf == '[' && zu == ']')
                || (auf == '{' && zu == '}');
    }

    public static String umkehren(String s) {
        // Alles hineinlegen, alles wieder herausnehmen: Ein Stapel dreht die
        // Reihenfolge um. (StringBuilder.reverse() kann das auch - hier geht es
        // darum, den Stapel zu benutzen.)
        Stapel<Character> stapel = new Stapel<>();
        for (int i = 0; i < s.length(); i++) {
            stapel.push(s.charAt(i));
        }
        StringBuilder sb = new StringBuilder(s.length());
        while (!stapel.istLeer()) {
            sb.append(stapel.pop());
        }
        return sb.toString();
        // Aufwand: O(n). StringBuilder statt "ergebnis += ..." - das waere
        // O(n^2), weil jedes += einen neuen String kopiert (Kapitel 3).
    }
}
