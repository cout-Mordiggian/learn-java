import java.util.Locale;

public class Tests {
    public static void main(String[] args) {

        Pruef.abschnitt("Kreis");
        Kreis k = new Kreis(2);
        Pruef.gleich("Kreis", k.getName(), "getName");
        Pruef.fastGleich(2.0, k.getRadius(), "getRadius");
        Pruef.fastGleich(Math.PI * 4, k.flaeche(), "Flaeche");
        Pruef.fastGleich(4 * Math.PI, k.umfang(), "Umfang");
        // Bei r = 2 sind Flaeche und Umfang zufaellig gleich (4 * pi) - deshalb
        // zusaetzlich r = 1, damit vertauschte Formeln auffallen.
        Pruef.fastGleich(2 * Math.PI, new Kreis(1).umfang(), "Umfang bei Radius 1");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Kreis(-1), "negativer Radius abgelehnt");

        Pruef.abschnitt("Rechteck");
        Rechteck r = new Rechteck(3, 4);
        Pruef.gleich("Rechteck", r.getName(), "getName");
        Pruef.fastGleich(12.0, r.flaeche(), "Flaeche");
        Pruef.fastGleich(14.0, r.umfang(), "Umfang");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Rechteck(-1, 2), "negative Breite abgelehnt");
        Pruef.wirft(IllegalArgumentException.class,
                () -> new Rechteck(2, -1), "negative Hoehe abgelehnt");

        Pruef.abschnitt("Quadrat: Vererbung ueber zwei Ebenen");
        Quadrat q = new Quadrat(3);
        Pruef.gleich("Quadrat", q.getName(), "eigener Name trotz Rechteck-Konstruktor");
        Pruef.fastGleich(3.0, q.getSeite(), "getSeite");
        Pruef.fastGleich(9.0, q.flaeche(), "Flaeche wird von Rechteck geerbt");
        Pruef.fastGleich(12.0, q.umfang(), "Umfang wird von Rechteck geerbt");
        Pruef.wahr(q instanceof Rechteck, "Quadrat ist ein Rechteck");
        Pruef.wahr(q instanceof Figur, "Quadrat ist eine Figur");
        Pruef.wahr(q instanceof Skalierbar, "Quadrat ist skalierbar (geerbt vom Rechteck)");

        Pruef.abschnitt("beschreibung (Locale.ROOT!)");
        // Absichtlich ein deutsches Locale einstellen: Ohne Locale.ROOT kaeme
        // dann "12,00" statt "12.00" heraus - egal, wie dein System eingestellt ist.
        Locale systemLocale = Locale.getDefault();
        Locale.setDefault(Locale.GERMANY);
        Pruef.gleich("Rechteck: Flaeche=12.00, Umfang=14.00", r.beschreibung(), "Rechteck");
        Pruef.gleich("Kreis: Flaeche=3.14, Umfang=6.28", new Kreis(1).beschreibung(),
                "Kreis mit Radius 1, auf 2 Stellen gerundet");
        Pruef.gleich("Quadrat: Flaeche=9.00, Umfang=12.00", q.beschreibung(),
                "geerbte Methode nutzt die ueberschriebenen Werte");
        Locale.setDefault(systemLocale);

        Pruef.abschnitt("Skalierbar und die default-Methode");
        Figur k2 = k.skaliert(3);
        Pruef.fastGleich(Math.PI * 36, k2.flaeche(), "Kreis skaliert: r=6");
        Pruef.fastGleich(Math.PI * 4, k.flaeche(), "Original unveraendert");
        Figur r2 = r.skaliert(2);
        Pruef.fastGleich(48.0, r2.flaeche(), "Rechteck skaliert: 6x8");
        Figur kv = k.verdoppelt();
        Pruef.fastGleich(Math.PI * 16, kv.flaeche(), "verdoppelt() = skaliert(2)");

        Pruef.abschnitt("Kovarianter Rueckgabetyp");
        // Wenn skaliert() in Quadrat richtig ueberschrieben ist, braucht diese
        // Zeile KEINEN Cast - der Compiler kennt den Typ Quadrat.
        Quadrat q2 = q.skaliert(2);
        Pruef.fastGleich(6.0, q2.getSeite(), "Quadrat.skaliert liefert ein Quadrat");
        Pruef.gleich("Quadrat", q2.getName(), "und es heisst auch so");

        Pruef.abschnitt("Polymorphie: Figuren-Hilfsmethoden");
        Figur[] alle = { new Kreis(1), new Rechteck(2, 3), new Quadrat(4) };
        Pruef.fastGleich(Math.PI + 6 + 16, Figuren.gesamtFlaeche(alle), "Gesamtflaeche");
        Pruef.gleich("Quadrat", Figuren.groesste(alle).getName(), "groesste Figur");
        Figur[] groessteVorne = { new Quadrat(4), new Kreis(1), new Rechteck(2, 3) };
        Pruef.gleich("Quadrat", Figuren.groesste(groessteVorne).getName(),
                "groesste Figur steht vorne");
        Pruef.fastGleich(0.0, Figuren.gesamtFlaeche(new Figur[]{}), "leeres Array");
        Pruef.gleich(null, Figuren.groesste(new Figur[]{}), "groesste von leer ist null");

        Pruef.abschnitt("Dynamische Bindung");
        // Statischer Typ ist Figur, dynamischer Typ ist Kreis.
        // Aufgerufen wird die Kreis-Version - das ist der ganze Trick.
        Figur alsFigur = new Kreis(1);
        Pruef.fastGleich(Math.PI, alsFigur.flaeche(), "Figur-Variable, Kreis-Verhalten");
        Pruef.gleich("Kreis", alsFigur.getName(), "getName kommt vom echten Typ");

        Pruef.bericht();
    }
}
