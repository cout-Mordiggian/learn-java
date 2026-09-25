# Kapitel 10 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## `Artikel.java` — Record mit Validierung

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.2, besonders "Kompakter Konstruktor" und "Zusaetzliche Methoden und
statische Fabriken". Drei Teilaufgaben: pruefen und normalisieren im kompakten
Konstruktor, `gesamtCent()` rechnen, `mitMenge(...)` ein neues Objekt bauen.
Frag dich bei `mitMenge`: Darf ein Record sein eigenes Feld aendern?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Im kompakten Konstruktor heissen die Parameter genauso wie die Komponenten:
`name`, `preisCent`, `menge`. Du pruefst sie mit `if` und wirfst
`IllegalArgumentException`. Danach darfst du `name` selbst neu zuweisen
(`strip()`); die Felder setzt der Compiler am Ende automatisch.

Randfaelle aus den Tests:

- `null` als Name muss eine `IllegalArgumentException` geben, **keine**
  `NullPointerException`. Pruefe also zuerst auf `null` — erst danach darfst du
  eine Methode auf `name` aufrufen. `||` wertet kurzschliessend aus.
- `""` und `"   "` erkennt `isBlank()` beide.
- Preis oder Menge `0` sind erlaubt, nur negative Werte nicht.
- Pruefe den Namen vor `strip()`, sonst gibt es bei `null` wieder die NPE.

`gesamtCent()` gibt `long` zurueck. `mitMenge` baut ein **neues** `Artikel`
ueber den Konstruktor — damit laeuft die Pruefung auch fuer die neue Menge.
Der Test prueft, dass das Original danach unveraendert ist.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public Artikel {
    if (name == null || ...) {
        throw new IllegalArgumentException("...");
    }
    if (...) { throw ... }          // Preis
    if (...) { throw ... }          // Menge
    name = ...;                     // normalisieren
}

public long gesamtCent() {
    return ...;
}

public Artikel mitMenge(int neueMenge) {
    return new Artikel(..., ..., ...);
}
```

</details>

## `Wochentag.java` — Enum mit Zustand

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.3. Ein Enum ist eine Klasse mit Feld und Konstruktor; die Argumente
in `MONTAG(true)` gehen an diesen Konstruktor. Frag dich bei `naechster()`: Wie
kommst du von einer Position zur naechsten, sodass nach der letzten wieder die
erste kommt? Und bei `vonNummer`: Wie verhaelt sich die Nummer 1..7 zu den
Positionen im Array?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Feld `private final boolean werktag;` anlegen, im Konstruktor mit
  `this.werktag = werktag;` setzen, `istWerktag()` gibt es zurueck.
- `values()` liefert alle Konstanten als Array in Deklarationsreihenfolge,
  `ordinal()` die eigene Position ab 0. Der Rest-Operator `%` mit der
  Array-Laenge sorgt dafuer, dass nach Position 6 wieder 0 kommt.
- `vonNummer`: Nummer 1 ist Position 0. **Vorher** den Bereich pruefen.

Denkfalle: `values()[n - 1]` wirft bei `0` oder `8` von selbst schon eine
Exception — aber eine `ArrayIndexOutOfBoundsException`. Die Tests verlangen
`IllegalArgumentException`, du musst also selbst pruefen und werfen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public Wochentag naechster() {
    Wochentag[] alle = values();
    return alle[(... + 1) % ...];
}

public static Wochentag vonNummer(int n) {
    if (... || ...) {
        throw new IllegalArgumentException("...");
    }
    return values()[...];
}
```

</details>

