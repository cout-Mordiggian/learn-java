/**
 * Kapitel 10: ein sealed interface mit verschachtelten Records.
 *
 * Weil alle erlaubten Untertypen hier drin stehen, darf die
 * permits-Klausel entfallen - der Compiler sieht sie ja.
 *
 * Records sind automatisch final, erfuellen die sealed-Bedingung also von selbst.
 */
public sealed interface Form {

    record Kreis(double radius) implements Form {
        public Kreis {
            // TODO: negativen Radius mit IllegalArgumentException ablehnen
        }
    }

    record Rechteck(double breite, double hoehe) implements Form {
        public Rechteck {
            // TODO: negative Seiten ablehnen
        }
    }

    record Dreieck(double a, double b, double c) implements Form {
        public Dreieck {
            // TODO: negative Seiten ablehnen
        }
    }
}
