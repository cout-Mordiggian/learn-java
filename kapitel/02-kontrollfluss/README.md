# Kapitel 02 — Operatoren und Kontrollfluss

**Ziel:** Du steuerst den Programmablauf mit Bedingungen und Schleifen, kennst
den Unterschied zwischen `switch`-Anweisung und `switch`-Ausdruck und weisst,
warum `&&` "faul" ist.

---

## 2.1 Operatoren

### Arithmetisch

```java
int summe    = 7 + 3;   // 10
int rest     = 7 % 3;   // 1   Modulo: der Rest der Division
int quotient = 7 / 3;   // 2   Ganzzahldivision (siehe Kapitel 1)
```

`%` ist erstaunlich vielseitig: `n % 2 == 0` prueft auf gerade, `n % 10` liefert
die letzte Ziffer, `index % laenge` laesst einen Zaehler zyklisch umlaufen.

Vorsicht bei negativen Zahlen: Das Vorzeichen des Rests folgt dem **linken**
Operanden. `-7 % 3` ist `-1`, nicht `2`. Deshalb prueft man "ungerade" mit
`n % 2 != 0` und nicht mit `n % 2 == 1` — Letzteres ist fuer `-3` falsch.
(`Math.floorMod(-7, 3)` liefert `2`, falls du den mathematischen Rest brauchst.)

### Rangfolge und die `+`-Falle bei Strings

Es gilt Punkt vor Strich, ansonsten wird **von links nach rechts** gerechnet.
Das wird bei Strings tueckisch, weil `+` dort verkettet:

```java
System.out.println("Summe: " + 1 + 2);     // "Summe: 12"  <- erst "Summe: 1", dann + "2"
System.out.println("Summe: " + (1 + 2));   // "Summe: 3"
System.out.println(1 + 2 + " Stueck");     // "3 Stueck"   <- links wird noch gerechnet
```

**Im Zweifel Klammern setzen.** Sie kosten nichts und beenden jede Diskussion.

### Zuweisung und Inkrement

```java
int x = 5;
x += 3;      // Kurzform fuer x = x + 3   -> 8
x++;         // Post-Inkrement            -> 9
++x;         // Prae-Inkrement            -> 10

int a = 5;
int b = a++;   // b = 5, a = 6   (erst zuweisen, dann erhoehen)
int c = ++a;   // c = 7, a = 7   (erst erhoehen, dann zuweisen)
```

In einer eigenstaendigen Zeile ist `x++` und `++x` dasselbe. Verschachtelt
(`arr[i++] = i`) wird es schnell unlesbar — dann lieber zwei Zeilen schreiben.

Die Kurzformen `+=`, `-=`, `*=` … enthalten einen **versteckten Cast**:

```java
int x = 5;
x = x + 1.7;    // Compilerfehler: possible lossy conversion from double to int
x += 1.7;       // kompiliert! x ist jetzt 6 - die Nachkommastellen sind still weg
```

### Vergleich

`==` `!=` `<` `>` `<=` `>=` — Ergebnis ist immer `boolean`.

> **Achtung:** Bei Objekten (also auch bei `String`) vergleicht `==` die
> *Referenz*, nicht den Inhalt. Dazu ausfuehrlich Kapitel 3. Merke fuers Erste:
> **`equals` fuer Inhalt, `==` nur fuer primitive Typen.**

### Logisch — und warum Kurzschluss wichtig ist

```java
boolean b1 = a && b;   // UND - wertet b nur aus, wenn a true ist
boolean b2 = a || b;   // ODER - wertet b nur aus, wenn a false ist
boolean b3 = !a;       // NICHT
```

`&&` und `||` sind **kurzschluessig** (short-circuit): Sie werten den rechten
Operanden nur aus, wenn das Ergebnis noch offen ist. Das ist nicht nur schneller,
sondern oft die Voraussetzung fuer Korrektheit:

```java
if (text != null && text.length() > 3) { ... }
//  ^^^^^^^^^^^^ schuetzt den rechten Teil
```

Ohne Kurzschluss wuerde `text.length()` bei `null` eine `NullPointerException`
werfen. Die Reihenfolge der Bedingungen ist hier also nicht beliebig.

Es gibt auch `&` und `|` fuer booleans — die werten *immer beide* Seiten aus.
Die braucht man fast nie; in 99 % der Faelle willst du `&&` und `||`.

### Ternaerer Operator

```java
String status = alter >= 18 ? "volljaehrig" : "minderjaehrig";
```

Ein `if`/`else` als Ausdruck. Gut fuer kurze Entscheidungen, schlecht wenn
verschachtelt — drei ineinander geschachtelte `?:` liest niemand gern.

## 2.2 `if` / `else if` / `else`

