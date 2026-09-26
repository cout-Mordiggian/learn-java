# Kapitel 04 — Methoden

**Ziel:** Du zerlegst Programme in Methoden, verstehst Javas
Parameterübergabe (die fast jeder falsch erklärt) und schreibst Rekursion,
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

**Signatur** = Name + Parametertypen. *Nicht* der Rückgabetyp — das ist wichtig
für Überladung (4.3).

`void` bedeutet "gibt nichts zurück". Ein `return;` ohne Wert ist trotzdem
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

Früh zurückkehren ist in Java guter Stil und macht Methoden flach lesbar.

## 4.2 Parameterübergabe: Java ist *immer* call-by-value

Das ist der am häufigsten missverstandene Punkt der Sprache.

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

`arr[0] = 99` hätte das gemeinsame Array links verändert. `arr = ...` biegt
nur den Pfeil der Methode um; `a` zeigt weiter auf das alte Array.

**Die Auflösung:** Bei Objekten ist der Wert der Variablen die *Referenz*
(die Adresse). Diese Referenz wird kopiert. Beide Kopien zeigen auf dasselbe
Objekt — deshalb wirken Änderungen **am Objekt** nach aussen. Aber die
Zuweisung `arr = ...` ändert nur die lokale Kopie der Referenz, nicht die
Variable des Aufrufers.

> Merksatz: **Du kannst das Objekt verändern, aber nicht, worauf die Variable
> des Aufrufers zeigt.**

Praktische Folge: Eine Methode, die ein übergebenes Array oder eine Liste
verändert, ist eine versteckte Nebenwirkung. Dokumentiere das — oder gib
lieber eine neue Kopie zurück.

## 4.3 Überladung (Overloading)

Mehrere Methoden dürfen denselben Namen tragen, wenn sich ihre
**Parameterlisten** unterscheiden.

```java
static int    max(int a, int b)        { ... }
static double max(double a, double b)  { ... }
static int    max(int a, int b, int c) { ... }
```

Der Compiler wählt anhand der Argumenttypen aus — zur **Compile-Zeit**, nicht
zur Laufzeit. (Der Unterschied wird in Kapitel 6 bei Polymorphie wichtig.)

Nicht erlaubt: Unterscheidung nur am Rückgabetyp.

```java
static int  wert() { ... }
static long wert() { ... }   // Compilerfehler
```

Weil `wert();` als Anweisung gültig wäre und der Compiler dann nicht
entscheiden könnte, welche gemeint ist.

## 4.4 Rekursion

Eine Methode, die sich selbst aufruft. Sie braucht **zwingend** zwei Teile:

```java
static long fakultaet(int n) {
    if (n <= 1) return 1;              // 1. Abbruchbedingung (Basisfall)
    return n * fakultaet(n - 1);       // 2. Schritt, der dem Basisfall naeherkommt
}
```

Fehlt der Basisfall — oder nähert sich der Schritt ihm nicht — bekommst du:

```
Exception in thread "main" java.lang.StackOverflowError
```

Jeder Aufruf belegt einen **Stack-Frame** (Parameter, lokale Variablen,
Rücksprungadresse). Der Stack ist klein, typischerweise 512 KB bis 1 MB pro
Thread (einstellbar mit `java -Xss4m ...`). Rekursionstiefen von einigen
Tausend bis Zehntausend sind die Grenze — Java hat keine
Endrekursionsoptimierung (*tail call optimization*).

### Wann Rekursion, wann Schleife?

- **Rekursion** ist natürlich bei rekursiven Datenstrukturen: Bäume,
  Verzeichnisse, geschachtelte Ausdrücke, Teile-und-herrsche.
- **Schleife** ist besser bei linearen Abläufen: Summen, Suchen in Listen.

Das Musterbeispiel für schlechte Rekursion:

```java
static long fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);   // exponentiell viele Aufrufe!
}
```

`fib(50)` braucht rund **40 Milliarden** Aufrufe, weil dieselben Werte immer
wieder neu berechnet werden (`fib(48)` zweimal, `fib(47)` dreimal, …) — die
Anzahl wächst mit etwa 1,6^n. Iterativ sind es 50 Schritte. In Aufgabe 3
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

Faustregel für später: `static` nur für echte Hilfsfunktionen, die
ausschliesslich von ihren Parametern abhängen.

## 4.7 Methoden gut schneiden

