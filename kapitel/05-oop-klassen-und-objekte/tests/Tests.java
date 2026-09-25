import java.util.HashSet;
import java.util.Set;

public class Tests {
    public static void main(String[] args) {

        Pruef.abschnitt("Konto: Konstruktor und Invarianten");
        int vorher = Konto.getAnzahlKonten();
        Konto anna = new Konto("Anna", 5000);
        Pruef.gleich("Anna", anna.getInhaber(), "Inhaber gemerkt");
        Pruef.gleich(5000L, anna.getGuthaben(), "Startguthaben gemerkt");
        Pruef.wahr(anna.getNummer() > 0, "Nummer wurde vergeben");
        Pruef.gleich(vorher + 1, Konto.getAnzahlKonten(), "Zaehler hochgezaehlt");

        Konto bert = new Konto("Bert", 0);
        Pruef.wahr(bert.getNummer() == anna.getNummer() + 1, "Nummern sind fortlaufend");

        Pruef.wirft(IllegalArgumentException.class,
                () -> new Konto(null, 100), "null-Inhaber abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Konto("  ", 100), "leerer Inhaber abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Konto("Cem", -1), "negatives Startguthaben abgelehnt");
        Pruef.gleich(vorher + 2, Konto.getAnzahlKonten(),
                "abgelehnte Konstruktoraufrufe verbrauchen keine Nummer");

        Pruef.abschnitt("Konto: einzahlen");
        anna.einzahlen(2500);
        Pruef.gleich(7500L, anna.getGuthaben(), "nach Einzahlung");
        Pruef.wirft(IllegalArgumentException.class,
                () -> anna.einzahlen(0), "Einzahlung von 0 abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> anna.einzahlen(-100), "negative Einzahlung abgelehnt");

        Pruef.abschnitt("Konto: abheben");
        Pruef.wahr(anna.abheben(500), "abheben mit Deckung -> true");
        Pruef.gleich(7000L, anna.getGuthaben(), "Guthaben reduziert");
        Pruef.falsch(anna.abheben(999_999), "abheben ohne Deckung -> false");
        Pruef.gleich(7000L, anna.getGuthaben(), "Guthaben bei Misserfolg unveraendert");
        Pruef.wirft(IllegalArgumentException.class,
                () -> anna.abheben(-5), "negatives Abheben abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> anna.abheben(0), "Abheben von 0 abgelehnt");
        Konto emil = new Konto("Emil", 300);
        Pruef.wahr(emil.abheben(300), "komplettes Guthaben abheben -> true");
        Pruef.gleich(0L, emil.getGuthaben(), "danach ist das Konto leer");

        Pruef.abschnitt("Konto: ueberweisen");
        Pruef.wahr(anna.ueberweiseAn(bert, 1000), "Ueberweisung klappt");
        Pruef.gleich(6000L, anna.getGuthaben(), "Sender belastet");
        Pruef.gleich(1000L, bert.getGuthaben(), "Empfaenger gutgeschrieben");
        Pruef.falsch(bert.ueberweiseAn(anna, 50_000), "Ueberweisung ohne Deckung -> false");
        Pruef.gleich(1000L, bert.getGuthaben(), "Sender bei Misserfolg unveraendert");
        Pruef.gleich(6000L, anna.getGuthaben(), "Empfaenger bei Misserfolg unveraendert");

        Pruef.abschnitt("Konto: toString");
        Konto dora = new Konto("Dora", 250);
        Pruef.gleich("Konto[" + dora.getNummer() + ", Dora, 250 Cent]", dora.toString(), "Format stimmt");

        Pruef.abschnitt("Punkt: Grundlagen");
        Punkt p = new Punkt(3, 4);
        Pruef.fastGleich(3.0, p.getX(), "getX");
        Pruef.fastGleich(4.0, p.getY(), "getY");
        Pruef.fastGleich(5.0, p.abstandZumUrsprung(), "3-4-5-Dreieck");
        Pruef.fastGleich(5.0, p.abstand(new Punkt(0, 0)), "Abstand zum Ursprung");
        Pruef.fastGleich(0.0, p.abstand(p), "Abstand zu sich selbst");
        Pruef.fastGleich(2.0, new Punkt(1, 1).abstand(new Punkt(3, 1)), "waagerechter Abstand");

        Pruef.abschnitt("Punkt: Unveraenderlichkeit");
        Punkt q = p.verschoben(1, 1);
        Pruef.fastGleich(4.0, q.getX(), "verschobener Punkt: x");
        Pruef.fastGleich(5.0, q.getY(), "verschobener Punkt: y");
        Pruef.fastGleich(3.0, p.getX(), "Original unveraendert: x");
        Pruef.fastGleich(4.0, p.getY(), "Original unveraendert: y");
        Pruef.falsch(p == q, "es ist wirklich ein neues Objekt");

        Pruef.abschnitt("Punkt: toString, equals, hashCode");
        Pruef.gleich("Punkt(3.0, 4.0)", p.toString(), "toString-Format");
        Pruef.wahr(p.equals(new Punkt(3, 4)), "gleiche Koordinaten sind equals");
        Pruef.wahr(p.equals(p), "reflexiv");
        Pruef.falsch(p.equals(new Punkt(3, 5)), "nur y verschieden -> nicht equals");
        Pruef.falsch(p.equals(new Punkt(2, 4)), "nur x verschieden -> nicht equals");
        Pruef.falsch(p.equals(null), "equals(null) ist false, keine Exception");
        Pruef.falsch(p.equals("Punkt(3.0, 4.0)"), "anderer Typ ist nicht equals");
        Pruef.gleich(p.hashCode(), new Punkt(3, 4).hashCode(), "equals -> gleicher hashCode");

        // Die Ecken von double: 0.0/-0.0 und NaN (siehe README 5.6).
        Punkt plusNull = new Punkt(0.0, 0.0);
        Punkt minusNull = new Punkt(-0.0, 0.0);
        Pruef.wahr(!plusNull.equals(minusNull) || plusNull.hashCode() == minusNull.hashCode(),
                "0.0 und -0.0: equals und hashCode passen zusammen");
        Pruef.wahr(new Punkt(Double.NaN, 1).equals(new Punkt(Double.NaN, 1)),
                "zwei NaN-Punkte sind equals (Double.compare statt ==)");

        Set<Punkt> menge = new HashSet<>();
        menge.add(new Punkt(1, 1));
        menge.add(new Punkt(1, 1));
        Pruef.gleich(1, menge.size(), "HashSet erkennt das Duplikat (der hashCode-Vertrag)");

        Pruef.bericht();
    }
}