## `Form.java` — sealed interface mit verschachtelten Records

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.4 (`sealed`) und 10.2 (kompakter Konstruktor). Das `sealed`
und die drei Records stehen schon da — du ergaenzt nur die Pruefungen in den
drei kompakten Konstruktoren. Frag dich: Was genau ist "ungueltig"? Und ist
`0` erlaubt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Pro Konstruktor ein `if` mit `< 0`, bei Rechteck und Dreieck mit `||`
verknuepft. Die Tests pruefen **jede Komponente einzeln** (`Rechteck(1, -1)`
und `Rechteck(-1, 1)`, beim Dreieck jede der drei Seiten) — ein `&&` statt
`||` faellt also auf. `Kreis(0)` muss erlaubt sein, der Test fuer
`flaeche` benutzt ihn.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
record Rechteck(double breite, double hoehe) implements Form {
    public Rechteck {
        if (... || ...) {
            throw new IllegalArgumentException("...");
        }
    }
}
```

</details>

## Aufgabe 1: `flaeche`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.5, "Record-Muster", und 10.4 (warum kein `default` noetig ist).
Das TODO in `src/Aufgaben.java` zeigt dir die drei `case`-Zeilen schon. Frag
dich: Wie lautet die Formel pro Form — und wie schreibst du einen Zweig, der
mehr als einen Ausdruck braucht?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Da die Records **in** `Form` stehen, heissen sie ausserhalb `Form.Kreis`,
`Form.Rechteck`, `Form.Dreieck`. Kreis: `Math.PI * r * r`, Rechteck: `b * h`.

Fuer Heron brauchst du erst `s`, dann die Wurzel (`Math.sqrt`). Das sind zwei
Schritte, also ein Block `{ ... }` hinter dem Pfeil — und im Block gibst du das
Ergebnis mit `yield` zurueck, nicht mit `return` (das wuerde die ganze Methode
verlassen und kompiliert hier nicht).

Test: Dreieck 3/4/5 hat Flaeche `6.0`. Achte darauf, `(a + b + c) / 2` mit
`double` zu rechnen — sind `a`, `b`, `c` `double`, passt das von selbst.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return switch (form) {
    case Form.Kreis(double r) -> ...;
    case Form.Rechteck(double b, double h) -> ...;
    case Form.Dreieck(double a, double b, double c) -> {
        double s = ...;
        yield Math.sqrt(...);
    }
};
```

</details>

## Aufgabe 2: `benenne`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.5 (Record-Muster), wie in Aufgabe 1 — nur liefert jeder Zweig
jetzt einen `String`. Frag dich: Wie formatierst du eine Kommazahl so, dass
unabhaengig von der Systemsprache `2.0` und nicht `2,0` herauskommt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Derselbe `switch` wie in Aufgabe 1, jeder Zweig ruft
`String.format(Locale.ROOT, "...", ...)` auf. `%.1f` gibt genau eine
Nachkommastelle aus. Achte auf die exakten Texte der Tests:
`"Kreis mit Radius 2.0"`, `"Rechteck 3.0x4.0"` (kleines `x`, keine
Leerzeichen), `"Dreieck 3.0/4.0/5.0"`.

Ohne `Locale.ROOT` liefert `%.1f` auf einem deutschen System `2,0` — auf deinem
Rechner ist der Test dann vielleicht gruen, auf einem anderen rot.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return switch (form) {
    case Form.Kreis(double r) ->
            String.format(Locale.ROOT, "Kreis mit Radius %.1f", ...);
    case Form.Rechteck(double b, double h) ->
            String.format(Locale.ROOT, "...", ...);
    case Form.Dreieck(...) ->
            ...;
};
```

</details>

## Aufgabe 3: `beschreibe`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.5, "`switch` mit Typmustern" und "guarded pattern" (`when`).
Frag dich: In welcher **Reihenfolge** muessen die Faelle stehen, wenn "das erste
passende Muster gewinnt"? Und warum braucht dieser `switch` — anders als in
Aufgabe 1 — ein `default`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `case null -> ...` ist ein eigener Fall. Ohne ihn wirft `switch (o)` bei
  `null` eine `NullPointerException`.
- Die eingeschraenkten Faelle (`case Integer i when i < 0`, `when i == 0`) muessen
  **vor** dem allgemeinen `case Integer i` stehen. Andersherum meldet der
  Compiler: `this case label is dominated by a preceding case label`.
- Dasselbe fuer `String`: erst der leere, dann der allgemeine Fall.
- `Object` ist nicht `sealed`, also ist `default` Pflicht — fuer `3.14`
  (ein `Double`) muss `"unbekannt"` herauskommen.

Achtung beim Text fuer `0`: Erwartet wird das Wort `"null"` — nicht zu
verwechseln mit dem Fall `case null`, der `"nichts"` liefert.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return switch (o) {
    case null -> ...;
    case Integer i when ... -> "negative Zahl";
    case Integer i when ... -> "null";
    case Integer i -> ...;
    case String s when ... -> "leerer Text";
    case String s -> "Text der Laenge " + ...;
    default -> ...;
};
```

</details>

## Aufgabe 4: `steckbrief`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 10.6, Textbloecke mit `formatted`. Frag dich: Welche Teile des Textes
sind fest, welche kommen aus dem `Artikel`? Und wovon haengt ab, ob am Ende ein
Zeilenumbruch steht?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Schreib die vier Zeilen in einen Textblock `"""` … `"""` und setze Platzhalter
ein: `%s` fuer den Namen, `%d` fuer `long`/`int`. Danach
`.formatted(...)` mit den Werten in derselben Reihenfolge — `gesamtCent()`
hast du im Record schon.

Zwei Stolperstellen, die der Test sieht:

- Die Leerzeichen nach `Preis:`, `Menge:`, `Gesamt:` muessen exakt stimmen
  (alle Werte beginnen in derselben Spalte).
