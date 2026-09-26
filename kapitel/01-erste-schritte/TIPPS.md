# Kapitel 01 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `begruessung`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 1.1 und 1.3: Ein `String` ist ein Wert wie jeder andere, und `+`
verkettet Strings. Frag dich: Aus welchen drei Teilen besteht der Satz
`Hallo, Anna!` — was ist fest, was kommt aus dem Parameter?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Fester Text vorne, der Name in der Mitte, fester Text hinten — alles mit `+`
aneinanderhängen und direkt mit `return` zurückgeben. Die typische Falle
sind Leerzeichen und Satzzeichen: Der Test vergleicht **zeichengenau**.
Komma und Leerzeichen nach `Hallo`, Ausrufezeichen direkt hinter dem Namen,
kein Leerzeichen davor.

</details>

## Aufgabe 2: `celsiusZuFahrenheit`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 1.4, "Ganzzahldivision". Frag dich bei jedem Teilausdruck der Formel
`C * 9/5 + 32`: Welchen Typ haben die beiden Operanden genau dieses Operators?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`9 / 5` sind zwei `int`-Literale, also ergibt das `1` — egal, dass `celsius`
ein `double` ist. Wer `celsius * (9 / 5) + 32` schreibt, bekommt für 100 Grad
`132.0` statt `212.0`. Sicher bist du, wenn du die Konstanten gleich als
Kommazahl-Literale schreibst (mit `.0`) oder den Faktor `1.8` direkt verwendest.
Ohne Klammern wäre es übrigens auch korrekt, weil Java von links nach rechts
rechnet und `celsius * 9` schon ein `double` ist — aber darauf solltest du dich
nicht verlassen. Der Test mit `-40.0` zeigt dir, ob auch negative Werte stimmen.

</details>

## Aufgabe 3: `kreisFlaeche`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 1.3 (`double`) und die Konstante `Math.PI`. Frag dich: Wie schreibt
man "r hoch 2" in Java — gibt es dafür überhaupt einen Operator?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Denkfalle: `radius ^ 2` ist in Java **kein** Potenzieren, sondern bitweises XOR
— mit einem `double` kompiliert das nicht einmal (`bad operand types for binary
operator '^'`). Quadrieren heisst: die Zahl mit sich selbst multiplizieren.
`Math.pow(radius, 2)` ginge auch, ist aber für einen festen Exponenten 2
umständlicher zu lesen. Danach nur noch mit `Math.PI` multiplizieren. Radius
`0.0` muss `0.0` ergeben — das klappt ohne Sonderfall.

</details>

## Aufgabe 4: `letzteZiffer`

<details><summary>Tipp 1 — Richtung</summary>

Du brauchst den **Rest** einer Division. Der Rest-Operator `%` (Modulo) wird
kurz in Abschnitt 1.4 und ausführlich in Kapitel 2, Abschnitt 2.1 erklärt — du darfst ihn hier schon
benutzen. Frag dich: Durch welche Zahl musst du teilen, damit der Rest genau
die letzte Stelle ist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Im Dezimalsystem steht jede Stelle für eine Zehnerpotenz. Teilst du `1234`
durch die richtige Zahl, bleibt genau `4` übrig; bei `1000` bleibt `0`, bei
`7` bleibt `7`. Ein einziger Operator mit einer Konstanten genügt, keine
Schleife, kein `String`. Die Aufgabe garantiert nicht-negative Eingaben — das
ist wichtig, denn in Java hat der Rest das Vorzeichen des linken Operanden
(`-7 % 10` ist `-7`).

</details>

