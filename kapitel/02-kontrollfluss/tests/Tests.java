public class Tests {
    public static void main(String[] args) {
        Pruef.abschnitt("Aufgabe 1: fizzbuzz");
        Pruef.gleich("1", Aufgaben.fizzbuzz(1), "1");
        Pruef.gleich("Fizz", Aufgaben.fizzbuzz(3), "3 -> Fizz");
        Pruef.gleich("Buzz", Aufgaben.fizzbuzz(5), "5 -> Buzz");
        Pruef.gleich("FizzBuzz", Aufgaben.fizzbuzz(15), "15 -> FizzBuzz");
        Pruef.gleich("FizzBuzz", Aufgaben.fizzbuzz(0), "0 -> FizzBuzz");
        Pruef.gleich("7", Aufgaben.fizzbuzz(7), "7");

        Pruef.abschnitt("Aufgabe 2: notenText");
        Pruef.gleich("sehr gut", Aufgaben.notenText(1), "Note 1");
        Pruef.gleich("gut", Aufgaben.notenText(2), "Note 2");
        Pruef.gleich("befriedigend", Aufgaben.notenText(3), "Note 3");
        Pruef.gleich("ausreichend", Aufgaben.notenText(4), "Note 4");
        Pruef.gleich("mangelhaft", Aufgaben.notenText(5), "Note 5");
        Pruef.gleich("ungenuegend", Aufgaben.notenText(6), "Note 6");
        Pruef.gleich("ungueltig", Aufgaben.notenText(0), "Note 0");
        Pruef.gleich("ungueltig", Aufgaben.notenText(7), "Note 7");
        Pruef.gleich("ungueltig", Aufgaben.notenText(-1), "Note -1");

        Pruef.abschnitt("Aufgabe 3: istPrimzahl");
        Pruef.falsch(Aufgaben.istPrimzahl(-7), "negative Zahlen sind nie prim");
        Pruef.falsch(Aufgaben.istPrimzahl(0), "0 ist keine Primzahl");
        Pruef.falsch(Aufgaben.istPrimzahl(1), "1 ist keine Primzahl");
        Pruef.wahr(Aufgaben.istPrimzahl(2), "2 ist prim");
        Pruef.wahr(Aufgaben.istPrimzahl(3), "3 ist prim");
        Pruef.falsch(Aufgaben.istPrimzahl(4), "4 = 2*2 ist nicht prim");
        Pruef.falsch(Aufgaben.istPrimzahl(9), "9 = 3*3 ist nicht prim");
        Pruef.falsch(Aufgaben.istPrimzahl(25), "25 = 5*5 ist nicht prim");
        Pruef.wahr(Aufgaben.istPrimzahl(97), "97 ist prim");
        Pruef.falsch(Aufgaben.istPrimzahl(100), "100 ist nicht prim");
        Pruef.wahr(Aufgaben.istPrimzahl(7919), "7919 ist prim");

        Pruef.abschnitt("Aufgabe 4: fakultaet");
        Pruef.gleich(1L, Aufgaben.fakultaet(0), "0! = 1");
        Pruef.gleich(1L, Aufgaben.fakultaet(1), "1! = 1");
        Pruef.gleich(120L, Aufgaben.fakultaet(5), "5! = 120");
        Pruef.gleich(2_432_902_008_176_640_000L, Aufgaben.fakultaet(20), "20! passt gerade noch in long");

        Pruef.abschnitt("Aufgabe 5: quersumme");
        Pruef.gleich(0, Aufgaben.quersumme(0), "quersumme(0)");
        Pruef.gleich(5, Aufgaben.quersumme(5), "quersumme(5)");
        Pruef.gleich(10, Aufgaben.quersumme(1234), "quersumme(1234)");
        Pruef.gleich(1, Aufgaben.quersumme(1000000), "quersumme(1000000)");

        Pruef.abschnitt("Aufgabe 6: summeVielfache");
        Pruef.gleich(0, Aufgaben.summeVielfache(2), "bis 2");
        Pruef.gleich(33, Aufgaben.summeVielfache(10), "bis 10");
        Pruef.gleich(233168, Aufgaben.summeVielfache(999), "bis 999 (Project Euler 1)");

        Pruef.abschnitt("Aufgabe 7: sternDreieck");
        Pruef.gleich("", Aufgaben.sternDreieck(0), "Hoehe 0");
        Pruef.gleich("*\n", Aufgaben.sternDreieck(1), "Hoehe 1");
        Pruef.gleich("*\n**\n***\n", Aufgaben.sternDreieck(3), "Hoehe 3");

        Pruef.bericht();
    }
}
