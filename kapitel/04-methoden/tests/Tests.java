public class Tests {
    public static void main(String[] args) {
        Pruef.abschnitt("Aufgabe 1: ggT");
        Pruef.gleich(6, Aufgaben.ggT(48, 18), "ggT(48,18)");
        Pruef.gleich(6, Aufgaben.ggT(18, 48), "ggT(18,48) - Reihenfolge egal");
        Pruef.gleich(7, Aufgaben.ggT(7, 0), "ggT(7,0)");
        Pruef.gleich(5, Aufgaben.ggT(0, 5), "ggT(0,5)");
        Pruef.gleich(1, Aufgaben.ggT(13, 17), "teilerfremd");

        Pruef.abschnitt("Aufgabe 2: max (ueberladen)");
        Pruef.gleich(5, Aufgaben.max(3, 5), "max(int,int)");
        Pruef.gleich(5, Aufgaben.max(5, 3), "max(int,int): erstes ist groesser");
        Pruef.gleich(-2, Aufgaben.max(-5, -2), "max mit negativen Zahlen");
        Pruef.gleich(9, Aufgaben.max(3, 9, 5), "max(int,int,int)");
        Pruef.gleich(9, Aufgaben.max(9, 3, 5), "max: erstes ist groesstes");
        Pruef.gleich(9, Aufgaben.max(3, 5, 9), "max: letztes ist groesstes");
        Pruef.fastGleich(5.5, Aufgaben.max(3.2, 5.5), "max(double,double)");
        Pruef.fastGleich(5.5, Aufgaben.max(5.5, 3.2), "max(double,double): erstes ist groesser");

        Pruef.abschnitt("Aufgabe 3: Fibonacci");
        Pruef.gleich(0L, Aufgaben.fibRekursiv(0), "fibRekursiv(0)");
        Pruef.gleich(1L, Aufgaben.fibRekursiv(1), "fibRekursiv(1)");
        Pruef.gleich(55L, Aufgaben.fibRekursiv(10), "fibRekursiv(10)");
        Pruef.gleich(0L, Aufgaben.fibIterativ(0), "fibIterativ(0)");
        Pruef.gleich(55L, Aufgaben.fibIterativ(10), "fibIterativ(10)");
        Pruef.gleich(2_880_067_194_370_816_120L, Aufgaben.fibIterativ(90),
                "fibIterativ(90) - rekursiv unmoeglich");

        Pruef.abschnitt("Aufgabe 4: summeAlle (Varargs)");
        Pruef.gleich(0, Aufgaben.summeAlle(), "ohne Argumente");
        Pruef.gleich(6, Aufgaben.summeAlle(1, 2, 3), "drei Argumente");
        Pruef.gleich(-1, Aufgaben.summeAlle(4, -5), "negative Werte");

        Pruef.abschnitt("Aufgabe 5: binaereSuche");
        int[] s = {1, 3, 5, 7, 9, 11};
        Pruef.gleich(0, Aufgaben.binaereSuche(s, 1), "erstes Element");
        Pruef.gleich(5, Aufgaben.binaereSuche(s, 11), "letztes Element");
        Pruef.gleich(2, Aufgaben.binaereSuche(s, 5), "mittleres Element");
        Pruef.gleich(-1, Aufgaben.binaereSuche(s, 4), "nicht enthalten");
        Pruef.gleich(-1, Aufgaben.binaereSuche(s, 0), "kleiner als alle");
        Pruef.gleich(-1, Aufgaben.binaereSuche(s, 12), "groesser als alle");
        Pruef.gleich(-1, Aufgaben.binaereSuche(new int[]{}, 1), "leeres Array");
        Pruef.gleich(0, Aufgaben.binaereSuche(new int[]{42}, 42), "ein Element");

        Pruef.abschnitt("Aufgabe 6: verdoppleAlle (veraendert das Original)");
        int[] werte = {1, 2, 3};
        Aufgaben.verdoppleAlle(werte);
        Pruef.gleich(new int[]{2, 4, 6}, werte, "Aufrufer sieht die Aenderung");

        Pruef.abschnitt("Aufgabe 7: getauscht (laesst das Original in Ruhe)");
        int[] original = {1, 2, 3};
        int[] neu = Aufgaben.getauscht(original, 0, 2);
        Pruef.gleich(new int[]{3, 2, 1}, neu, "Kopie ist getauscht");
        Pruef.gleich(new int[]{1, 2, 3}, original, "Original ist unveraendert");
        Pruef.gleich(new int[]{1, 2, 3}, Aufgaben.getauscht(original, 1, 1), "i == j");

        Pruef.bericht();
    }
}
