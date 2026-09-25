/**
 * Kapitel 13 - Musterloesung Teil A mit Erklaerungen.
 *
 * Aufbau: Jede Pruefung ist eine Zeile nach dem Muster
 *   Vorbereiten (die Eingabe waehlen) - Ausfuehren (r.endpreisCent) - Pruefen (gleich).
 * Die erwarteten Werte sind von Hand ausgerechnet, der Rechenweg steht
 * jeweils im Kommentar. So kann jemand anderes (oder du in drei Monaten)
 * jede Zahl nachvollziehen.
 */
public class MeineTests {

    private static int fehler = 0;

    static boolean alleBestanden(Rabattrechner r) {
        fehler = 0;

        // --- Aequivalenzklassen: je ein typischer Vertreter pro Staffel ----
        gleich(300, r.endpreisCent(100, 3), "3 Stueck: kein Rabatt");
        // 20 * 100 = 2000, 5 % = 100
        gleich(1900, r.endpreisCent(100, 20), "20 Stueck: 5 %");
        // 100 * 100 = 10000, 10 % = 1000
        gleich(9000, r.endpreisCent(100, 100), "100 Stueck: 10 %");

        // --- Grenzwerte: genau an jeder Grenze UND knapp darunter ----------
        // Die meisten Fehler sitzen an den Raendern (> statt >=).
        gleich(100, r.endpreisCent(100, 1), "1 Stueck");
        gleich(900, r.endpreisCent(100, 9), "9 Stueck: knapp unter 10, noch kein Rabatt");
        // 1000, 5 % = 50
        gleich(950, r.endpreisCent(100, 10), "genau 10 Stueck: schon 5 %");
        // 4900, 5 % = 245
        gleich(4655, r.endpreisCent(100, 49), "49 Stueck: knapp unter 50, noch 5 %");
        // 5000, 10 % = 500
        gleich(4500, r.endpreisCent(100, 50), "genau 50 Stueck: schon 10 %");

        // --- Null: leere Bestellung und Gratisartikel sind erlaubt ---------
        // Wirft die Implementierung hier unerwartet, ist das ebenfalls ein
        // Fehlschlag. Ohne dieses try/catch wuerde die Exception aus unserer
        // Methode herausfliegen - das "entlarvt" den Fehler zwar auch, aber
        // ohne verstaendliche Meldung.
        try {
            gleich(0, r.endpreisCent(500, 0), "0 Stueck kosten 0");
        } catch (IllegalArgumentException e) {
            fehlschlag("0 Stueck: unerwartete IllegalArgumentException (" + e.getMessage() + ")");
        }
        gleich(0, r.endpreisCent(0, 20), "Preis 0 bleibt 0, auch mit Rabatt");

        // --- Rundung: Faelle, in denen der Rabatt KEIN ganzer Cent ist -----
        // 1990, 5 % = 99,5 -> 99 abgerundet -> 1891 (das Beispiel aus der Spezifikation)
        gleich(1891, r.endpreisCent(199, 10), "Rundung: Rabatt 99,5 wird zu 99");
        // 70, 5 % = 3,5 -> 3 -> 67
        gleich(67, r.endpreisCent(7, 10), "Rundung: Rabatt 3,5 wird zu 3");

        // --- Grosse Werte: jenseits von Integer.MAX_VALUE (2.147.483.647) --
        // 5.000.000 * 500 = 2.500.000.000, 10 % = 250.000.000
        gleich(2_250_000_000L, r.endpreisCent(5_000_000, 500), "Gesamtpreis groesser als int");
        gleich(3_000_000_000L, r.endpreisCent(3_000_000_000L, 1), "Stueckpreis groesser als int");

        // --- Ungueltige Eingaben: BEIDE Parameter einzeln pruefen ----------
        // Wer nur einen negativen Fall testet, merkt nicht, wenn die Pruefung
        // fuer den anderen Parameter fehlt.
        try {
            r.endpreisCent(-1, 5);
            fehlschlag("negativer Preis haette eine IllegalArgumentException werfen muessen");
        } catch (IllegalArgumentException e) {
            // erwartet
        }
        try {
            r.endpreisCent(100, -1);
            fehlschlag("negative Menge haette eine IllegalArgumentException werfen muessen");
        } catch (IllegalArgumentException e) {
            // erwartet
        }

        // Nicht vorzeitig mit return aussteigen: Alle Pruefungen laufen durch,
        // damit du ALLE Abweichungen auf einmal siehst, nicht nur die erste.
        return fehler == 0;
    }

    private static boolean gleich(long erwartet, long tatsaechlich, String was) {
        if (erwartet == tatsaechlich) {
            return true;
        }
        fehlschlag(was + ": erwartet " + erwartet + ", bekommen " + tatsaechlich);
        return false;
    }

    private static void fehlschlag(String meldung) {
        fehler++;
        System.out.println("       [deine Pruefung] " + meldung);
    }
}
