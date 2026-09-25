# Kapitel 04 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `ggT`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.4, Rekursion: Du brauchst einen Basisfall und einen Schritt, der
dem Basisfall naeherkommt. Die Formel steht schon in der Aufgabe. Frag dich:
Was ist der ggT von `a` und `0` — und warum wird das zweite Argument bei jedem
Schritt kleiner?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Basisfall: Ist `b` gleich `0`, ist `a` das Ergebnis (`ggT(7, 0)` ist `7`).
Sonst rufst du `ggT` mit neuen Argumenten auf — dem alten `b` und dem Rest
`a % b` — und gibst dessen Ergebnis mit `return` weiter. Denkfalle: Den
rekursiven Aufruf ohne `return` hinschreiben; dann wird das Ergebnis
weggeworfen. Die Reihenfolge der Argumente musst du nicht vorher sortieren:
Bei `ggT(18, 48)` ist `18 % 48` gleich `18`, der erste Schritt vertauscht die
beiden also von selbst. Auch `ggT(0, 5)` klappt ohne Sonderfall.

</details>

## Aufgabe 2: `max` (dreifach ueberladen)

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.3, Ueberladung: gleicher Name, verschiedene Parameterlisten. Fuer
den eigentlichen Vergleich hilft der ternaere Operator aus Kapitel 2,
Abschnitt 2.1. Frag dich: Wenn du das Maximum von zwei Zahlen schon kannst —
wie bekommst du damit das Maximum von drei?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`max(int, int)` und `max(double, double)` haben denselben Rumpf, nur andere
Typen: eine Bedingung "ist `a` groesser als `b`?" und je nach Ergebnis `a` oder
`b` — kurz mit `? :`. `Math.max` gibt es natuerlich auch, hier sollst du den
Vergleich aber selbst schreiben. Fuer drei Werte **keine** neue Vergleichslogik
bauen: Die Zwei-Parameter-Version zweimal aufrufen, wobei das Ergebnis des einen
Aufrufs Argument des anderen ist. Der Compiler waehlt anhand der
Argumenttypen die passende Ueberladung. Die Tests setzen das Maximum an jede
der drei Positionen — wer von Hand mit `if`-Ketten vergleicht, vergisst leicht
einen Fall; die Wiederverwendung kann das nicht.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```text
max(a, b)       -> wenn a > b, dann a, sonst b
max(a, b, c)    -> m = groesserer Wert von a und b   (mit max(int, int))
                   Ergebnis = groesserer Wert von m und c   (wieder max(int, int))
max(a, b) double -> wie die int-Version, nur mit double-Parametern und -Rueckgabe
```

</details>

## Aufgabe 3: `fibRekursiv` und `fibIterativ`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.4, Rekursion und "Wann Rekursion, wann Schleife?" — das
`fib`-Beispiel dort ist die rekursive Variante. Fuer die iterative: die
`for`-Schleife aus Kapitel 2, Abschnitt 2.4. Frag dich: Welche zwei Werte musst
du dir merken, um den naechsten Fibonacci-Wert auszurechnen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

**Rekursiv:** Basisfall `n <= 1` — dann ist das Ergebnis `n` selbst (das deckt
`fib(0) = 0` und `fib(1) = 1` ab). Sonst die Summe der beiden Vorgaenger, direkt
nach der Definition.

**Iterativ:** Zwei `long`-Variablen fuer die beiden letzten Werte, starten mit
`fib(0)` und `fib(1)`. In jedem Durchlauf den naechsten Wert als Summe
berechnen und beide Variablen eine Position weiterschieben. Denkfalle beim
Weiterschieben: Ueberschreibst du eine Variable, bevor du ihren alten Wert
benutzt hast, ist er weg — nimm eine Hilfsvariable. Zaehle genau, wie viele
Durchlaeufe du fuer `n` brauchst, und behandle `n = 0` so, dass `0` herauskommt.
`long` ist Pflicht: `fib(90)` ist `2880067194370816120`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static long fibRekursiv(int n) {
    if (...) return n;
    return ...;
}

