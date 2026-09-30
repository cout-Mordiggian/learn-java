# Kapitel 10: Modernes Java

**Ziel:** Du schreibst Java, wie es 2026 geschrieben wird: `record` statt
Boilerplate, `enum` mit Verhalten, `sealed` plus `switch`-Pattern-Matching
statt `instanceof`-Ketten.

---

## 10.1 `var`, lokale Typinferenz (Java 10)

```java
var namen = new ArrayList<String>();          // ArrayList<String>
var eintrag = map.entrySet().iterator().next();  // spart einen Bandwurmtyp
for (var e : map.entrySet()) { ... }
```

Nur für **lokale Variablen** mit Initialisierung. Nicht für Felder, nicht für
Parameter, nicht für Rückgabetypen.

**Nimm `var`,** wenn der Typ rechts ohnehin dasteht (`var x = new Kunde()`) oder
der Typ so lang ist, dass er nichts erklärt.
**Nimm ihn nicht,** wenn der Typ die einzige Information ist:
`var ergebnis = berechne();` sagt dem Leser nichts.

## 10.2 `record`: Datenklassen in einer Zeile (Java 16)

```java
public record Punkt(double x, double y) { }
```

Der Compiler erzeugt daraus: private final Felder, einen Konstruktor,
Zugriffsmethoden `x()` und `y()` (ohne `get`!), `equals`, `hashCode` und
`toString`: die ganze Klasse aus Kapitel 5 in einer Zeile.

```java
var p = new Punkt(3, 4);
p.x();                      // 3.0    - kein getX()
p.toString();               // "Punkt[x=3.0, y=4.0]"
p.equals(new Punkt(3, 4));  // true
```

### Kompakter Konstruktor, für Validierung

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
Parameter ändern (`name = name.strip()`), aber nicht `this.name` setzen.

### Zusätzliche Methoden und statische Fabriken

```java
public record Artikel(String name, long preisCent, int menge) {
    public long gesamtCent() { return preisCent * menge; }

    public static Artikel einzeln(String name, long preisCent) {
        return new Artikel(name, preisCent, 1);
    }
}
```

Warum eine statische Fabrik oft besser ist als ein weiterer Konstruktor: 5.10.

### Grenzen

- Records sind **immer** `final` und ihre Felder **immer** `final`
- Aber nur *flach* unveränderlich: Ist eine Komponente eine `ArrayList`, kann
  man die Liste trotzdem ändern (Kapitel 5.5). Abhilfe im kompakten
  Konstruktor: `namen = List.copyOf(namen);`
- Keine zusätzlichen Instanzfelder ausser den Komponenten
- Keine Vererbung (aber `implements` beliebig vieler Interfaces)

**Wann ein `record`?** Immer wenn ein Typ vor allem *Daten transportiert*:
DTOs, Koordinaten, Ergebnisobjekte, Konfigurationswerte, Schlüssel für Maps.
Wenn der Typ dagegen veränderlichen Zustand und Verhalten kapselt (wie das
`Konto` aus Kapitel 5), bleibt es eine normale Klasse.

## 10.3 `enum`, mehr als eine Liste von Konstanten

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

Enums sind vollwertige Klassen, mit Feldern, Konstruktor und Methoden:

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

Im `switch`-**Ausdruck** (Pfeilform mit Ergebnis, Kapitel 2) prüft der
Compiler bei Enums die Vollständigkeit, kommt eine Konstante dazu, zeigt er
dir jede Stelle, die du anpassen musst. (Eine klassische `switch`-Anweisung
ohne Ergebnis prüft er nicht; ein weiterer Grund für die Pfeilform.) Das ist der
Hauptgrund, Enums statt `String`-Konstanten oder `int`-Codes zu verwenden.

`ordinal()` hängt an der Deklarationsreihenfolge. Speicherst du den Wert
irgendwo, bricht das erste Umsortieren deine Daten. Nutze `name()`.

## 10.4 `sealed`, kontrollierte Hierarchien (Java 17)

```java
public sealed interface Form permits Kreis, Rechteck, Dreieck { }

public record Kreis(double radius) implements Form { }
public record Rechteck(double breite, double hoehe) implements Form { }
public record Dreieck(double a, double b, double c) implements Form { }
```

`sealed` heisst: **Nur diese** Typen dürfen `Form` implementieren. Damit weiss
der Compiler, dass die Liste vollständig ist.

Sind alle Untertypen in **derselben Datei** (z. B. als verschachtelte Records),
darf `permits` entfallen.

