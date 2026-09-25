# Kapitel 10 — Modernes Java

**Ziel:** Du schreibst Java, wie es 2026 geschrieben wird: `record` statt
Boilerplate, `enum` mit Verhalten, `sealed` plus `switch`-Pattern-Matching
statt `instanceof`-Ketten.

---

## 10.1 `var` — lokale Typinferenz (Java 10)

```java
var namen = new ArrayList<String>();          // ArrayList<String>
var eintrag = map.entrySet().iterator().next();  // spart einen Bandwurmtyp
for (var e : map.entrySet()) { ... }
```

Nur fuer **lokale Variablen** mit Initialisierung. Nicht fuer Felder, nicht fuer
Parameter, nicht fuer Rueckgabetypen.

**Nimm `var`,** wenn der Typ rechts ohnehin dasteht (`var x = new Kunde()`) oder
der Typ so lang ist, dass er nichts erklaert.
**Nimm ihn nicht,** wenn der Typ die einzige Information ist:
`var ergebnis = berechne();` sagt dem Leser nichts.

## 10.2 `record` — Datenklassen in einer Zeile (Java 16)

```java
public record Punkt(double x, double y) { }
```

Der Compiler erzeugt daraus: private final Felder, einen Konstruktor,
Zugriffsmethoden `x()` und `y()` (ohne `get`!), `equals`, `hashCode` und
`toString` — die ganze Klasse aus Kapitel 5 in einer Zeile.

```java
var p = new Punkt(3, 4);
p.x();                      // 3.0    - kein getX()
p.toString();               // "Punkt[x=3.0, y=4.0]"
p.equals(new Punkt(3, 4));  // true
```

### Kompakter Konstruktor — fuer Validierung

```java
public record Artikel(String name, long preisCent) {
    public Artikel {                                    // keine Parameterliste!
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name fehlt");
        }
        if (preisCent < 0) {
            throw new IllegalArgumentException("Preis negativ");
        }
        name = name.strip();          // Parameter darf man normalisieren
    }
}
```

Die Zuweisung an die Felder passiert danach automatisch. Du darfst die
Parameter aendern (`name = name.strip()`), aber nicht `this.name` setzen.

### Zusaetzliche Methoden und statische Fabriken

```java
public record Artikel(String name, long preisCent, int menge) {
    public long gesamtCent() { return preisCent * menge; }

    public static Artikel einzeln(String name, long preisCent) {
        return new Artikel(name, preisCent, 1);
    }
}
```

### Grenzen

- Records sind **immer** `final` und ihre Felder **immer** `final`
- Aber nur *flach* unveraenderlich: Ist eine Komponente eine `ArrayList`, kann
  man die Liste trotzdem aendern (Kapitel 5.5). Abhilfe im kompakten
  Konstruktor: `namen = List.copyOf(namen);`
- Keine zusaetzlichen Instanzfelder ausser den Komponenten
- Keine Vererbung (aber `implements` beliebig vieler Interfaces)

**Wann ein `record`?** Immer wenn ein Typ vor allem *Daten transportiert*:
DTOs, Koordinaten, Ergebnisobjekte, Konfigurationswerte, Schluessel fuer Maps.
Wenn der Typ dagegen veraenderlichen Zustand und Verhalten kapselt (wie das
`Konto` aus Kapitel 5), bleibt es eine normale Klasse.

## 10.3 `enum` — mehr als eine Liste von Konstanten

```java
public enum Ampel {
    ROT, GELB, GRUEN
}

Ampel a = Ampel.ROT;
a.name();       // "ROT"
a.ordinal();    // 0     - benutze das nicht fuer Fachlogik!
Ampel.values(); // Ampel[]{ROT, GELB, GRUEN}
Ampel.valueOf("ROT");
```

Enums sind vollwertige Klassen — mit Feldern, Konstruktor und Methoden:

```java
public enum Wochentag {
    MONTAG(true), SAMSTAG(false), SONNTAG(false);

    private final boolean werktag;

    Wochentag(boolean werktag) {        // Konstruktor ist implizit private
        this.werktag = werktag;
    }

    public boolean istWerktag() { return werktag; }
}
```

Jede Konstante darf sogar eigenes Verhalten mitbringen:

```java
public enum Rechenart {
    PLUS { public int rechne(int a, int b) { return a + b; } },
    MAL  { public int rechne(int a, int b) { return a * b; } };

    public abstract int rechne(int a, int b);
}
```

