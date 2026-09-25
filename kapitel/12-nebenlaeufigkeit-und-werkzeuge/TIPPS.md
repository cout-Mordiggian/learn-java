# Kapitel 12 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **naechste** Stufe auf — jede verraet mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## `Zaehler.java`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 12.2 (warum `zaehler++` nicht atomar ist) und 12.3, "`synchronized`".
Frag dich: Welche Methoden fassen das Feld `stand` an — lesend **oder**
schreibend? Genau die brauchen denselben Schutz.

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`synchronized` als Modifier an der Methode sperrt auf `this`. Weil alle drei
Methoden auf demselben Objekt sperren, schliessen sie sich gegenseitig aus. Der
Rumpf selbst ist jeweils eine Zeile: erhoehen, um `betrag` erhoehen, zurueckgeben.

Die Tests lassen 8 Threads gleichzeitig `erhoehen()` **und** `erhoeheUm(2)`
aufrufen. Vergisst du `synchronized` bei einer der beiden, gehen Erhoehungen
verloren. Bei `wert()` sieht der Test den Fehler nicht (die README erklaert,
warum) — setz es trotzdem, sonst ist die Sichtbarkeit nicht garantiert.
`erhoeheUm(-3)` ist erlaubt; du musst negative Schritte nicht abfangen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
public synchronized void erhoehen() {
    ...
}

public ... void erhoeheUm(int betrag) {
    ...
}

public ... int wert() {
    return ...;
}
```

</details>

## Aufgabe 1: `zaehleMitZaehler`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 12.1 (`Thread`, `start()`, `join()`) und dein `Zaehler` von oben.
Frag dich: Was soll jeder Thread tun — und wann wartest du auf die Threads,
damit sie wirklich **gleichzeitig** laufen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

In einer Schleife `threads`-mal einen `Thread` mit einem Lambda anlegen, das
`proThread`-mal `zaehler.erhoehen()` aufruft. Jeden Thread in einer
`List<Thread>` merken und **starten**. Erst danach, in einer zweiten Schleife,
alle mit `join()` abwarten. Dann `zaehler.wert()` zurueckgeben.

Typische Fehler:

- `t.run()` statt `t.start()`: Dann laeuft alles im aktuellen Thread.
- `start()` und `join()` in derselben Schleife: Dann wartet jeder Thread, bis er
  fertig ist, bevor der naechste startet — korrektes Ergebnis, aber nichts
  laeuft parallel. Den Fehler sieht der Test nicht.
- `threads = 0` (Test "keine Threads"): Beide Schleifen laufen nicht, es kommt
  `0` heraus — dafuer brauchst du keinen Sonderfall.

`zaehler` und `proThread` darfst du im Lambda benutzen, weil sie effektiv final
sind (Kapitel 9.4). Das Objekt hinter `zaehler` aendert sich zwar, die
Referenz aber nicht.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
Zaehler zaehler = new Zaehler();
List<Thread> alle = new ArrayList<>();
for (int i = 0; i < threads; i++) {
    Thread t = new Thread(() -> {
        for (...) {
            ...
        }
    });
    alle.add(t);
    t.start();
}
for (Thread t : alle) {
    ...
}
return zaehler.wert();
```

</details>

## Aufgabe 2: `zaehleMitAtomic`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 12.3, "`AtomicInteger` und Freunde". Der Aufbau ist derselbe wie in
Aufgabe 1 — nur der Zaehler ist ein anderer. Frag dich: Welche Methode von
`AtomicInteger` erhoeht in **einem** unteilbaren Schritt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Uebernimm die Struktur aus Aufgabe 1 (erst alle starten, dann alle joinen) und
ruf im Thread `zaehler.incrementAndGet()` auf. Am Ende liefert `zaehler.get()`
den Stand.

Nicht in die Falle tappen, `zaehler.set(zaehler.get() + 1)` zu schreiben: Das
sind wieder zwei getrennte Schritte, und zwischen ihnen kann ein anderer Thread
dazwischenkommen — genau wie bei `zaehler++`.

