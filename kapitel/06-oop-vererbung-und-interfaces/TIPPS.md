# Kapitel 06 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## 1. Figur.java: Name, Konstruktor und `getName`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 6.4 (Abstrakte Klassen) zeigt diese Klasse fast wortgleich. Frag
dich: Warum darf eine abstrakte Klasse ueberhaupt einen Konstruktor haben, wenn
man sie nie mit `new` erzeugen kann?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Ein Feld `private final String name`, im `protected`-Konstruktor gesetzt,
`getName()` gibt es zurueck. Den Konstruktor ruft niemand direkt auf, sondern
jede Unterklasse ueber `super(...)` — deshalb landet dort der Name "Kreis",
"Rechteck" oder "Quadrat". `private` statt `protected` beim Feld reicht, weil
Unterklassen ueber `getName()` an den Namen kommen.

</details>

## 1. Figur.java: `beschreibung`

<details><summary>Tipp 1 — Richtung</summary>

Das ist die *Template Method* aus Abschnitt 6.4: Eine fertige Methode in der
Basisklasse ruft abstrakte Methoden auf, die erst die Unterklasse liefert. Frag
dich: Woher bekommt `Figur` Flaeche und Umfang, obwohl sie selbst gar nicht
weiss, wie man sie berechnet?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Du rufst `flaeche()` und `umfang()` **als Methoden** auf — zur Laufzeit wird
dann automatisch die Version des echten Objekts genommen (dynamische Bindung,
Abschnitt 6.3). Das Format kommt aus `String.format` mit drei Platzhaltern:
`%s` fuer den Namen, zweimal `%.2f` fuer zwei Nachkommastellen (gerundet).

**Die Falle der Tests:** Der Test stellt absichtlich `Locale.GERMANY` ein. Ohne
`Locale.ROOT` als **erstes** Argument von `String.format` kommt `3,14` statt
`3.14` heraus. `Locale` ist in `src/Figur.java` schon importiert.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public String beschreibung() {
    return String.format(Locale.ROOT, "%s: Flaeche=%.2f, Umfang=%.2f",
            ..., ..., ...);
}
```

</details>

## 2. Skalierbar.java: `verdoppelt`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 6.5, Unterabschnitt "`default`-Methoden". Frag dich: Welche Methode
des Interfaces kann eine `default`-Methode aufrufen, obwohl das Interface sie
selbst gar nicht implementiert?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Die Methode `skaliert` ist abstrakt, aber zur Laufzeit gibt es immer ein
konkretes Objekt (`Kreis`, `Rechteck`, ...), das sie implementiert. Deine
`default`-Methode ruft also einfach `skaliert` mit dem richtigen Faktor auf —
der Platzhalter in `src/` benutzt noch den falschen. Kein `Kreis` und kein
`Rechteck` muss dafuer eine Zeile schreiben; genau das ist der Sinn.

</details>

## 3. Kreis.java

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 6.1 (Vererbung, `super(...)`) und 6.5 (`implements`). Das
`super("Kreis")` ist schon vorgegeben. Frag dich: Wo genau darf die Pruefung
auf einen negativen Radius stehen — und warum in Java 21 nicht davor?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Feld `private final double radius`; im Konstruktor **nach** `super(...)`
  pruefen (`< 0` -> `IllegalArgumentException`), dann zuweisen.
- Flaeche ist `pi * r * r`, Umfang `2 * pi * r`, mit `Math.PI`.
- `skaliert` erzeugt einen **neuen** Kreis mit skaliertem Radius — das Original
  bleibt unveraendert (Test "Original unveraendert").

**Die Falle der Tests:** Bei Radius 2 sind Flaeche und Umfang zufaellig beide
`4 * pi`. Vertauschst du die Formeln, faellt das erst beim zusaetzlichen Test
mit Radius 1 auf.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
private final double radius;

public Kreis(double radius) {
    super("Kreis");
    if (...) {
        throw new IllegalArgumentException("...");
    }
    this.radius = radius;
}

@Override
public Figur skaliert(double faktor) {
    return new Kreis(...);
}
```

