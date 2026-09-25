# Kapitel 02 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `fizzbuzz`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.1 (`%` und `&&`) und Abschnitt 2.2 (`if` / `else if`). "Durch 3
teilbar" heisst: Der Rest der Division durch 3 ist 0. Frag dich: Wenn eine Zahl
durch 15 teilbar ist — welche deiner Bedingungen sind dann alle wahr, und
welche davon wird zuerst geprueft?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Denkfalle ist die Reihenfolge: Pruefst du zuerst "durch 3 teilbar", gibt
`fizzbuzz(15)` schon `"Fizz"` zurueck, und der `"FizzBuzz"`-Zweig wird nie
erreicht. Der speziellste Fall muss also nach vorn. Mit fruehem `return` in
jedem Zweig brauchst du nicht einmal `else`. Fuer den letzten Fall wandelst du
die Zahl mit `String.valueOf(n)` in Text um. `0` musst du nicht extra
behandeln: `0 % 3` ist `0`, also landet `0` von selbst bei `"FizzBuzz"`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static String fizzbuzz(int n) {
    if (... && ...) return "FizzBuzz";
    if (...) return "Fizz";
    if (...) return "Buzz";
    return ...;
}
```

</details>

## Aufgabe 2: `notenText`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.3, "Modern (Ausdruck, mit Pfeil)". Frag dich: Wie sorgst du dafuer,
dass **jede** moegliche `int`-Zahl — auch `0`, `7` oder `-1` — einen Text
bekommt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Ein `switch`-Ausdruck liefert einen Wert, den du direkt mit `return`
zurueckgeben kannst. Pro Note ein `case` mit `->`, kein `break`. Fuer alle
uebrigen Werte gibt es den `default`-Zweig — ohne ihn meldet der Compiler
`the switch expression does not cover all possible input values`, denn ein
Ausdruck muss immer einen Wert haben. Vergiss nicht das Semikolon nach der
schliessenden Klammer: Das Ganze ist ein Ausdruck in einer `return`-Anweisung.
Achte auf die exakte Schreibweise, z. B. `"ungenuegend"` und `"ungueltig"`.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static String notenText(int note) {
    return switch (note) {
        case 1 -> "sehr gut";
        case 2 -> ...;
        // ... bis 6
        default -> ...;
    };
}
```

</details>

## Aufgabe 3: `istPrimzahl`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.2 (`if` mit fruehem `return`) und Abschnitt 2.4 (`for`-Schleife). Eine
Primzahl hat genau zwei Teiler: 1 und sich selbst. Frag dich: Welche Zahlen
kannst du sofort aussortieren, ohne ueberhaupt eine Schleife zu starten — und
bis zu welchem Kandidaten musst du hoechstens suchen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Erst die Sonderfaelle: Alles unter 2 (auch `0`, `1` und negative Zahlen) ist
nicht prim. Dann probierst du Teiler ab 2 durch; findest du einen mit Rest 0,
ist die Zahl nicht prim. Ist `n = a * b` mit `a <= b`, dann ist `a` hoechstens
die Wurzel von `n` — groessere Kandidaten musst du nicht pruefen.

