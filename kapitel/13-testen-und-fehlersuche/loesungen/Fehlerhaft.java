/**
 * Kapitel 13 - Musterloesung Teil B: die korrigierten Methoden.
 * Jede reparierte Stelle ist mit "BUG WAR:" markiert.
 */
public class Fehlerhaft {

    public static double durchschnitt(int[] zahlen) {
        int summe = 0;
        for (int z : zahlen) {
            summe += z;
        }
        // BUG WAR: return summe / zahlen.length;
        // int / int ist Ganzzahldivision: 3 / 2 ergibt 1, und erst DANACH wird
        // das Ergebnis fuer den Rueckgabetyp double zu 1.0 erweitert. Zu spaet -
        // die Nachkommastellen sind schon weg. Der Cast muss VOR der Division
        // stehen, damit mit double gerechnet wird.
        return (double) summe / zahlen.length;
    }

    public static int zaehleGroesser(int[] zahlen, int grenze) {
        int anzahl = 0;
        // BUG WAR: i <= zahlen.length
        // Gueltige Indizes laufen von 0 bis length - 1. Mit <= greift der letzte
        // Durchlauf auf zahlen[length] zu -> ArrayIndexOutOfBoundsException,
        // sogar beim leeren Array (zahlen[0]). Merksatz: "< length".
        // Noch sicherer ist hier die for-each-Schleife: for (int z : zahlen).
        for (int i = 0; i < zahlen.length; i++) {
            if (zahlen[i] > grenze) {
                anzahl++;
            }
        }
        return anzahl;
    }

    public static boolean istAdmin(String name) {
        // BUG WAR: return name == "admin";
        // == vergleicht bei Objekten, ob es DASSELBE Objekt ist. Mit einem
        // Literal klappt das zufaellig (String-Pool, Kapitel 3.2), mit einem
        // zur Laufzeit erzeugten String (Eingabe, substring, new String) nicht.
        // Das Literal steht vorne, damit name == null kein Problem ist:
        // "admin".equals(null) liefert false, name.equals("admin") wuerde
        // bei null eine NullPointerException werfen.
        return "admin".equals(name);
    }

    public static String wochentag(int tag) {
        String name;
        switch (tag) {
            case 1:
                name = "Montag";
                break;
            case 2:
                name = "Dienstag";
                break;
            case 3:
                name = "Mittwoch";
                break;
            case 4:
                name = "Donnerstag";
                break;
            case 5:
                name = "Freitag";
                break; // BUG WAR: dieses break fehlte - Freitag fiel in "Samstag" durch.
            case 6:
                name = "Samstag";
                break;
            case 7:
                name = "Sonntag";
                break;
            default:
                name = "ungueltig";
        }
        return name;
        // Besser gleich die Pfeilform (Kapitel 2), die kein Durchfallen kennt:
        //   return switch (tag) {
        //       case 1 -> "Montag";
        //       ...
        //       default -> "ungueltig";
        //   };
    }

    public static String normalisiere(String eingabe) {
        // BUG WAR: eingabe.strip();   (Ergebnis weggeworfen)
        // Strings sind unveraenderlich. strip() aendert eingabe NICHT, sondern
        // liefert einen NEUEN String - und den muss man auffangen.
        return eingabe.strip().toLowerCase();
    }

    public static int hoechstens(int wert, int grenze) {
        int ergebnis = wert;
        // BUG WAR: if (wert > grenze); {
        // Das Semikolon IST der ganze if-Rumpf (eine leere Anweisung). Der
        // Block { ... } danach ist ein gewoehnlicher Block, der IMMER laeuft.
        if (wert > grenze) {
            ergebnis = grenze;
        }
        return ergebnis;
        // Kuerzer und ohne Fallstrick: return Math.min(wert, grenze);
    }
}
