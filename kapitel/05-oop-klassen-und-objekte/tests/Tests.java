import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
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

        Pruef.abschnitt("Punkt: statische Fabrikmethoden");
        Punkt o = Punkt.ursprung();
        Pruef.fastGleich(0.0, o.getX(), "ursprung(): x = 0");
        Pruef.fastGleich(0.0, o.getY(), "ursprung(): y = 0");
        Pruef.wahr(Punkt.ursprung() == Punkt.ursprung(),
                "ursprung() liefert jedes Mal DASSELBE Objekt (==), nicht nur ein gleiches");
        Punkt oben = Punkt.polar(2, 90);
        Pruef.fastGleich(0.0, oben.getX(), "polar(2, 90): x = 0");
        Pruef.fastGleich(2.0, oben.getY(), "polar(2, 90): y = 2");
        Punkt rechts = Punkt.polar(3, 0);
        Pruef.fastGleich(3.0, rechts.getX(), "polar(3, 0): x = 3");
        Pruef.fastGleich(0.0, rechts.getY(), "polar(3, 0): y = 0");
        Punkt schraeg = Punkt.polar(Math.sqrt(2), 45);
        Pruef.fastGleich(1.0, schraeg.getX(), "polar(Wurzel 2, 45): x = 1");
        Pruef.fastGleich(1.0, schraeg.getY(), "polar(Wurzel 2, 45): y = 1");
        Pruef.fastGleich(5.0, Punkt.polar(5, 123).abstandZumUrsprung(),
                "polar(5, 123) hat Abstand 5 zum Ursprung");

        // Singleton: Die Reihenfolge dieser Pruefungen ist nicht egal! Die eine
        // Instanz lebt bis zum Programmende, und was ein Test an ihr aendert,
        // sieht der naechste. Deshalb stehen die Standardwerte ganz vorn.
        // (Genau dieses Problem mit Singletons erklaert Kapitel 13.8.)
        Pruef.abschnitt("Konfiguration: Singleton");
        Konfiguration k1 = Konfiguration.instanz();
        Pruef.gleich("de", k1.getSprache(), "Standard-Sprache ist \"de\"");
        Pruef.falsch(k1.isFarbig(), "Standard: nicht farbig");
        Konfiguration k2 = Konfiguration.instanz();
        Pruef.wahr(k1 != null, "instanz() liefert ein Objekt");
        Pruef.wahr(k1 == k2, "instanz() liefert jedes Mal DASSELBE Objekt (==)");
        k1.setSprache("en");
        k1.setFarbig(true);
        Pruef.gleich("en", k2.getSprache(), "Sprache ueber k1 gesetzt, ueber k2 gelesen");
        Pruef.wahr(k2.isFarbig(), "farbig ueber k1 gesetzt, ueber k2 gelesen");
        Pruef.gleich("en", Konfiguration.instanz().getSprache(), "auch ein spaeterer instanz()-Aufruf sieht es");
        Pruef.wirft(IllegalArgumentException.class, () -> k1.setSprache(null), "setSprache(null) abgelehnt");
        Pruef.wirft(IllegalArgumentException.class, () -> k1.setSprache("  "), "leere Sprache abgelehnt");
        Pruef.gleich("en", k2.getSprache(), "nach den Fehlversuchen: Sprache unveraendert");
        // Ein Blick in die Klasse zur Laufzeit ("Reflection" - kein Kursthema,
        // hier nur, um zu pruefen, was der Compiler in einem Test nicht pruefen
        // kann): Gibt es irgendeinen Konstruktor, den andere Klassen aufrufen koennen?
        boolean allePrivat = true;
        for (Constructor<?> konstruktor : Konfiguration.class.getDeclaredConstructors()) {
            if (!Modifier.isPrivate(konstruktor.getModifiers())) allePrivat = false;
        }
        Pruef.wahr(allePrivat, "alle Konstruktoren sind private - niemand sonst kann 'new' sagen");

        Pruef.bericht();
    }
}
