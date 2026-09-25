public class Tests {
    public static void main(String[] args) {
        Pruef.abschnitt("Aufgabe 1: umdrehen");
        Pruef.gleich("cba", Aufgaben.umdrehen("abc"), "umdrehen(\"abc\")");
        Pruef.gleich("", Aufgaben.umdrehen(""), "leerer String");
        Pruef.gleich("a", Aufgaben.umdrehen("a"), "ein Zeichen");
        Pruef.gleich("tteohcS", Aufgaben.umdrehen("Schoett"), "gemischt");

        Pruef.abschnitt("Aufgabe 2: istPalindrom");
        Pruef.wahr(Aufgaben.istPalindrom(""), "leerer String");
        Pruef.wahr(Aufgaben.istPalindrom("a"), "ein Zeichen");
        Pruef.wahr(Aufgaben.istPalindrom("otto"), "otto");
        Pruef.wahr(Aufgaben.istPalindrom("Ein Esel lese nie"), "Satz mit Leerzeichen");
        Pruef.wahr(Aufgaben.istPalindrom("A man, a plan, a canal: Panama"), "mit Satzzeichen");
        Pruef.falsch(Aufgaben.istPalindrom("Hallo"), "Hallo ist keins");
        Pruef.falsch(Aufgaben.istPalindrom("abca"), "abca: aussen gleich, innen nicht");
        Pruef.wahr(Aufgaben.istPalindrom("12321"), "Zahlenpalindrom");

        Pruef.abschnitt("Aufgabe 3: wortAnzahl");
        Pruef.gleich(0, Aufgaben.wortAnzahl(""), "leer");
        Pruef.gleich(0, Aufgaben.wortAnzahl("   "), "nur Leerzeichen");
        Pruef.gleich(1, Aufgaben.wortAnzahl("Hallo"), "ein Wort");
        Pruef.gleich(2, Aufgaben.wortAnzahl("Hallo Welt"), "zwei Woerter");
        Pruef.gleich(2, Aufgaben.wortAnzahl("  viel   Abstand  "), "mehrfacher Abstand");
        Pruef.gleich(3, Aufgaben.wortAnzahl("a\tb\nc"), "Tab und Umbruch als Trenner");

        Pruef.abschnitt("Aufgabe 4: maximum");
        Pruef.gleich(9, Aufgaben.maximum(new int[]{3, 9, 2}), "positive Zahlen");
        Pruef.gleich(-2, Aufgaben.maximum(new int[]{-5, -2, -9}), "nur negative Zahlen");
        Pruef.gleich(7, Aufgaben.maximum(new int[]{7}), "ein Element");
        Pruef.gleich(10, Aufgaben.maximum(new int[]{1, 2, 10}), "Maximum am Ende");
        Pruef.gleich(10, Aufgaben.maximum(new int[]{10, 2, 1}), "Maximum am Anfang");

        Pruef.abschnitt("Aufgabe 5: mittelwert");
        Pruef.fastGleich(0.0, Aufgaben.mittelwert(new int[]{}), "leer");
        Pruef.fastGleich(1.5, Aufgaben.mittelwert(new int[]{1, 2}), "1 und 2");
        Pruef.fastGleich(2.0, Aufgaben.mittelwert(new int[]{1, 2, 3}), "1,2,3");
        Pruef.fastGleich(-1.0, Aufgaben.mittelwert(new int[]{-3, 1}), "negativ");
        Pruef.fastGleich(Integer.MAX_VALUE, Aufgaben.mittelwert(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}),
                "grosse Werte: Summe laeuft nicht ueber");

        Pruef.abschnitt("Aufgabe 6: sortierteKopie");
        int[] original = {3, 1, 2};
        int[] sortiert = Aufgaben.sortierteKopie(original);
        Pruef.gleich(new int[]{1, 2, 3}, sortiert, "Ergebnis ist sortiert");
        Pruef.gleich(new int[]{3, 1, 2}, original, "Original ist unveraendert");
        Pruef.gleich(new int[]{}, Aufgaben.sortierteKopie(new int[]{}), "leeres Array");

        Pruef.abschnitt("Aufgabe 7: transponiere");
        Pruef.gleich(new int[][]{{1, 4}, {2, 5}, {3, 6}},
                Aufgaben.transponiere(new int[][]{{1, 2, 3}, {4, 5, 6}}), "2x3 -> 3x2");
        Pruef.gleich(new int[][]{{1}}, Aufgaben.transponiere(new int[][]{{1}}), "1x1");
        Pruef.gleich(new int[][]{}, Aufgaben.transponiere(new int[][]{}), "leere Matrix");

        Pruef.abschnitt("Aufgabe 8: zusammenfuegen");
        Pruef.gleich("a-b-c", Aufgaben.zusammenfuegen(new String[]{"a", "b", "c"}, "-"), "drei Teile");
        Pruef.gleich("a", Aufgaben.zusammenfuegen(new String[]{"a"}, "-"), "ein Teil");
        Pruef.gleich("", Aufgaben.zusammenfuegen(new String[]{}, "-"), "kein Teil");
        Pruef.gleich("a, b", Aufgaben.zusammenfuegen(new String[]{"a", "b"}, ", "), "mehrzeichiger Trenner");

        Pruef.bericht();
    }
}
