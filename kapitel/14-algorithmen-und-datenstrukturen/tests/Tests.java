import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class Tests {

    public static void main(String[] args) {

        // ------------------------------------------------------------ Sortieren
        Pruef.abschnitt("Aufgabe 1: insertionSort (in place)");
        insertion(new int[]{5, 2, 9, 1, 7}, "unsortiert");
        insertion(new int[]{}, "leeres Array");
        insertion(new int[]{42}, "ein Element");
        insertion(new int[]{2, 1}, "zwei Elemente");
        insertion(new int[]{1, 2, 3, 4, 5}, "schon sortiert");
        insertion(new int[]{5, 4, 3, 2, 1}, "umgekehrt sortiert");
        insertion(new int[]{3, 1, 3, 2, 1, 3}, "mit Duplikaten");
        insertion(new int[]{7, 7, 7, 7}, "alle gleich");
        insertion(new int[]{0, -3, 8, -10, 2}, "negative Zahlen");
        insertion(zufall(new Random(42), 5_000), "5.000 Zufallszahlen (Seed 42) wie Arrays.sort");

        Pruef.abschnitt("Aufgabe 2: mergeSort (neues Array, Original bleibt)");
        merge(new int[]{5, 2, 9, 1, 7}, "unsortiert");
        merge(new int[]{}, "leeres Array");
        merge(new int[]{42}, "ein Element");
        merge(new int[]{2, 1}, "zwei Elemente");
        merge(new int[]{1, 2, 3, 4, 5}, "schon sortiert");
        merge(new int[]{6, 5, 4, 3, 2, 1}, "umgekehrt sortiert, gerade Laenge");
        merge(new int[]{7, 6, 5, 4, 3, 2, 1}, "umgekehrt sortiert, ungerade Laenge");
        merge(new int[]{3, 1, 3, 2, 1, 3}, "mit Duplikaten");
        merge(new int[]{0, -3, 8, -10, 2}, "negative Zahlen");
        merge(zufall(new Random(42), 100_000), "100.000 Zufallszahlen (Seed 42) wie Arrays.sort");
        sicher("mergeSort: Original bleibt unveraendert", () -> {
            int[] original = {4, 1, 3, 2};
            Sortieren.mergeSort(original);
            Pruef.gleich(new int[]{4, 1, 3, 2}, original, "mergeSort: Original bleibt unveraendert");
        });
        sicher("mergeSort: liefert ein neues Array", () -> {
            int[] original = {1};
            Pruef.wahr(Sortieren.mergeSort(original) != original,
                    "mergeSort: liefert ein NEUES Array, auch bei einem Element");
        });

        Pruef.abschnitt("Aufgabe 3: istSortiert");
        sicher("istSortiert", () -> {
            Pruef.wahr(Sortieren.istSortiert(new int[]{}), "leeres Array ist sortiert");
            Pruef.wahr(Sortieren.istSortiert(new int[]{7}), "ein Element ist sortiert");
            Pruef.wahr(Sortieren.istSortiert(new int[]{1, 2, 2, 5}), "Duplikate sind erlaubt");
            Pruef.wahr(Sortieren.istSortiert(new int[]{-5, -1, 0}), "negative Zahlen");
            Pruef.falsch(Sortieren.istSortiert(new int[]{3, 1}), "{3, 1} ist nicht sortiert");
            Pruef.falsch(Sortieren.istSortiert(new int[]{1, 3, 2, 4}), "Fehler in der Mitte");
            Pruef.falsch(Sortieren.istSortiert(new int[]{1, 2, 3, 0}), "Fehler ganz am Ende");
            Pruef.falsch(Sortieren.istSortiert(new int[]{9, 1, 2, 3}), "Fehler ganz am Anfang");
        });

        // ------------------------------------------------------------ Suchen
        Pruef.abschnitt("Aufgabe 4: ersteGroesserGleich (binaere Suche nach einer Grenze)");
        int[] s = {1, 3, 3, 3, 5};
        sicher("ersteGroesserGleich", () -> {
            Pruef.gleich(1, Suchen.ersteGroesserGleich(s, 3), "{1,3,3,3,5}, 3 -> 1 (die ERSTE 3)");
            Pruef.gleich(4, Suchen.ersteGroesserGleich(s, 4), "4 -> 4 (Wert fehlt, die 5 folgt)");
            Pruef.gleich(0, Suchen.ersteGroesserGleich(s, 1), "1 -> 0 (erstes Element)");
            Pruef.gleich(0, Suchen.ersteGroesserGleich(s, -7), "kleiner als alle -> 0");
            Pruef.gleich(4, Suchen.ersteGroesserGleich(s, 5), "5 -> 4 (letztes Element)");
            Pruef.gleich(5, Suchen.ersteGroesserGleich(s, 6), "groesser als alle -> length");
            Pruef.gleich(0, Suchen.ersteGroesserGleich(new int[]{}, 3), "leeres Array -> 0");
            Pruef.gleich(0, Suchen.ersteGroesserGleich(new int[]{8}, 8), "ein Element, gefunden");
            Pruef.gleich(1, Suchen.ersteGroesserGleich(new int[]{8}, 9), "ein Element, zu klein");
            Pruef.gleich(0, Suchen.ersteGroesserGleich(new int[]{2, 2, 2, 2}, 2), "nur Duplikate");
        });
        sicher("ersteGroesserGleich: grosses Array", () -> {
            int[] gerade = new int[100_000];
            for (int i = 0; i < gerade.length; i++) gerade[i] = 2 * i;   // 0, 2, 4, ...
            boolean alleRichtig = true;
            for (int wert = -3; wert <= 200_003; wert += 997) {
                // erste gerade Zahl >= wert steht an Index "wert / 2 aufgerundet"
                int erwartet = wert <= 0 ? 0 : Math.min(gerade.length, (wert + 1) / 2);
                if (Suchen.ersteGroesserGleich(gerade, wert) != erwartet) alleRichtig = false;
            }
            Pruef.wahr(alleRichtig, "100.000 gerade Zahlen, rund 200 verschiedene Suchwerte");
        });

        Pruef.abschnitt("Aufgabe 5: zaehleVorkommen");
        sicher("zaehleVorkommen", () -> {
            Pruef.gleich(3, Suchen.zaehleVorkommen(s, 3), "{1,3,3,3,5}, 3 -> 3");
            Pruef.gleich(1, Suchen.zaehleVorkommen(s, 5), "5 -> 1 (am Ende)");
            Pruef.gleich(1, Suchen.zaehleVorkommen(s, 1), "1 -> 1 (am Anfang)");
            Pruef.gleich(0, Suchen.zaehleVorkommen(s, 4), "4 -> 0 (Luecke)");
            Pruef.gleich(0, Suchen.zaehleVorkommen(s, 99), "99 -> 0 (groesser als alle)");
            Pruef.gleich(0, Suchen.zaehleVorkommen(new int[]{}, 1), "leeres Array -> 0");
            Pruef.gleich(4, Suchen.zaehleVorkommen(new int[]{2, 2, 2, 2}, 2), "nur Duplikate");
            Pruef.gleich(2, Suchen.zaehleVorkommen(
                    new int[]{1, Integer.MAX_VALUE, Integer.MAX_VALUE}, Integer.MAX_VALUE),
                    "Integer.MAX_VALUE -> kein Ueberlauf");
        });

        // ------------------------------------------------------------ MeineListe
        Pruef.abschnitt("Aufgabe 6: MeineListe (eigene ArrayList)");
        sicher("MeineListe: Grundfunktionen", () -> {
            MeineListe<String> l = new MeineListe<>();
            Pruef.gleich(0, l.size(), "neue Liste hat size 0");
            Pruef.gleich("[]", l.toString(), "toString der leeren Liste");
            l.add("a");
            l.add("b");
            l.add("c");
            Pruef.gleich(3, l.size(), "size nach drei add");
            Pruef.gleich("a", l.get(0), "get(0)");
            Pruef.gleich("c", l.get(2), "get(2)");
            Pruef.gleich("[a, b, c]", l.toString(), "toString wie ArrayList");
        });
        sicher("MeineListe: Wachstum", () -> {
            MeineListe<Integer> l = new MeineListe<>();
            Pruef.gleich(4, l.kapazitaet(), "Anfangskapazitaet 4");
            for (int i = 0; i < 4; i++) l.add(i);
            Pruef.gleich(4, l.kapazitaet(), "4 Elemente passen ohne Wachsen");
            l.add(4);
            Pruef.gleich(8, l.kapazitaet(), "das 5. Element verdoppelt auf 8");
            for (int i = 5; i < 9; i++) l.add(i);
            Pruef.gleich(16, l.kapazitaet(), "das 9. Element verdoppelt auf 16");
            for (int i = 9; i < 100; i++) l.add(i);
            Pruef.gleich(100, l.size(), "100 Elemente - weit ueber der Anfangskapazitaet");
            Pruef.gleich(128, l.kapazitaet(), "Kapazitaet nach 100 Elementen: 128");
            boolean alleDa = true;
            for (int i = 0; i < 100; i++) {
                if (l.get(i) != i) alleDa = false;   // Integer != int packt aus - hier ok
            }
            Pruef.wahr(alleDa, "alle 100 Werte in der richtigen Reihenfolge");
        });
        sicher("MeineListe: set", () -> {
            MeineListe<String> l = liste("a", "b", "c");
            Pruef.gleich("b", l.set(1, "X"), "set gibt den alten Wert zurueck");
            Pruef.gleich("X", l.get(1), "get nach set");
            Pruef.gleich(3, l.size(), "set aendert die Groesse nicht");
        });
        sicher("MeineListe: remove", () -> {
            MeineListe<String> l = liste("a", "b", "c", "d", "e");
            Pruef.gleich("b", l.remove(1), "remove(1) gibt das entfernte Element zurueck");
            Pruef.gleich("[a, c, d, e]", l.toString(), "remove(1): der Rest rueckt nach links");
            Pruef.gleich(4, l.size(), "size nach remove");
            Pruef.gleich("a", l.remove(0), "remove(0): das erste");
            Pruef.gleich("e", l.remove(l.size() - 1), "remove(size-1): das letzte");
            Pruef.gleich("[c, d]", l.toString(), "Liste nach drei remove");
            l.add("x");
            Pruef.gleich("[c, d, x]", l.toString(), "add nach remove haengt richtig an");
        });
        sicher("MeineListe: Index-Pruefung", () -> {
            MeineListe<String> leer = new MeineListe<>();
            Pruef.wirft(IndexOutOfBoundsException.class, () -> leer.get(0), "get(0) auf leerer Liste");
            MeineListe<String> l = liste("a");
            Pruef.wirft(IndexOutOfBoundsException.class, () -> l.get(1),
                    "get(1) bei size 1 - obwohl das Array dort einen Platz hat!");
            Pruef.wirft(IndexOutOfBoundsException.class, () -> l.get(-1), "get(-1)");
            Pruef.wirft(IndexOutOfBoundsException.class, () -> l.set(1, "x"), "set(1, ...) bei size 1");
            Pruef.wirft(IndexOutOfBoundsException.class, () -> l.remove(1), "remove(1) bei size 1");
            Pruef.gleich(1, l.size(), "nach den Fehlversuchen ist size immer noch 1");
        });
        sicher("MeineListe: contains", () -> {
            MeineListe<String> l = liste("Anna", "Bert");
            Pruef.wahr(l.contains("Bert"), "contains findet ein Element");
            Pruef.wahr(l.contains(new String("Anna")),
                    "contains vergleicht mit equals, nicht mit == (anderes String-Objekt)");
            Pruef.falsch(l.contains("Cem"), "contains: nicht enthalten");
            Pruef.falsch(l.contains(null), "contains(null) auf Liste ohne null");
            l.remove(1);
            Pruef.falsch(l.contains("Bert"), "nach remove nicht mehr enthalten");
            l.add(null);
            Pruef.wahr(l.contains(null), "null als Element ist erlaubt und wird gefunden");
            Pruef.gleich("[Anna, null]", l.toString(), "toString mit null");
            MeineListe<Integer> zahlen = new MeineListe<>();
            zahlen.add(1000);
            Pruef.wahr(zahlen.contains(1000), "Integer 1000: equals statt == (kein Cache ueber 127)");
        });

        // ------------------------------------------------------------ Stapel
        Pruef.abschnitt("Aufgabe 7: Stapel (verkettete Liste, LIFO)");
        sicher("Stapel: leer", () -> {
            Stapel<Integer> st = new Stapel<>();
            Pruef.wahr(st.istLeer(), "neuer Stapel ist leer");
            Pruef.gleich(0, st.groesse(), "neuer Stapel hat groesse 0");
            Pruef.wirft(NoSuchElementException.class, st::pop, "pop auf leerem Stapel");
            Pruef.wirft(NoSuchElementException.class, st::peek, "peek auf leerem Stapel");
        });
        sicher("Stapel: push, peek, pop", () -> {
            Stapel<Integer> st = new Stapel<>();
            st.push(1);
            st.push(2);
            st.push(3);
            Pruef.falsch(st.istLeer(), "nach push nicht mehr leer");
            Pruef.gleich(3, st.groesse(), "groesse 3");
            Pruef.gleich(3, st.peek(), "peek zeigt das zuletzt hineingelegte");
            Pruef.gleich(3, st.groesse(), "peek entfernt nichts");
            Pruef.gleich(3, st.pop(), "pop 1: 3");
            Pruef.gleich(2, st.pop(), "pop 2: 2");
            Pruef.gleich(1, st.groesse(), "groesse nach zwei pop");
            Pruef.gleich(1, st.pop(), "pop 3: 1");
            Pruef.wahr(st.istLeer(), "nach drei pop wieder leer");
            Pruef.wirft(NoSuchElementException.class, st::pop, "pop auf leer gewordenem Stapel");
            st.push(7);
            Pruef.gleich(7, st.pop(), "danach funktioniert push/pop wieder");
        });
        sicher("Stapel: viele Elemente", () -> {
            Stapel<String> st = new Stapel<>();
            for (int i = 0; i < 10_000; i++) st.push("e" + i);
            Pruef.gleich(10_000, st.groesse(), "10.000 push");
            boolean reihenfolge = true;
            for (int i = 9_999; i >= 0; i--) {
                if (!("e" + i).equals(st.pop())) reihenfolge = false;
            }
            Pruef.wahr(reihenfolge, "10.000 pop in umgekehrter Reihenfolge");
            Pruef.wahr(st.istLeer(), "danach leer");
        });

        // ------------------------------------------------------------ Anwendungen
        Pruef.abschnitt("Aufgabe 8a: klammernKorrekt");
        klammern("", true, "leerer String");
        klammern("()", true, "()");
        klammern("([]{})", true, "([]{})");
        klammern("{[()()]}", true, "{[()()]} geschachtelt");
        klammern("a(b[c]d)e", true, "andere Zeichen werden ignoriert");
        klammern("keine Klammern", true, "gar keine Klammern");
        klammern("([)]", false, "([)] - gleich viele, aber ueberkreuzt");
        klammern("(]", false, "(] - falsche Sorte");
        klammern("((", false, "(( - nicht geschlossen");
        klammern("))", false, ")) - nie geoeffnet");
        klammern(")(", false, ")( - falsche Reihenfolge");
        klammern("{[}", false, "{[} - innen nicht geschlossen");

        Pruef.abschnitt("Aufgabe 8b: umkehren");
        sicher("umkehren", () -> {
            Pruef.gleich("", Anwendungen.umkehren(""), "leerer String");
            Pruef.gleich("x", Anwendungen.umkehren("x"), "ein Zeichen");
            Pruef.gleich("cba", Anwendungen.umkehren("abc"), "abc -> cba");
            Pruef.gleich("ottO", Anwendungen.umkehren("Otto"), "Otto -> ottO");
            Pruef.gleich("!tleW ollaH", Anwendungen.umkehren("Hallo Welt!"), "mit Leerzeichen");
        });

        Pruef.bericht();
    }

    // ================================================================ Helfer

    /**
     * Fuehrt einen Pruefblock aus. Wirft dein Code unterwegs eine Exception
     * (typisch: ein Index daneben), wird das als FEHL gemeldet - mit Datei und
     * Zeile - und die restlichen Pruefungen laufen trotzdem weiter.
     */
    private static void sicher(String was, Runnable block) {
        try {
            block.run();
        } catch (RuntimeException | StackOverflowError e) {
            Pruef.gleich("keine Exception", beschreibe(e), was);
        }
    }

    private static String beschreibe(Throwable e) {
        String ort = "";
        for (StackTraceElement stelle : e.getStackTrace()) {
            String klasse = stelle.getClassName();
            String datei = stelle.getFileName();
            if (datei != null && !datei.equals("Tests.java") && !datei.equals("Pruef.java")
                    && !klasse.startsWith("java.") && !klasse.startsWith("jdk.")) {
                ort = "  bei " + datei + ":" + stelle.getLineNumber();
                break;
            }
        }
        return e.getClass().getSimpleName() + ": " + e.getMessage() + ort;
    }

    private static void insertion(int[] eingabe, String was) {
        int[] erwartet = eingabe.clone();
        Arrays.sort(erwartet);
        int[] a = eingabe.clone();
        sicher("insertionSort: " + was, () -> {
            Sortieren.insertionSort(a);
            // Geprueft wird DASSELBE Array, das wir uebergeben haben: in place.
            Pruef.gleich(erwartet, a, "insertionSort: " + was);
        });
    }

    private static void merge(int[] eingabe, String was) {
        int[] erwartet = eingabe.clone();
        Arrays.sort(erwartet);
        int[] a = eingabe.clone();
        sicher("mergeSort: " + was, () -> {
            int[] ergebnis = Sortieren.mergeSort(a);
            boolean originalHeil = Arrays.equals(a, eingabe);
            Pruef.gleich(erwartet, ergebnis, "mergeSort: " + was);
            if (!originalHeil) {
                Pruef.gleich(Arrays.toString(eingabe), Arrays.toString(a),
                        "mergeSort: " + was + " - das Original wurde veraendert");
            }
        });
    }

    private static void klammern(String eingabe, boolean erwartet, String was) {
        sicher("klammernKorrekt: " + was, () ->
                Pruef.gleich(erwartet, Anwendungen.klammernKorrekt(eingabe), "klammernKorrekt: " + was));
    }

    private static MeineListe<String> liste(String... werte) {
        MeineListe<String> l = new MeineListe<>();
        for (String w : werte) l.add(w);
        return l;
    }

    private static int[] zufall(Random zufall, int n) {
        int[] a = new int[n];
        // -1000 .. 1000: viele Duplikate und negative Zahlen
        for (int i = 0; i < n; i++) a[i] = zufall.nextInt(2001) - 1000;
        return a;
    }
}
