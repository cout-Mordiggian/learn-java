/**
 * Kapitel 13 - Teil A: die Spezifikation, gegen die du testest.
 *
 * Diese Datei musst du nicht aendern. Sie liegt identisch in src/ und
 * loesungen/, damit beide Varianten kompilieren.
 *
 * <h2>Regeln fuer endpreisCent</h2>
 * <ol>
 *   <li>{@code preisCent} ist der Stueckpreis in Cent, {@code menge} die Stueckzahl.</li>
 *   <li>Ist {@code preisCent} oder {@code menge} negativ, wird eine
 *       {@link IllegalArgumentException} geworfen.</li>
 *   <li>Menge 0 ist erlaubt (leere Bestellung) und kostet 0 Cent.
 *       Ein Preis von 0 Cent ist ebenfalls erlaubt.</li>
 *   <li>Gesamtpreis = preisCent * menge. Das muss auch fuer grosse Werte
 *       stimmen, die nicht mehr in einen {@code int} passen. Du darfst annehmen,
 *       dass Gesamtpreis * 100 noch in einen {@code long} passt.</li>
 *   <li>Mengenrabatt: 0 bis 9 Stueck: 0 %, 10 bis 49 Stueck: 5 %,
 *       ab 50 Stueck: 10 %.</li>
 *   <li>Rabatt in Cent = Gesamtpreis * Prozent / 100, auf ganze Cent
 *       <b>abgerundet</b>. Endpreis = Gesamtpreis - Rabatt.</li>
 * </ol>
 *
 * Beispiel: 199 Cent * 10 Stueck = 1990 Cent. 5 % davon sind 99,5 Cent,
 * abgerundet 99 Cent Rabatt. Endpreis: 1990 - 99 = 1891 Cent.
 */
public interface Rabattrechner {

    /**
     * Berechnet den Endpreis einer Bestellung nach den Regeln oben.
     *
     * @param preisCent Stueckpreis in Cent, nicht negativ
     * @param menge     Stueckzahl, nicht negativ
     * @return Endpreis in Cent nach Abzug des Mengenrabatts
     * @throws IllegalArgumentException wenn preisCent oder menge negativ ist
     */
    long endpreisCent(long preisCent, int menge);
}