</details>

## Aufgabe 3: `summeParallel`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 12.4 (`ExecutorService`, `submit`, `Future.get()`) und 12.3, "Gar
keinen gemeinsamen Zustand". Jede Teilaufgabe rechnet ihre eigene Teilsumme
und gibt sie zurueck; zusammengezaehlt wird erst am Ende. Frag dich: Wie teilst
du `1..bis` in hoechstens `threads` Abschnitte auf, ohne dass eine Zahl fehlt
oder doppelt vorkommt?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- Abschnittslaenge **aufrunden**: `(bis + threads - 1) / threads`. Bei
  `bis = 10, threads = 3` ergibt das `4`, also `1-4`, `5-8`, `9-10`.
- Eine Schleife ueber die Abschnittsanfaenge; das Ende ist das Minimum aus
  "Anfang + Laenge - 1" und `bis` (`Math.min`), damit der letzte Abschnitt
  nicht ueber `bis` hinausgeht.
- Pro Abschnitt ein `Callable<Long>` mit `pool.submit(...)` abgeben, die
  `Future<Long>`s in einer Liste sammeln, danach mit `get()` aufaddieren.
- Den Pool in try-with-resources oeffnen — dann wird er am Ende sauber
  geschlossen.

Randfaelle und Fallen aus den Tests:

- `bis = 0` muss `0` liefern, `bis = 1` mit 4 Threads muss `1` liefern.
  Ein frueher Ausstieg fuer `bis <= 0` macht das eindeutig.
- `1..1.000.000` ergibt `500000500000` — das passt nicht in `int`. Rechne die
  Teilsumme in `long`.
- Die Schleifenvariable darfst du nicht direkt im Lambda benutzen (sie ist nicht
  effektiv final). Kopiere Anfang und Ende vorher in lokale Variablen.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
if (bis <= 0) return 0L;

try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
    List<Future<Long>> ergebnisse = new ArrayList<>();
    long proAbschnitt = ...;
    for (long von = 1; von <= bis; von += proAbschnitt) {
        long start = von;
        long ende = Math.min(..., bis);
        ergebnisse.add(pool.submit(() -> ...));   // Teilsumme start..ende als long
    }

    long gesamt = 0;
    for (Future<Long> f : ergebnisse) {
        gesamt += ...;
    }
    return gesamt;
}
```

Fuer die Teilsumme kannst du eine kleine private Hilfsmethode mit einer
`for`-Schleife schreiben oder `LongStream.rangeClosed(start, ende)` benutzen.

</details>

## Aufgabe 4: `laengenParallel`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 12.4, `invokeAll`. Frag dich: Wie kommst du zu den Ergebnissen in
**Eingabereihenfolge**, obwohl die Aufgaben in beliebiger Reihenfolge fertig
werden?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Baue eine `List<Callable<Integer>>`, fuer jeden Text ein Callable, das seine
Laenge liefert. `pool.invokeAll(aufgaben)` startet alle, wartet auf alle und
gibt die `Future`s **in derselben Reihenfolge wie die Eingabeliste** zurueck.
Die liest du der Reihe nach mit `get()` aus.

Randfaelle und Fallen:

- Leere Liste -> leere Liste. Berechnest du die Poolgroesse aus der Listenlaenge,
  wird sie hier `0`, und `Executors.newFixedThreadPool(0)` wirft eine
  `IllegalArgumentException`. Ein frueher Ausstieg fuer die leere Liste hilft.
- Leerer String -> `0`, das passt von selbst.
- Ergebnisse in **Fertigstellungs**-Reihenfolge einsammeln (z. B. jede Aufgabe
  schreibt selbst in eine gemeinsame Liste) ist falsch — und bei einer normalen
  `ArrayList` sogar eine Race Condition. Der Test mit 2000 Texten findet das.

</details>

<details><summary>Tipp 3 — Geruest</summary>

```java
if (texte.isEmpty()) return List.of();

