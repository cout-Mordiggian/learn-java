# Kapitel 13 — Testen und Fehlersuche

**Ziel:** Du schreibst Tests, die echte Fehler finden, weil du weisst, *welche*
Fälle du prüfen musst. Und wenn etwas schiefgeht, suchst du den Fehler
systematisch statt durch Raten.

---

## 13.1 Warum eigene Tests?

Bisher lagen die Tests fertig in `tests/Tests.java` und haben dir gesagt, was
richtig ist. Im echten Leben schreibt sie dir niemand. Du bekommst eine
Beschreibung ("ab 20 Euro Warenwert kostet der Versand 2,90") und musst selbst
entscheiden, woran du erkennst, dass dein Code stimmt. Warum lohnt sich das?

- **Ein Test ist ausführbare Spezifikation:** "genau 2000 Cent kosten 290
  Cent Versand" ist präziser als jeder Absatz Prosa.
- **Tests fangen Rückfälle:** Änderst du später etwas, sagt dir ein
  Knopfdruck, ob dabei etwas anderes kaputtgegangen ist.
- **Tests zwingen zum Nachdenken:** Oft merkst du erst beim Schreiben, dass
  die Anforderung einen Fall gar nicht regelt.

Aber: Ein grüner Test beweist nur, dass *diese* Fälle stimmen. Deshalb geht
es in diesem Kapitel vor allem darum, **die richtigen Fälle** zu wählen.

## 13.2 Was einen guten Test ausmacht

**Ein Verhalten pro Prüfung.** Schlägt "genau 20 Euro: 2,90 Versand" fehl,
weisst du sofort, wo du suchen musst. Bei "Versand funktioniert" weisst du nichts.

**Vorbereiten — Ausführen — Prüfen** (englisch *Arrange — Act — Assert*):

```java
Konto konto = new Konto("Anna", 1000);          // Vorbereiten
konto.einzahlen(500);                           // Ausfuehren
Pruef.gleich(1500L, konto.getGuthaben(), "Einzahlung erhoeht das Guthaben");  // Pruefen
```

**Sprechende Namen.** Die Beschreibung sagt, *was gelten soll*, nicht welche
Methode aufgerufen wird: `"negativer Betrag wird abgelehnt"` statt `"test3"`.

**Erwartete Werte von Hand ausrechnen.** Trägst du ein, was dein Code gerade
liefert, testest du nur, dass er tut, was er tut, mitsamt seinen Fehlern.

**Wiederholbar.** Keine Zufallszahlen, keine Uhrzeit, keine Tastatureingabe.
Sonst ist ein roter Test morgen wieder grün, und du weisst nicht warum.

## 13.3 Welche Fälle testen: Äquivalenzklassen und Grenzwerte

Alle Eingaben durchzuprobieren ist unmöglich. Der Trick: Teile die Eingaben
in **Äquivalenzklassen** ein. Das sind Bereiche, in denen sich der Code
gleich verhalten *sollte*. Aus jeder Klasse nimmst du einen typischen Vertreter.
Dann testest du die **Grenzen** zwischen den Klassen, denn dort sitzen die
meisten Fehler (`>` statt `>=`, `<=` statt `<`).

Checkliste für Grenzwerte: **0, 1, viele** — **leer** (`""`, `{}`) —
**`null`**, wenn die Spezifikation etwas dazu sagt — **negativ** (`-1`) —
**genau an der Grenze** (`2000` bei "ab 2000") — **knapp daneben** (`1999`) —
**sehr gross** (über `Integer.MAX_VALUE` = 2.147.483.647, wenn `long` erlaubt ist).

### Durchgerechnet: Versandkosten

`long versandCent(long warenwertCent)`: Unter 20 Euro kostet der Versand
4,90 Euro, ab 20 Euro 2,90 Euro, ab 50 Euro nichts. Negativer Warenwert:
`IllegalArgumentException`.

Schritt 1 — **Äquivalenzklassen** (alles in Cent):