public static long fibIterativ(int n) {
    if (...) return ...;          // kleine n direkt beantworten
    long vorletzte = 0;           // fib(0)
    long letzte = 1;              // fib(1)
    for (int i = ...; i <= n; i++) {
        long naechste = ...;
        vorletzte = ...;
        letzte = ...;
    }
    return ...;
}
```

</details>

## Aufgabe 4: `summeAlle`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.5, Varargs. Frag dich: Was fuer ein Typ ist `zahlen` **innerhalb**
der Methode — und was steckt darin, wenn jemand `summeAlle()` ganz ohne
Argumente aufruft?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Innen ist `int... zahlen` ein ganz normales `int[]`. Du kannst also mit
`for-each` darueberlaufen und in einer Variablen aufsummieren, die bei `0`
startet. Ein leerer Aufruf uebergibt ein **leeres Array**, nicht `null` — die
Schleife laeuft dann einfach nicht, und `0` kommt ohne Sonderfall heraus.
Negative Zahlen brauchen keine Extrabehandlung (`summeAlle(4, -5)` ist `-1`).

</details>

## Aufgabe 5: `binaereSuche`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.4 (Rekursion: Basisfall und Schritt) und die Guard Clauses aus
Abschnitt 4.1. Die oeffentliche Methode ruft nur eine private Hilfsmethode
auf, die zusaetzlich den Suchbereich `von`/`bis` kennt. Frag dich: Woran
erkennst du, dass der Suchbereich leer ist — und in welcher Haelfte suchst du
weiter, wenn das mittlere Element zu klein ist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Hilfsmethode bekommt Array, gesuchten Wert, `von` und `bis` (beide
inklusiv; der Startaufruf steht im TODO). Ablauf pro Aufruf:

1. Basisfall: `von > bis` heisst "Bereich leer" -> `-1`.
2. Mitte bestimmen — **nicht** mit `(von + bis) / 2`, denn die Summe kann bei
   riesigen Indizes ueberlaufen. Rechne stattdessen von `von` aus die halbe
   Differenz dazu.
3. Treffer -> Mitte zurueckgeben. Sonst in der passenden Haelfte weitersuchen.

Denkfalle: Die neue Grenze muss `mitte + 1` bzw. `mitte - 1` sein, nicht
`mitte` — sonst schrumpft der Bereich irgendwann nicht mehr, und du bekommst
einen `StackOverflowError`. Beim leeren Array ist `bis` gleich `-1`, der
Basisfall greift sofort; genau das prueft der Test.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static int binaereSuche(int[] sortiert, int gesucht) {
    return suche(sortiert, gesucht, 0, sortiert.length - 1);
}

private static int suche(int[] a, int gesucht, int von, int bis) {
    if (...) return -1;
    int mitte = von + ...;
    if (a[mitte] == gesucht) return ...;
    if (a[mitte] < gesucht) return suche(a, gesucht, ..., ...);
    return suche(a, gesucht, ..., ...);
}
```

</details>

## Aufgabe 6: `verdoppleAlle`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.2, "Java ist immer call-by-value", besonders der Merksatz. Frag
dich: Willst du hier das **Objekt** (den Array-Inhalt) veraendern oder die
Variable `werte` auf etwas Neues zeigen lassen — und was davon sieht der
Aufrufer?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Du musst jedes Element **ueber seinen Index** neu setzen, also eine klassische
`for`-Schleife mit `i` und `*= 2`. Zwei Fallen, die beide still nichts tun:
`for (int w : werte) w *= 2;` veraendert nur die lokale Kopie `w`, nicht das
Array — `for-each` taugt hier nicht. Und `werte = new int[]{...}` biegt nur die
lokale Kopie der Referenz um; der Aufrufer sieht weiter sein altes Array. Ein
`return` brauchst du nicht, die Methode ist `void`.

</details>

