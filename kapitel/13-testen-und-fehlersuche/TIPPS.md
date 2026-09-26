# Kapitel 13 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

> **Teil A und Teil B sind unabhängig.** In Teil A sind die Tests in
> `tests/Tests.java` ausnahmsweise *nicht* die Aufgabenstellung, sondern nur
> der Schiedsrichter. Die Aufgabenstellung ist die Spezifikation in
> `src/Rabattrechner.java`.

## Teil A: `MeineTests.alleBestanden`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 13.3, Äquivalenzklassen und Grenzwerte, und das durchgerechnete
Versand-Beispiel. Geh die sechs Regeln der Spezifikation einzeln durch und frag
dich bei jeder: In welche Bereiche teilt diese Regel die Eingaben, und wo
liegen die Grenzen? Mach dir eine Tabelle auf Papier, Spalten "Preis",
"Menge", "erwartet", "warum". Erst die Tabelle, dann der Code.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Eine gute Tabelle deckt mindestens ab:

- je einen **Vertreter** pro Rabattstufe (0 %, 5 %, 10 %),
- **beide Seiten jeder Grenze**: knapp darunter und genau darauf,
- die **Null-Fälle**, die die Spezifikation ausdrücklich erlaubt,
- einen Fall, in dem der Rabatt **kein ganzer Cent** ist (Regel 6 hat ein Beispiel),
- **grosse Werte**, bei denen der Gesamtpreis nicht mehr in einen `int` passt,
- **jeden** Parameter einzeln negativ, jeweils mit `try`/`catch`.

Zwei Fallen: Wirft ein Aufruf, bei dem du *keine* Exception erwartest, fliegt
sie aus deiner Methode heraus. Das zählt zwar als "nicht bestanden", aber du
siehst nicht, welche Prüfung es war. Pack solche Aufrufe in ein
`try`/`catch`, das `fehlschlag(...)` meldet. Und: Steig nicht mit `return false`
bei der ersten Abweichung aus. Wenn alle Prüfungen laufen, siehst du alle
Meldungen auf einmal.

Ist schon die Prüfung gegen die **korrekte** Implementierung rot, stimmt
einer deiner erwarteten Werte nicht. Die Meldung `[deine Pruefung] ...` davor
sagt dir, welcher. Rechne ihn noch einmal von Hand nach.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
// Grenze 10: knapp darunter und genau darauf
gleich(..., r.endpreisCent(100, 9), "9 Stueck: noch kein Rabatt");
gleich(..., r.endpreisCent(100, 10), "genau 10 Stueck: 5 %");

// eine erlaubte Eingabe, die eine falsche Implementierung ablehnen koennte
try {
    gleich(..., r.endpreisCent(..., ...), "...");
} catch (IllegalArgumentException e) {
    fehlschlag("... unerwartete Exception");
}