```
      ungueltig   |      490      |      290      |       0
  ----------------+---------------+---------------+------------------->
                  0             1999 2000       4999 5000       Warenwert
```

Schritt 2 — **Testwerte wählen**: je ein Vertreter und dann beide Seiten jeder Grenze.

| Warenwert | Erwartet | Warum dieser Wert? |
|----------:|---------:|--------------------|
| 1000 | 490 | Vertreter "unter 20 Euro" |
| 3000 | 290 | Vertreter "ab 20 Euro" |
| 8000 | 0 | Vertreter "ab 50 Euro" |
| 1999 | 490 | knapp unter der ersten Grenze |
| 2000 | 290 | genau an der ersten Grenze |
| 4999 | 290 | knapp unter der zweiten Grenze |
| 5000 | 0 | genau an der zweiten Grenze |
| 0 | 490 | untere Grenze der gültigen Werte |
| -1 | Exception | knapp unter dem gültigen Bereich |

Neun Tests, und jeder falsche Vergleich fällt auf: Mit `warenwert > 2000`
liefert 2000 die falschen 490 Cent; mit nur 1000, 3000 und 8000 merkst du das
nie. Nebenbei: Auch 0 Cent Warenwert kosten 4,90 Versand. Gewollt? Das fragst
du den Auftraggeber. **Gute Tests decken auch Lücken in der Anforderung auf.**

## 13.4 Mit Prüf selbst testen

`lib/Pruef.java` ist das Mini-Framework, das alle Kapiteltests benutzen. Du
kannst es genauso für eigenen Code verwenden. Ein Testprogramm ist einfach
eine Klasse mit `main`:

```java
public class VersandTests {
    public static void main(String[] args) {
        Pruef.abschnitt("Versandkosten");
        Pruef.gleich(490L, Versand.versandCent(1999), "knapp unter 20 Euro: 4,90");
        Pruef.gleich(290L, Versand.versandCent(2000), "genau 20 Euro: 2,90");
        Pruef.gleich(0L,   Versand.versandCent(5000), "genau 50 Euro: frei");
        Pruef.wirft(IllegalArgumentException.class,
                () -> Versand.versandCent(-1), "negativer Warenwert wird abgelehnt");
        Pruef.bericht();
    }
}
```

Liegt sie in `src/`, startest du sie mit `./lerne.sh 13 -r VersandTests`.
`490L` statt `490`, weil `gleich` Objekte vergleicht: Ein `Integer` ist nie
`equals` zu einem `Long`. Und `() -> Versand.versandCent(-1)` ist ein
**Lambda**: ein Stück Code, das `wirft` erst selbst in einem `try` ausführt
(mehr in Kapitel 9). Ohne Framework prüfst du Werte mit `if` und Exceptions
mit `try`/`catch`, so wie in Teil A der Aufgaben:

```java
try {
    Versand.versandCent(-1);
    System.out.println("FEHL: -1 haette werfen muessen");   // nur erreicht, wenn NICHT geworfen wurde
} catch (IllegalArgumentException e) {
    // erwartet - bestanden
}
```

## 13.5 Ausblick: JUnit

In echten Projekten nimmt man **JUnit 5**: `assertEquals` statt `Pruef.gleich`,
`assertThrows` statt `Pruef.wirft`, jeder Test eine Methode mit `@Test`, die
Maven, Gradle oder die IDE automatisch finden. Beispiel in Abschnitt 12.12,
Projektdateien in [`../12-nebenlaeufigkeit-und-werkzeuge/werkzeuge/`](../12-nebenläufigkeit-und-werkzeuge/werkzeuge/).
Die Regeln für gute Testfälle gelten dort unverändert.

## 13.6 Fehlersuche systematisch

Ein Test ist rot. Der Anfängerreflex: irgendwo etwas ändern und hoffen.
Besser ist ein fester Ablauf:

1. **Reproduzieren.** Finde eine Eingabe, bei der der Fehler *jedes Mal*
   auftritt. Ein roter Test ist genau das.