Im `switch`-**Ausdruck** (Pfeilform mit Ergebnis, Kapitel 2) prueft der
Compiler bei Enums die Vollstaendigkeit — kommt eine Konstante dazu, zeigt er
dir jede Stelle, die du anpassen musst. (Eine klassische `switch`-Anweisung
ohne Ergebnis prueft er nicht; ein weiterer Grund fuer die Pfeilform.) Das ist der
Hauptgrund, Enums statt `String`-Konstanten oder `int`-Codes zu verwenden.

`ordinal()` haengt an der Deklarationsreihenfolge. Speicherst du den Wert
irgendwo, bricht das erste Umsortieren deine Daten. Nutze `name()`.

## 10.4 `sealed` — kontrollierte Hierarchien (Java 17)

```java
public sealed interface Form permits Kreis, Rechteck, Dreieck { }

public record Kreis(double radius) implements Form { }
public record Rechteck(double breite, double hoehe) implements Form { }
public record Dreieck(double a, double b, double c) implements Form { }
```

`sealed` heisst: **Nur diese** Typen duerfen `Form` implementieren. Damit weiss
der Compiler, dass die Liste vollstaendig ist.

Sind alle Untertypen in **derselben Datei** (z. B. als verschachtelte Records),
darf `permits` entfallen.

Jeder erlaubte Untertyp muss selbst `final`, `sealed` oder `non-sealed` sein —
sonst waere das Siegel loechrig. Records sind automatisch `final`.

## 10.5 Pattern Matching

### `instanceof` mit Bindung (Java 16)

```java
if (o instanceof String s && s.length() > 3) {
    System.out.println(s.toUpperCase());     // s ist schon vom Typ String
}
```

### `switch` mit Typmustern (Java 21)

```java
String text = switch (o) {
    case Integer i  -> "Zahl: " + i;
    case String s   -> "Text: " + s;
    case null       -> "nichts";              // null ist ein eigener Fall!
    default         -> "unbekannt";
};
```

Mit Bedingung (*guarded pattern*):

```java
String bewertung = switch (zahl) {
    case Integer i when i < 0   -> "negativ";
    case Integer i when i == 0  -> "null";
    case Integer i              -> "positiv";
    default                     -> "keine Zahl";
};
```

Die Reihenfolge zaehlt: Das erste passende Muster gewinnt.

### Record-Muster (Java 21)

```java
double flaeche = switch (form) {
    case Kreis(double r)              -> Math.PI * r * r;
    case Rechteck(double b, double h) -> b * h;
    case Dreieck(double a, double b, double c) -> heron(a, b, c);
};
```

Das Muster **zerlegt** den Record direkt in seine Komponenten — kein
`k.radius()` noetig. Statt des Typs darfst du auch `var` schreiben
(`case Kreis(var r)`), und *seit Java 22* steht `_` fuer eine Komponente, die
dich nicht interessiert: `case Rechteck(var b, _) -> ...`. Und weil `Form` `sealed` ist, braucht dieser `switch`
**kein `default`**: Der Compiler weiss, dass alle Faelle abgedeckt sind.

Der Gewinn wird beim Erweitern sichtbar: Kommt `Dreieck` dazu, meldet der
Compiler **jeden** `switch`, dem der Fall fehlt. Bei einer `instanceof`-Kette
mit `else` haettest du still ein falsches Ergebnis bekommen.

### Sealed + Records + Switch = algebraische Datentypen

Diese drei Features zusammen sind Javas Antwort auf das, was funktionale
Sprachen "sum types" nennen. Typisches Muster: ein `sealed interface Ergebnis`
mit `record Erfolg(T wert)` und `record Fehler(String grund)`.

## 10.6 Textbloecke (Java 15)

```java
String abfrage = """
        SELECT name, alter
        FROM person
        WHERE stadt = 'Koeln'
        """;
```

Die gemeinsame Einrueckung wird abgeschnitten; die Position der schliessenden
`"""` bestimmt, wie viel. `\` am Zeilenende unterdrueckt den Umbruch,
`\s` erzwingt ein Leerzeichen am Zeilenende.

Praktisch mit `formatted`:

```java
String brief = """
        Hallo %s,
        dein Guthaben betraegt %d Cent.
        """.formatted(name, guthaben);