</details>

## 4. Rechteck.java

<details><summary>Tipp 1 — Richtung</summary>

Es gibt zwei Konstruktoren, und der oeffentliche delegiert schon per `this(...)`
an den `protected`. Frag dich: In welchem der beiden gehoert die Pruefung hin,
damit sie nur **einmal** dasteht und trotzdem auch fuer `Quadrat` gilt?
(Abschnitt 5.2 zu `this(...)`, Abschnitt 6.1 zu `super(...)`.)

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Alle Arbeit steckt im `protected Rechteck(String name, double breite, double hoehe)`:
nach dem vorgegebenen `super(name)` pruefen, dann beide `final`-Felder setzen.
Der oeffentliche Konstruktor bleibt, wie er ist — und `Quadrat` landet ueber
`super("Quadrat", seite, seite)` ebenfalls hier.

Die Tests pruefen negative Breite **und** negative Hoehe getrennt. Eine
Bedingung, die nur eine der beiden Seiten ansieht, besteht nur einen der zwei
Tests.

Flaeche ist Breite mal Hoehe, Umfang zweimal die Summe der Seiten. `skaliert`
liefert ein neues Rechteck, in dem **beide** Seiten skaliert sind (3x4 mal 2
ergibt 6x8, also Flaeche 48).

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
private final double breite;
private final double hoehe;

protected Rechteck(String name, double breite, double hoehe) {
    super(name);
    if (... || ...) {
        throw new IllegalArgumentException("...");
    }
    this.breite = ...;
    this.hoehe = ...;
}

@Override
public double umfang() {
    return 2 * (...);
}
```

</details>

## 5. Quadrat.java

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 6.2, der kovariante Rueckgabetyp. Frag dich zweierlei: Wo steckt die
Seitenlaenge schon, ohne dass `Quadrat` ein eigenes Feld braucht? Und warum darf
`skaliert` hier `Quadrat` statt `Figur` zurueckgeben?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`getSeite()` braucht kein neues Feld: Das Quadrat hat seine Seite als Breite
(und Hoehe) an `Rechteck` weitergegeben, also liest du sie ueber den geerbten
Getter. Die Felder von `Rechteck` sind `private` — direkt siehst du sie nicht.

`flaeche()` und `umfang()` musst du **nicht** ueberschreiben, die
Rechteck-Formeln stimmen schon.

**Die Falle:** `super.skaliert(faktor)` hilft hier nicht — das liefert ein
`Rechteck` namens "Rechteck", und der Rueckgabetyp waere `Figur`, kein `Quadrat`.
Der Test verlangt aber `q2.getName()` gleich `"Quadrat"`. Du brauchst also ein
**neues Quadrat**.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public double getSeite() {
    return get...();
}

@Override
public Quadrat skaliert(double faktor) {
    return new Quadrat(... * faktor);
}
```

</details>

## 6. Figuren.java: `gesamtFlaeche` und `groesste`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 6.3 (Polymorphie). Frag dich: Was kannst du mit einer Variablen vom
Typ `Figur` tun, ohne zu wissen, ob dahinter ein Kreis oder ein Quadrat steckt?
Die Regel der Aufgabe: kein `instanceof`, kein Cast.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`gesamtFlaeche`: eine for-each-Schleife, die `flaeche()` jeder Figur aufsummiert.
Bei einem leeren Array laeuft die Schleife nie, und die Summe bleibt `0.0` —
das ist automatisch richtig.