- **Ein Job pro Methode.** Wenn du im Namen ein "und" brauchst, sind es zwei.
- **Kurz.** Passt sie nicht auf den Bildschirm, ist sie ein Kandidat zum Teilen.
- **Sprechender Name.** `berechneEndpreis` statt `calc2`. Verben für Aktionen,
  `ist`/`hat` für Wahrheitswerte.
- **Wenige Parameter.** Ab vier lohnt es sich zu fragen, ob dahinter ein
  eigenes Objekt steckt (Kapitel 5).

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Datei: [`src/Aufgaben.java`](src/Aufgaben.java) — prüfen mit `./lerne.sh 04`.

1. **`ggT(int, int)`** — Größter gemeinsamer Teiler, **rekursiv** nach Euklid:
   `ggT(a, b) = ggT(b, a % b)`, Basisfall `b == 0`. Zwei Zeilen genügen.
2. **`max` dreifach überladen** — `max(int,int)`, `max(int,int,int)` und
   `max(double,double)`. Kann die Drei-Parameter-Version die andere nutzen?
3. **`fibRekursiv` und `fibIterativ`** — `fib(0)=0, fib(1)=1, fib(n)=fib(n-1)+fib(n-2)`.
   Der Test ruft `fibIterativ(90)` auf — die rekursive Variante würde daran
   praktisch nie fertig werden. Genau das ist der Punkt.
4. **`summeAlle(int...)`** — Varargs, leerer Aufruf ergibt `0`.
5. **`binaereSuche(int[], int)`** — Index des gesuchten Werts im **sortierten**
   Array, `-1` wenn nicht enthalten. Schreibe eine private rekursive
   Hilfsmethode mit `von`/`bis` und rufe sie aus der öffentlichen Methode auf.
   *Denkfalle:* `(von + bis) / 2` kann bei sehr grossen Indizes überlaufen.
   Ehrlicher Hinweis: Die Tests prüfen nur die Ergebnisse. Eine simple Schleife
   über alle Elemente würde sie auch bestehen — ob du wirklich binär und
   rekursiv suchst, musst du selbst kontrollieren (Musterlösung vergleichen).
6. **`verdoppleAlle(int[])`** — `void`. Verändere das übergebene Array
   **an Ort und Stelle**. Der Test prüft, dass der Aufrufer die Änderung sieht.
7. **`getauscht(int[], int, int)`** — Gibt eine **neue** Kopie zurück, in der
   zwei Positionen vertauscht sind. Das Original bleibt unangetastet.
   Vergleiche bewusst mit Aufgabe 6: zwei Stile, zwei Verträge.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
static void tausche(int a, int b) {
    int t = a; a = b; b = t;
}

int x = 1, y = 2;
tausche(x, y);
System.out.println(x + " " + y);
```

<details><summary>Auflösung</summary>

`1 2` — Die Methode tauscht ihre eigenen **Kopien**. Mit primitiven Werten kann eine Java-Methode die Variablen des Aufrufers nie ändern. Ein funktionierendes `tausche` gibt es in Java nicht; man gibt stattdessen etwas Neues zurück (vgl. Aufgabe 7).

</details>

**2.**

```java
static void f(long x)    { System.out.println("long"); }
static void f(Integer x) { System.out.println("Integer"); }

f(5);
```

<details><summary>Auflösung</summary>

`long` — Bei der Überladung wählt der Compiler zuerst ohne Autoboxing: `int` -> `long` ist eine einfache Erweiterung und gewinnt gegen das Verpacken in ein `Integer`. Entschieden wird zur Compile-Zeit.

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

<details><summary>Auflösung</summary>

`123` — Die Ausgabe steht **nach** dem rekursiven Aufruf. `p(3)` wartet auf `p(2)`, das auf `p(1)`, das auf `p(0)`. Erst auf dem Rückweg wird gedruckt, vom innersten Aufruf nach aussen. Steht das `print` vor `p(n - 1)`, kommt `321` heraus.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum wirkt `arr[0] = 99` in einer Methode nach aussen, `arr = neuesArray` aber nicht?
- Warum darf man nicht nur über den Rückgabetyp überladen?
- Welche zwei Bestandteile braucht jede terminierende Rekursion?
- Wann ist Rekursion die bessere Wahl als eine Schleife?
- Was ist der Unterschied im Vertrag zwischen `verdoppleAlle` und `getauscht`?
