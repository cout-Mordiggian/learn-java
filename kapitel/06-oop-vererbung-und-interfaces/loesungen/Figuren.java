/**
 * Kapitel 06 - Musterloesung: Polymorphie ohne instanceof.
 */
public class Figuren {

    public static double gesamtFlaeche(Figur[] figuren) {
        double summe = 0;
        for (Figur f : figuren) {
            // Hier passiert die dynamische Bindung: Zur Compile-Zeit ist nur
            // bekannt, dass f eine Figur ist. Zur Laufzeit wird die flaeche()
            // des tatsaechlichen Objekts aufgerufen - Kreis, Rechteck oder
            // eine Klasse, die es heute noch gar nicht gibt.
            summe += f.flaeche();
        }
        return summe;
    }

    public static Figur groesste(Figur[] figuren) {
        if (figuren.length == 0) return null;
        Figur beste = figuren[0];
        for (Figur f : figuren) {
            if (f.flaeche() > beste.flaeche()) beste = f;
        }
        return beste;
    }
}
