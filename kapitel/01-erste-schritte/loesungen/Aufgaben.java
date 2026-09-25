/**
 * Kapitel 01 - Musterloesung mit Erklaerungen.
 */
public class Aufgaben {

    public static String begruessung(String name) {
        // Der +-Operator ist bei Strings ueberladen: er verkettet statt zu addieren.
        // Wie der Compiler das intern umsetzt, ist sein Detail (seit Java 9 per
        // invokedynamic) - fuer einzelne Ausdruecke wie diesen ist + ideal.
        return "Hallo, " + name + "!";
    }

    public static double celsiusZuFahrenheit(double celsius) {
        // celsius * 9 / 5 + 32 waere korrekt: celsius ist double, also rechnet
        // Java von links nach rechts in double weiter.
        // celsius * (9 / 5) + 32 waere FALSCH: 9 / 5 ist eine int-Division und
        // ergibt 1. Die Klammern entscheiden.
        // Am robustesten schreibt man die Konstanten direkt als Kommazahl:
        return celsius * 9.0 / 5.0 + 32.0;
    }

    public static double kreisFlaeche(double radius) {
        // radius * radius statt Math.pow(radius, 2): kurz und fuer den Leser
        // eindeutig. Math.pow lohnt sich erst bei variablen oder krummen Exponenten.
        return Math.PI * radius * radius;
    }

    public static int letzteZiffer(int zahl) {
        // Modulo 10 liefert den Rest der Division durch 10 - genau die letzte Ziffer.
        // Hinweis: Bei negativen Zahlen liefert Java einen negativen Rest
        // (-7 % 10 == -7). Die Aufgabe schliesst negative Eingaben aus.
        return zahl % 10;
    }

    public static String zeitFormat(int sekundenGesamt) {
        int stunden = sekundenGesamt / 3600;          // ganze Stunden
        int rest = sekundenGesamt % 3600;             // was uebrig bleibt
        int minuten = rest / 60;
        int sekunden = rest % 60;
        // %02d = Ganzzahl, mindestens 2 Stellen, links mit Nullen aufgefuellt.
        return String.format("%02d:%02d:%02d", stunden, minuten, sekunden);
    }

    public static long millisekundenProJahr() {
        // Entscheidend ist das L am ERSTEN Faktor. Java wertet von links nach
        // rechts aus; sobald ein Operand long ist, laeuft der Rest der Rechnung
        // in long weiter. Ohne das L waere 365 * 24 * 60 * 60 * 1000 eine
        // reine int-Rechnung, die bei 31.536.000.000 ueberlaeuft - das Ergebnis
        // waere 1471228928, und der Compiler wuerde nicht einmal warnen.
        return 365L * 24 * 60 * 60 * 1000;
    }
}
