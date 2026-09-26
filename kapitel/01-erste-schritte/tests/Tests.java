public class Tests {
    public static void main(String[] args) {
        Pruef.abschnitt("Aufgabe 1: begruessung");
        Pruef.gleich("Hallo, Anna!", Aufgaben.begruessung("Anna"), "begruessung(\"Anna\")");
        Pruef.gleich("Hallo, Greg!", Aufgaben.begruessung("Greg"), "begruessung(\"Greg\")");

        Pruef.abschnitt("Aufgabe 2: celsiusZuFahrenheit");
        Pruef.fastGleich(32.0, Aufgaben.celsiusZuFahrenheit(0.0), "0 Grad C sind 32 F");
        Pruef.fastGleich(212.0, Aufgaben.celsiusZuFahrenheit(100.0), "100 Grad C sind 212 F");
        Pruef.fastGleich(98.6, Aufgaben.celsiusZuFahrenheit(37.0), "37 Grad C sind 98.6 F");
        Pruef.fastGleich(-40.0, Aufgaben.celsiusZuFahrenheit(-40.0), "-40 Grad C sind -40 F");

        Pruef.abschnitt("Aufgabe 3: kreisFlaeche");
        Pruef.fastGleich(Math.PI, Aufgaben.kreisFlaeche(1.0), "Radius 1");
        Pruef.fastGleich(78.53981633974483, Aufgaben.kreisFlaeche(5.0), "Radius 5");
        Pruef.fastGleich(0.0, Aufgaben.kreisFlaeche(0.0), "Radius 0");

        Pruef.abschnitt("Aufgabe 4: letzteZiffer");
        Pruef.gleich(4, Aufgaben.letzteZiffer(1234), "letzteZiffer(1234)");
        Pruef.gleich(7, Aufgaben.letzteZiffer(7), "letzteZiffer(7)");
        Pruef.gleich(0, Aufgaben.letzteZiffer(0), "letzteZiffer(0)");
        Pruef.gleich(0, Aufgaben.letzteZiffer(1000), "letzteZiffer(1000)");

        Pruef.abschnitt("Aufgabe 5: zeitFormat");
        Pruef.gleich("00:00:00", Aufgaben.zeitFormat(0), "zeitFormat(0)");
        Pruef.gleich("00:00:59", Aufgaben.zeitFormat(59), "zeitFormat(59)");
        Pruef.gleich("00:01:00", Aufgaben.zeitFormat(60), "zeitFormat(60)");
        Pruef.gleich("01:01:01", Aufgaben.zeitFormat(3661), "zeitFormat(3661)");
        Pruef.gleich("23:59:59", Aufgaben.zeitFormat(86399), "zeitFormat(86399)");

        Pruef.abschnitt("Aufgabe 6: millisekundenProJahr");
        Pruef.gleich(31_536_000_000L, Aufgaben.millisekundenProJahr(), "365 Tage in ms");

        Pruef.bericht();
    }
}
