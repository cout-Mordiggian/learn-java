package de.beispiel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * So sieht ein JUnit-5-Test aus. Diese Datei gehoert nach
 * src/test/java/de/beispiel/ in einem Maven- oder Gradle-Projekt.
 *
 * Vergleiche mit lib/Pruef.java aus diesem Kurs: dieselben Ideen,
 * nur ausgereifter.
 *
 *   Pruef.gleich(a, b, "text")   ->  assertEquals(a, b, "text")
 *   Pruef.wahr(b, "text")        ->  assertTrue(b, "text")
 *   Pruef.wirft(T.class, r, "t") ->  assertThrows(T.class, r)
 */
class BeispielTest {

    private Konto konto;

    /** Laeuft vor JEDEM Test neu - jeder Test startet auf gruener Wiese. */
    @BeforeEach
    void aufbauen() {
        konto = new Konto("Anna", 1000);
    }

    @Test
    @DisplayName("Eine Einzahlung erhoeht das Guthaben um den Betrag")
    void einzahlenErhoehtGuthaben() {
        konto.einzahlen(500);
        assertEquals(1500, konto.getGuthaben());
    }

    @Test
    void abhebenOhneDeckungAendertNichts() {
        boolean erfolg = konto.abheben(9999);

        assertEquals(false, erfolg);
        assertEquals(1000, konto.getGuthaben(), "Guthaben darf sich nicht geaendert haben");
    }

    /** Ein Test pro Wert - die Meldung nennt den fehlgeschlagenen. */
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void ungueltigeBetraegeWerdenAbgelehnt(int betrag) {
        assertThrows(IllegalArgumentException.class, () -> konto.einzahlen(betrag));
    }

    /** Verwandte Tests gruppieren - die Ausgabe wird dadurch lesbar. */
    @Nested
    class Ueberweisungen {

        @Test
        void erfolgreicheUeberweisungBelastetBeideSeiten() {
            Konto ziel = new Konto("Bert", 0);

            assertTrue(konto.ueberweiseAn(ziel, 400));
            assertEquals(600, konto.getGuthaben());
            assertEquals(400, ziel.getGuthaben());
        }
    }

    // Was einen guten Test ausmacht:
    //  - EIN Verhalten pro Test
    //  - der Name ist ein Satz, der beschreibt, was gelten soll
    //  - Aufbau: Vorbereiten - Ausfuehren - Pruefen (arrange, act, assert)
    //  - keine Abhaengigkeit zwischen Tests, keine Reihenfolge
    //  - Randfaelle mitnehmen: leer, null, negativ, genau an der Grenze
}