`groesste`: ein klassisches Maximum-Suchen. Bei leerem Array sofort `null`.
Sonst merkst du dir die erste Figur als bisher beste und ersetzt sie, sobald
eine Figur eine **groessere** Flaeche hat. Vergleiche Flaechen, nicht Namen und
nicht Figuren. Die Tests pruefen, dass das auch dann klappt, wenn die groesste
Figur vorne **oder** hinten steht.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public static Figur groesste(Figur[] figuren) {
    if (...) return null;
    Figur beste = ...;
    for (Figur f : figuren) {
        if (...) {
            beste = f;
        }
    }
    return beste;
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum muss <code>super(...)</code> laufen, bevor der Konstruktor das Objekt benutzt?</summary>

Ein `Kreis` besteht aus einem `Figur`-Anteil und einem `Kreis`-Anteil, und der
`Kreis`-Teil baut auf dem `Figur`-Teil auf. Bevor der Oberklassen-Konstruktor
gelaufen ist, sind deren Felder nicht gesetzt und ihre Invarianten nicht
hergestellt — `getName()` wuerde z. B. `null` liefern. Deshalb wird von oben nach
unten initialisiert. In Java 21 muss `super(...)` darum die erste Anweisung
sein; seit Java 25 duerfen davor Anweisungen stehen, die `this` noch nicht
benutzen (etwa eine Parameterpruefung).

</details>

<details><summary>Was ist der Unterschied zwischen statischem und dynamischem Typ?</summary>

Der **statische** Typ ist der Typ der Variablen, so wie er im Quelltext steht;
nach ihm entscheidet der Compiler, welche Methoden du ueberhaupt aufrufen darfst.
Der **dynamische** Typ ist die Klasse des Objekts, das zur Laufzeit wirklich
dahinter steckt. Bei `Figur f = new Kreis(1);` ist der statische Typ `Figur`,
der dynamische `Kreis`. Ruft man `f.flaeche()` auf, entscheidet der dynamische
Typ, welche Implementierung laeuft (dynamic dispatch).

</details>

<details><summary>Warum sind Felder nicht polymorph?</summary>

Nur Methoden werden ueberschrieben und zur Laufzeit nach dem dynamischen Typ
ausgewaehlt. Ein gleichnamiges Feld in einer Unterklasse **verdeckt** das Feld
der Oberklasse nur, es ueberschreibt es nicht — der Zugriff wird schon zur
Compile-Zeit nach dem statischen Typ aufgeloest.

```java
class A { String s = "A"; }
class B extends A { String s = "B"; }
A a = new B();
a.s;   // "A" - obwohl das Objekt ein B ist
```

Deshalb Felder `private` halten und nur ueber Methoden zugreifen.

</details>

<details><summary>Wann Interface, wann abstrakte Klasse?</summary>

Eine abstrakte Klasse passt, wenn verwandte Klassen gemeinsamen **Zustand**
(Felder) und gemeinsamen Code teilen und fachlich ein "ist ein" besteht — wie
`Figur` mit ihrem Namen. Ein Interface beschreibt eine **Faehigkeit** ("kann
skaliert werden"), hat keinen Instanzzustand, und eine Klasse kann beliebig
viele davon implementieren, aber nur von einer Klasse erben. Im Zweifel: Interface —
es bindet weniger fest, und eine abstrakte Basisklasse kann man spaeter
zusaetzlich einziehen.

</details>

<details><summary>Warum bricht <code>equals</code> zwischen <code>Rechteck</code> und <code>Quadrat</code> die Symmetrie?</summary>

Der `equals`-Vertrag verlangt: `a.equals(b)` genau dann, wenn `b.equals(a)`.
Prueft `Rechteck.equals` mit `instanceof Rechteck`, besteht auch ein `Quadrat`
diese Pruefung, also sagt `new Rechteck(3, 3).equals(new Quadrat(3))` "ja".
Prueft `Quadrat.equals` aber mit `instanceof Quadrat`, faellt das Rechteck durch,
und `new Quadrat(3).equals(new Rechteck(3, 3))` sagt "nein". Auswege sind
`getClass() != o.getClass()` statt `instanceof` oder, besser, solche Werttypen
gar nicht voneinander erben zu lassen.

</details>