Jeder erlaubte Untertyp muss selbst `final`, `sealed` oder `non-sealed` sein,
sonst wäre das Siegel löchrig. Records sind automatisch `final`.

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

Die Reihenfolge zählt: Das erste passende Muster gewinnt.

### Record-Muster (Java 21)

```java
double flaeche = switch (form) {
    case Kreis(double r)              -> Math.PI * r * r;
    case Rechteck(double b, double h) -> b * h;
    case Dreieck(double a, double b, double c) -> heron(a, b, c);
};
```

Das Muster **zerlegt** den Record direkt in seine Komponenten, kein
`k.radius()` nötig. Statt des Typs darfst du auch `var` schreiben
(`case Kreis(var r)`), und *seit Java 22* steht `_` für eine Komponente, die
dich nicht interessiert: `case Rechteck(var b, _) -> ...`. Und weil `Form` `sealed` ist, braucht dieser `switch`
**kein `default`**: Der Compiler weiss, dass alle Fälle abgedeckt sind.

Der Gewinn wird beim Erweitern sichtbar: Kommt `Dreieck` dazu, meldet der
Compiler **jeden** `switch`, dem der Fall fehlt. Bei einer `instanceof`-Kette
mit `else` hättest du still ein falsches Ergebnis bekommen.

### Sealed + Records + Switch = algebraische Datentypen

Diese drei Features zusammen sind Javas Antwort auf das, was funktionale
Sprachen "sum types" nennen. Typisches Muster: ein `sealed interface Ergebnis`
mit `record Erfolg(T wert)` und `record Fehler(String grund)`.

## 10.6 Textblöcke (Java 15)

```java
String abfrage = """
        SELECT name, alter
        FROM person
        WHERE stadt = 'Koeln'
        """;
```

Die gemeinsame Einrückung wird abgeschnitten; die Position der schliessenden
`"""` bestimmt, wie viel. `\` am Zeilenende unterdrückt den Umbruch,
`\s` erzwingt ein Leerzeichen am Zeilenende.

Praktisch mit `formatted`:

```java
String brief = """
        Hallo %s,
        dein Guthaben betraegt %d Cent.
        """.formatted(name, guthaben);
```

## 10.7 Datum und Zeit mit `java.time`

Seit Java 8 gibt es eine durchdachte Datums-API, und sie folgt genau den
Ideen dieses Kapitels: unveränderliche Wertobjekte, Enums, klare Typen.

| Typ | Bedeutung | Beispiel |
|-----|-----------|----------|
| `LocalDate` | Datum ohne Uhrzeit | `2026-09-25` |
| `LocalTime` | Uhrzeit ohne Datum | `14:30` |
| `LocalDateTime` | beides, ohne Zeitzone | `2026-09-25T14:30` |
| `ZonedDateTime` | mit Zeitzone | für Termine über Ländergrenzen |
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

`LocalDate.parse("2026-02-30")` wirft eine `DateTimeParseException`, ungültige
Daten gibt es gar nicht erst.

Merke dir drei Dinge:

- Wie bei `String`: `plusDays` & Co. ändern nichts, sie liefern ein **neues**
  Objekt. `termin.plusDays(7);` ohne Zuweisung tut nichts.
- **Nie** `java.util.Date` oder `Calendar`, veränderlich, Monate ab 0
  gezählt, voller Fallen. Nur noch in altem Code.
- Für testbaren Code `LocalDate.now()` nicht tief im Inneren aufrufen, sondern
  "heute" als Parameter hereinreichen: `istUeberfaellig(LocalDate heute)`.
  Dann kann ein Test jedes beliebige Datum vorgeben. (Das brauchst du im
  Abschlussprojekt.)

## 10.8 Kurzüberblick: was noch dazukam

| Version | Feature |
|---------|---------|
| 8 | Lambdas, Streams, `Optional`, `java.time` |
| 9 | `List.of`, Module |
| 10 | `var` |
| 11 | `String.strip/isBlank/repeat`, `Files.readString`, HTTP-Client |
| 14 | `switch`-Ausdruck, hilfreiche NPE-Meldungen |
| 15 | Textblöcke |
| 16 | `record`, `instanceof`-Pattern, `Stream.toList` |
| 17 | `sealed` |
| 21 | Pattern Matching im `switch`, Record-Muster, virtuelle Threads, `getFirst`/`getLast` (Sequenced Collections) |
| 22 | `_` für unbenutzte Variablen und Muster, `java` startet Programme aus mehreren Quelldateien |
| 23 | Javadoc-Kommentare in Markdown (`///`) |
| 25 | `void main()` ohne Klasse + `IO.println`, Anweisungen vor `super(...)`, `import module java.base;` |