2. **Eingrenzen.** Wo genau? Welche Methode, welche Zeile? Halbiere den
   Suchraum: Stimmt der Wert in der Mitte der Methode noch?
3. **Hypothese.** Formuliere einen Satz: "Ich glaube, `summe / anzahl` rechnet
   ganzzahlig." Ohne Hypothese ist jede Änderung Raten.
4. **Prüfen.** Bestätige die Hypothese, *bevor* du etwas reparierst: mit einer
   Ausgabe, im Debugger, in `jshell` (`3 / 2` ergibt `1`).
5. **Beheben.** Eine kleine, gezielte Änderung. Danach alle Tests laufen lassen.
6. **Test dafür schreiben.** Die Eingabe aus Schritt 1 wird ein fester Test.
   Kommt der Fehler je zurück, ist er sofort rot.

### Stacktraces lesen

Die Regel aus Abschnitt 7.9: von oben lesen, die erste Zeile mit **deiner**
Datei ist der Tatort. Die Tests dieses Kapitels fassen das für dich zusammen:

```
FEHL leeres Array
     erwartet: 0  |  bekommen: "ArrayIndexOutOfBoundsException (Index 0 out of bounds for length 0) in Fehlerhaft.java:35"
```

Typ, Nachricht und Zeile ergeben fast schon die Hypothese: Index 0 bei
Länge 0, die Schleife greift auf ein Element zu, das es nicht gibt.

### Print-Debugging, aber richtig

`System.out.println` ist völlig legitim, wenn du es gezielt einsetzt:

```java
System.out.println("DEBUG zaehle: i=" + i + " zahlen[i]=" + zahlen[i] + " anzahl=" + anzahl);
```

**Beschriften** (Methode, Variable, Wert; ein nacktes `println(x)` hilft
niemandem), an die **richtige Stelle** setzen (vor und nach der verdächtigen
Zeile, in der Schleife: *wann* wird der Wert falsch?) und **wieder
entfernen**, sobald der Fehler behoben ist. Das `DEBUG`-Präfix hilft beim Finden.

### Der Debugger in der IDE

Ein Debugger hält das Programm an einer Zeile an und lässt dich
hineinschauen. So geht es in IntelliJ IDEA und VS Code (siehe `SETUP.md`):

- **Breakpoint:** in den Rand links neben die Zeilennummer klicken (roter Punkt).
- **Debug starten:** eine kleine Klasse mit `main` in `src/`, die die
  verdächtige Methode aufruft; in IntelliJ das Käfer-Symbol, in VS Code
  "Debug" über `main`. Hält das Programm, siehst du alle lokalen Variablen.
- **Step Over** (IntelliJ `F8`, VS Code `F10`) führt die Zeile aus, **Step
  Into** (`F7` / `F11`) springt in den Methodenaufruf, **Resume** (`F9` / `F5`)
  läuft bis zum nächsten Breakpoint.

Anders als bei `println` musst du nicht vorher wissen, welche Variable dich
interessiert.

### `assert`: Annahmen im Code festhalten

```java
assert anzahl >= 0 : "anzahl negativ: " + anzahl;
```

Ist die Bedingung falsch, wirft Java einen `AssertionError` mit der Meldung
hinter dem Doppelpunkt. Aber: **Assertions sind standardmäßig aus.** Nur mit
`java -ea` (*enable assertions*) werden sie ausgewertet, etwa
`java -ea MeinProgramm.java`; `lerne.sh` schaltet sie nicht ein. Das ist
gewollt: Assertions prüfen Annahmen, die bei korrektem Code *immer* gelten.
Sie helfen beim Entwickeln und kosten im Betrieb ausgeschaltet keine
Rechenzeit. Daraus folgen zwei Regeln:

- **Nie Eingaben mit `assert` prüfen.** Ein negativer Preis verlangt eine
  `IllegalArgumentException` (Kapitel 7), die auch im Betrieb greift.
- **Keine Nebenwirkungen im `assert`.** `assert liste.remove(x);` entfernt
  ohne `-ea` einfach nichts.

