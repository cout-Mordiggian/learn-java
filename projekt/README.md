# Abschlussprojekt — Aufgabenverwaltung

Eine Konsolen-Anwendung, die alles aus dem Kurs zusammenfuehrt: eigene Typen,
Kapselung, Collections, Streams, Exceptions und Dateizugriff.

Anders als in den Kapiteln gibt es hier **keine Tests, die dir sagen, wann du
fertig bist**. Genau das ist der Punkt: Du entscheidest ueber den Entwurf.

---

## Was am Ende laufen soll

```
$ bash starte.sh

  Aufgabenverwaltung  (14 Aufgaben, 3 offen)
  ------------------------------------------
  1  Aufgaben anzeigen
  2  Aufgabe hinzufuegen
  3  Aufgabe abhaken
  4  Aufgabe loeschen
  5  Suchen
  6  Statistik
  0  Beenden

> 1

    ID  Prio     Faellig      Titel
  --------------------------------------------------
     1  HOCH     2026-09-05   [ ] Steuererklaerung
     2  MITTEL   2026-09-20   [x] Reifen wechseln
     3  NIEDRIG  -            [ ] Keller aufraeumen
```

Beim Beenden werden die Aufgaben in `aufgaben.csv` gespeichert und beim naechsten
Start wieder geladen.

---

## Meilensteine

Arbeite sie der Reihe nach ab. Nach jedem Schritt soll das Programm laufen —
nicht alles auf einmal bauen.

### M1 — Datenmodell *(Kapitel 5, 10)*

- `enum Prioritaet { NIEDRIG, MITTEL, HOCH }`
- `record Aufgabe(int id, String titel, Prioritaet prioritaet, boolean erledigt, LocalDate faellig)`
  - Kompakter Konstruktor: Titel darf nicht leer sein, `id` muss positiv sein
  - `faellig` darf `null` sein (= kein Termin)
  - `Aufgabe abgehakt()` — gibt eine Kopie mit `erledigt = true` zurueck
  - `boolean istUeberfaellig()` — Termin in der Vergangenheit und nicht erledigt

  *Tipp fuer spaetere Tests (M6):* Mit `LocalDate.now()` im Inneren haengt das
  Ergebnis vom Tag ab, an dem der Test laeuft. Testbarer ist
  `boolean istUeberfaellig(LocalDate heute)` — der Aufrufer uebergibt das Datum,
  und `istUeberfaellig()` ruft einfach `istUeberfaellig(LocalDate.now())` auf.

`LocalDate` erklaert Kapitel 10, Abschnitt 10.7 — das Wichtigste:
`LocalDate.now()`, `LocalDate.parse("2026-09-05")`, `d.isBefore(andere)`.
Nutze **niemals** `java.util.Date` — die Klasse ist seit 2014 abgeloest.

### M2 — Verwaltung *(Kapitel 7, 8)*

Klasse `Aufgabenliste`:

```java
Aufgabenliste(List<Aufgabe> geladene)                            // Startbestand, z. B. aus der CSV
int hinzufuegen(String titel, Prioritaet p, LocalDate faellig)   // vergibt die ID, gibt sie zurueck
void abhaken(int id)                                             // wirft, wenn es die ID nicht gibt
void loeschen(int id)
Optional<Aufgabe> finde(int id)
List<Aufgabe> alle()                                             // unveraenderliche Sicht!
```

Eigene Exception `AufgabeNichtGefundenException`.
*Entwurfsfrage:* checked oder unchecked? Begruende deine Wahl in einem Kommentar.

Die Liste ist der einzige Ort, an dem IDs vergeben werden — und `alle()` gibt
keine veraenderbare Referenz nach draussen (Kapitel 5).

Der Konstruktor bekommt die beim Start geladenen Aufgaben (leere Liste beim
ersten Start). Die naechste freie ID ist dann *groesste vorhandene ID + 1*,
nicht `size() + 1` — sonst vergibst du nach einem Loeschen eine ID doppelt.
Kopiere die uebergebene Liste, statt sie direkt zu speichern (Kapitel 5).

### M3 — Abfragen *(Kapitel 9)*

```java
List<Aufgabe> offene()
List<Aufgabe> nachPrioritaet()                    // HOCH zuerst, dann nach Termin
List<Aufgabe> ueberfaellige()
List<Aufgabe> suche(String text)                  // Titel enthaelt text, ohne Gross/Klein
Map<Prioritaet, Long> anzahlProPrioritaet()
```

Alles mit Streams. `Comparator.comparing(...).thenComparing(...)` und
`Comparator.nullsLast(...)` fuer die Termine.

### M4 — Persistenz *(Kapitel 11)*

Klasse `CsvSpeicher`:

```java
void speichern(Path pfad, List<Aufgabe> aufgaben) throws IOException
List<Aufgabe> laden(Path pfad) throws IOException     // fehlende Datei -> leere Liste
```

Format (Trennzeichen `;`, erste Zeile ist die Kopfzeile):