## Aufgabe 5: `zeitFormat`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 1.4 (Ganzzahldivision — hier ausnahmsweise dein Freund) und
Abschnitt 1.6 (`String.format`, Platzhalter `%02d`). Den Rest-Operator `%`
brauchst du auch (Abschnitt 1.4). Frag dich: Wie viele Sekunden hat
eine Stunde, wie viele eine Minute — und was bleibt jeweils übrig?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zerlege schrittweise: `/` liefert, wie oft eine Einheit ganz hineinpasst, `%`
liefert, was danach übrig bleibt. Erst die Stunden aus der Gesamtzahl, dann
aus dem Rest die Minuten, der Rest davon sind die Sekunden. Typische Falle:
Minuten als `sekundenGesamt / 60` berechnen — dann bekommst du bei `3661` den
Wert `61` statt `1`, weil die Stunden noch mitgezählt werden. Zum Schluss
`String.format` mit `%02d`, damit aus `0` ein `"00"` wird (Test mit `0` und `59`).

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
public static String zeitFormat(int sekundenGesamt) {
    int stunden  = sekundenGesamt / ...;
    int rest     = sekundenGesamt % ...;
    int minuten  = ...;
    int sekunden = ...;
    return String.format("...", stunden, minuten, sekunden);
}
```

</details>

## Aufgabe 6: `millisekundenProJahr`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 1.4, "Überlauf ohne Warnung", und die Literal-Spalte der Typtabelle
in Abschnitt 1.3. Frag dich: In welchem Typ rechnet Java `365 * 24 * 60 * 60 * 1000`
— und wird dieser Typ durch den Rückgabetyp `long` beeinflusst?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Der Rückgabetyp ändert nichts an der Rechnung: Fünf `int`-Literale ergeben
eine reine `int`-Multiplikation, die still auf `1471228928` überläuft und erst
**danach** zu `long` erweitert wird. Auch ein Cast um den ganzen Ausdruck,
`(long) (365 * 24 * ...)`, kommt zu spät — der Schaden ist dann schon passiert.
Du musst dafür sorgen, dass schon die **erste** Multiplikation in `long`
stattfindet: Java rechnet von links nach rechts, und sobald ein Operand `long`
ist, bleibt der Rest der Kette `long`. Das Suffix aus der Literal-Spalte der
Tabelle ist dein Werkzeug. Erwartet wird `31536000000`.

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum ist `System.out.println(1/2)` gleich `0`?</summary>

`1` und `2` sind beides `int`-Literale, also führt Java eine
Ganzzahldivision durch: Das Ergebnis ist wieder ein `int`, die Nachkommastellen
werden einfach abgeschnitten. Aus `0.5` wird so `0`. Sobald mindestens ein
Operand eine Kommazahl ist, wird in `double` gerechnet: `1.0 / 2` oder
`(double) 1 / 2` ergeben `0.5`. Achtung: `double b = 1 / 2;` ergibt trotzdem
`0.0`, weil die Division vor der Zuweisung schon in `int` passiert.

</details>

<details><summary>Was ist der Unterschied zwischen `javac Hallo.java` und `java Hallo`?</summary>

`javac` ist der **Compiler**: Er prüft den Quelltext `Hallo.java` und
übersetzt ihn in Bytecode, die Datei `Hallo.class`. `java` startet die
**JVM**, die die Klasse `Hallo` lädt und ihre `main`-Methode ausführt;
dabei übersetzt der JIT-Compiler den Bytecode zur Laufzeit in Maschinencode.
Deshalb schreibst du bei `java` den Klassennamen ohne `.class`. (Seit Java 11
kann `java Hallo.java` eine einzelne Quelldatei auch direkt starten — dabei wird
im Hintergrund trotzdem kompiliert.)

</details>

<details><summary>Warum ist `var` keine dynamische Typisierung?</summary>

Bei `var` leitet der **Compiler** den Typ aus dem Initialwert ab, und dieser
Typ steht danach endgültig fest. `var name = "Gregor";` ist exakt dasselbe wie
`String name = "Gregor";`. Ein späteres `name = 5;` ist ein Compilerfehler
(`incompatible types: int cannot be converted to String`). Bei echter
dynamischer Typisierung (z. B. in Python) könnte dieselbe Variable nacheinander
Werte verschiedener Typen halten — das geht in Java nie.

</details>

<details><summary>Warum sollte man Geldbeträge nicht als `double` speichern?</summary>

`double` speichert Zahlen im Binärsystem nach IEEE 754, und viele Dezimalbrüche
wie `0.1` sind dort periodisch, also nicht exakt darstellbar. Deshalb ergibt
`0.1 + 0.2` den Wert `0.30000000000000004`. Bei Geld summieren sich solche
Rundungsfehler und führen zu Centabweichungen. Besser: in Cent als `long`
rechnen oder `BigDecimal` verwenden, das Dezimalzahlen exakt darstellt.

</details>

<details><summary>Was passiert bei `(int) -3.7`?</summary>

Das Ergebnis ist `-3`. Der Cast nach `int` schneidet die Nachkommastellen ab,
er rundet **Richtung Null** — nicht mathematisch und nicht nach unten
(abgerundet wäre es `-4`). Willst du runden, nimm `Math.round(-3.7)`, das
liefert `-4` — und zwar als `long`, du brauchst also noch `(int) Math.round(...)`,
wenn du einen `int` willst.

</details>
