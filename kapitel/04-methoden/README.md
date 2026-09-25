# Kapitel 04 — Methoden

**Ziel:** Du zerlegst Programme in Methoden, verstehst Javas
Parameteruebergabe (die fast jeder falsch erklaert) und schreibst Rekursion,
die terminiert.

---

## 4.1 Aufbau einer Methode

```java
public static double durchschnitt(int a, int b) {
//  ^      ^      ^          ^         ^
//  |      |      |          |         Parameterliste
//  |      |      |          Methodenname
//  |      |      Rueckgabetyp
//  |      gehoert zur Klasse, nicht zum Objekt
//  Sichtbarkeit
    return (a + b) / 2.0;
}
```

**Signatur** = Name + Parametertypen. *Nicht* der Rueckgabetyp — das ist wichtig
fuer Ueberladung (4.3).

`void` bedeutet "gibt nichts zurueck". Ein `return;` ohne Wert ist trotzdem
erlaubt und beendet die Methode sofort.

### Guard Clauses statt Verschachtelung

```java
// Verschachtelt - der eigentliche Code rutscht nach rechts:
static String pruefe(String s) {
    if (s != null) {
        if (!s.isBlank()) {
            return s.strip();
        } else {
            return "leer";
        }
    } else {
        return "fehlt";
    }
}

// Mit Guard Clauses - Sonderfaelle vorne raus, Hauptfall bleibt links:
static String pruefe(String s) {
    if (s == null) return "fehlt";
    if (s.isBlank()) return "leer";
    return s.strip();
}
```

Frueh zurueckkehren ist in Java guter Stil und macht Methoden flach lesbar.

## 4.2 Parameteruebergabe: Java ist *immer* call-by-value

Das ist der am haeufigsten missverstandene Punkt der Sprache.

```java
static void aendern(int zahl) {
    zahl = 99;
}

int x = 5;
aendern(x);
System.out.println(x);   // 5 - unveraendert
```

Klar: Der **Wert** 5 wurde kopiert.

```java
static void aendern(int[] arr) {
    arr[0] = 99;
}

int[] a = {1, 2, 3};
aendern(a);
System.out.println(a[0]);   // 99 - veraendert!
```

Sieht nach call-by-reference aus, ist es aber nicht:

```java
static void ersetzen(int[] arr) {
    arr = new int[]{7, 7, 7};   // zeigt jetzt woanders hin
}

int[] a = {1, 2, 3};
ersetzen(a);
System.out.println(a[0]);   // 1 - unveraendert!
```

Als Bild — beim Aufruf `ersetzen(a)` wird der **Pfeil** kopiert:

```
   vor der Zuweisung in ersetzen:      nach  arr = new int[]{7, 7, 7}:

   a   [o]---+                          a   [o]------>  {1, 2, 3}
             +-->  {1, 2, 3}
   arr [o]---+                          arr [o]------>  {7, 7, 7}
```

`arr[0] = 99` haette das gemeinsame Array links veraendert. `arr = ...` biegt
nur den Pfeil der Methode um; `a` zeigt weiter auf das alte Array.

**Die Aufloesung:** Bei Objekten ist der Wert der Variablen die *Referenz*
(die Adresse). Diese Referenz wird kopiert. Beide Kopien zeigen auf dasselbe
Objekt — deshalb wirken Aenderungen **am Objekt** nach aussen. Aber die
Zuweisung `arr = ...` aendert nur die lokale Kopie der Referenz, nicht die
Variable des Aufrufers.

> Merksatz: **Du kannst das Objekt veraendern, aber nicht, worauf die Variable
> des Aufrufers zeigt.**

Praktische Folge: Eine Methode, die ein uebergebenes Array oder eine Liste
veraendert, ist eine versteckte Nebenwirkung. Dokumentiere das — oder gib
lieber eine neue Kopie zurueck.

## 4.3 Ueberladung (Overloading)

Mehrere Methoden duerfen denselben Namen tragen, wenn sich ihre
**Parameterlisten** unterscheiden.

```java
static int    max(int a, int b)        { ... }
static double max(double a, double b)  { ... }
static int    max(int a, int b, int c) { ... }
```