```
id;titel;prioritaet;erledigt;faellig
1;Steuererklaerung;HOCH;false;2026-09-05
3;Keller aufraeumen;NIEDRIG;false;
```

Fallen, die du loesen musst:

- Ein Titel mit `;` darin zerlegt deine Zeile. Wie gehst du damit um?
  (Ablehnen beim Anlegen ist eine voellig legitime Antwort — dokumentiere sie.)
- Leeres Datumsfeld -> `null`
- `split(";", -1)`, sonst verschwindet das leere letzte Feld
- Kaputte Zeile: Ueberspringen mit Warnung oder abbrechen? Entscheide und
  begruende es.
- `Prioritaet.valueOf(...)` wirft bei unbekanntem Text

### M5 — Benutzeroberflaeche *(Kapitel 2, 3, 11)*

Klasse `Konsole` mit der Menueschleife. Anforderungen:

- Ungueltige Eingaben duerfen das Programm **nie** abstuerzen lassen
- Zahlen mit `Integer.parseInt` in `try`/`catch`, nicht `scanner.nextInt()`
- Die Tabelle sauber ausrichten, wie im Beispiel oben:
  `String.format("  %4s  %-8s %-12s %s", id, prio, termin, titel)` — `%4s`
  richtet rechtsbuendig aus, `%-8s` linksbuendig auf 8 Zeichen
- Beim Beenden speichern
- `laden` und `speichern` werfen `IOException` (checked). `main` muss sie also
  fangen und eine verstaendliche Meldung ausgeben — oder `main` bekommt
  `throws IOException`. Entscheide dich; ein Stacktrace ist keine Meldung
  fuer Benutzer.

### M6 — Ausbau (freiwillig)

- `--datei pfad` als Kommandozeilenargument (`String[] args` in `main`)
- Unteraufgaben — dann wird das Datenmodell rekursiv
- Sortierrichtung umschaltbar
- Farbige Ausgabe fuer ueberfaellige Aufgaben (ANSI-Codes wie in `lib/Pruef.java`)
- Statt CSV: JSON von Hand schreiben und lesen
- JUnit-Tests fuer `Aufgabenliste` und `CsvSpeicher`
  (Kapitel 12, `werkzeuge/pom.xml` als Vorlage)

---

## Vorgeschlagene Struktur

```
projekt/
├── starte.sh
├── aufgaben.csv                (entsteht beim ersten Speichern)
└── src/
    ├── Main.java               Einstiegspunkt
    ├── Prioritaet.java         enum
    ├── Aufgabe.java            record
    ├── Aufgabenliste.java      Verwaltung
    ├── AufgabeNichtGefundenException.java
    ├── CsvSpeicher.java        Laden und Speichern
    └── Konsole.java            Menue und Ausgabe
```

Starten:

Aus dem Kursordner:

```bash
cd projekt && ./starte.sh
```

In `src/` liegen bereits drei Startdateien:

- `Main.java` — ein Geruest, das schon jetzt kompiliert und startet und nur
  einen Hinweis ausgibt. Es verweist bewusst noch auf keine Klasse aus M2–M5
  (die stehen nur im Kommentar), damit `starte.sh` von Anfang an laeuft.
  Den Kommentar ersetzt du nach und nach durch echten Code.
- `Prioritaet.java` — fertig.
- `Aufgabe.java` — das Geruest fuer M1: die Komponenten stehen, der kompakte
  Konstruktor und die beiden Methoden sind noch `TODO` (die Methoden werfen
  bis dahin `UnsupportedOperationException`).

---

## Woran du merkst, dass es gut ist

- **Jede Klasse hat einen Job.** `Konsole` rechnet nicht, `Aufgabenliste` gibt
  nichts aus, `CsvSpeicher` kennt kein Menue. Wenn du in `Aufgabenliste` ein
  `System.out.println` schreibst, ist etwas verrutscht.
- **Keine `public`-Felder.** Zustand aendert sich nur ueber Methoden.
- **Kein `null` als Rueckgabewert** — `Optional` oder eine leere Liste.
  Die eine bewusste Ausnahme ist `faellig()`: Die Record-Komponente darf `null`
  sein (= kein Termin). Wer das vermeiden will, ergaenzt eine Methode
  `Optional<LocalDate> termin()` und benutzt nur noch die.
- **Fehlermeldungen nennen den Kontext.** "Aufgabe 42 existiert nicht" statt
  "Fehler".
- **Du kannst `Konsole` austauschen**, ohne den Rest anzufassen. Das ist der
  eigentliche Test fuer die Trennung der Zustaendigkeiten.

## Wenn du steckenbleibst

Sieh nicht in eine Musterloesung (es gibt hier absichtlich keine), sondern
zurueck ins passende Kapitel. Und bau kleiner: Ein Programm, das nur Aufgaben
anlegen und anzeigen kann, aber sauber, ist mehr wert als sechs halbfertige
Funktionen.
