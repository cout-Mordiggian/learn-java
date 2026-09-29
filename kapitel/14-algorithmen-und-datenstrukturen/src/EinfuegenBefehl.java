/**
 * Kapitel 14 - Aufgabe 10a: der Befehl "Text einfuegen".
 * Pruefen:  ./lerne.sh 14
 *
 *   Textpuffer t = ...;                          // "Hallo"
 *   Befehl b = new EinfuegenBefehl(t, 5, " Welt");
 *   b.ausfuehren();                              // "Hallo Welt"
 *   b.rueckgaengig();                            // "Hallo"
 *
 * Der Konstruktor merkt sich nur, WAS zu tun ist - er tut es noch nicht.
 */
public class EinfuegenBefehl implements Befehl {

    // TODO: Felder

    public EinfuegenBefehl(Textpuffer puffer, int pos, String text) {
        // TODO
    }

    @Override
    public void ausfuehren() {
        // TODO
    }

    @Override
    public void rueckgaengig() {
        // TODO: Was ist das Gegenteil von Einfuegen? Welche Angaben brauchst du dafuer?
    }
}