```java
if (punkte >= 90) {
    note = 1;
} else if (punkte >= 80) {
    note = 2;
} else {
    note = 5;
}
```

Die geschweiften Klammern sind bei einer einzelnen Anweisung optional — **setze
sie trotzdem immer**. Der beruehmte "goto fail"-Bug in Apples TLS-Code entstand
genau daran.

Die Bedingung muss ein `boolean` sein. `if (x = 5)` ist in Java ein
Compilerfehler (in C waere es ein stiller Bug) — eine der guten Entscheidungen
der Sprache. Einzige Luecke: Bei `boolean`-Variablen kompiliert
`if (fertig = true)` leider doch (Zuweisung statt Vergleich, immer `true`).
Schreib deshalb nie `== true`, sondern einfach `if (fertig)` bzw. `if (!fertig)`.

## 2.3 `switch` — zwei Bauformen

### Klassisch (Anweisung, mit `break`)

```java
switch (tag) {
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
        typ = "Werktag";
        break;          // ohne break laeuft es in den naechsten case durch!
    case 6:
    case 7:
        typ = "Wochenende";
        break;
    default:
        typ = "unbekannt";
}
```

Das "Durchfallen" (fall-through) ohne `break` ist eine der haeufigsten
Fehlerquellen in aelterem Java-Code.

### Modern (Ausdruck, mit Pfeil) — seit Java 14

```java
String typ = switch (tag) {
    case 1, 2, 3, 4, 5 -> "Werktag";
    case 6, 7          -> "Wochenende";
    default            -> "unbekannt";
};
```

Kein `break`, kein Durchfallen, und der `switch` **liefert einen Wert**.
Ausserdem prueft der Compiler bei `enum`-Typen, ob du alle Faelle abgedeckt hast.

Brauchst du mehrere Anweisungen in einem Zweig, nutze einen Block mit `yield`:

```java
int laenge = switch (form) {
    case "quadrat" -> 4;
    case "dreieck" -> 3;
    default -> {
        System.out.println("Unbekannte Form: " + form);
        yield 0;
    }
};
```

**Faustregel:** Neuen Code immer mit der Pfeil-Form schreiben.

## 2.4 Schleifen

### `while` — solange die Bedingung gilt

```java
int rest = zahl;
while (rest > 0) {
    quersumme += rest % 10;
    rest /= 10;
}
```

Nimm `while`, wenn du **nicht vorher weisst**, wie oft es laeuft.

### `do`/`while` — mindestens einmal

```java
do {
    eingabe = frageNutzer();
} while (!istGueltig(eingabe));
```

Der Koerper laeuft garantiert einmal. Selten gebraucht, aber genau richtig bei
Eingabeschleifen.

### `for` — Zaehlschleife

```java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
//   ^^^^^^^^^  ^^^^^^  ^^^
//   Start      Test    Schritt (nach jedem Durchlauf)
```

`i` existiert nur innerhalb der Schleife. Nimm `for`, wenn die Anzahl feststeht.

### `for-each` — ueber alles iterieren

```java
for (String name : namen) {
    System.out.println(name);
}
```

Lies das als "fuer jeden String `name` in `namen`". Funktioniert mit Arrays und
allem, was `Iterable` ist (Kapitel 8). Kein Index, keine Off-by-one-Fehler —
**die Standardwahl**, solange du den Index nicht wirklich brauchst.

### `break` und `continue`

```java
for (int i = 0; i < 100; i++) {
    if (i % 2 != 0) continue;   // diesen Durchlauf ueberspringen
    if (i > 20) break;          // Schleife ganz verlassen
    summe += i;
}
```

Bei verschachtelten Schleifen bricht `break` nur die **innere** ab. Fuer die
aeussere gibt es Labels:

```java
aussen:
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        if (gefunden) break aussen;
    }
}
```

Labels sind legitim, aber ein Warnsignal: Oft ist es klarer, den inneren Teil
in eine eigene Methode mit `return` auszulagern (Kapitel 4).

## 2.5 Gueltigkeitsbereich (Scope)

Eine Variable lebt von ihrer Deklaration bis zur schliessenden Klammer des
Blocks, in dem sie steht.

```java
if (bedingung) {
    int temp = 5;
}
System.out.println(temp);   // Compilerfehler: cannot find symbol
```

Deklariere Variablen so **spaet und so eng wie moeglich**. Das reduziert
Namenskollisionen und macht sichtbar, wo ein Wert wirklich gebraucht wird.

## 2.6 Vorgriff: Eingaben von der Tastatur lesen

Bisher rechnen deine Programme nur mit festen Werten. Mit einem `Scanner`
reagieren sie auf das, was jemand eintippt:

```java
import java.util.Scanner;

Scanner eingabe = new Scanner(System.in);
System.out.print("Wie alt bist du? ");
int alter = eingabe.nextInt();          // wartet, bis eine Zahl + Enter kommt
System.out.println(alter >= 18 ? "volljaehrig" : "minderjaehrig");
```

Was `import` und `new` genau bedeuten, kommt in Kapitel 5 und 8 — fuer jetzt
reicht: So bekommst du eine Zahl von der Tastatur. Die Fallstricke (Buchstaben
statt Zahl, `nextInt` gemischt mit `nextLine`) behandelt Kapitel 11.7.

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Datei: [`src/Aufgaben.java`](src/Aufgaben.java) — pruefen mit `./lerne.sh 02`.

1. **`fizzbuzz(int n)`** — Der Klassiker. Vielfaches von 3 -> `"Fizz"`,
   von 5 -> `"Buzz"`, von beiden -> `"FizzBuzz"`, sonst die Zahl als String.
   Auch `0` ist durch 3 und 5 teilbar (Rest 0) und ergibt `"FizzBuzz"`.
   *Denkfalle:* Welche Bedingung musst du zuerst pruefen?
2. **`notenText(int note)`** — 1 bis 6 in Text ("sehr gut" … "ungenuegend"),
   alles andere `"ungueltig"`. Nutze einen **switch-Ausdruck** mit Pfeilen.
3. **`istPrimzahl(int n)`** — Zahlen < 2 (auch negative) sind keine Primzahlen.
   *Optimierung:* Es genuegt, bis `Math.sqrt(n)` zu testen — warum?
   Gleichwertig und ohne Kommazahlen: `i * i <= n` als Schleifenbedingung.
4. **`fakultaet(int n)`** — `0! = 1`, `5! = 120`. Rueckgabetyp `long`.
   Rekursiv geht das auch, aber hier bitte **iterativ** mit einer Schleife.
5. **`quersumme(int n)`** — `1234` -> `10`. `while` mit `%` und `/`.
6. **`summeVielfache(int grenze)`** — Summe aller Zahlen von 1 bis `grenze`
   (einschliesslich), die durch 3 **oder** 5 teilbar sind. Nutze `continue`.
7. **`sternDreieck(int hoehe)`** — Verschachtelte Schleifen. Fuer `hoehe = 3`:
   ```
   *
   **
   ***
   ```
   Jede Zeile endet mit `\n`, also auch die letzte.

### Bonus: `Zahlenraten.java` — dein erstes Spiel

Keine Tests, kein `Pruef` — du pruefst, indem du spielst. Das Programm denkt
sich eine Zahl von 1 bis 100, du raetst, es antwortet mit "zu gross" oder
"zu klein". Das Geruest mit TODOs liegt in [`src/Zahlenraten.java`](src/Zahlenraten.java).

```bash
bash lerne.sh 02 -r Zahlenraten        # dein Spiel
bash lerne.sh 02 -l -r Zahlenraten     # die Musterloesung zum Vergleich
```

Hier kommt alles aus diesem Kapitel zusammen: eine Schleife mit Abbruchbedingung,
`if`/`else if`, ein Zaehler. Welche Schleifenart passt am besten?

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
int x = 5;
if (x > 3 || ++x > 0) { }
System.out.println(x);
```

<details><summary>Aufloesung</summary>

`5` — `x > 3` ist schon `true`, also wertet `||` die rechte Seite gar nicht aus: `++x` passiert nie. Kurzschluss ist nicht nur Optimierung, er aendert, *was* ausgefuehrt wird.

</details>

**2.**

```java
System.out.println(1 + 2 + "3" + 4 + 5);
```

<details><summary>Aufloesung</summary>

`3345` — Von links: `1 + 2` ist noch Rechnen (`3`), ab dem String wird verkettet: `"33"`, `"334"`, `"3345"`.

</details>

**3.**

```java
int tag = 2;
String s = "";
switch (tag) {
    case 1: s += "A";
    case 2: s += "B";
    case 3: s += "C"; break;
    default: s += "D";
}
System.out.println(s);
```

<details><summary>Aufloesung</summary>

`BC` — Der Sprung geht zu `case 2`, und weil dort kein `break` steht, faellt die Ausfuehrung in `case 3` durch, bis zum ersten `break`. Mit der Pfeilform `case 2 -> ...` gaebe es dieses Problem nicht.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum ist `if (obj != null && obj.wert() > 0)` sicher, `if (obj.wert() > 0 && obj != null)` aber nicht?
- Was passiert bei einem klassischen `switch` ohne `break`?
- Wann nimmst du `while`, wann `for`, wann `for-each`?
- Warum ist `for (int i = 0; ...)` besser als `i` ausserhalb zu deklarieren?
