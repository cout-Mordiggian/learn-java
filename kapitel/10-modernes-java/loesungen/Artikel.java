/**
 * Kapitel 10 - Musterloesung: record mit Validierung.
 *
 * Was dieser Einzeiler ersetzt: drei private final Felder, einen
 * Konstruktor, drei Zugriffsmethoden, equals, hashCode und toString -
 * ungefaehr 60 Zeilen Code, die alle gleich aussehen und trotzdem
 * jedes Mal falsch sein koennen.
 */
public record Artikel(String name, long preisCent, int menge) {

    /**
     * Kompakter Konstruktor: keine Parameterliste, kein this.x = x.
     * Er laeuft VOR der automatischen Zuweisung an die Felder - deshalb
     * darf man die Parameter noch veraendern ("name = name.strip()").
     * Eine Zuweisung an this.name ist hier dagegen ein Compilerfehler -
     * die Felder setzt ausschliesslich der Compiler, ganz am Ende.
     */
    public Artikel {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
        if (preisCent < 0) {
            throw new IllegalArgumentException("Preis darf nicht negativ sein: " + preisCent);
        }
        if (menge < 0) {
            throw new IllegalArgumentException("Menge darf nicht negativ sein: " + menge);
        }
        name = name.strip();   // Normalisierung - landet so im Feld
    }

    public long gesamtCent() {
        return preisCent * menge;
    }

    public static Artikel einzeln(String name, long preisCent) {
        return new Artikel(name, preisCent, 1);
    }

    /**
     * "Wither"-Methode: Records sind unveraenderlich, also gibt jede
     * Aenderung ein neues Objekt zurueck. Bei vielen Komponenten wird
     * das muehsam - dann lohnt ein Builder.
     */
    public Artikel mitMenge(int neueMenge) {
        return new Artikel(name, preisCent, neueMenge);
    }
}
