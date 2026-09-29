/**
 * Kapitel 12: ein Singleton fuer viele Threads (README 12.3).
 * Pruefen:  ./lerne.sh 12
 *
 * Das Singleton selbst ist schon fertig: Ein enum mit genau einer Konstante
 * HAT genau eine Instanz, dafuer sorgt die JVM (README 10.9). Deine Aufgabe
 * ist der Zaehler.
 *
 *   IdGenerator.INSTANZ.naechsteId()   -> 1
 *   IdGenerator.INSTANZ.naechsteId()   -> 2
 *
 * Achtung: Die Tests holen IDs aus vier Threads gleichzeitig. Keine ID darf
 * doppelt vergeben werden. Abschnitt 12.3 hat das Werkzeug dafuer.
 */
public enum IdGenerator {

    INSTANZ;

    // TODO: ein Zaehler-Feld

    /** Liefert 1, 2, 3, ... - jede Zahl genau einmal, auch bei mehreren Threads. */
    public long naechsteId() {
        // TODO
        return 0;
    }
}