**LTS-Versionen** (8, 11, 17, 21, 25) bekommen jahrelang Updates, in
Unternehmen läuft fast immer eine davon. Die Versionen dazwischen erscheinen
alle sechs Monate und sind ein guter Blick auf das, was als Nächstes kommt.
Neue Features starten oft als *Preview* und müssen dann mit
`--enable-preview` freigeschaltet werden.

## 10.9 Muster mit `record`, `enum` und `sealed`

Entwurfsmuster kennst du seit 5.10. Mit den Sprachmitteln dieses Kapitels
bekommen vier davon eine moderne Form. Einen, den Builder, schreibst du selbst.

### Builder, wenn es für einen Record zu viele Angaben werden

Ein `record` ist ideal für wenige Pflichtfelder. Hat ein Objekt aber viele
Felder, die meisten freiwillig, werden Konstruktoraufrufe unlesbar:

```java
new Pizza(Groesse.MITTEL, List.of("Salami"), true, false, null, 0);   // was ist "true"?
```

Für jede Kombination einen eigenen Konstruktor zu schreiben (*telescoping
constructors*) skaliert nicht. Setter würden halb fertige Objekte erlauben, und
unveränderlich wäre nichts mehr. Das Muster **Builder** (*Erbauer*) löst
beides: Ein eigenes Objekt sammelt die Angaben, `build()` baut daraus das
fertige, unveränderliche Objekt.

```java
Pizza p = Pizza.builder(Pizza.Groesse.MITTEL)   // Pflichtangaben als Parameter
        .belag("Salami")                        // freiwillige als Methoden
        .belag("Pilze")
        .extraKaese()
        .build();                               // prueft und baut
```

So ist er aufgebaut:

```java
public final class Pizza {
    private final Groesse groesse;
    private final List<String> belaege;

    private Pizza(Builder b) {                  // nur der Builder baut Pizzen
        this.groesse = b.groesse;
        this.belaege = List.copyOf(b.belaege);  // Kopie! (vgl. "Was gibt das aus?" Nr. 2)
    }

    public static Builder builder(Groesse g) { return new Builder(g); }

    public static final class Builder {         // statische innere Klasse wie Knoten in 14.6
        private final Groesse groesse;
        private final List<String> belaege = new ArrayList<>();

        private Builder(Groesse g) { this.groesse = Objects.requireNonNull(g, "groesse"); }

        public Builder belag(String b) {
            belaege.add(b);
            return this;                        // "fluent": erlaubt die Kette
        }

        public Pizza build() {
            /* Regeln pruefen, die das ganze Objekt betreffen */
            return new Pizza(this);
        }
    }
}
```

- **Pflichtangaben** sind Parameter von `builder(...)`, sie fehlen also nie.
- Jede Methode liefert **`this`** zurück, deshalb lässt sich alles verketten
  (eine *fluent* Schnittstelle).
- **`build()`** prüft Regeln, die das ganze Objekt betreffen, an einer Stelle.
- **`static`**, weil ein Builder vor der Pizza existiert. Aussen und innen dürfen
  gegenseitig auf ihre privaten Member zugreifen, wie bei `Knoten` in 14.6.
- **`List.copyOf`** im Konstruktor: Ohne Kopie teilten sich Builder und Pizza
  eine Liste, und ein späteres `belag()` änderte die schon fertige Pizza.

Im JDK: `StringBuilder` (sammelt Text, `toString()` baut den String),
`HttpRequest.newBuilder().uri(...).GET().build()`, `Stream.builder()`,
`Locale.Builder`. **Wann nicht:** Bei zwei, drei Pflichtfeldern ist ein `record`
mit kompaktem Konstruktor kürzer und genauso klar. Ein Builder lohnt sich ab
etwa vier Angaben, vor allem wenn viele freiwillig sind.

### Singleton als `enum`

Den klassischen Singleton mit privatem Konstruktor kennst du aus 5.10. Kürzer
und sicherer ist ein `enum` mit genau einer Konstante. Die JVM garantiert, dass
es keine zweite Instanz geben kann, auch nicht über Tricks mit Serialisierung
oder Reflection:

```java
public enum Waehrung {
    INSTANZ;

    public String formatiere(long cent) {
        return String.format(Locale.ROOT, "%d,%02d EUR", cent / 100, cent % 100);
    }
}

Waehrung.INSTANZ.formatiere(1299);   // "12,99 EUR"
```

Für zustandslose Helfer wie diesen ist das unproblematisch. Hat der Singleton
**Zustand** und benutzen ihn mehrere Threads, reicht das nicht. Das zeigt 12.3,
dort baust du einen thread-sicheren ID-Generator.

### Zustand (State), das Verhalten hängt am Zustand

Ein Objekt, das sich je nach Zustand anders verhält, bekommt schnell überall
`if (status == ...)`-Ketten. Das Muster **Zustand** legt das Verhalten in die
Zustände selbst. Stehen die Zustände fest, ist ein `enum` mit Methoden je
Konstante (10.3) die natürliche Form:

```java
public enum Bestellstatus {
    NEU        { public Bestellstatus weiter() { return BEZAHLT; } },
    BEZAHLT    { public Bestellstatus weiter() { return VERSENDET; } },
    VERSENDET  { public Bestellstatus weiter() { return ZUGESTELLT; } },
    ZUGESTELLT { public Bestellstatus weiter() { throw new IllegalStateException("schon zugestellt"); } };

    public abstract Bestellstatus weiter();

    public boolean darfStornieren() { return this == NEU || this == BEZAHLT; }
}
```

Kommt ein Zustand dazu, verlangt der Compiler sein `weiter()`, und jeder
`switch`-Ausdruck über den Status meldet die fehlende Konstante. Dein
`Wochentag.naechster()` ist ein kleiner Verwandter davon.

### Besucher (Visitor), heute `sealed` + `switch`

Das GoF-Muster **Besucher** löst ein Problem, das du aus 10.5 kennst: Eine feste
Typ-Hierarchie (`Form` mit `Kreis`, `Rechteck`, `Dreieck`) soll immer neue
Operationen bekommen (Fläche, Umfang, Name), ohne dass man jedes Mal alle
Klassen ändert. Früher brauchte man dafür ein Besucher-Interface mit einer
Methode je Typ und in jeder Klasse eine `accept`-Methode:

```java
interface FormBesucher<R> {                         // der alte Weg
    R kreis(Kreis k);
    R rechteck(Rechteck r);
    R dreieck(Dreieck d);
}
// jede Form: public <R> R accept(FormBesucher<R> b) { return b.kreis(this); }
```

Heute ist jede neue Operation einfach eine Methode mit einem `switch` über das
`sealed interface`, genau wie `flaeche` und `benenne` in den Aufgaben. Der
Compiler prüft die Vollständigkeit, ein vergessener Typ ist ein Compilerfehler.
Den Besucher schreibt man nur noch, wo es kein `sealed` gibt, etwa in altem Code.

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md): erst Tipp 1, dann wieder selbst probieren.

Fünf Dateien in [`src/`](src/), prüfen mit `./lerne.sh 10`.

### `Artikel.java`: Record mit Validierung

`record Artikel(String name, long preisCent, int menge)`:

- Kompakter Konstruktor: `null`, leerer oder nur aus Leerzeichen bestehender
  Name, negativer Preis, negative Menge -> `IllegalArgumentException`
  (auch bei `null` bitte keine `NullPointerException`); ausserdem `name` mit
  `strip()` normalisieren
- `long gesamtCent()`
- `static Artikel einzeln(String name, long preisCent)`
- `Artikel mitMenge(int neueMenge)`: neues Objekt, unveränderlich bleiben!

### `Wochentag.java`: Enum mit Zustand

Sieben Konstanten, Feld `werktag`, dazu:

- `boolean istWerktag()`
- `Wochentag naechster()`: nach `SONNTAG` kommt wieder `MONTAG`
  (Tipp: `values()` und `ordinal()`)
- `static Wochentag vonNummer(int n)`: 1 = MONTAG … 7 = SONNTAG,
  alles andere -> `IllegalArgumentException`

### `Form.java`, sealed interface mit verschachtelten Records

`Kreis`, `Rechteck`, `Dreieck` als Records **innerhalb** des Interfaces.
Negative Werte (bei jeder Komponente) mit `IllegalArgumentException` ablehnen.

Bewusst offen gelassen: Ein "Dreieck" wie `(1, 1, 5)` verletzt die
Dreiecksungleichung, und `NaN` rutscht durch jede `< 0`-Prüfung, in beiden
Fällen liefert `flaeche` dann `NaN`. Wer mag, lehnt auch das ab; die Tests
prüfen nur negative Werte.

