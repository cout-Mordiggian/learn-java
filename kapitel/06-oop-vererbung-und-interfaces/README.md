# Kapitel 06 — OOP II: Vererbung, Polymorphie, Interfaces

**Ziel:** Du baust Typhierarchien, verstehst, warum Polymorphie erst zur
Laufzeit entscheidet, und weisst, wann ein Interface besser passt als eine
Oberklasse.

---

## 6.1 Vererbung

```java
public class Tier {
    protected String name;

    public Tier(String name) { this.name = name; }

    public String laut() { return "..."; }
}

public class Hund extends Tier {
    public Hund(String name) {
        super(name);              // MUSS die erste Anweisung sein
    }

    @Override
    public String laut() { return "Wuff"; }
}
```

`extends` heisst: "`Hund` ist ein `Tier` und hat alles, was ein `Tier` hat."
Java kennt nur **Einfachvererbung** — genau eine Oberklasse. (Mehrfachvererbung
gibt es nur bei Interfaces, siehe 6.5.)

`super(...)` ruft den Konstruktor der Oberklasse. Schreibst du ihn nicht, fügt
der Compiler ein implizites `super()` ein — was fehlschlägt, wenn die
Oberklasse keinen parameterlosen Konstruktor hat. Das ist eine der häufigsten
Fehlermeldungen beim Einstieg in Vererbung.

Reihenfolge bei `new Hund("Rex")`: erst der `Tier`-Konstruktor, dann der
`Hund`-Konstruktor. Von oben nach unten — der `Tier`-Anteil des Objekts muss
fertig sein, bevor der `Hund`-Teil darauf aufbaut.

> *Seit Java 25* dürfen vor `super(...)` Anweisungen stehen, die `this` noch
> nicht benutzen (z. B. Parameter prüfen oder umrechnen). Das Prinzip bleibt:
> Kein Zugriff auf das Objekt, bevor die Oberklasse initialisiert ist.

`super.laut()` ruft die überschriebene Version der Oberklasse auf — nützlich,
wenn du sie erweitern statt ersetzen willst.

## 6.2 Überschreiben (Overriding) vs. Überladen (Overloading)

| | Overriding | Overloading |
|---|---|---|
| Wo | in der Unterklasse | in derselben Klasse (oder einer Unterklasse) |
| Signatur | **identisch** | **unterschiedlich** |
| Entschieden | zur **Laufzeit** (dynamisch) | zur **Compile-Zeit** (statisch) |
| Annotation | `@Override` | keine |

Beim Überschreiben darf die Sichtbarkeit nur **weiter** werden (`protected` ->
`public`), nie enger. Der Rückgabetyp darf **spezifischer** werden
(*kovarianter Rückgabetyp*):

```java
class Rechteck { Rechteck skaliert(double f) { ... } }
class Quadrat extends Rechteck {
    @Override Quadrat skaliert(double f) { ... }   // erlaubt: Quadrat ist ein Rechteck
}
```

## 6.3 Polymorphie — der eigentliche Gewinn

```java
Tier[] tiere = { new Hund("Rex"), new Katze("Mia"), new Hund("Bello") };

for (Tier t : tiere) {
    System.out.println(t.laut());   // Wuff / Miau / Wuff
}
```

Die Variable hat den Typ `Tier` (**statischer Typ**), das Objekt dahinter ist
ein `Hund` (**dynamischer Typ**). Beim Aufruf entscheidet der **dynamische**
Typ — das nennt man *dynamic dispatch* oder *späte Bindung*.

Der Nutzen: Die Schleife kennt `Hund` und `Katze` gar nicht. Du kannst morgen
`Papagei` hinzufügen, ohne diese Zeile anzufassen. Das ist das
*Open-Closed-Prinzip*: offen für Erweiterung, geschlossen für Änderung.

> **Wichtig:** Nur **Methoden** sind polymorph. **Felder** nicht — sie werden
> nach dem statischen Typ aufgelöst. Ein weiterer Grund, Felder `private` zu
> halten.

### Auf- und Abwärtscasting

```java
Tier t = new Hund("Rex");        // Upcast: immer sicher, ohne Syntax
Hund h = (Hund) t;               // Downcast: du behauptest etwas
Katze k = (Katze) t;             // kompiliert - wirft zur Laufzeit ClassCastException
```

Sicher prüfen — mit Pattern Matching (Java 16+):

```java
if (t instanceof Hund h) {       // prueft UND deklariert h in einem Zug
    h.apportieren();
}
```

Viele `instanceof`-Ketten sind allerdings ein Geruch: Meist gehört das
Verhalten als überschriebene Methode in die Klassen. (Die legitime Ausnahme
sind `sealed`-Hierarchien mit `switch` — Kapitel 10.)

## 6.4 Abstrakte Klassen

