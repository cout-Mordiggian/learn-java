# Java von Grund auf

Ein vollständiger Selbstlernkurs: **Dokumentation + Aufgaben + Musterlösungen**,
ausführbar direkt im Terminal, ohne Maven, Gradle oder IDE.

Zielsprache: **Java 21 (LTS) oder neuer.** Der Kurs nutzt nur Sprachmittel, die
es seit Java 21 gibt, und läuft deshalb mit jedem neueren JDK (auf diesem
Rechner ist gerade JDK 27 installiert — `java -version` zeigt es). Wo neuere
Versionen eine Regel gelockert haben, steht ein Hinweis *"Seit Java 25"*.

---

## In 60 Sekunden starten

```bash
cd ~/dev/learn-java   # bzw. der Ordner, in den du den Kurs geklont hast
./lerne.sh            # Kapiteluebersicht
./lerne.sh 01         # Kapitel 1 pruefen (schlaegt anfangs fehl - das ist der Plan)
```

Dann:

1. `kapitel/01-erste-schritte/README.md` lesen.
2. Die `TODO`-Stellen in `kapitel/01-erste-schritte/src/Aufgaben.java` ausfüllen.
3. `./lerne.sh 01` erneut ausführen, bis alles grün ist.
4. Erst **danach** die Musterlösung ansehen: `./lerne.sh 01 -l` bzw. `kapitel/01-*/loesungen/`.
5. Die Rätsel unter **Was gibt das aus?** lösen und die Fragen unter
   **Selbstcheck** ohne Nachschlagen beantworten. Die Antworten stehen in
   `TIPPS.md` des Kapitels.

Meldet die Shell `Permission denied`, fehlt das Ausführungsrecht:
`chmod +x lerne.sh projekt/starte.sh` — oder einfach `bash lerne.sh 01` schreiben.
(Das passiert z. B. auf Cloud-Laufwerken, die keine Dateirechte speichern.)

> Der wichtigste Lerneffekt entsteht zwischen "es kompiliert nicht" und "es ist grün".
> Widerstehe der Versuchung, zu früh in `loesungen/` zu schauen.

---

## Aufbau des Kurses

| Nr | Kapitel | Worum es geht |
|----|---------|---------------|
| 01 | Erste Schritte | JVM, `main`, Kompilieren, Variablen, primitive Typen, Casting |
| 02 | Kontrollfluss | Operatoren, `if`/`switch`, Schleifen, `break`/`continue` |
| 03 | Strings und Arrays | Unveränderlichkeit, `equals` vs `==`, `StringBuilder`, 1D-/2D-Arrays |
| 04 | Methoden | Signatur, call-by-value, Überladung, Rekursion, Varargs |
| 05 | OOP I: Klassen und Objekte | Felder, Konstruktoren, Kapselung, `static`, `toString`/`equals` |
| 06 | OOP II: Vererbung und Interfaces | `extends`, Polymorphie, abstrakte Klassen, Interfaces, Sichtbarkeiten |
| 07 | Exceptions | checked/unchecked, `try`/`catch`/`finally`, try-with-resources, eigene Fehler |
| 08 | Collections und Generics | `List`, `Set`, `Map`, `Comparator`, Typparameter, Wildcards |
| 09 | Lambdas und Streams | funktionale Interfaces, Methodenreferenzen, Pipelines, `Optional` |
| 10 | Modernes Java | `var`, `record`, `enum`, `sealed`, Pattern Matching, Textblöcke |
| 11 | Dateien und IO | `Path`/`Files`, Zeichensätze, Zeilen-Streams, CSV |
| 12 | Nebenläufigkeit und Werkzeuge | Threads, `ExecutorService`, Race Conditions, Maven/Gradle, JUnit |
| 13 | Testen und Fehlersuche | eigene Tests schreiben, Grenzwerte, Mutanten entlarven, systematisch debuggen |
| 14 | Algorithmen und Datenstrukturen | Aufwand/O-Notation, Sortieren, eigene Liste und eigener Stapel |
|  — | [Abschlussprojekt](projekt/README.md) | Aufgabenverwaltung als Konsolen-Anwendung |

Zusätzlich:

- [`SETUP.md`](SETUP.md) — JDK, Editor, Kompilieren von Hand, Fehlermeldungen lesen
- [`spickzettel/syntax.md`](spickzettel/syntax.md) — Syntax auf einen Blick
- [`spickzettel/glossar.md`](spickzettel/glossar.md) — Begriffe von "Bytecode" bis "Wildcard"
- [`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md) — die häufigsten Compiler- und Laufzeitfehler und was sie bedeuten

---

## Wie ein Kapitel aufgebaut ist

```
kapitel/05-oop-klassen-und-objekte/
├── README.md        Dokumentation: Konzepte, Beispiele, Fallstricke,
│                    Aufgaben, "Was gibt das aus?", Selbstcheck
├── TIPPS.md         gestufte Hinweise je Aufgabe + Antworten zum Selbstcheck
├── src/             DEIN Arbeitsbereich - hier stehen die TODOs
├── tests/           Pruefungen (nicht aendern - sie definieren die Aufgabe)
└── loesungen/       Musterloesung mit Kommentaren (erst danach lesen)
```

`./lerne.sh 05` kompiliert `lib/ + src/ + tests/` und startet die Prüfungen.
`./lerne.sh 05 -l` macht dasselbe mit `loesungen/` statt `src/`.

Eigenen Code ausprobieren? Lege eine Klasse mit `main`-Methode in `src/` an
und starte sie mit `./lerne.sh 05 -r MeinTest`.

Für schnelle Einzeiler ("Was ergibt eigentlich `7 / 2`?") ist **`jshell`** noch
bequemer — die interaktive Java-Konsole, siehe [`SETUP.md`](SETUP.md).

---

## Wenn du feststeckst

1. **Fehlermeldung lesen** — den *ersten* Fehler, von oben. Nachschlagen in
   [`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md).
2. **Den Test lesen.** `tests/Tests.java` zeigt genau, welche Eingabe welches
   Ergebnis erwartet. Die Tests sind die eigentliche Aufgabenstellung.
3. **Klein ausprobieren** — in `jshell` oder mit `System.out.println` in einer
   eigenen Klasse (`./lerne.sh NN -r MeinTest`).
4. **Das README-Kapitel noch einmal lesen**, gezielt den Abschnitt zum Thema.
5. **`TIPPS.md` im Kapitelordner** — pro Aufgabe drei Stufen, von "in welche
   Richtung denken" bis "Gerüst mit Lücken". Immer nur die nächste Stufe
   aufklappen, dann wieder selbst probieren.
6. Erst dann: die Musterlösung — und danach die Aufgabe **noch einmal ohne
   Vorlage** lösen. Abschreiben allein bringt nichts.

---

## Empfohlener Rhythmus

- **Ein Kapitel pro Sitzung**, 45–90 Minuten. Kapitel 5, 6, 8 und 9 sind die dicksten Brocken.
- Die Kapitel 13 und 14 stehen hinten, gehören aber **dazwischen**:
  **Kapitel 13 direkt nach Kapitel 7**, **Kapitel 14 nach Kapitel 8**.
  Empfohlene Reihenfolge also: 1–7, **13**, 8, **14**, 9–12.
- Nach jedem Kapitel: eine Sache aus dem Kapitel **freihändig** nachbauen, ohne README.
- Nach Kapitel 8: Meilensteine M1 und M2 des Abschlussprojekts angehen.
  M1 benutzt `record` und `enum` — lies dafür vorab die Abschnitte 10.2 und
  10.3, sie sind kurz und setzen nur Kapitel 5 voraus.
- Nach Kapitel 11: das Projekt fertigstellen (M3–M5), mit Kapitel 12 erweitern (M6).
- Wiederholen lohnt sich: Die Selbstcheck-Fragen von zwei Kapiteln zuvor nach
  einer Woche noch einmal beantworten — Vergessenes jetzt aufzufrischen ist billiger als später.

Wenn du weniger Zeit hast: Kapitel 1–8 und 13 sind der unverzichtbare Kern.
Kapitel 9–12 machen dich vom "kann Java lesen" zum "schreibe Java, wie man es heute schreibt".

---

## Was dieser Kurs bewusst weglässt

Module (JPMS), Reflection, Annotationsprozessoren, JNI, Frameworks wie Spring.
Das sind Aufbauthemen — sie ergeben erst Sinn, wenn die Grundlagen sitzen.
Am Ende von Kapitel 12 findest du einen Ausblick, wie es danach weitergeht.
