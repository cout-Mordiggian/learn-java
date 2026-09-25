# Kapitel 05 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Konto.java: Felder, Zaehler und Konstruktor

<details><summary>Tipp 1 — Richtung</summary>

Du brauchst drei Bausteine aus dem README: Felder und `final` (Abschnitt 5.5),
einen `static`-Zaehler (Abschnitt 5.4) und einen Konstruktor, der nur gueltige
Objekte zulaesst (Abschnitt 5.2, "Konstruktoren sollen gueltige Objekte
garantieren").

Frag dich: Welcher Wert gehoert **jedem einzelnen** Konto, und welcher Wert
gehoert der **Klasse** als Ganzes? Und: In welcher Reihenfolge muessen Pruefen,
Zaehlen und Zuweisen passieren?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `nummer` und `inhaber` sind `private final`, `guthaben` ist `private long`
  (nicht `final` — es aendert sich ja).
- Der Zaehler ist `private static int anzahlKonten`. Jedes Konto bekommt den
  Zaehlerstand **nach** dem Hochzaehlen als Nummer — so beginnt die erste bei 1.
- Pruefung des Inhabers: erst `null`, dann `isBlank()`. Die Reihenfolge ist
  wichtig: `inhaber.isBlank()` auf `null` wirft eine `NullPointerException`
  statt der erwarteten `IllegalArgumentException`. Mit `||` wird der zweite
  Teil gar nicht erst ausgewertet, wenn der erste schon `true` ist.
- **Die Falle der Tests:** Zaehlst du den Zaehler ganz am Anfang hoch, verbraucht
  auch ein abgelehnter Aufruf eine Nummer. Der Test
  "abgelehnte Konstruktoraufrufe verbrauchen keine Nummer" faellt dann durch.

</details>

<details><summary>Tipp 3 — Geruest</summary>

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
eine schreibende Fachmethode, die Regeln prueft. Frag dich: Welcher Betrag ist
fachlich keine Einzahlung?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Getter geben nur das jeweilige Feld zurueck. Einen `setGuthaben` gibt es
**nicht** — das ist Absicht.

`einzahlen` lehnt jeden Betrag ab, der nicht **positiv** ist. Die Tests pruefen
beide Grenzfaelle: `0` **und** negative Betraege. Eine Pruefung auf `< 0` laesst
die `0` durch — du brauchst `<= 0`. Erst wenn die Pruefung bestanden ist, wird
das Guthaben erhoeht.

</details>

## Konto.java: `abheben`

<details><summary>Tipp 1 — Richtung</summary>

Lies die *Entwurfsfrage* direkt unter der Aufgabe im README: Es gibt hier zwei
verschiedene Arten von "geht nicht". Frag dich fuer jeden Fall: Ist das ein
Programmierfehler (Exception) oder ein normaler Geschaeftsfall (`false`)?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Nicht-positiver Betrag -> `IllegalArgumentException` (wie bei `einzahlen`).
- Zu wenig Deckung -> `return false`, **ohne** das Guthaben anzufassen.
- Sonst abziehen und `true` zurueckgeben.

**Die Falle der Tests:** "Das komplette Guthaben abzuheben ist erlaubt." Emil hat
300 Cent und hebt 300 ab — das muss `true` liefern. Die Deckung fehlt also erst,
wenn der Betrag **groesser** als das Guthaben ist, nicht schon bei Gleichheit.
Achte ausserdem auf die Reihenfolge: Die Betragspruefung kommt zuerst, sonst
liefert `abheben(-5)` womoeglich `true` und *erhoeht* das Guthaben.

</details>

<details><summary>Tipp 3 — Geruest</summary>

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

Du hast `abheben` und `einzahlen` schon gebaut, und beide pruefen ihre Regeln.
Frag dich: Musst du hier irgendetwas neu pruefen — oder kannst du die beiden
Methoden einfach **wiederverwenden**? Und: Was darf auf keinen Fall passieren,
wenn das Abheben scheitert?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Rufe `abheben(betrag)` auf dem eigenen Konto auf und werte den `boolean` aus.
Nur bei `true` zahlst du beim Ziel ein. Sonst waere Geld aus dem Nichts
entstanden — genau das prueft der Test "Empfaenger bei Misserfolg unveraendert".

Der Javadoc verlangt eine `NullPointerException`, wenn `ziel` `null` ist. Pruefe
das **vor** dem Abheben, am besten mit `Objects.requireNonNull(ziel, "ziel")`
(Abschnitt 5.7). Pruefst du erst danach bzw. gar nicht, ist das Geld beim
Sender schon abgebucht, wenn `ziel.einzahlen(...)` knallt — es waere verschwunden.

</details>

<details><summary>Tipp 3 — Geruest</summary>

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

`getAnzahlKonten` gibt nur den statischen Zaehler zurueck — auf `nummer` oder
`guthaben` kann sie gar nicht zugreifen (kein `this`).

`toString` baut den String per Verkettung mit `+`. Das Format muss **zeichengenau**
stimmen: `Konto[1, Anna, 5000 Cent]` — eckige Klammern, Komma **plus Leerzeichen**
als Trenner, und das Wort `Cent` mit Leerzeichen davor. Vergleiche deine Ausgabe
Zeichen fuer Zeichen mit der Erwartung, wenn der Test rot ist.

</details>

## Punkt.java: Felder, Konstruktor und Getter

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 5.8 (Unveraenderliche Objekte) zeigt fast genau diese Klasse. Frag
dich: Was macht `final` bei einem Feld, und warum braucht der Konstruktor
deshalb `this.x = x`?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zwei Felder `private final double x` und `y`, im Konstruktor gesetzt, die Getter
geben sie zurueck. Wenn du ein `final`-Feld im Konstruktor vergisst, meldet der
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
Ueberlauf; `Math.sqrt` geht aber genauso. Das Vorzeichen der Differenzen ist
egal, weil quadriert wird.

Du darfst auf `anderer.x` direkt zugreifen, obwohl `x` `private` ist:
`private` gilt pro **Klasse**, nicht pro Objekt.

`abstandZumUrsprung` braucht keine zweite Formel — ein Aufruf von `abstand` mit
dem passenden Punkt reicht. Eine Stelle, die stimmen muss, statt zwei.

</details>

<details><summary>Tipp 3 — Geruest</summary>

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

Abschnitt 5.8: Ein unveraenderliches Objekt aendert sich nie — "aendernde"
Methoden liefern stattdessen ein **neues** Objekt. Frag dich: Wie kannst du
einen Punkt "verschieben", ohne `x` und `y` anzufassen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`verschoben` erzeugt mit `new Punkt(...)` einen neuen Punkt aus den alten
Koordinaten plus `dx` bzw. `dy`. `this.x += dx` wuerde bei einem `final`-Feld
gar nicht kompilieren — gut so. Der Test prueft ausdruecklich, dass das
Original unveraendert bleibt und `p == q` falsch ist.

`toString` liefert `Punkt(3.0, 4.0)`. Ein `double` in einer String-Verkettung
erscheint automatisch als `3.0` — du brauchst **kein** `String.format`.
(`String.format("%.1f", x)` waere sogar gefaehrlich: Auf einem deutschen System
kaeme `3,0` heraus.)

</details>

## Punkt.java: `equals` und `hashCode`

<details><summary>Tipp 1 — Richtung</summary>

Das ist der Kern des Kapitels: Abschnitt 5.6, Unterabschnitte "`equals(Object)`"
und "`hashCode()` — und der Vertrag". Frag dich: Welche Faelle muss `equals`
abfangen, bevor es ueberhaupt Felder vergleichen kann? Und welche Felder muss
`hashCode` dann benutzen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Drei Schritte in `equals`:

1. Gleiche Referenz? Dann sofort `true`.
2. `instanceof Punkt p` mit Pattern Matching — das erledigt `null` **und** den
   falschen Typ in einem Schritt (`null instanceof Punkt` ist `false`).
3. Die Felder mit `Double.compare(a, b) == 0` vergleichen, nicht mit `==`.

**Die Falle der Tests:** Mit `==` scheitern zwei Pruefungen. `Double.NaN == Double.NaN`
ist `false` — der Test "zwei NaN-Punkte sind equals" wird rot. Und `0.0 == -0.0`
ist `true`, aber `Objects.hash(0.0, 0.0)` und `Objects.hash(-0.0, 0.0)` sind
verschieden — damit waeren zwei `equals`-gleiche Punkte in verschiedenen
Hash-Eimern. `Double.compare` behandelt beide Ecken genau so wie
`Double.hashCode`, deshalb passt alles zusammen.

Der Parameter muss `Object` sein, nicht `Punkt` — sonst ueberlaedst du nur, und
`HashSet` benutzt weiter die alte Methode. `@Override` steht schon da und passt
auf dich auf.

`hashCode` benutzt **genau die Felder**, die `equals` vergleicht:
`Objects.hash(...)`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

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
Betraege, das Guthaben waechst. Ein `setGuthaben` erlaubt jedem, einen beliebigen
Wert hineinzuschreiben — auch `-5000` — und die Invariante des Kontos waere
wertlos. Kapselung heisst Kontrolle darueber, **wie** sich der Zustand aendert,
nicht nur, dass ein Feld `private` ist. Ein blinder Setter ist Kapselung nur dem
Namen nach.

</details>

<details><summary>Warum darf eine <code>static</code>-Methode nicht auf Instanzfelder zugreifen?</summary>

Eine `static`-Methode gehoert der Klasse, nicht einem Objekt — sie wird z. B. als
`Konto.getAnzahlKonten()` ohne jedes Objekt aufgerufen. Deshalb gibt es in ihr
kein `this`. Instanzfelder wie `guthaben` existieren aber nur pro Objekt: Welches
Konto sollte gemeint sein? Der Compiler meldet
`non-static variable guthaben cannot be referenced from a static context`.
Statische Felder wie `anzahlKonten` darf sie dagegen benutzen.

</details>

<details><summary>Was geht kaputt, wenn du <code>equals</code> ohne <code>hashCode</code> ueberschreibst?</summary>

Der Vertrag "gleiche Objekte haben gleichen `hashCode`" ist gebrochen, denn der
geerbte `Object.hashCode` liefert fuer zwei verschiedene Objekte in aller Regel
verschiedene Werte. `HashSet` und `HashMap` suchen zuerst per `hashCode` den
Eimer und fragen erst darin `equals`. Zwei inhaltlich gleiche Punkte landen so
in verschiedenen Eimern: Das Set enthaelt sie doppelt, und `map.get(...)` mit
einem gleichen, aber neuen Schluessel findet nichts. Deshalb: immer beide
zusammen ueberschreiben.

</details>

<details><summary>Warum ist <code>private final List&lt;X&gt; liste</code> trotzdem veraenderbar?</summary>

`final` macht nur die **Referenz** unveraenderlich: Das Feld zeigt fuer immer
auf dieselbe Liste. Die Liste selbst ist ein ganz normales, veraenderliches
Objekt, also funktionieren `liste.add(...)` und `liste.remove(...)` weiterhin.
Nur `liste = new ArrayList<>();` ist ein Compilerfehler. Willst du auch den
Inhalt einfrieren, brauchst du eine unveraenderliche Liste, z. B. `List.copyOf(...)`
(Kapitel 8).

</details>