```java
public abstract class Figur {
    private final String name;

    protected Figur(String name) { this.name = name; }

    public String getName() { return name; }

    public abstract double flaeche();     // kein Rumpf: Unterklassen MUESSEN liefern

    public String beschreibung() {        // konkrete Methode, nutzt die abstrakte
        return name + " mit Flaeche " + flaeche();
    }
}
```

- `abstract class` kann **nicht** instanziiert werden: `new Figur(...)` ist ein Fehler.
- Sie darf Felder, Konstruktoren und fertige Methoden haben.
- Eine Unterklasse muss **alle** abstrakten Methoden implementieren — oder
  selbst `abstract` sein.

`beschreibung()` ruft `flaeche()` auf, ohne zu wissen, wie sie rechnet. Diese
Umkehrung ("die Basis ruft die Ableitung") heisst *Template Method* und ist der
häufigste sinnvolle Einsatz abstrakter Klassen.

Warum `protected` beim Konstruktor? Er ist für Unterklassen gedacht, die ihn
für `super(...)` brauchen — `public` würde ihn unnötig allen anbieten.
(Genau genommen sieht ihn auch jede Klasse im selben Paket; in diesem Kurs
liegen alle Klassen im selben, unbenannten Paket. Und `new Figur(...)` bleibt
ohnehin verboten, weil die Klasse abstrakt ist.)

## 6.5 Interfaces

```java
public interface Skalierbar {
    Figur skaliert(double faktor);            // implizit public abstract

    default Figur verdoppelt() {              // seit Java 8: mit Rumpf
        return skaliert(2.0);
    }

    static boolean istGueltig(double f) {     // statische Hilfsmethode
        return f > 0;
    }
}

public class Kreis extends Figur implements Skalierbar { ... }
```

Ein Interface beschreibt **Fähigkeiten**, keine Herkunft. Eine Klasse kann
beliebig viele implementieren — hier kommt die Mehrfachvererbung von *Verhalten*
zurück, ohne die Probleme der Mehrfachvererbung von *Zustand*.

Regeln:

- Methoden sind implizit `public abstract` (ausser `default`, `static`, `private`)
- Felder sind implizit `public static final` — also Konstanten, kein Zustand
- Ein Interface hat keine Konstruktoren

### `default`-Methoden

Sie erlauben, ein bestehendes Interface zu erweitern, ohne alle Implementierer
zu brechen. So kam `forEach` nachträglich in `Iterable`, ohne dass jede
Collection der Welt neu geschrieben werden musste.

### Abstrakte Klasse oder Interface?

| Frage | Antwort |
|-------|---------|
| Gemeinsamer **Zustand** (Felder)? | abstrakte Klasse |
| Mehrere unabhängige **Fähigkeiten**? | Interfaces |
| "**ist ein**" (Quadrat ist ein Rechteck)? | Vererbung |
| "**kann**" (Kreis kann skaliert werden)? | Interface |

**Im Zweifel: Interface.** Es bindet weniger fest, und du kannst später eine
abstrakte Basisklasse *zusätzlich* einziehen.

## 6.6 Komposition schlägt Vererbung

Vererbung ist die engste Kopplung, die Java kennt: Die Unterklasse hängt an
den *internen* Entscheidungen der Oberklasse.

```java
// Fragil: erbt und muss jede interne Aenderung der Oberklasse mittragen
class ZaehlendeListe extends ArrayList<String> { ... }

// Robust: benutzt statt zu erben
class ZaehlendeListe {
    private final List<String> intern = new ArrayList<>();
    private int anzahlHinzugefuegt;

    public void hinzufuegen(String s) {
        intern.add(s);
        anzahlHinzugefuegt++;
    }
}
```

Faustregel: Vererbung nur, wenn "ist ein" fachlich wirklich stimmt **und** die
Oberklasse dafür entworfen (und dokumentiert) wurde. Sonst: Komposition.

## 6.7 `final` und Sichtbarkeiten

```java
public final class Punkt { ... }         // keine Unterklassen moeglich
public final void kritisch() { ... }     // nicht ueberschreibbar
```

| Modifier | eigene Klasse | Paket | Unterklasse | überall |
|----------|:---:|:---:|:---:|:---:|
| `private` | X | | | |
| (nichts) | X | X | | |
| `protected` | X | X | X | |
| `public` | X | X | X | X |

`protected` ist keine Abkürzung für "fast public" — es ist Teil der
öffentlichen Zusage an alle, die von dir erben.

## 6.8 `Object` — die Wurzel

Jede Klasse erbt letztlich von `java.lang.Object`. Daher hat wirklich jedes
Objekt `toString()`, `equals()`, `hashCode()`, `getClass()`.

Bei Vererbung wird `equals` heikel: Ist ein `Quadrat(3)` gleich einem
`Rechteck(3,3)`? Wenn `Rechteck.equals` mit `instanceof` arbeitet, sagt das
Rechteck "ja" und das Quadrat "nein" — die Symmetrie des Vertrags ist
gebrochen. Der übliche Ausweg: `getClass() != o.getClass()` statt `instanceof`,
oder — besser — solche Typen gar nicht erst voneinander erben lassen.

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Sechs Dateien in [`src/`](src/) — prüfen mit `./lerne.sh 06`.