Die Falle sitzt in der Schleifenbedingung: Mit `i * i < n` (echt kleiner)
wuerdest du bei `9` und `25` den Teiler `3` bzw. `5` nie testen und sie als
prim melden. Genau diese Quadratzahlen prueft der Test — es muss `<=` sein.
Fuer sehr grosse `n` kann `i * i` als `int` ueberlaufen; mit `(long) i * i` bist
du auf der sicheren Seite.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static boolean istPrimzahl(int n) {
    if (...) return false;           // Sonderfaelle
    for (int i = 2; ... <= n; i++) {
        if (...) return false;       // Teiler gefunden
    }
    return ...;
}
```

Optional: `2` separat als prim erkennen, gerade Zahlen aussortieren und dann
nur ungerade Kandidaten ab `3` mit `i += 2` testen.

</details>

## Aufgabe 4: `fakultaet`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.4 (`for` — die Anzahl der Durchlaeufe steht fest) und aus Kapitel 1
Abschnitt 1.4 ("Ueberlauf ohne Warnung"). Frag dich: Mit welchem Wert muss eine
Variable starten, in der du ein **Produkt** aufsammelst?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Du brauchst einen Akkumulator, den du in jedem Durchlauf mit dem Zaehler
multiplizierst (`*=`). Zwei typische Fehler: Startwert `0` — dann ist alles `0`.
Und ein `int` als Akkumulator — der laeuft schon bei `13!` still ueber; der Test
mit `20!` (`2432902008176640000`) braucht `long`. Waehle die Schleifengrenzen so,
dass `0!` und `1!` ohne Sonderfall `1` ergeben: Laeuft die Schleife bei `n = 0`
gar nicht, bleibt einfach der Startwert stehen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static long fakultaet(int n) {
    long ergebnis = ...;
    for (int i = ...; i <= n; i++) {
        ...
    }
    return ergebnis;
}
```

</details>

## Aufgabe 5: `quersumme`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.4, "`while`" — das Beispiel dort ist fast genau diese Aufgabe.
Frag dich: Woher weisst du, wann keine Ziffer mehr uebrig ist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`% 10` holt die letzte Ziffer, `/ 10` schneidet sie ab (Ganzzahldivision).
Wiederhole das, solange die Zahl groesser als `0` ist. Du weisst vorher nicht,
wie viele Ziffern es sind — deshalb `while`, nicht `for`. Bei `0` laeuft die
Schleife gar nicht und die Summe bleibt `0`, genau wie erwartet. Guter Stil:
Arbeite mit einer Kopie (`int rest = n;`) statt den Parameter zu veraendern.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static int quersumme(int n) {
    int summe = 0;
    int rest = n;
    while (...) {
        summe += ...;
        rest ...;
    }
    return summe;
}
```

</details>

## Aufgabe 6: `summeVielfache`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.4, "`break` und `continue`", und die logischen Operatoren in
Abschnitt 2.1. Frag dich: Wie lautet die Bedingung fuer eine Zahl, die du
**ueberspringen** willst — also das Gegenteil von "durch 3 oder durch 5
teilbar"?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Schleife laeuft von `1` bis einschliesslich `grenze` (also `<=`, sonst fehlt
bei `summeVielfache(10)` die `10`). Uninteressante Zahlen ueberspringst du mit
`continue`, alle anderen addierst du. Denkfalle beim Verneinen: "nicht (durch 3
teilbar **oder** durch 5 teilbar)" ist "nicht durch 3 teilbar **und** nicht
durch 5 teilbar". Nimmst du dort `||`, ueberspringst du fast alles. Zahlen wie
`15` zaehlen nur **einmal** — das passiert automatisch, weil jede Zahl nur einen
Schleifendurchlauf hat. Kontrolle: bis `999` muss `233168` herauskommen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static int summeVielfache(int grenze) {
    int summe = 0;
    for (int i = 1; ...; i++) {
        if (...) continue;   // weder durch 3 noch durch 5 teilbar
        summe += ...;
    }
    return summe;
}
```

</details>

