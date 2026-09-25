import java.util.Random;
import java.util.Scanner;

/**
 * Bonus-Aufgabe zu Kapitel 02: dein erstes interaktives Programm.
 * Es gibt keine automatischen Tests - du pruefst, indem du spielst.
 *
 * Starten:  bash lerne.sh 02 -r Zahlenraten
 *
 * So soll es sich anfuehlen:
 *
 *   Ich denke mir eine Zahl zwischen 1 und 100.
 *   Dein Tipp: 50
 *   Zu gross!
 *   Dein Tipp: 25
 *   Zu klein!
 *   Dein Tipp: 37
 *   Richtig! Du hast 3 Versuche gebraucht.
 */
public class Zahlenraten {

    public static void main(String[] args) {
        Scanner eingabe = new Scanner(System.in);
        int geheim = new Random().nextInt(1, 101);   // 1 bis 100 (die 101 ist exklusiv)

        System.out.println("Ich denke mir eine Zahl zwischen 1 und 100.");

        // TODO 1: Eine Schleife, die so lange laeuft, bis richtig geraten wurde.
        //         Welche Schleifenart passt, wenn du mindestens einmal fragen musst?
        // TODO 2: In der Schleife: "Dein Tipp: " ausgeben, mit eingabe.nextInt()
        //         eine Zahl lesen und "Zu gross!" / "Zu klein!" melden.
        // TODO 3: Die Versuche mitzaehlen und am Ende ausgeben.
        //
        // Ausbau, wenn es laeuft:
        //  - Nach 7 Fehlversuchen verloren (und die Zahl verraten).
        //  - Tipps ausserhalb von 1..100 ablehnen, ohne sie als Versuch zu zaehlen.
        //  - Am Ende fragen: "Nochmal? (j/n)" - Achtung, dafuer brauchst du
        //    eingabe.next() und equals (Kapitel 3), nicht ==.
    }
}