- Der abschliessende Zeilenumbruch entsteht, wenn die schliessenden `"""` auf
  einer **eigenen Zeile** stehen. Rueck sie nicht weiter nach links als den
  Inhalt, sonst landen fuehrende Leerzeichen im Ergebnis.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
return """
        Artikel: %s
        Preis:   ...
        Menge:   ...
        Gesamt:  ...
        """.formatted(a.name(), ..., ..., ...);
```

</details>

## Aufgabe 5: `werktageZaehlen`

<details><summary>Tipp 1 — Richtung</summary>

Kapitel 9, Abschnitt 9.5 (Streams) — hier zusammen mit deinem Enum. Frag dich:
Welche Tage willst du behalten, und welche Terminaloperation liefert direkt ein
`long`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`filter` mit der Methode `istWerktag()` aus `Wochentag` — das geht elegant als
Methodenreferenz auf den Parameter (Abschnitt 9.3). Danach zaehlt `count()`,
und das gibt schon `long` zurueck. Leere Liste und "nur Wochenende" ergeben von
selbst `0`.

</details>

---

## Selbstcheck — Antworten

<details><summary>Wann `record`, wann normale Klasse?</summary>

Ein `record` passt, wenn der Typ vor allem **Daten transportiert** und sein
Wert ihn ausmacht: Koordinaten, Ergebnisobjekte, Schluessel fuer Maps, DTOs.
Er ist unveraenderlich, und `equals`/`hashCode`/`toString` bekommst du gratis.
Eine normale Klasse brauchst du, wenn der Typ **veraenderlichen Zustand**
kapselt (wie das `Konto` aus Kapitel 5), zusaetzliche private Felder braucht,
die nicht Teil der Schnittstelle sind, oder von einer anderen Klasse erben
muss — das kann ein Record nicht.

</details>

<details><summary>Warum ist `ordinal()` gefaehrlich, sobald Werte gespeichert werden?</summary>

`ordinal()` ist nur die Position in der Deklarationsreihenfolge. Sortiert
jemand die Konstanten um oder fuegt eine in der Mitte ein, verschieben sich alle
Nummern — und gespeicherte Werte (Datenbank, Datei) bedeuten still etwas
anderes: Aus der gespeicherten `5` wird ploetzlich ein anderer Tag. Kein
Compiler und keine Exception warnen dich. Speichere deshalb `name()` und lies
mit `valueOf(...)` zurueck, oder gib dem Enum ein eigenes, festes Feld als Code.

</details>

<details><summary>Warum braucht ein `switch` ueber ein `sealed interface` kein `default`?</summary>

`sealed` legt abschliessend fest, welche Typen das Interface implementieren
duerfen. Deckt der `switch` alle diese Typen ab, weiss der Compiler, dass kein
Fall fehlen kann — der `switch` ist **vollstaendig** (exhaustive). Das ist sogar
ein Vorteil: Kommt spaeter ein neuer Untertyp dazu, meldet der Compiler jede
Stelle, an der der Fall fehlt. Ein `default` wuerde genau diese Warnung
verschlucken und den neuen Typ still falsch behandeln.

</details>

<details><summary>Was ist der Unterschied zwischen `case Kreis k` und `case Kreis(double r)`?</summary>

`case Kreis k` ist ein **Typmuster**: Es prueft den Typ und bindet das ganze
Objekt an `k`; an den Radius kommst du mit `k.radius()`. `case Kreis(double r)`
ist ein **Record-Muster**: Es prueft den Typ und **zerlegt** den Record gleich
in seine Komponenten, `r` ist direkt der Radius. Record-Muster lassen sich auch
verschachteln (`case Linie(Punkt(var x1, var y1), Punkt p2)`). Welches du nimmst,
haengt davon ab, ob du das Objekt als Ganzes oder seine Teile brauchst.

```java
case Form.Kreis k        -> Math.PI * k.radius() * k.radius();
case Form.Kreis(double r) -> Math.PI * r * r;
```

</details>

<details><summary>Warum darf ein kompakter Konstruktor `name = name.strip()`, aber nicht `this.name = ...`?</summary>

Im kompakten Konstruktor ist `name` der **Parameter**, eine gewoehnliche lokale
Variable — die darfst du neu zuweisen. Die Felder sind `final` und werden vom
Compiler **ganz am Ende** automatisch aus den Parametern gesetzt
(`this.name = name;`). Ein `final`-Feld darf nur genau einmal zugewiesen werden;
wuerdest du `this.name` selbst setzen, kaeme die automatische Zuweisung ein
zweites Mal. Deshalb meldet der Compiler `cannot assign a value to final
variable name`. Du veraenderst also den Parameter, und der landet dann im Feld.

</details>