### Gummienten-Debugging

Erkläre deinen Code Zeile für Zeile laut einer Gummiente (oder einer
Kollegin). Nicht "hier wird summiert", sondern genau: "`summe` ist ein `int`,
`zahlen.length` auch, also teilt Java ganzzahlig..." Beim Aussprechen merkst
du oft selbst, wo deine Vorstellung und der Code auseinandergehen.

## 13.7 Die häufigsten Anfängerbugs: eine Checkliste

Rot und keine Idee? Geh diese Liste durch:

| Bug | Beispiel | Symptom | Gegenmittel |
|-----|----------|---------|-------------|
| Off-by-one | `i <= a.length` | `ArrayIndexOutOfBoundsException` oder ein Element zu viel/zu wenig | `i < a.length` oder for-each |
| `==` bei Strings | `name == "admin"` | mal `true`, mal `false` | `"admin".equals(name)` (3.2) |
| Ganzzahldivision | `summe / anzahl` bei zwei `int` | Nachkommastellen fehlen | `(double) summe / anzahl` |
| fehlendes `break` | klassischer `switch` | Ergebnis vom *nächsten* `case` | `break` oder Pfeilform `case 1 ->` |
| `NullPointerException` | `name.length()` bei `name == null` | Abbruch mit NPE | früh auf `null` prüfen, Literal vorne bei `equals` |
| Ergebnis nicht zugewiesen | `s.strip();` | String bleibt unverändert | `s = s.strip();` (Strings sind unveränderlich) |
| Überlauf | `int gesamt = preis * menge;` | plötzlich negative Zahlen | `long` rechnen, ggf. `Math.multiplyExact` |
| `;` nach `if` | `if (x > max); { x = max; }` | Block läuft *immer* | Semikolon weg, Klammern direkt nach `)` |

Besonders heimtückisch ist `==` bei Strings, weil Tests mit Literalen
zufällig grün werden: `"admin" == "admin"` ist `true`, beide Literale sind
dasselbe Objekt aus dem String-Pool. Erst ein zur Laufzeit erzeugter String
(Eingabe, `substring`, `new String(...)`) deckt den Fehler auf. Auch das ist
eine Regel für die Testauswahl.

Der Compiler hilft mit, wenn du ihn lässt: `javac -Xlint:all ...` warnt unter
anderem vor `;` nach `if` (`empty statement after if`) und vor dem
Durchfallen im `switch` (`possible fall-through into case`).

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Prüfen mit `./lerne.sh 13`.

### Teil A — Tests schreiben, die Fehler finden

Das Interface [`Rabattrechner`](src/Rabattrechner.java) hat eine einzige Methode
`long endpreisCent(long preisCent, int menge)`. Die Regeln:

1. `preisCent` ist der Stückpreis in Cent, `menge` die Stückzahl.
2. Ist `preisCent` oder `menge` negativ: `IllegalArgumentException`.
3. Menge 0 ist erlaubt und kostet 0. Ein Preis von 0 ist ebenfalls erlaubt.
4. Gesamtpreis = `preisCent * menge`, auch für Werte, die nicht in einen `int`
   passen. (Du darfst annehmen, dass Gesamtpreis * 100 in einen `long` passt.)
5. Mengenrabatt: 0 bis 9 Stück 0 %, 10 bis 49 Stück 5 %, ab 50 Stück 10 %.
6. Rabatt = Gesamtpreis * Prozent / 100, auf ganze Cent **abgerundet**.
   Endpreis = Gesamtpreis - Rabatt. Beispiel: 199 Cent * 10 = 1990, Rabatt
   99,5 -> 99, Endpreis 1891.

In `src/Kandidaten.java` stecken eine korrekte Implementierung und **sieben
Mutanten**: Kopien mit je einem typischen Fehler. **Schau nicht hinein**,
bevor du fertig bist. Die Übung ist, Fehler zu finden, die du nicht kennst.