### `Aufgaben.java`

1. **`flaeche(Form)`**: `switch` mit **Record-Mustern**, **ohne `default`**.
   Dreieck nach Heron: `s = (a+b+c)/2`, `A = sqrt(s(s-a)(s-b)(s-c))`.
2. **`benenne(Form)`** -> `"Kreis mit Radius 2.0"`, `"Rechteck 3.0x4.0"`,
   `"Dreieck 3.0/4.0/5.0"`. Nutze `Locale.ROOT` beim Formatieren.
3. **`beschreibe(Object)`**: Pattern Matching mit Bedingungen:
   `null` -> `"nichts"`, negative Zahl -> `"negative Zahl"`, `0` -> `"null"`,
   positive Zahl -> `"positive Zahl"`, leerer String -> `"leerer Text"`,
   sonst String -> `"Text der Laenge 5"`, alles andere -> `"unbekannt"`.
4. **`steckbrief(Artikel)`**: Textblock:
   ```
   Artikel: Kaffee
   Preis:   499 Cent
   Menge:   3
   Gesamt:  1497 Cent
   ```
   (mit abschliessendem Zeilenumbruch)
5. **`werktageZaehlen(List<Wochentag>)`** -> `long`.

### `Pizza.java`: Builder (10.9)

`Pizza.Groesse` ist fertig. Du schreibst den `Builder` und den privaten
Konstruktor von `Pizza`:

- `Pizza.builder(null)` -> `NullPointerException`
- `belag(x)`: `x` `null` oder nur Leerzeichen -> `IllegalArgumentException`,
  sonst `x` ohne Leerzeichen am Rand anhängen. `belag` und `extraKaese`
  geben den Builder selbst zurück.
- `build()`: mehr als `MAX_BELAEGE` Beläge -> `IllegalStateException`
- `preisCent()`: Grundpreis der Größe + `PREIS_BELAG` je Belag +
  `PREIS_EXTRA_KAESE`, falls `extraKaese()`
- Eine fertige Pizza ist unveränderlich: `belaege()` lässt sich nicht ändern,
  und ein weiterbenutzter Builder ändert keine schon gebaute Pizza.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
record Punkt(int x, int y) { }

System.out.println(new Punkt(1, 2));
```

<details><summary>Auflösung</summary>

`Punkt[x=1, y=2]`: Das `toString` hat der Compiler erzeugt. Beachte die eckigen Klammern und die Feldnamen.

</details>

**2.**

```java
record Team(List<String> namen) { }

List<String> l = new ArrayList<>(List.of("Anna"));
Team t = new Team(l);
l.add("Bert");
System.out.println(t.namen());
```

<details><summary>Auflösung</summary>

`[Anna, Bert]`: Der Record speichert nur die **Referenz** auf die Liste. Wer die Liste von aussen ändert, ändert den "unveränderlichen" Record mit. Abhilfe: `namen = List.copyOf(namen);` im kompakten Konstruktor.

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

<details><summary>Auflösung</summary>

`gross`: Die Fälle werden von oben nach unten geprüft, das erste passende Muster samt Bedingung gewinnt. Stünde `case Integer i` ohne `when` oben, meldete der Compiler den zweiten Fall als unerreichbar (*dominated*).

</details>

**4.**

```java
StringBuilder sb = new StringBuilder("a");
StringBuilder sb2 = sb.append("b");
sb2.append("c");
System.out.println(sb + " " + (sb == sb2));
```

<details><summary>Auflösung</summary>

`abc true`: `append` verändert den `StringBuilder` und gibt ihn selbst zurück, `return this` wie in deinem Pizza-Builder. `sb` und `sb2` sind dasselbe Objekt. Genau das macht Ketten wie `sb.append("x").append(42).append('!')` möglich. Bei `String` ist es anders: `s.concat("b")` liefert ein **neues** Objekt, `s` bleibt, wie es war (Kapitel 3).

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Wann `record`, wann normale Klasse?
- Warum ist `ordinal()` gefährlich, sobald Werte gespeichert werden?
- Warum braucht ein `switch` über ein `sealed interface` kein `default`?
- Was ist der Unterschied zwischen `case Kreis k` und `case Kreis(double r)`?
- Warum darf ein kompakter Konstruktor `name = name.strip()`, aber nicht `this.name = ...`?
- Wann lohnt sich ein Builder, und wann ist ein `record` besser?
- Warum braucht man das Besucher-Muster heute kaum noch?
