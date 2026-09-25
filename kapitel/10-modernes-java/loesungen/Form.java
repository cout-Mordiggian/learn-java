/**
 * Kapitel 10 - Musterloesung: sealed interface.
 *
 * "sealed" ohne permits ist erlaubt, weil alle Untertypen in derselben
 * Datei stehen. Damit weiss der Compiler abschliessend, welche Formen
 * es gibt - und kann in switch-Ausdruecken auf Vollstaendigkeit pruefen.
 */
public sealed interface Form {

    record Kreis(double radius) implements Form {
        public Kreis {
            if (radius < 0) throw new IllegalArgumentException("Radius negativ: " + radius);
        }
    }

    record Rechteck(double breite, double hoehe) implements Form {
        public Rechteck {
            if (breite < 0 || hoehe < 0) {
                throw new IllegalArgumentException("Seiten duerfen nicht negativ sein");
            }
        }
    }

    record Dreieck(double a, double b, double c) implements Form {
        public Dreieck {
            if (a < 0 || b < 0 || c < 0) {
                throw new IllegalArgumentException("Seiten duerfen nicht negativ sein");
            }
        }
    }
}