Schreibe in [`src/MeineTests.java`](src/MeineTests.java) die Methode
`static boolean alleBestanden(Rabattrechner r)`: deine Prüfungen gegen `r`,
Ergebnis `true`, wenn alle stimmen. Benutze die Helfer `gleich(...)` und
`fehlschlag(...)`, für erwartete Exceptions `try`/`catch` wie in 13.4. Die
Tests rufen deine Methode achtmal auf. Mit der korrekten Implementierung muss
`true` herauskommen, sonst ist einer **deiner** erwarteten Werte falsch. Mit
jedem Mutanten muss `false` herauskommen: Deine Tests haben ihn **entlarvt**.
Ein Mutant, der überlebt, zeigt eine Lücke in deinen Tests.

Das heisst **Mutationstest** (Werkzeuge wie PIT erzeugen Mutanten automatisch).
Spielregel: Prüfe nur über `r.endpreisCent(...)`, kein `instanceof`.

### Teil B — Fehlersuche

[`src/Fehlerhaft.java`](src/Fehlerhaft.java) enthält sechs kleine Methoden.
Jede kompiliert, und jede hat **genau einen** Fehler aus der Checkliste in 13.7.
Das Javadoc beschreibt, was die Methode tun soll, und die Tests prüfen genau das.

`durchschnitt`, `zaehleGroesser`, `istAdmin`, `wochentag`, `normalisiere`
und `hoechstens`. Geh nach 13.6 vor und behebe jeden Fehler mit einer minimalen Änderung.
Wirft eine Methode eine Exception, melden die Tests sie als `FEHL` mit Datei
und Zeile, statt abzubrechen.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
long mikro = 1_000_000 * 1_000_000;
System.out.println(mikro);
```

<details><summary>Auflösung</summary>

`-727379968` — Beide Faktoren sind `int`-Literale, also rechnet Java in `int`. Das Ergebnis 10^12 passt nicht hinein und läuft über. Erst **danach** wird der kaputte Wert in den `long` erweitert. Die Zielvariable bestimmt nicht, womit gerechnet wird. Richtig: `1_000_000L * 1_000_000`.

</details>

**2.**

```java
int summe = 0;
for (int i = 1; i <= 3; i++); {
    summe += 10;
}
System.out.println(summe);
```

<details><summary>Auflösung</summary>

`10` — Das Semikolon hinter der `for`-Klammer ist der ganze Schleifenrumpf: eine leere Anweisung, dreimal ausgeführt. Der Block `{ summe += 10; }` gehört nicht zur Schleife und läuft genau einmal. Derselbe Fehler wie `;` nach `if` aus 13.7.

</details>

**3.** Gestartet **ohne** `-ea`:

```java
int konto = -50;
assert konto >= 0 : "Konto negativ: " + konto;
System.out.println("weiter mit " + konto);
```

<details><summary>Auflösung</summary>

`weiter mit -50` — Assertions sind standardmäßig ausgeschaltet, die Zeile mit `assert` wird gar nicht ausgewertet. Erst mit `java -ea` gibt es einen `AssertionError: Konto negativ: -50`. Genau deshalb taugt `assert` nicht zur Prüfung von Eingaben. In `jshell` siehst du den Unterschied mit `jshell -R-ea`.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum reicht es nicht, für eine Staffel "ab 10 Stück" nur mit 5 und 20 Stück zu testen?
- Was sagt dir ein Mutant, der deine Tests überlebt?
- Warum schreibt man nach dem Beheben eines Fehlers noch einen Test dafür?
- Warum darfst du einen negativen Preis nicht mit `assert` abweisen?
- Warum kann ein Test mit `istAdmin("admin")` den `==`-Fehler nicht finden?

---

**Wie geht es weiter?** Zurück in die Hauptlinie: [Kapitel 8 — Collections und Generics](../08-collections-und-generics/README.md).
Ab jetzt lohnt es sich, bei jeder Aufgabe kurz zu überlegen: Welche Grenzfälle hätten *deine* Tests geprüft?
