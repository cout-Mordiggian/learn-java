import java.time.LocalDate;

/**
 * Abschlussprojekt, Meilenstein 1: das Datenmodell.
 *
 * Ein record ist hier richtig, weil eine Aufgabe vor allem DATEN
 * transportiert (Kapitel 10). Der Compiler erzeugt Konstruktor,
 * Zugriffsmethoden, equals, hashCode und toString.
 *
 * Was er NICHT erzeugt, ist die Frage, was ueberhaupt eine gueltige
 * Aufgabe ist. Das entscheidest du - im kompakten Konstruktor.
 */
public record Aufgabe(int id,
                      String titel,
                      Prioritaet prioritaet,
                      boolean erledigt,
                      LocalDate faellig) {

    /**
     * Kompakter Konstruktor - keine Parameterliste, kein this.x = x.
     * Er laeuft VOR der Zuweisung an die Felder, du darfst die Parameter
     * also noch pruefen und normalisieren.
     */
    public Aufgabe {
        // TODO: Titel darf nicht null/leer sein, id muss positiv sein
        //       -> IllegalArgumentException. faellig darf null sein.
    }

    /** Kopie mit erledigt = true. Records sind unveraenderlich. */
    public Aufgabe abgehakt() {
        // TODO: neues Aufgabe-Objekt zurueckgeben
        throw new UnsupportedOperationException("TODO M1: abgehakt()");
    }

    /**
     * Termin in der Vergangenheit und noch nicht erledigt.
     * Ohne Termin (faellig == null) ist eine Aufgabe nie ueberfaellig.
     */
    public boolean istUeberfaellig() {
        // TODO: LocalDate.now() und isBefore(...) - siehe README, M1
        throw new UnsupportedOperationException("TODO M1: istUeberfaellig()");
    }
}