Der Compiler waehlt anhand der Argumenttypen aus — zur **Compile-Zeit**, nicht
zur Laufzeit. (Der Unterschied wird in Kapitel 6 bei Polymorphie wichtig.)

Nicht erlaubt: Unterscheidung nur am Rueckgabetyp.

```java
static int  wert() { ... }
static long wert() { ... }   // Compilerfehler
```

Weil `wert();` als Anweisung gueltig waere und der Compiler dann nicht
entscheiden koennte, welche gemeint ist.

## 4.4 Rekursion

Eine Methode, die sich selbst aufruft. Sie braucht **zwingend** zwei Teile:

```java
static long fakultaet(int n) {
    if (n <= 1) return 1;              // 1. Abbruchbedingung (Basisfall)
    return n * fakultaet(n - 1);       // 2. Schritt, der dem Basisfall naeherkommt
}
```

Fehlt der Basisfall — oder naehert sich der Schritt ihm nicht — bekommst du:

```
Exception in thread "main" java.lang.StackOverflowError
```

Jeder Aufruf belegt einen **Stack-Frame** (Parameter, lokale Variablen,
Ruecksprungadresse). Der Stack ist klein, typischerweise 512 KB bis 1 MB pro
Thread (einstellbar mit `java -Xss4m ...`). Rekursionstiefen von einigen
Tausend bis Zehntausend sind die Grenze — Java hat keine
Endrekursionsoptimierung (*tail call optimization*).

### Wann Rekursion, wann Schleife?

- **Rekursion** ist natuerlich bei rekursiven Datenstrukturen: Baeume,
  Verzeichnisse, geschachtelte Ausdruecke, Teile-und-herrsche.
- **Schleife** ist besser bei linearen Ablaeufen: Summen, Suchen in Listen.

Das Musterbeispiel fuer schlechte Rekursion:

```java
static long fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);   // exponentiell viele Aufrufe!
}
```

`fib(50)` braucht rund **40 Milliarden** Aufrufe, weil dieselben Werte immer
wieder neu berechnet werden (`fib(48)` zweimal, `fib(47)` dreimal, …) — die
Anzahl waechst mit etwa 1,6^n. Iterativ sind es 50 Schritte. In Aufgabe 3
baust du beides und siehst den Unterschied selbst.

## 4.5 Varargs — beliebig viele Argumente

```java
static int summe(int... zahlen) {
    int s = 0;
    for (int z : zahlen) s += z;      // zahlen ist innen ein int[]
    return s;
}

summe();              // 0
summe(1, 2, 3);       // 6
summe(new int[]{1,2}); // 3 - ein Array geht auch direkt
```

Regeln: Nur **ein** Varargs-Parameter, und er muss **der letzte** sein.

## 4.6 `static` — Klasse oder Objekt?

```java
Math.max(3, 5);        // static: an der Klasse aufgerufen
"hallo".length();      // Instanzmethode: an einem Objekt aufgerufen
```

Eine `static`-Methode kennt kein `this` und kann nicht auf Objektfelder
zugreifen. In diesem Kapitel ist noch alles `static` — ab Kapitel 5 wird sich
das umkehren.

Faustregel fuer spaeter: `static` nur fuer echte Hilfsfunktionen, die
ausschliesslich von ihren Parametern abhaengen.

## 4.7 Methoden gut schneiden

- **Ein Job pro Methode.** Wenn du im Namen ein "und" brauchst, sind es zwei.
- **Kurz.** Passt sie nicht auf den Bildschirm, ist sie ein Kandidat zum Teilen.
- **Sprechender Name.** `berechneEndpreis` statt `calc2`. Verben fuer Aktionen,
  `ist`/`hat` fuer Wahrheitswerte.
- **Wenige Parameter.** Ab vier lohnt es sich zu fragen, ob dahinter ein
  eigenes Objekt steckt (Kapitel 5).

---

## Aufgaben

> Haengst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Datei: [`src/Aufgaben.java`](src/Aufgaben.java) — pruefen mit `./lerne.sh 04`.

