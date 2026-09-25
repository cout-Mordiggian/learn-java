/**
 * Kapitel 06, Aufgabe 6: Polymorphie in Reinform.
 *
 * Regel fuer diese Datei: KEIN instanceof, KEIN Cast.
 * Diese Methoden funktionieren auch fuer Figuren, die es heute noch nicht gibt.
 */
public class Figuren {

    public static double gesamtFlaeche(Figur[] figuren) {
        // TODO
        return 0.0;
    }

    /** Die Figur mit der groessten Flaeche, oder null bei leerem Array. */
    public static Figur groesste(Figur[] figuren) {
        // TODO
        return figuren.length == 0 ? null : figuren[0];
    }
}