## Aufgabe 7: `getauscht`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 4.2 (Referenzen werden kopiert, nicht Objekte) und aus Kapitel 3,
Abschnitt 3.6 "Arrays sind Referenztypen" (`clone()`). Frag dich: Wie tauschst
du zwei Werte, ohne dass einer davon beim Ueberschreiben verloren geht?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Zuerst eine echte Kopie mit `werte.clone()` oder `Arrays.copyOf(...)`, dann nur
in der Kopie tauschen und sie zurueckgeben. `int[] kopie = werte;` waere keine
Kopie, und der Test "Original ist unveraendert" schluege fehl. Denkfalle beim
Tauschen: `kopie[i] = kopie[j]; kopie[j] = kopie[i];` schreibt zweimal
denselben Wert — der alte Inhalt von `kopie[i]` ist nach der ersten Zeile weg.
Du brauchst eine Hilfsvariable. Der Fall `i == j` funktioniert mit dem
Tausch ueber die Hilfsvariable automatisch.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static int[] getauscht(int[] werte, int i, int j) {
    int[] kopie = ...;
    int merker = ...;
    kopie[i] = ...;
    kopie[j] = ...;
    return kopie;
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum wirkt `arr[0] = 99` in einer Methode nach aussen, `arr = neuesArray` aber nicht?</summary>

Java uebergibt immer eine Kopie des Werts — bei Arrays und Objekten ist dieser
Wert die **Referenz**. Methode und Aufrufer haben also zwei Variablen, die auf
dasselbe Array zeigen. `arr[0] = 99` aendert das gemeinsame Array, das sieht
der Aufrufer. `arr = neuesArray` laesst dagegen nur die lokale Kopie der
Referenz auf ein anderes Array zeigen; die Variable des Aufrufers zeigt
weiterhin auf das alte. Merksatz: Du kannst das Objekt veraendern, aber nicht,
worauf die Variable des Aufrufers zeigt.

</details>

<details><summary>Warum darf man nicht nur ueber den Rueckgabetyp ueberladen?</summary>

Der Rueckgabetyp gehoert nicht zur Signatur (Name + Parametertypen). Der
Compiler waehlt die Ueberladung anhand der **Argumente** aus, und die waeren bei
`int wert()` und `long wert()` identisch. Da ein Aufruf wie `wert();` als
Anweisung gueltig ist und sein Ergebnis gar nicht verwendet, gaebe es keinen
Anhaltspunkt, welche Methode gemeint ist. Deshalb meldet schon die zweite
Deklaration einen Compilerfehler (`method wert() is already defined in class ...`).

</details>

<details><summary>Welche zwei Bestandteile braucht jede terminierende Rekursion?</summary>

Erstens einen **Basisfall**, der ohne weiteren Selbstaufruf ein Ergebnis
liefert (z. B. `if (n <= 1) return 1;`). Zweitens einen **rekursiven Schritt**,
der das Problem so verkleinert, dass er sich dem Basisfall garantiert naehert
(z. B. `n - 1` oder beim ggT der Rest `a % b`). Fehlt eins davon, ruft sich die
Methode endlos auf, bis der Stack voll ist und ein `StackOverflowError` kommt.

</details>

<details><summary>Wann ist Rekursion die bessere Wahl als eine Schleife?</summary>

Wenn die Daten oder das Problem selbst rekursiv aufgebaut sind: Baeume,
Verzeichnisstrukturen, geschachtelte Ausdruecke oder Teile-und-herrsche-Verfahren
wie die binaere Suche. Dort ist der rekursive Code kuerzer und entspricht
direkt der Struktur. Bei linearen Ablaeufen wie Summen oder Zaehlen ist eine
Schleife besser, weil jeder Aufruf einen Stack-Frame kostet, Java keine
Endrekursionsoptimierung hat und naive Rekursion wie bei `fib` Werte vielfach
neu berechnet.

</details>

<details><summary>Was ist der Unterschied im Vertrag zwischen `verdoppleAlle` und `getauscht`?</summary>

`verdoppleAlle` ist `void` und veraendert das uebergebene Array **an Ort und
Stelle** — die Wirkung ist eine Nebenwirkung, die der Aufrufer an seinem eigenen
Array sieht. `getauscht` laesst das Original unangetastet und liefert das
Ergebnis als **neues** Array zurueck. Der zweite Stil ist leichter zu testen und
ueberrascht niemanden; der erste spart Speicher, muss aber klar dokumentiert
sein, weil der Aufrufer sonst nicht mit der Aenderung rechnet.

</details>