try (ExecutorService pool = Executors.newFixedThreadPool(...)) {
    List<Callable<Integer>> aufgaben = new ArrayList<>();
    for (String t : texte) {
        aufgaben.add(() -> ...);
    }
    List<Future<Integer>> futures = pool.invokeAll(...);

    List<Integer> ergebnis = new ArrayList<>();
    for (Future<Integer> f : futures) {
        ergebnis.add(...);
    }
    return ergebnis;
}
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum ist `zaehler++` nicht atomar?</summary>

Es sind drei getrennte Schritte: Wert **lesen**, 1 **addieren**, Ergebnis
**zurueckschreiben**. Zwischen diesen Schritten kann ein anderer Thread
drankommen. Lesen zwei Threads beide `5`, schreiben beide `6` — eine Erhoehung
ist verloren, ohne Fehlermeldung. Das ist eine Race Condition; `javap -c`
zeigt dir die einzelnen Bytecode-Befehle (`getfield`, `iadd`, `putfield`).

</details>

<details><summary>Warum muss auch `wert()` synchronisiert sein?</summary>

`synchronized` sorgt nicht nur fuer gegenseitigen Ausschluss, sondern auch fuer
**Sichtbarkeit**: Wer eine Sperre betritt, sieht alles, was der letzte Inhaber
derselben Sperre vor dem Freigeben geschrieben hat (happens-before). Liest
`wert()` ohne Sperre, darf die JVM einen veralteten Wert aus einem Register
oder Cache liefern — womoeglich dauerhaft. Schutz nur beim Schreiben reicht
also nicht; Leser und Schreiber muessen **dieselbe** Sperre benutzen.

</details>

<details><summary>Wann `AtomicInteger`, wann `synchronized`?</summary>

`AtomicInteger` (bzw. `AtomicLong`, `AtomicBoolean`), wenn es um **eine
einzelne Variable** geht: Zaehler, Flags, IDs. Das ist schneller, braucht keine
Sperre und kann keinen Deadlock erzeugen. `synchronized`, wenn **mehrere Felder
gemeinsam** konsistent bleiben muessen oder ein Ablauf aus mehreren Schritten
unteilbar sein soll — z. B. "Guthaben pruefen, dann abbuchen" oder zwei Konten,
deren Summe gleich bleiben muss. Zwei einzeln atomare Variablen machen die
Kombination noch nicht atomar.

</details>

<details><summary>Was garantiert `invokeAll` bezueglich der Reihenfolge?</summary>

Die zurueckgegebene Liste von `Future`s hat **dieselbe Reihenfolge wie die
uebergebene Aufgabenliste**: Das `i`-te `Future` gehoert zur `i`-ten Aufgabe.
Ausserdem sind beim Zurueckkehren alle Aufgaben fertig (erfolgreich oder mit
Exception). Nicht garantiert ist, in welcher Reihenfolge die Aufgaben
**ausgefuehrt** werden oder fertig werden — das ist dem Pool ueberlassen.

</details>

<details><summary>Warum ist Unveraenderlichkeit die beste Nebenlaeufigkeitsstrategie?</summary>

Race Conditions entstehen nur, wenn mehrere Threads **gemeinsamen,
veraenderlichen** Zustand haben. Ein Objekt, das sich nach dem Konstruktor nie
mehr aendert, kann von beliebig vielen Threads gleichzeitig gelesen werden —
ohne Sperren, ohne `volatile`, ohne Deadlock-Gefahr. Fuer korrekt
konstruierte Objekte mit `final`-Feldern garantiert das Java-Speichermodell
sogar, dass andere Threads die Feldwerte vollstaendig sehen. Deshalb sind
Records (Kapitel 10), unveraenderliche Listen (`List.of`, `toList()`) und
"jede Aufgabe rechnet lokal, zusammengefuehrt wird am Ende" (wie in
`summeParallel`) der sicherste Weg.

</details>
