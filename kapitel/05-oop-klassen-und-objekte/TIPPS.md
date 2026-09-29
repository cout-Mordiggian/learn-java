# Kapitel 05 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Konto.java: Felder, Zähler und Konstruktor

<details><summary>Tipp 1 — Richtung</summary>

Du brauchst drei Bausteine aus dem README: Felder und `final` (Abschnitt 5.5),
einen `static`-Zähler (Abschnitt 5.4) und einen Konstruktor, der nur gültige
Objekte zulässt (Abschnitt 5.2, "Konstruktoren sollen gültige Objekte
garantieren").

Frag dich: Welcher Wert gehört **jedem einzelnen** Konto, und welcher Wert
gehört der **Klasse** als Ganzes? Und: In welcher Reihenfolge müssen Prüfen,
Zählen und Zuweisen passieren?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `nummer` und `inhaber` sind `private final`, `guthaben` ist `private long`
  (nicht `final` — es ändert sich ja).
- Der Zähler ist `private static int anzahlKonten`. Jedes Konto bekommt den
  Zählerstand **nach** dem Hochzählen als Nummer — so beginnt die erste bei 1.
- Prüfung des Inhabers: erst `null`, dann `isBlank()`. Die Reihenfolge ist
  wichtig: `inhaber.isBlank()` auf `null` wirft eine `NullPointerException`
  statt der erwarteten `IllegalArgumentException`. Mit `||` wird der zweite
  Teil gar nicht erst ausgewertet, wenn der erste schon `true` ist.
- **Die Falle der Tests:** Zählst du den Zähler ganz am Anfang hoch, verbraucht
  auch ein abgelehnter Aufruf eine Nummer. Der Test
  "abgelehnte Konstruktoraufrufe verbrauchen keine Nummer" fällt dann durch.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
private static int anzahlKonten = 0;

private final int nummer;
private final String inhaber;
private long guthaben;

public Konto(String inhaber, long startguthaben) {
    if (inhaber == null || ...) {
        throw new IllegalArgumentException("...");
    }
    if (...) {
        throw new IllegalArgumentException("...");
    }
    // erst jetzt, nach ALLEN Pruefungen:
    anzahlKonten...;
    this.nummer = ...;
    this.inhaber = ...;
    this.guthaben = ...;
}
```

</details>

## Konto.java: Getter und `einzahlen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.3 (Kapselung) zeigt genau dieses Muster: ein lesender Getter und
eine schreibende Fachmethode, die Regeln prüft. Frag dich: Welcher Betrag ist
fachlich keine Einzahlung?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Getter geben nur das jeweilige Feld zurück. Einen `setGuthaben` gibt es
**nicht** — das ist Absicht.

`einzahlen` lehnt jeden Betrag ab, der nicht **positiv** ist. Die Tests prüfen
beide Grenzfälle: `0` **und** negative Beträge. Eine Prüfung auf `< 0` lässt
die `0` durch — du brauchst `<= 0`. Erst wenn die Prüfung bestanden ist, wird
das Guthaben erhöht.

</details>

## Konto.java: `abheben`

<details><summary>Tipp 1 — Richtung</summary>

Lies die *Entwurfsfrage* direkt unter der Aufgabe im README: Es gibt hier zwei
verschiedene Arten von "geht nicht". Frag dich für jeden Fall: Ist das ein
Programmierfehler (Exception) oder ein normaler Geschäftsfall (`false`)?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Nicht-positiver Betrag -> `IllegalArgumentException` (wie bei `einzahlen`).
- Zu wenig Deckung -> `return false`, **ohne** das Guthaben anzufassen.
- Sonst abziehen und `true` zurückgeben.

**Die Falle der Tests:** "Das komplette Guthaben abzuheben ist erlaubt." Emil hat
300 Cent und hebt 300 ab — das muss `true` liefern. Die Deckung fehlt also erst,
wenn der Betrag **größer** als das Guthaben ist, nicht schon bei Gleichheit.
Achte ausserdem auf die Reihenfolge: Die Betragsprüfung kommt zuerst, sonst
liefert `abheben(-5)` womöglich `true` und *erhöht* das Guthaben.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public boolean abheben(long betrag) {
    if (...) {
        throw new IllegalArgumentException("...");
    }
    if (...) {          // zu wenig Deckung?
        return false;
    }
    guthaben ...;
    return true;
}
```

</details>

## Konto.java: `ueberweiseAn`

<details><summary>Tipp 1 — Richtung</summary>

Du hast `abheben` und `einzahlen` schon gebaut, und beide prüfen ihre Regeln.
Frag dich: Musst du hier irgendetwas neu prüfen — oder kannst du die beiden
Methoden einfach **wiederverwenden**? Und: Was darf auf keinen Fall passieren,
wenn das Abheben scheitert?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Rufe `abheben(betrag)` auf dem eigenen Konto auf und werte den `boolean` aus.
Nur bei `true` zahlst du beim Ziel ein. Sonst wäre Geld aus dem Nichts
entstanden — genau das prüft der Test "Empfänger bei Misserfolg unverändert".

Der Javadoc verlangt eine `NullPointerException`, wenn `ziel` `null` ist. Prüfe
das **vor** dem Abheben, am besten mit `Objects.requireNonNull(ziel, "ziel")`
(Abschnitt 5.7). Prüfst du erst danach bzw. gar nicht, ist das Geld beim
Sender schon abgebucht, wenn `ziel.einzahlen(...)` knallt — es wäre verschwunden.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public boolean ueberweiseAn(Konto ziel, long betrag) {
    Objects.requireNonNull(...);
    if (!...) {
        return false;
    }
    ziel...;
    return true;
}
```

`Objects` liegt in `java.util` — denk an `import java.util.Objects;` ganz oben
in der Datei.

</details>

## Konto.java: `toString` und `getAnzahlKonten`

<details><summary>Tipp 1 — Richtung</summary>

`toString` steht in Abschnitt 5.6, `static`-Methoden in Abschnitt 5.4. Frag
dich bei `getAnzahlKonten`: Warum kann diese Methode `static` sein, und auf
welche Felder darf sie deshalb zugreifen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`getAnzahlKonten` gibt nur den statischen Zähler zurück — auf `nummer` oder
`guthaben` kann sie gar nicht zugreifen (kein `this`).

`toString` baut den String per Verkettung mit `+`. Das Format muss **zeichengenau**
stimmen: `Konto[1, Anna, 5000 Cent]` — eckige Klammern, Komma **plus Leerzeichen**
als Trenner, und das Wort `Cent` mit Leerzeichen davor. Vergleiche deine Ausgabe
Zeichen für Zeichen mit der Erwartung, wenn der Test rot ist.

</details>

## Punkt.java: Felder, Konstruktor und Getter

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.8 (Unveränderliche Objekte) zeigt fast genau diese Klasse. Frag
dich: Was macht `final` bei einem Feld, und warum braucht der Konstruktor
deshalb `this.x = x`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zwei Felder `private final double x` und `y`, im Konstruktor gesetzt, die Getter
geben sie zurück. Wenn du ein `final`-Feld im Konstruktor vergisst, meldet der
Compiler `variable x might not have been initialized` — das ist der Compiler,
der dich an deine Invariante erinnert. Die Tests rufen `new Punkt(3, 4)` mit
`int`-Werten auf; die werden automatisch zu `double` erweitert.

</details>

## Punkt.java: `abstand` und `abstandZumUrsprung`

<details><summary>Tipp 1 — Richtung</summary>

Der euklidische Abstand ist Pythagoras: die Differenzen in x und in y bilden
die Katheten, der Abstand ist die Hypotenuse. Frag dich bei
`abstandZumUrsprung`: Welcher Punkt ist der Ursprung — und hast du nicht
gerade eine Methode geschrieben, die den Abstand zu einem Punkt berechnet?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Math.hypot(dx, dy)` berechnet `sqrt(dx*dx + dy*dy)` und ist robuster gegen
Überlauf; `Math.sqrt` geht aber genauso. Das Vorzeichen der Differenzen ist
egal, weil quadriert wird.

Du darfst auf `anderer.x` direkt zugreifen, obwohl `x` `private` ist:
`private` gilt pro **Klasse**, nicht pro Objekt.

`abstandZumUrsprung` braucht keine zweite Formel — ein Aufruf von `abstand` mit
dem passenden Punkt reicht. Eine Stelle, die stimmen muss, statt zwei.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public double abstand(Punkt anderer) {
    double dx = ...;
    double dy = ...;
    return Math.hypot(..., ...);
}

public double abstandZumUrsprung() {
    return abstand(new Punkt(..., ...));
}
```

</details>

## Punkt.java: `verschoben` und `toString`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.8: Ein unveränderliches Objekt ändert sich nie — "ändernde"
Methoden liefern stattdessen ein **neues** Objekt. Frag dich: Wie kannst du
einen Punkt "verschieben", ohne `x` und `y` anzufassen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`verschoben` erzeugt mit `new Punkt(...)` einen neuen Punkt aus den alten
Koordinaten plus `dx` bzw. `dy`. `this.x += dx` würde bei einem `final`-Feld
gar nicht kompilieren — gut so. Der Test prüft ausdrücklich, dass das
Original unverändert bleibt und `p == q` falsch ist.

`toString` liefert `Punkt(3.0, 4.0)`. Ein `double` in einer String-Verkettung
erscheint automatisch als `3.0` — du brauchst **kein** `String.format`.
(`String.format("%.1f", x)` wäre sogar gefährlich: Auf einem deutschen System
käme `3,0` heraus.)

</details>

## Punkt.java: `equals` und `hashCode`

<details><summary>Tipp 1 — Richtung</summary>

Das ist der Kern des Kapitels: Abschnitt 5.6, Unterabschnitte "`equals(Object)`"
und "`hashCode()` — und der Vertrag". Frag dich: Welche Fälle muss `equals`
abfangen, bevor es überhaupt Felder vergleichen kann? Und welche Felder muss
`hashCode` dann benutzen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Drei Schritte in `equals`:

1. Gleiche Referenz? Dann sofort `true`.
2. `instanceof Punkt p` mit Pattern Matching — das erledigt `null` **und** den
   falschen Typ in einem Schritt (`null instanceof Punkt` ist `false`).
3. Die Felder mit `Double.compare(a, b) == 0` vergleichen, nicht mit `==`.

**Die Falle der Tests:** Mit `==` scheitern zwei Prüfungen. `Double.NaN == Double.NaN`
ist `false` — der Test "zwei NaN-Punkte sind equals" wird rot. Und `0.0 == -0.0`
ist `true`, aber `Objects.hash(0.0, 0.0)` und `Objects.hash(-0.0, 0.0)` sind
verschieden — damit wären zwei `equals`-gleiche Punkte in verschiedenen
Hash-Eimern. `Double.compare` behandelt beide Ecken genau so wie
`Double.hashCode`, deshalb passt alles zusammen.

Der Parameter muss `Object` sein, nicht `Punkt` — sonst überlädst du nur, und
`HashSet` benutzt weiter die alte Methode. `@Override` steht schon da und passt
auf dich auf.

`hashCode` benutzt **genau die Felder**, die `equals` vergleicht:
`Objects.hash(...)`.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
@Override
public boolean equals(Object o) {
    if (this == o) return ...;
    if (!(o instanceof Punkt p)) return ...;
    return Double.compare(..., ...) == 0
        && ...;
}

@Override
public int hashCode() {
    return Objects.hash(...);
}
```

</details>

## Punkt.java: Fabrikmethoden `ursprung` und `polar`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.10, "Die statische Fabrikmethode". Beide Methoden sind `static`,
es gibt also kein `this`, sie liefern einen Punkt. Frag dich bei `ursprung`: Wie
bekommst du **dasselbe** Objekt bei jedem Aufruf, obwohl `new` jedes Mal ein
neues erzeugt? Und bei `polar`: Was muss vor `Math.cos` mit dem Winkel passieren?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `ursprung`: eine Konstante `private static final Punkt URSPRUNG = new Punkt(0, 0);`
  (5.4, Konstanten). Die Methode gibt nur noch `URSPRUNG` zurück. Das ist nur
  möglich, weil `Punkt` unveränderlich ist: Niemand kann den geteilten
  Ursprung verschieben. Bei einem veränderlichen Objekt wäre das gefährlich.
- `polar`: `Math.cos` und `Math.sin` erwarten den Winkel im **Bogenmass**
  (ein Vollkreis ist 2 * Pi statt 360). `Math.toRadians(winkelGrad)` rechnet um.
  Dann `new Punkt(radius * Math.cos(w), radius * Math.sin(w))`.
- Kleine Rundungsfehler sind normal: `Math.cos(Math.toRadians(90))` ist nicht
  genau 0, sondern etwa `6.1E-17`. Die Tests vergleichen deshalb mit Toleranz
  (`fastGleich`).
- Gern auch in `abstandZumUrsprung`: `abstand(ursprung())` spart ein Objekt.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
private static final Punkt URSPRUNG = ...;

public static Punkt ursprung() {
    return ...;
}

public static Punkt polar(double radius, double winkelGrad) {
    double w = Math.toRadians(...);
    return new Punkt(..., ...);
}
```

</details>

## Konfiguration.java: Singleton

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.10, "Der Singleton", der Codeblock. Drei Dinge sind im Gerüst
falsch: Es gibt kein Feld für die eine Instanz, der Konstruktor ist `public`,
und `instanz()` erzeugt bei jedem Aufruf ein neues Objekt. Frag dich: Wer darf
nach deiner Änderung noch `new Konfiguration()` schreiben?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Ein `private static final Konfiguration INSTANZ = new Konfiguration();` direkt
  in der Klasse. Innerhalb der Klasse darf der private Konstruktor aufgerufen
  werden, genau das tut diese Zeile.
- Den Konstruktor auf `private` setzen. **Nicht** löschen: Ohne eigenen
  Konstruktor erzeugt der Compiler einen öffentlichen (5.2).
- `instanz()` gibt nur noch `INSTANZ` zurück.
- `setSprache`: dieselbe Prüfung wie beim Inhaber von `Konto`, erst `null`,
  dann `isBlank()`, und erst danach zuweisen.

Wenn du willst, schreib testweise in `Punkt` irgendwo `new Konfiguration();`:
Der Compiler meldet jetzt *has private access*. Genau das soll er. (Danach
wieder löschen.)

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public final class Konfiguration {
    private static final Konfiguration INSTANZ = ...;

    private String sprache = "de";
    private boolean farbig = false;

    private Konfiguration() { }

    public static Konfiguration instanz() {
        return ...;
    }
    ...
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Was passiert mit dem Standardkonstruktor, sobald du selbst einen schreibst?</summary>

Er verschwindet. Der Compiler erzeugt den parameterlosen Standardkonstruktor
nur, wenn die Klasse **gar keinen** Konstruktor hat. Sobald du z. B.
`Konto(String, long)` schreibst, ist `new Konto()` ein Compilerfehler. Willst du
beides, musst du den parameterlosen Konstruktor selbst hinschreiben — oft ist
es aber gerade gewollt, dass niemand ein Konto ohne Inhaber anlegen kann.

</details>

<details><summary>Warum ist <code>einzahlen(long)</code> besser als <code>setGuthaben(long)</code>?</summary>

`einzahlen` beschreibt einen **fachlichen Vorgang** mit Regeln: nur positive
Beträge, das Guthaben wächst. Ein `setGuthaben` erlaubt jedem, einen beliebigen
Wert hineinzuschreiben — auch `-5000` — und die Invariante des Kontos wäre
wertlos. Kapselung heisst Kontrolle darüber, **wie** sich der Zustand ändert,
nicht nur, dass ein Feld `private` ist. Ein blinder Setter ist Kapselung nur dem
Namen nach.

</details>

<details><summary>Warum darf eine <code>static</code>-Methode nicht auf Instanzfelder zugreifen?</summary>

Eine `static`-Methode gehört der Klasse, nicht einem Objekt — sie wird z. B. als
`Konto.getAnzahlKonten()` ohne jedes Objekt aufgerufen. Deshalb gibt es in ihr
kein `this`. Instanzfelder wie `guthaben` existieren aber nur pro Objekt: Welches
Konto sollte gemeint sein? Der Compiler meldet
`non-static variable guthaben cannot be referenced from a static context`.
Statische Felder wie `anzahlKonten` darf sie dagegen benutzen.

</details>

<details><summary>Was geht kaputt, wenn du <code>equals</code> ohne <code>hashCode</code> überschreibst?</summary>

Der Vertrag "gleiche Objekte haben gleichen `hashCode`" ist gebrochen, denn der
geerbte `Object.hashCode` liefert für zwei verschiedene Objekte in aller Regel
verschiedene Werte. `HashSet` und `HashMap` suchen zuerst per `hashCode` den
Eimer und fragen erst darin `equals`. Zwei inhaltlich gleiche Punkte landen so
in verschiedenen Eimern: Das Set enthält sie doppelt, und `map.get(...)` mit
einem gleichen, aber neuen Schlüssel findet nichts. Deshalb: immer beide
zusammen überschreiben.

</details>

<details><summary>Warum ist <code>private final List&lt;X&gt; liste</code> trotzdem veränderbar?</summary>

`final` macht nur die **Referenz** unveränderlich: Das Feld zeigt für immer
auf dieselbe Liste. Die Liste selbst ist ein ganz normales, veränderliches
Objekt, also funktionieren `liste.add(...)` und `liste.remove(...)` weiterhin.
Nur `liste = new ArrayList<>();` ist ein Compilerfehler. Willst du auch den
Inhalt einfrieren, brauchst du eine unveränderliche Liste, z. B. `List.copyOf(...)`
(Kapitel 8).

</details>

<details><summary>Nenne zwei Dinge, die eine statische Fabrikmethode kann, ein Konstruktor aber nicht.</summary>

1. **Einen sprechenden Namen haben.** `Punkt.polar(2, 90)` und `new Punkt(2, 90)`
   haben dieselbe Parameterliste `(double, double)`. Zwei Konstruktoren damit
   erlaubt der Compiler nicht, zwei Methoden mit verschiedenen Namen schon. Der
   Name sagt ausserdem, was die Zahlen bedeuten.
2. **Ein vorhandenes Objekt zurückgeben.** `Punkt.ursprung()` liefert immer
   dieselbe Konstante, `Integer.valueOf(5)` ein Objekt aus einem
   Zwischenspeicher. `new` erzeugt dagegen garantiert jedes Mal ein neues.

Dazu kommt drittens: Sie darf ein Objekt einer Unterklasse liefern und die
tatsächliche Klasse verstecken (Kapitel 6 und 9.8).

</details>

<details><summary>Warum braucht ein Singleton einen <code>private</code>-Konstruktor, und was passiert, wenn du gar keinen schreibst?</summary>

Der Sinn des Singletons ist, dass es nur **ein** Objekt gibt. Kann irgendwer
`new Konfiguration()` schreiben, gibt es beliebig viele, und das statische Feld
ist nur noch eine unter vielen. Der private Konstruktor lässt genau einen
Aufrufer zu: die Klasse selbst, in der Zeile mit `INSTANZ`. Schreibst du gar
keinen Konstruktor, erzeugt der Compiler automatisch einen öffentlichen
parameterlosen (5.2), und jeder kann wieder Objekte anlegen. Deshalb steht in
jedem Singleton ein leerer `private Konfiguration() { }`.

</details>