### 1. `Figur.java` — abstrakte Basisklasse

- `private final String name`, `protected Figur(String name)`, `getName()`
- `public abstract double flaeche()` und `public abstract double umfang()`
- `public String beschreibung()` -> für einen Kreis mit Radius 1:
  `"Kreis: Flaeche=3.14, Umfang=6.28"`.
  Nutze `String.format(Locale.ROOT, "%s: Flaeche=%.2f, Umfang=%.2f", ...)`.
  **`Locale.ROOT` ist Pflicht** — sonst hängt die Ausgabe von den
  Systemeinstellungen ab und wird auf einem deutschen System zu `3,14`.
  Der Test stellt deshalb absichtlich ein deutsches Locale ein.
- Die beiden abstrakten Methoden sind in `src/` schon vorgegeben, weil
  `Kreis` und `Rechteck` sie mit `@Override` überschreiben — ohne sie
  würde nichts kompilieren.

### 2. `Skalierbar.java` — Interface

- `Figur skaliert(double faktor)`
- `default Figur verdoppelt()` — soll `skaliert(2.0)` aufrufen

### 3. `Kreis.java` — `extends Figur implements Skalierbar`

Feld `radius` mit `getRadius()`, negative Radien mit `IllegalArgumentException`
ablehnen. `skaliert` liefert einen **neuen** Kreis.

### 4. `Rechteck.java` — `extends Figur implements Skalierbar`

- Felder `breite`, `hoehe` mit `getBreite()`/`getHoehe()`; negative Breite
  **oder** Höhe mit `IllegalArgumentException` ablehnen
- `public Rechteck(double breite, double hoehe)` — Name "Rechteck"
- `protected Rechteck(String name, double breite, double hoehe)` —
  damit `Quadrat` seinen eigenen Namen durchreichen kann. (Achtung: `protected`
  heisst "Unterklassen **und** dasselbe Paket". Da alle Kursdateien im selben
  Paket liegen, könnte hier jede Klasse den Konstruktor aufrufen.)

### 5. `Quadrat.java` — `extends Rechteck`

- `public Quadrat(double seite)` ruft `super("Quadrat", seite, seite)`
- `getSeite()`
- `skaliert` **kovariant** überschreiben: Rückgabetyp `Quadrat`, nicht `Figur`.
  Die Signatur ist in `src/` vorgegeben (sonst kompiliert der Test nicht) —
  deine Aufgabe ist der Rumpf und das Verstehen, *warum* das erlaubt ist.

### 6. `Figuren.java` — Polymorphie nutzen

Zwei statische Methoden, die **nur** den Typ `Figur` kennen:

- `static double gesamtFlaeche(Figur[] figuren)`
- `static Figur groesste(Figur[] figuren)` — größte Fläche, `null` bei leerem Array

Der Punkt der Aufgabe: In diesen beiden Methoden darf kein `instanceof` und
kein Cast vorkommen. Genau das ist Polymorphie.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
class Tier {
    String laut()       { return "..."; }
    String vorstellen() { return "Ich sage " + laut(); }
}
class Hund extends Tier {
    @Override String laut() { return "Wuff"; }
}

Tier t = new Hund();
System.out.println(t.vorstellen());
```

<details><summary>Auflösung</summary>

`Ich sage Wuff` — `vorstellen` ist in `Tier` geschrieben, aber der Aufruf `laut()` darin richtet sich nach dem **dynamischen** Typ, und das ist `Hund`. Das ist das Template-Method-Prinzip aus 6.4.

</details>

**2.**

```java
class A { String name = "A"; String n() { return name; } }
class B extends A { String name = "B"; String n() { return name; } }

A x = new B();
System.out.println(x.name + x.n());
```

<details><summary>Auflösung</summary>

`AB` — Der Feldzugriff `x.name` folgt dem **statischen** Typ `A`. Die Methode `n()` wird dagegen dynamisch aufgelöst und gehört zu `B`. Felder sind nicht polymorph, deshalb gehören sie auf `private`.

</details>

**3.**

```java
class A { A() { System.out.print("A"); } }
class B extends A { B() { System.out.print("B"); } }
class C extends B { C() { System.out.print("C"); } }

new C();
```

<details><summary>Auflösung</summary>

`ABC` — Jeder Konstruktor ruft zuerst (implizit) `super()` auf. Die Oberklasse wird also immer zuerst fertig gebaut, von oben nach unten.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum muss `super(...)` laufen, bevor der Konstruktor das Objekt benutzt?
- Was ist der Unterschied zwischen statischem und dynamischem Typ?
- Warum sind Felder nicht polymorph?
- Wann Interface, wann abstrakte Klasse?
- Warum bricht `equals` zwischen `Rechteck` und `Quadrat` die Symmetrie?
