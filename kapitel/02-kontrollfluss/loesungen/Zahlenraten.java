import java.util.Random;
import java.util.Scanner;

/**
 * Musterloesung zur Bonus-Aufgabe Zahlenraten (Kapitel 02).
 *
 * Starten:  bash lerne.sh 02 -l -r Zahlenraten
 */
public class Zahlenraten {

    public static void main(String[] args) {
        Scanner eingabe = new Scanner(System.in);
        int geheim = new Random().nextInt(1, 101);

        System.out.println("Ich denke mir eine Zahl zwischen 1 und 100.");

        // do/while, weil mindestens einmal gefragt werden muss - bevor der
        // erste Tipp da ist, gibt es nichts, was man pruefen koennte.
        int versuche = 0;
        int tipp;
        do {
            System.out.print("Dein Tipp: ");
            tipp = eingabe.nextInt();
            versuche++;

            if (tipp > geheim) {
                System.out.println("Zu gross!");
            } else if (tipp < geheim) {
                System.out.println("Zu klein!");
            }
        } while (tipp != geheim);

        System.out.println("Richtig! Du hast " + versuche + " Versuche gebraucht.");

        // Tippt jemand Buchstaben, stuerzt nextInt() mit einer
        // InputMismatchException ab. Wie man das abfaengt, lernst du in Kapitel 7.
        // System.in wird bewusst NICHT geschlossen (siehe Kapitel 11.7).
    }
}