// ungueltige Eingabe
try {
    r.endpreisCent(..., ...);
    fehlschlag("... haette werfen muessen");
} catch (IllegalArgumentException e) {
    // erwartet
}
```

Grosse Zahlen schreibst du mit `L` und Unterstrichen: `3_000_000_000L`.

</details>

## Teil A: Ein Mutant überlebt

<details><summary>Tipp 1 — Richtung</summary>

Bei einem überlebenden Mutanten schlägt **keine** deiner Prüfungen an. Er
unterscheidet sich also nur bei Eingaben von der korrekten Version, die du
noch gar nicht ausprobierst. Geh die Grenzwert-Checkliste aus 13.3 durch:
Welcher Punkt fehlt in deiner Tabelle noch?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Welche Regel der Spezifikation der jeweilige Mutant verletzt:

| Mutant | Regel |
|-------:|-------|
| 1 | Regel 6: Rundung |
| 2 | Regel 5: untere Rabattgrenze |
| 3 | Regel 2: Validierung |
| 4 | Regel 5: höchste Rabattstufe |
| 5 | Regel 4: grosse Werte |
| 6 | Regel 3: Null-Fälle |
| 7 | Regel 5: obere Rabattgrenze |

</details>

<details><summary>Tipp 3 — Gerüst</summary>

Eine Eingabe, die den jeweiligen Mutanten entlarvt. Den erwarteten Wert
rechnest du selbst aus:

| Mutant | Preis (Cent) | Menge |
|-------:|-------------:|------:|
| 1 | 199 | 10 |
| 2 | 100 | 10 |
| 3 | -1 | 5 |
| 4 | 100 | 100 |
| 5 | 5_000_000 | 500 |
| 6 | 500 | 0 |
| 7 | 100 | 50 |

Erst danach, wenn alles grün ist: `src/Kandidaten.java` lesen und vergleichen,
ob du die Fehler richtig vermutet hast.

</details>

## Teil B: `durchschnitt`

<details><summary>Tipp 1 — Kategorie</summary>

Ganzzahldivision (13.7). Die Tests zeigen `1.0` statt `1.5`: Die
Nachkommastellen gehen verloren. Probier in `jshell`: `3 / 2` und `3 / 2.0`.

</details>

<details><summary>Tipp 2 — Zeile</summary>

Das `return`. `summe` und `zahlen.length` sind beide `int`, also wird ganzzahlig
geteilt, und erst das fertige Ergebnis wird zu `double`. Mach einen der beiden
Operanden **vor** der Division zum `double`. Achtung:
`(double) (summe / zahlen.length)` hilft nicht, die Klammer teilt zuerst.

</details>

## Teil B: `zaehleGroesser`

<details><summary>Tipp 1 — Kategorie</summary>

Off-by-one (13.7). Die Meldung sagt `ArrayIndexOutOfBoundsException (Index 4 out of
bounds for length 4)` samt Zeilennummer. Welche Indizes hat ein Array der Länge 4?

</details>

<details><summary>Tipp 2 — Zeile</summary>

Der Schleifenkopf. Die Bedingung lässt `i` bis `zahlen.length` laufen, der
letzte gültige Index ist aber `zahlen.length - 1`. Ein einziges Zeichen ist
zu viel. (Eine for-each-Schleife hätte das Problem gar nicht erst.)

</details>

## Teil B: `istAdmin`

<details><summary>Tipp 1 — Kategorie</summary>

`==` bei Strings (13.7 und Kapitel 3.2). Auffällig: Der Test mit dem Literal
`"admin"` ist grün, die mit `new String("admin")` und `substring` sind rot.

</details>

<details><summary>Tipp 2 — Zeile</summary>

Das `return`. `==` fragt "dasselbe Objekt?", du willst "gleicher Inhalt?". Nimm
`equals`, aber pass auf den Test `null -> false` auf: `name.equals(...)` wirft
bei `name == null` eine `NullPointerException`. Dreh den Aufruf um, sodass das
Literal vorne steht.

</details>

## Teil B: `wochentag`

<details><summary>Tipp 1 — Kategorie</summary>

Fehlendes `break` (13.7, Kapitel 2). Nur **ein** Tag ist falsch, und er
liefert den Namen des **folgenden** Tags.

</details>

<details><summary>Tipp 2 — Zeile</summary>

`case 5`. Nach `name = "Freitag";` fehlt das `break`, die Ausführung fällt in
`case 6` durch und überschreibt `name` mit `"Samstag"`.

</details>

## Teil B: `normalisiere`

<details><summary>Tipp 1 — Kategorie</summary>

Ergebnis nicht zugewiesen (13.7). Die Kleinschreibung klappt, der Rand-Leerraum
bleibt aber stehen. Strings sind unveränderlich (Kapitel 3).

</details>

<details><summary>Tipp 2 — Zeile</summary>

`eingabe.strip();` liefert einen **neuen** String und wirft ihn weg, `eingabe`
bleibt unverändert. Weise das Ergebnis zu oder hänge beide Aufrufe
aneinander: `eingabe.strip().toLowerCase()`.

</details>

## Teil B: `hoechstens`

<details><summary>Tipp 1 — Kategorie</summary>

Semikolon nach `if` (13.7). Auffällig: Es kommt **immer** die Grenze heraus,
auch wenn der Wert darunter liegt. Der Block wird also immer ausgeführt.

</details>

<details><summary>Tipp 2 — Zeile</summary>

Die `if`-Zeile. Das `;` direkt hinter `(wert > grenze)` ist der komplette
Rumpf des `if`, eine leere Anweisung. Der Block `{ ergebnis = grenze; }`
danach steht für sich und läuft immer. Semikolon weg, fertig.

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum reicht es nicht, für eine Staffel "ab 10 Stück" nur mit 5 und 20 Stück zu testen?</summary>

Weil 5 und 20 mitten in ihren Äquivalenzklassen liegen. Ob die Grenze bei 10
mit `>=` oder mit `>` programmiert ist, macht dort keinen Unterschied, beide
Varianten liefern dasselbe. Erst **genau 10** trennt die richtige von der
falschen Version, und **9** stellt sicher, dass die Grenze nicht zu früh
greift. Fehler sitzen bevorzugt an den Rändern, also gehören die Ränder in
die Tests.

</details>

<details><summary>Was sagt dir ein Mutant, der deine Tests überlebt?</summary>

Dass deine Tests eine Lücke haben: Es gibt eine fehlerhafte Implementierung,
die trotzdem alle deine Prüfungen besteht. Genau so ein Fehler könnte also
auch in echtem Code unbemerkt bleiben. Die Frage ist dann: Bei welcher Eingabe
verhielte sich der Mutant anders, und warum prüfe ich die nicht? (Selten ist
ein Mutant "äquivalent", also gar nicht falsch. Dann kann ihn kein Test finden.)

</details>

<details><summary>Warum schreibt man nach dem Beheben eines Fehlers noch einen Test dafür?</summary>

Damit der Fehler nie unbemerkt zurückkommt. Schon beim Reproduzieren hast du
eine Eingabe gefunden, die den Fehler auslöst. Als fester Test ist sie ab jetzt
bei jedem Lauf dabei. Wird der Fehler später durch eine andere Änderung
wieder eingebaut (das passiert öfter, als man denkt), ist der Test sofort
rot. Ausserdem beweist der Test, dass deine Reparatur wirklich wirkt: vorher
rot, nachher grün.

</details>

<details><summary>Warum darfst du einen negativen Preis nicht mit <code>assert</code> abweisen?</summary>

Weil Assertions standardmäßig **ausgeschaltet** sind. Ohne `java -ea` wird die
`assert`-Zeile gar nicht ausgewertet, und der negative Preis rutscht im Betrieb
einfach durch. Ein negativer Preis kommt von aussen, vom Aufrufer, und muss
immer abgewiesen werden. Das ist ein Fall für
`throw new IllegalArgumentException(...)`. `assert` ist nur für interne
Annahmen gedacht, die bei korrektem Code ohnehin immer gelten.

</details>

<details><summary>Warum kann ein Test mit <code>istAdmin("admin")</code> den <code>==</code>-Fehler nicht finden?</summary>

Weil gleiche String-**Literale** im String-Pool landen und dasselbe Objekt sind.
Das `"admin"` im Test und das `"admin"` in der Methode sind ein und dasselbe
Objekt, also ist `==` zufällig `true`. Der Fehler zeigt sich erst bei einem
String, der zur Laufzeit entsteht, etwa durch Tastatureingabe, `substring` oder
`new String("admin")`. Gleicher Inhalt, anderes Objekt, und `==` liefert
`false`. Ein guter Test wählt deshalb auch solche Eingaben.

</details>
