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

        Pruef.abschnitt("Aufgabe 7a: MitZeilennummer (Dekorierer)");
        TextProtokoll pn1 = new TextProtokoll();
        schreibeAlle(new MitZeilennummer(pn1), "a", "b", "c");
        Pruef.gleich("1: a\n2: b\n3: c\n", pn1.inhalt(), "nummeriert ab 1");
        TextProtokoll pn2 = new TextProtokoll();
        new MitZeilennummer(pn2).schreibe("x");
        Pruef.gleich("1: x\n", pn2.inhalt(), "jeder Dekorierer zaehlt fuer sich");

        Pruef.abschnitt("Aufgabe 7b: Grossgeschrieben (Dekorierer)");
        TextProtokoll pg1 = new TextProtokoll();
        new Grossgeschrieben(pg1).schreibe("Hallo Welt");
        Pruef.gleich("HALLO WELT\n", pg1.inhalt(), "Hallo Welt -> HALLO WELT");

        Pruef.abschnitt("Aufgabe 7c: NurMit (Dekorierer)");
        String[] eingabe = {"INFO Start", "FEHLER Platte voll", "INFO weiter", "FEHLER Netz weg"};
        TextProtokoll pf1 = new TextProtokoll();
        schreibeAlle(new NurMit("FEHLER", pf1), eingabe);
        Pruef.gleich("FEHLER Platte voll\nFEHLER Netz weg\n", pf1.inhalt(),
                "nur Zeilen, die FEHLER enthalten");
        TextProtokoll pf2 = new TextProtokoll();
        schreibeAlle(new NurMit("fehler", pf2), eingabe);
        Pruef.gleich("", pf2.inhalt(), "Gross-/Kleinschreibung zaehlt: \"fehler\" passt nicht");

        Pruef.abschnitt("Aufgabe 7: Dekorierer kombinieren");
        TextProtokoll pk1 = new TextProtokoll();
        schreibeAlle(new NurMit("FEHLER", new MitZeilennummer(pk1)), eingabe);
        Pruef.gleich("1: FEHLER Platte voll\n2: FEHLER Netz weg\n", pk1.inhalt(),
                "aussen Filter, innen Nummer: nur durchgelassene Zeilen werden gezaehlt");
        TextProtokoll pk2 = new TextProtokoll();
        schreibeAlle(new MitZeilennummer(new NurMit("FEHLER", pk2)), eingabe);
        Pruef.gleich("2: FEHLER Platte voll\n4: FEHLER Netz weg\n", pk2.inhalt(),
                "aussen Nummer, innen Filter: ALLE Zeilen werden gezaehlt");
        TextProtokoll pk3 = new TextProtokoll();
        schreibeAlle(new Grossgeschrieben(new NurMit("FEHLER", pk3)), "fehler klein", "info");
        Pruef.gleich("FEHLER KLEIN\n", pk3.inhalt(),
                "aussen gross, innen Filter: der Filter sieht schon Grossbuchstaben");
        TextProtokoll pk4 = new TextProtokoll();
        schreibeAlle(new NurMit("FEHLER", new Grossgeschrieben(pk4)), "fehler klein", "info");
        Pruef.gleich("", pk4.inhalt(), "aussen Filter, innen gross: der Filter sieht noch Kleinbuchstaben");
        TextProtokoll pk5 = new TextProtokoll();
        Protokoll dreiSchichten = new Grossgeschrieben(new MitZeilennummer(new Grossgeschrieben(pk5)));
        dreiSchichten.schreibe("drei schichten");
        Pruef.gleich("1: DREI SCHICHTEN\n", pk5.inhalt(), "drei Schichten, derselbe Dekorierer zweimal");

        Pruef.abschnitt("Aufgabe 7: null frueh ablehnen");
        Pruef.wirft(NullPointerException.class, () -> new MitZeilennummer(null),
                "new MitZeilennummer(null) -> sofort NullPointerException");
        Pruef.wirft(NullPointerException.class, () -> new Grossgeschrieben(null),
                "new Grossgeschrieben(null) -> sofort NullPointerException");
        Pruef.wirft(NullPointerException.class, () -> new NurMit("x", null),
                "new NurMit(\"x\", null) -> sofort NullPointerException");
        Pruef.wirft(NullPointerException.class, () -> new NurMit(null, new TextProtokoll()),
                "new NurMit(null, ..) -> sofort NullPointerException");

        Pruef.abschnitt("Aufgabe 8b: Verhalten (Strategien)");
        Pruef.gleich("Ich fliege!", new FliegtMitFluegeln().fliegen(), "FliegtMitFluegeln");
        Pruef.gleich("Ich kann nicht fliegen.", new FliegtNicht().fliegen(), "FliegtNicht");
        Pruef.gleich("Quak", new Quaken().quaken(), "Quaken");
        Pruef.gleich("Quietsch", new Quietschen().quaken(), "Quietschen");
        Pruef.gleich("<< Stille >>", new Stumm().quaken(), "Stumm");

        Pruef.abschnitt("Aufgabe 8a/8c: Enten delegieren an ihre Verhalten");
        pruefeEnte(new Stockente(), "Ich bin eine Stockente", "Ich fliege!", "Quak");
        pruefeEnte(new Gummiente(), "Ich bin eine Gummiente", "Ich kann nicht fliegen.", "Quietsch");
        pruefeEnte(new Lockente(), "Ich bin eine Lockente", "Ich kann nicht fliegen.", "<< Stille >>");
        pruefeEnte(new Modellente(), "Ich bin eine Modellente", "Ich kann nicht fliegen.", "Quak");
        Pruef.gleich("Alle Enten schwimmen, sogar Lockenten.", new Gummiente().schwimmen(),
                "schwimmen kommt fuer alle aus Ente");

        Pruef.abschnitt("Aufgabe 8a: Polymorphie ueber den Teich");
        // Die Schleife kennt nur Ente - welche Strategie dahinter steckt, entscheidet jedes Objekt.
        Ente[] teich = { new Stockente(), new Gummiente(), new Lockente() };
        StringBuilder laute = new StringBuilder();
        for (Ente e : teich) {
            laute.append(e.quaken()).append(' ');
        }
        Pruef.gleich("Quak Quietsch << Stille >> ", laute.toString(), "jede Ente quakt auf ihre Art");

        Pruef.abschnitt("Aufgabe 8a/8d: Verhalten zur Laufzeit austauschen");
        Modellente modell = new Modellente();
        Modellente zweitesModell = new Modellente();
        Pruef.gleich("Ich kann nicht fliegen.", modell.fliegen(), "vorher: Modellente fliegt nicht");
        modell.setzeFlugVerhalten(new FliegtMitRaketenantrieb());
        Pruef.gleich("Ich fliege mit Raketenantrieb!", modell.fliegen(), "nachher: mit Raketenantrieb");
        Pruef.gleich("Quak", modell.quaken(), "das QuakVerhalten bleibt dabei unberuehrt");
        Pruef.gleich("Ich kann nicht fliegen.", zweitesModell.fliegen(),
                "jede Ente hat ihr eigenes Verhalten (kein static-Feld!)");
        Gummiente gummi = new Gummiente();
        gummi.setzeQuakVerhalten(new Quaken());
        Pruef.gleich("Quak", gummi.quaken(), "auch das QuakVerhalten ist austauschbar");
        Pruef.gleich("Ich bin eine Gummiente", gummi.anzeigen(), "die Ente bleibt, was sie ist");

        Pruef.abschnitt("Aufgabe 8a: null frueh ablehnen");
        Pruef.wirft(NullPointerException.class, () -> new Stockente().setzeFlugVerhalten(null),
                "setzeFlugVerhalten(null) -> NullPointerException");
        Pruef.wirft(NullPointerException.class, () -> new Stockente().setzeQuakVerhalten(null),
                "setzeQuakVerhalten(null) -> NullPointerException");
        // Eine Unterklasse, die null an den Ente-Konstruktor reicht. Die Schreibweise
        // "new Ente(...) { ... }" (anonyme Klasse) lernst du in Kapitel 9 kennen.
        Pruef.wirft(NullPointerException.class, () -> new Ente(null, new Quaken()) {
                    @Override public String anzeigen() { return "kaputt"; }
                }, "new Ente(null, ..) -> sofort NullPointerException");
        Pruef.wirft(NullPointerException.class, () -> new Ente(new FliegtNicht(), null) {
                    @Override public String anzeigen() { return "kaputt"; }
                }, "new Ente(.., null) -> sofort NullPointerException");

        Pruef.bericht();
    }

    /** Prueft anzeigen, fliegen und quaken einer Ente auf einmal. */
    private static void pruefeEnte(Ente e, String anzeige, String flug, String laut) {
        Pruef.gleich(anzeige, e.anzeigen(), "anzeigen: " + anzeige);
        Pruef.gleich(flug, e.fliegen(), anzeige + ": fliegen");
        Pruef.gleich(laut, e.quaken(), anzeige + ": quaken");
    }

    /** Schreibt alle Zeilen der Reihe nach in das Protokoll. */
    private static void schreibeAlle(Protokoll p, String... zeilen) {
        for (String z : zeilen) {
            p.schreibe(z);
        }
    }
}