## Aufgabe 7: `sternDreieck`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.4, "`for` — Zaehlschleife", zweimal ineinander. Frag dich: Wie viele
Sterne stehen in Zeile 1, 2, 3 — und welche Schleifenvariable kennt diese Zahl
schon?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die aeussere Schleife zaehlt die Zeilen, die innere haengt die Sterne einer
Zeile an. Die Grenze der inneren Schleife ist die **Zeilennummer** der aeusseren
— nicht `hoehe`, sonst bekommst du ein Rechteck. Nach der inneren Schleife kommt
`'\n'`, und zwar nach **jeder** Zeile, auch der letzten (`"*\n"` bei Hoehe 1).
Sammle alles in einem `StringBuilder` mit `append(...)` und gib am Ende
`toString()` zurueck. Bei Hoehe `0` laeuft keine Schleife und es kommt `""`
heraus.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static String sternDreieck(int hoehe) {
    StringBuilder sb = new StringBuilder();
    for (int zeile = 1; zeile <= ...; zeile++) {
        for (int stern = 1; stern <= ...; stern++) {
            sb.append(...);
        }
        sb.append(...);
    }
    return sb.toString();
}
```

</details>

## Bonus: `Zahlenraten`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 2.4 (Schleifen) und 2.6 (Scanner). Frag dich: Wie oft muss das
Programm fragen? Du weisst es vorher nicht — aber **mindestens einmal**.
Welche Schleifenart garantiert genau das?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`do`/`while` passt, weil die Bedingung ("noch nicht richtig") erst nach dem
ersten Tipp pruefbar ist. Die Variable fuer den Tipp muss **vor** der Schleife
deklariert werden, sonst kennt die `while`-Bedingung sie nicht (Scope, 2.5).
Den Zaehler erhoehst du bei jedem Durchlauf.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
int versuche = 0;
int tipp;
do {
    System.out.print("Dein Tipp: ");
    tipp = ...;
    ...
    if (...) {
        System.out.println("Zu gross!");
    } else if (...) {
        ...
    }
} while (...);
System.out.println(...);
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum ist `if (obj != null && obj.wert() > 0)` sicher, `if (obj.wert() > 0 && obj != null)` aber nicht?</summary>

`&&` wertet von links nach rechts aus und ist kurzschluessig: Ist die linke
Seite `false`, wird die rechte gar nicht mehr ausgewertet. In der ersten
Variante verhindert `obj != null`, dass `obj.wert()` bei `null` aufgerufen wird.
In der zweiten wird `obj.wert()` **zuerst** aufgerufen — ist `obj` `null`, fliegt
sofort eine `NullPointerException`, bevor die Pruefung ueberhaupt drankommt.

</details>

<details><summary>Was passiert bei einem klassischen `switch` ohne `break`?</summary>

Die Ausfuehrung "faellt durch" (fall-through): Nach dem passenden `case` laufen
auch alle Anweisungen der folgenden `case`-Zweige weiter, bis ein `break`, ein
`return` oder das Ende des `switch` kommt. Die `case`-Marken sind nur
Sprungziele, keine Grenzen. Gewollt nutzt man das, um mehrere Faelle
zusammenzufassen (`case 1: case 2: ...`), ungewollt ist es ein klassischer Bug.
Die Pfeilform `case 1 ->` hat dieses Problem nicht.

</details>

<details><summary>Wann nimmst du `while`, wann `for`, wann `for-each`?</summary>

`while`, wenn du vorher **nicht weisst**, wie oft die Schleife laeuft — etwa bei
der Quersumme, die so lange laeuft, bis keine Ziffer mehr uebrig ist. `for`,
wenn die Anzahl feststeht oder du einen Zaehler bzw. Index brauchst, z. B. von
`1` bis `n`. `for-each`, wenn du einfach jedes Element eines Arrays oder einer
Collection der Reihe nach brauchst und den Index nicht — das ist die
Standardwahl, weil dabei keine Off-by-one-Fehler moeglich sind.

</details>

<details><summary>Warum ist `for (int i = 0; ...)` besser als `i` ausserhalb zu deklarieren?</summary>

So lebt `i` nur innerhalb der Schleife (Abschnitt 2.5, "so spaet und so eng wie
moeglich"). Nach der Schleife kann niemand versehentlich mit dem veralteten
Endwert von `i` weiterrechnen — der Compiler meldet dann `cannot find symbol`.
Ausserdem kannst du in der naechsten Schleife wieder `int i` deklarieren, ohne
dass sich die beiden in die Quere kommen. Und der Leser sieht sofort: `i` gehoert
nur zu dieser Schleife.

</details>
