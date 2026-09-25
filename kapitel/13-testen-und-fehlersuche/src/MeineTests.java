/**
 * Kapitel 13 - Teil A: Tests schreiben, die Fehler finden.
 * Pruefen:  ./lerne.sh 13
 *
 * Deine Aufgabe: Schreibe in alleBestanden eigene Pruefungen gegen den
 * uebergebenen Rabattrechner r. Die Regeln stehen in Rabattrechner.java.
 *
 * tests/Tests.java ruft deine Methode dann achtmal auf: einmal mit der
 * korrekten Implementierung (dort muss true herauskommen) und einmal mit
 * jedem der sieben Mutanten (dort muss false herauskommen - deine Tests
 * muessen den Fehler bemerken).
 *
 * Spielregeln:
 *  - Pruefe nur ueber r.endpreisCent(...). Kein instanceof, kein Blick auf
 *    den Klassennamen - das waere Schummeln und in echt nutzlos.
 *  - Schau NICHT in Kandidaten.java, bevor alles gruen ist.
 *  - Rechne die erwarteten Werte selbst aus (Stift und Papier), nicht mit
 *    der Implementierung, die du gerade testest.
 */
public class MeineTests {

    /** Zaehlt die fehlgeschlagenen Pruefungen des aktuellen Durchlaufs. */
    private static int fehler = 0;

    /**
     * Fuehrt alle deine Pruefungen gegen r aus.
     *
     * @return true, wenn jede Pruefung gestimmt hat
     */
    static boolean alleBestanden(Rabattrechner r) {
        fehler = 0;

        // Beispiel fuer einen Normalfall (Vorbereiten - Ausfuehren - Pruefen):
        gleich(300, r.endpreisCent(100, 3), "3 Stueck a 100 Cent, kein Rabatt");

        // TODO: Weitere Pruefungen. Denk an Abschnitt 13.3:
        //       Aequivalenzklassen und Grenzwerte - genau an der Grenze
        //       und knapp daneben, 0, negative Werte, sehr grosse Werte,
        //       und eine Rechnung, bei der die Rundung eine Rolle spielt.

        // TODO: Erwartete Exception pruefen. Das Muster:
        //
        //   try {
        //       r.endpreisCent(..., ...);
        //       fehlschlag("... haette eine IllegalArgumentException werfen muessen");
        //   } catch (IllegalArgumentException e) {
        //       // erwartet - diese Pruefung ist bestanden
        //   }

        return fehler == 0;
    }

    /**
     * Vergleicht zwei Werte. Bei Abweichung gibt es eine Meldung, damit du
     * siehst, WELCHE deiner Pruefungen angeschlagen hat.
     */
    private static boolean gleich(long erwartet, long tatsaechlich, String was) {
        if (erwartet == tatsaechlich) {
            return true;
        }
        fehlschlag(was + ": erwartet " + erwartet + ", bekommen " + tatsaechlich);
        return false;
    }

    /** Meldet eine fehlgeschlagene Pruefung. */
    private static void fehlschlag(String meldung) {
        fehler++;
        System.out.println("       [deine Pruefung] " + meldung);
    }
}