```

## 10.7 Datum und Zeit mit `java.time`

Seit Java 8 gibt es eine durchdachte Datums-API — und sie folgt genau den
Ideen dieses Kapitels: unveraenderliche Wertobjekte, Enums, klare Typen.

| Typ | Bedeutung | Beispiel |
|-----|-----------|----------|
| `LocalDate` | Datum ohne Uhrzeit | `2026-09-25` |
| `LocalTime` | Uhrzeit ohne Datum | `14:30` |
| `LocalDateTime` | beides, ohne Zeitzone | `2026-09-25T14:30` |
| `ZonedDateTime` | mit Zeitzone | fuer Termine ueber Laendergrenzen |
| `Duration` / `Period` | Zeitspanne in Sekunden / in Tagen-Monaten-Jahren | `PT2H`, `P2M29D` |
| `DayOfWeek`, `Month` | Enums | `DayOfWeek.FRIDAY` |

```java
LocalDate heute   = LocalDate.now();
LocalDate termin  = LocalDate.of(2026, 12, 24);
LocalDate parsed  = LocalDate.parse("2026-09-05");     // ISO-Format jjjj-mm-tt

termin.isBefore(heute)                  // true/false
termin.plusDays(7)                      // NEUES Objekt - termin bleibt unveraendert!
LocalDate.of(2026, 1, 31).plusMonths(1) // 2026-02-28 - rechnet kalenderrichtig
ChronoUnit.DAYS.between(heute, termin)  // Tage dazwischen (long)
termin.getDayOfWeek()                   // DayOfWeek.THURSDAY

DateTimeFormatter deutsch = DateTimeFormatter.ofPattern("dd.MM.yyyy");
termin.format(deutsch)                  // "24.12.2026"
LocalDate.parse("24.12.2026", deutsch)  // und zurueck
```

`LocalDate.parse("2026-02-30")` wirft eine `DateTimeParseException` — ungueltige
Daten gibt es gar nicht erst.

Merke dir drei Dinge:

- Wie bei `String`: `plusDays` & Co. aendern nichts, sie liefern ein **neues**
  Objekt. `termin.plusDays(7);` ohne Zuweisung tut nichts.
- **Nie** `java.util.Date` oder `Calendar` — veraenderlich, Monate ab 0
  gezaehlt, voller Fallen. Nur noch in altem Code.
- Fuer testbaren Code `LocalDate.now()` nicht tief im Inneren aufrufen, sondern
  "heute" als Parameter hereinreichen: `istUeberfaellig(LocalDate heute)`.
  Dann kann ein Test jedes beliebige Datum vorgeben. (Das brauchst du im
  Abschlussprojekt.)

## 10.8 Kurzueberblick: was noch dazukam

| Version | Feature |
|---------|---------|
| 8 | Lambdas, Streams, `Optional`, `java.time` |
| 9 | `List.of`, Module |
| 10 | `var` |
| 11 | `String.strip/isBlank/repeat`, `Files.readString`, HTTP-Client |
| 14 | `switch`-Ausdruck, hilfreiche NPE-Meldungen |
| 15 | Textbloecke |
| 16 | `record`, `instanceof`-Pattern, `Stream.toList` |
| 17 | `sealed` |
| 21 | Pattern Matching im `switch`, Record-Muster, virtuelle Threads, `getFirst`/`getLast` (Sequenced Collections) |
| 22 | `_` fuer unbenutzte Variablen und Muster, `java` startet Programme aus mehreren Quelldateien |
| 23 | Javadoc-Kommentare in Markdown (`///`) |
| 25 | `void main()` ohne Klasse + `IO.println`, Anweisungen vor `super(...)`, `import module java.base;` |

**LTS-Versionen** (8, 11, 17, 21, 25) bekommen jahrelang Updates — in
Unternehmen laeuft fast immer eine davon. Die Versionen dazwischen erscheinen
alle sechs Monate und sind ein guter Blick auf das, was als Naechstes kommt.
Neue Features starten oft als *Preview* und muessen dann mit
`--enable-preview` freigeschaltet werden.

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Vier Dateien in [`src/`](src/) — pruefen mit `./lerne.sh 10`.

### `Artikel.java` — Record mit Validierung

`record Artikel(String name, long preisCent, int menge)`:

