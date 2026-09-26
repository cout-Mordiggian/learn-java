# Abschlussprojekt — Aufgabenverwaltung

Eine Konsolen-Anwendung, die alles aus dem Kurs zusammenführt: eigene Typen,
Kapselung, Collections, Streams, Exceptions und Dateizugriff.

Anders als in den Kapiteln gibt es hier **keine Tests, die dir sagen, wann du
fertig bist**. Genau das ist der Punkt: Du entscheidest über den Entwurf.

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

Beim Beenden werden die Aufgaben in `aufgaben.csv` gespeichert und beim nächsten
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
  - `Aufgabe abgehakt()` — gibt eine Kopie mit `erledigt = true` zurück
  - `boolean istUeberfaellig()` — Termin in der Vergangenheit und nicht erledigt

  *Tipp für spätere Tests (M6):* Mit `LocalDate.now()` im Inneren hängt das
  Ergebnis vom Tag ab, an dem der Test läuft. Testbarer ist
  `boolean istUeberfaellig(LocalDate heute)` — der Aufrufer übergibt das Datum,
  und `istUeberfaellig()` ruft einfach `istUeberfaellig(LocalDate.now())` auf.

`LocalDate` erklärt Kapitel 10, Abschnitt 10.7 — das Wichtigste:
`LocalDate.now()`, `LocalDate.parse("2026-09-05")`, `d.isBefore(andere)`.
Nutze **niemals** `java.util.Date` — die Klasse ist seit 2014 abgelöst.

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
*Entwurfsfrage:* checked oder unchecked? Begründe deine Wahl in einem Kommentar.

Die Liste ist der einzige Ort, an dem IDs vergeben werden — und `alle()` gibt
keine veränderbare Referenz nach draussen (Kapitel 5).

Der Konstruktor bekommt die beim Start geladenen Aufgaben (leere Liste beim
ersten Start). Die nächste freie ID ist dann *größte vorhandene ID + 1*,
nicht `size() + 1` — sonst vergibst du nach einem Löschen eine ID doppelt.
Kopiere die übergebene Liste, statt sie direkt zu speichern (Kapitel 5).

### M3 — Abfragen *(Kapitel 9)*

```java
List<Aufgabe> offene()
List<Aufgabe> nachPrioritaet()                    // HOCH zuerst, dann nach Termin
List<Aufgabe> ueberfaellige()
List<Aufgabe> suche(String text)                  // Titel enthaelt text, ohne Gross/Klein
Map<Prioritaet, Long> anzahlProPrioritaet()
```

Alles mit Streams. `Comparator.comparing(...).thenComparing(...)` und
`Comparator.nullsLast(...)` für die Termine.

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

Fallen, die du lösen musst:

- Ein Titel mit `;` darin zerlegt deine Zeile. Wie gehst du damit um?
  (Ablehnen beim Anlegen ist eine völlig legitime Antwort — dokumentiere sie.)
- Leeres Datumsfeld -> `null`
- `split(";", -1)`, sonst verschwindet das leere letzte Feld
- Kaputte Zeile: Überspringen mit Warnung oder abbrechen? Entscheide und
  begründe es.
- `Prioritaet.valueOf(...)` wirft bei unbekanntem Text

### M5 — Benutzeroberfläche *(Kapitel 2, 3, 11)*

Klasse `Konsole` mit der Menüschleife. Anforderungen:

- Ungültige Eingaben dürfen das Programm **nie** abstürzen lassen
- Zahlen mit `Integer.parseInt` in `try`/`catch`, nicht `scanner.nextInt()`
- Die Tabelle sauber ausrichten, wie im Beispiel oben:
  `String.format("  %4s  %-8s %-12s %s", id, prio, termin, titel)` — `%4s`
  richtet rechtsbündig aus, `%-8s` linksbündig auf 8 Zeichen
- Beim Beenden speichern
- `laden` und `speichern` werfen `IOException` (checked). `main` muss sie also
  fangen und eine verständliche Meldung ausgeben — oder `main` bekommt
  `throws IOException`. Entscheide dich; ein Stacktrace ist keine Meldung
  für Benutzer.

### M6 — Ausbau (freiwillig)

- `--datei pfad` als Kommandozeilenargument (`String[] args` in `main`)
- Unteraufgaben — dann wird das Datenmodell rekursiv
- Sortierrichtung umschaltbar
- Farbige Ausgabe für überfällige Aufgaben (ANSI-Codes wie in `lib/Pruef.java`)
- Statt CSV: JSON von Hand schreiben und lesen
- JUnit-Tests für `Aufgabenliste` und `CsvSpeicher`
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

- `Main.java` — ein Gerüst, das schon jetzt kompiliert und startet und nur
  einen Hinweis ausgibt. Es verweist bewusst noch auf keine Klasse aus M2–M5
  (die stehen nur im Kommentar), damit `starte.sh` von Anfang an läuft.
  Den Kommentar ersetzt du nach und nach durch echten Code.
- `Prioritaet.java` — fertig.
- `Aufgabe.java` — das Gerüst für M1: die Komponenten stehen, der kompakte
  Konstruktor und die beiden Methoden sind noch `TODO` (die Methoden werfen
  bis dahin `UnsupportedOperationException`).

---

## Woran du merkst, dass es gut ist

- **Jede Klasse hat einen Job.** `Konsole` rechnet nicht, `Aufgabenliste` gibt
  nichts aus, `CsvSpeicher` kennt kein Menü. Wenn du in `Aufgabenliste` ein
  `System.out.println` schreibst, ist etwas verrutscht.
- **Keine `public`-Felder.** Zustand ändert sich nur über Methoden.
- **Kein `null` als Rückgabewert** — `Optional` oder eine leere Liste.
  Die eine bewusste Ausnahme ist `faellig()`: Die Record-Komponente darf `null`
  sein (= kein Termin). Wer das vermeiden will, ergänzt eine Methode
  `Optional<LocalDate> termin()` und benutzt nur noch die.
- **Fehlermeldungen nennen den Kontext.** "Aufgabe 42 existiert nicht" statt
  "Fehler".
- **Du kannst `Konsole` austauschen**, ohne den Rest anzufassen. Das ist der
  eigentliche Test für die Trennung der Zuständigkeiten.

## Wenn du steckenbleibst

Sieh nicht in eine Musterlösung (es gibt hier absichtlich keine), sondern
zurück ins passende Kapitel. Und bau kleiner: Ein Programm, das nur Aufgaben
anlegen und anzeigen kann, aber sauber, ist mehr wert als sechs halbfertige
Funktionen.
