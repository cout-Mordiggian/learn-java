/**
 * Kapitel 14 - Musterloesung: Suchen.
 */
public class Suchen {

    public static int ersteGroesserGleich(int[] sortiert, int wert) {
        // Halboffener Bereich [von, bis): "von" gehoert noch zum Suchbereich,
        // "bis" nicht mehr. Deshalb startet bis bei sortiert.length (nicht
        // length - 1) - "kein Element ist gross genug" ist ja auch eine
        // moegliche Antwort, und die lautet length.
        int von = 0;
        int bis = sortiert.length;

        // Invariante:  alles LINKS von "von" ist < wert,
        //              alles AB "bis"       ist >= wert.
        // Der Bereich dazwischen ist noch unbekannt. Er schrumpft in jedem
        // Schritt, bis von == bis - dann ist "von" genau die gesuchte Grenze.
        while (von < bis) {
            int mitte = von + (bis - von) / 2;   // ueberlaufsicher, siehe Kapitel 4
            if (sortiert[mitte] < wert) {
                von = mitte + 1;   // mitte ist zu klein -> gehoert nach links
            } else {
                bis = mitte;       // mitte ist >= wert -> koennte die Antwort sein,
                                   // deshalb NICHT mitte - 1
            }
        }
        return von;
        // Aufwand: der Bereich halbiert sich pro Schritt -> O(log n).
        // Leeres Array: die Schleife laeuft nicht, Ergebnis 0 == length. Passt.
        // Anders als die binaereSuche aus Kapitel 4 bricht diese Variante bei
        // einem Treffer NICHT ab - sonst faende sie bei Duplikaten irgendeinen
        // statt den ersten.
    }

    public static int zaehleVorkommen(int[] sortiert, int wert) {
        // In einem sortierten Array stehen alle Kopien von "wert" am Stueck:
        //   [1, 3, 3, 3, 5]
        //       ^        ^
        //  erste >= 3    erste >= 4
        // Die Anzahl ist der Abstand der beiden Grenzen. Zweimal O(log n) statt
        // einmal O(n) durchzaehlen.
        int anfang = ersteGroesserGleich(sortiert, wert);

        // Falle: wert + 1 laeuft bei Integer.MAX_VALUE ueber und wird zur
        // kleinsten int-Zahl. Groesser als MAX_VALUE kann aber nichts sein -
        // das Ende ist dann einfach das Array-Ende.
        int ende = (wert == Integer.MAX_VALUE)
                ? sortiert.length
                : ersteGroesserGleich(sortiert, wert + 1);
        return ende - anfang;
    }
}