- Kompakter Konstruktor: `null`, leerer oder nur aus Leerzeichen bestehender
  Name, negativer Preis, negative Menge -> `IllegalArgumentException`
  (auch bei `null` bitte keine `NullPointerException`); ausserdem `name` mit
  `strip()` normalisieren
- `long gesamtCent()`
- `static Artikel einzeln(String name, long preisCent)`
- `Artikel mitMenge(int neueMenge)` — neues Objekt, unveraenderlich bleiben!

### `Wochentag.java` — Enum mit Zustand

Sieben Konstanten, Feld `werktag`, dazu:

- `boolean istWerktag()`
- `Wochentag naechster()` — nach `SONNTAG` kommt wieder `MONTAG`
  (Tipp: `values()` und `ordinal()`)
- `static Wochentag vonNummer(int n)` — 1 = MONTAG … 7 = SONNTAG,
  alles andere -> `IllegalArgumentException`

### `Form.java` — sealed interface mit verschachtelten Records

`Kreis`, `Rechteck`, `Dreieck` als Records **innerhalb** des Interfaces.
Negative Werte (bei jeder Komponente) mit `IllegalArgumentException` ablehnen.

Bewusst offen gelassen: Ein "Dreieck" wie `(1, 1, 5)` verletzt die
Dreiecksungleichung, und `NaN` rutscht durch jede `< 0`-Pruefung — in beiden
Faellen liefert `flaeche` dann `NaN`. Wer mag, lehnt auch das ab; die Tests
pruefen nur negative Werte.

### `Aufgaben.java`

1. **`flaeche(Form)`** — `switch` mit **Record-Mustern**, **ohne `default`**.
   Dreieck nach Heron: `s = (a+b+c)/2`, `A = sqrt(s(s-a)(s-b)(s-c))`.
2. **`benenne(Form)`** -> `"Kreis mit Radius 2.0"`, `"Rechteck 3.0x4.0"`,
   `"Dreieck 3.0/4.0/5.0"`. Nutze `Locale.ROOT` beim Formatieren.
3. **`beschreibe(Object)`** — Pattern Matching mit Bedingungen:
   `null` -> `"nichts"`, negative Zahl -> `"negative Zahl"`, `0` -> `"null"`,
   positive Zahl -> `"positive Zahl"`, leerer String -> `"leerer Text"`,
   sonst String -> `"Text der Laenge 5"`, alles andere -> `"unbekannt"`.
4. **`steckbrief(Artikel)`** — Textblock:
   ```
   Artikel: Kaffee
   Preis:   499 Cent
   Menge:   3
   Gesamt:  1497 Cent
   ```
   (mit abschliessendem Zeilenumbruch)
5. **`werktageZaehlen(List<Wochentag>)`** -> `long`.

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
record Punkt(int x, int y) { }

System.out.println(new Punkt(1, 2));
```

<details><summary>Aufloesung</summary>

`Punkt[x=1, y=2]` — Das `toString` hat der Compiler erzeugt. Beachte die eckigen Klammern und die Feldnamen.

</details>

**2.**

```java
record Team(List<String> namen) { }

List<String> l = new ArrayList<>(List.of("Anna"));
Team t = new Team(l);
l.add("Bert");
System.out.println(t.namen());
```

<details><summary>Aufloesung</summary>

`[Anna, Bert]` — Der Record speichert nur die **Referenz** auf die Liste. Wer die Liste von aussen aendert, aendert den "unveraenderlichen" Record mit. Abhilfe: `namen = List.copyOf(namen);` im kompakten Konstruktor.

</details>

**3.**

```java
Object o = 42;
String s = switch (o) {
    case Integer i when i > 40 -> "gross";
    case Integer i             -> "Zahl";
    default                    -> "?";
};
System.out.println(s);
```

<details><summary>Aufloesung</summary>

`gross` — Die Faelle werden von oben nach unten geprueft, das erste passende Muster samt Bedingung gewinnt. Stuende `case Integer i` ohne `when` oben, meldete der Compiler den zweiten Fall als unerreichbar (*dominated*).

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Wann `record`, wann normale Klasse?
- Warum ist `ordinal()` gefaehrlich, sobald Werte gespeichert werden?
- Warum braucht ein `switch` ueber ein `sealed interface` kein `default`?
- Was ist der Unterschied zwischen `case Kreis k` und `case Kreis(double r)`?
- Warum darf ein kompakter Konstruktor `name = name.strip()`, aber nicht `this.name = ...`?