1. **`ggT(int, int)`** — Groesster gemeinsamer Teiler, **rekursiv** nach Euklid:
   `ggT(a, b) = ggT(b, a % b)`, Basisfall `b == 0`. Zwei Zeilen genuegen.
2. **`max` dreifach ueberladen** — `max(int,int)`, `max(int,int,int)` und
   `max(double,double)`. Kann die Drei-Parameter-Version die andere nutzen?
3. **`fibRekursiv` und `fibIterativ`** — `fib(0)=0, fib(1)=1, fib(n)=fib(n-1)+fib(n-2)`.
   Der Test ruft `fibIterativ(90)` auf — die rekursive Variante wuerde daran
   praktisch nie fertig werden. Genau das ist der Punkt.
4. **`summeAlle(int...)`** — Varargs, leerer Aufruf ergibt `0`.
5. **`binaereSuche(int[], int)`** — Index des gesuchten Werts im **sortierten**
   Array, `-1` wenn nicht enthalten. Schreibe eine private rekursive
   Hilfsmethode mit `von`/`bis` und rufe sie aus der oeffentlichen Methode auf.
   *Denkfalle:* `(von + bis) / 2` kann bei sehr grossen Indizes ueberlaufen.
   Ehrlicher Hinweis: Die Tests pruefen nur die Ergebnisse. Eine simple Schleife
   ueber alle Elemente wuerde sie auch bestehen — ob du wirklich binaer und
   rekursiv suchst, musst du selbst kontrollieren (Musterloesung vergleichen).
6. **`verdoppleAlle(int[])`** — `void`. Veraendere das uebergebene Array
   **an Ort und Stelle**. Der Test prueft, dass der Aufrufer die Aenderung sieht.
7. **`getauscht(int[], int, int)`** — Gibt eine **neue** Kopie zurueck, in der
   zwei Positionen vertauscht sind. Das Original bleibt unangetastet.
   Vergleiche bewusst mit Aufgabe 6: zwei Stile, zwei Vertraege.

## Was gibt das aus?

Erst ueberlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachpruefen. Code lesen und vorhersagen trainiert
genau das Verstaendnis, das du zum Schreiben brauchst.

**1.**

```java
static void tausche(int a, int b) {
    int t = a; a = b; b = t;
}

int x = 1, y = 2;
tausche(x, y);
System.out.println(x + " " + y);
```

<details><summary>Aufloesung</summary>

`1 2` — Die Methode tauscht ihre eigenen **Kopien**. Mit primitiven Werten kann eine Java-Methode die Variablen des Aufrufers nie aendern. Ein funktionierendes `tausche` gibt es in Java nicht; man gibt stattdessen etwas Neues zurueck (vgl. Aufgabe 7).

</details>

**2.**

```java
static void f(long x)    { System.out.println("long"); }
static void f(Integer x) { System.out.println("Integer"); }

f(5);
```

<details><summary>Aufloesung</summary>

`long` — Bei der Ueberladung waehlt der Compiler zuerst ohne Autoboxing: `int` -> `long` ist eine einfache Erweiterung und gewinnt gegen das Verpacken in ein `Integer`. Entschieden wird zur Compile-Zeit.

</details>

**3.**

```java
static void p(int n) {
    if (n == 0) return;
    p(n - 1);
    System.out.print(n);
}

p(3);
```

<details><summary>Aufloesung</summary>

`123` — Die Ausgabe steht **nach** dem rekursiven Aufruf. `p(3)` wartet auf `p(2)`, das auf `p(1)`, das auf `p(0)`. Erst auf dem Rueckweg wird gedruckt, vom innersten Aufruf nach aussen. Steht das `print` vor `p(n - 1)`, kommt `321` heraus.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum wirkt `arr[0] = 99` in einer Methode nach aussen, `arr = neuesArray` aber nicht?
- Warum darf man nicht nur ueber den Rueckgabetyp ueberladen?
- Welche zwei Bestandteile braucht jede terminierende Rekursion?
- Wann ist Rekursion die bessere Wahl als eine Schleife?
- Was ist der Unterschied im Vertrag zwischen `verdoppleAlle` und `getauscht`?
