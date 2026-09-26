# Kapitel 12 — Nebenläufigkeit und Werkzeuge

**Ziel:** Du weisst, warum `zaehler++` aus zwei Threads falsch zählt, nutzt
`ExecutorService` statt roher Threads — und findest dich in einem echten
Java-Projekt mit Maven, Gradle und JUnit zurecht.

---

# Teil A: Nebenläufigkeit

## 12.1 Thread, Runnable, und warum du beides selten brauchst

```java
Thread t = new Thread(() -> System.out.println("laeuft nebenher"));
t.start();      // start(), NICHT run()! run() liefe im aktuellen Thread.
t.join();       // warten, bis er fertig ist
```

Rohe Threads sind teuer (jeder reserviert rund 1 MB Stack) und unbequem:
Ergebnisse zurückzubekommen ist mühsam, und eine Exception im Thread landet
nur als Stacktrace auf der Konsole — der startende Thread erfährt nichts davon.
In echtem Code nimmt man den `ExecutorService`.

`join()` (und später `Future.get()`) können eine checked
`InterruptedException` werfen: Jemand hat den wartenden Thread gebeten
aufzuhören. Wenn du sie fangen musst, verschluck sie nicht, sondern setze das
Signal wieder: `Thread.currentThread().interrupt();`.

## 12.2 Das Kernproblem: gemeinsamer veränderlicher Zustand

```java
private int zaehler = 0;

public void erhoehen() {
    zaehler++;      // sieht atomar aus, ist es aber nicht
}
```

`zaehler++` sind in Wahrheit drei Schritte:

```
1. lies zaehler          (Thread A liest 5)
2. addiere 1             (Thread B liest ebenfalls 5)
3. schreib zurueck       (beide schreiben 6 - eine Erhoehung ist verloren)
```

Das nennt man **Race Condition**. Sie tritt nicht immer auf, sondern
gelegentlich — und darum ist sie so schwer zu finden. Zwei Threads, die je
100.000-mal erhöhen, kommen statt auf 200.000 auf irgendetwas dazwischen.

Sieh es dir selbst an:

```bash
./lerne.sh 12 -r Demo
```

Zusätzlich gibt es das **Sichtbarkeitsproblem**: Ohne Synchronisierung darf
die JVM Werte in Registern oder CPU-Caches halten und Lesezugriffe aus
Schleifen herausziehen. Thread B sieht dann womöglich **nie**, was Thread A
geschrieben hat — auch nach Minuten nicht.

```java
private boolean stopp = false;            // ohne volatile: Schleife endet evtl. nie
private volatile boolean stopp = false;   // mit volatile: Aenderung wird sofort sichtbar
```

`volatile` garantiert nur **Sichtbarkeit**, keine Atomarität: Für ein
Flag, das ein Thread setzt und ein anderer liest, genügt es. `zaehler++`
auf einem `volatile int` bleibt trotzdem kaputt.

## 12.3 Drei Lösungen

### `synchronized`

```java
public synchronized void erhoehen() { zaehler++; }
public synchronized int wert()      { return zaehler; }
```

Nur ein Thread hält zur Zeit den Monitor des Objekts. Wichtig: Auch das
**Lesen** muss synchronisiert sein — sonst ist zwar das Schreiben korrekt, aber
die Sichtbarkeit nicht garantiert.

Feiner steuerbar mit einem eigenen Sperrobjekt:

```java
private final Object sperre = new Object();
synchronized (sperre) { zaehler++; }
```

### `AtomicInteger` und Freunde

```java
private final AtomicInteger zaehler = new AtomicInteger();

zaehler.incrementAndGet();
zaehler.addAndGet(5);
zaehler.get();
```

Nutzt CPU-Befehle (Compare-and-Swap) statt Sperren — schneller und ohne
Deadlock-Gefahr. **Für einzelne Zähler und Flags immer die erste Wahl.**

### Gar keinen gemeinsamen Zustand

Die mit Abstand beste Lösung. Unveränderliche Objekte (Kapitel 5), lokale
Variablen und Ergebnisse, die am Ende zusammengeführt werden, brauchen keine
Synchronisierung — es gibt nichts zu schützen.

Deshalb ist alles, was du in Kapitel 5, 9 und 10 über Unveränderlichkeit
gelernt hast, hier die eigentliche Pointe.

## 12.4 `ExecutorService`

```java
try (ExecutorService pool = Executors.newFixedThreadPool(4)) {   // seit Java 19 AutoCloseable
    Future<Long> f = pool.submit(() -> teilsumme(1, 1000));      // Callable<Long>
    long ergebnis = f.get();                                     // blockiert bis fertig
}   // close() wartet, bis alle Aufgaben fertig sind, und beendet den Pool
```

Ohne try-with-resources musst du selbst `pool.shutdown()` aufrufen — sonst
laufen die Pool-Threads weiter und dein Programm endet nie.

- `execute(Runnable)` — feuern und vergessen
- `submit(Callable<T>)` -> `Future<T>` — mit Ergebnis
- `invokeAll(liste)` -> `List<Future<T>>` — alle starten, auf alle warten;
  **die Reihenfolge der Ergebnisse entspricht der Eingabe**

`future.get()` blockiert und wirft `ExecutionException`, wenn die Aufgabe eine
Exception geworfen hat — das Original steckt in `getCause()`.

Fabriken: `newFixedThreadPool(n)`, `newCachedThreadPool()`,
`newSingleThreadExecutor()`, `newVirtualThreadPerTaskExecutor()` (Java 21).

**Faustregel für die Poolgröße:** CPU-lastige Arbeit ->
`Runtime.getRuntime().availableProcessors()`. Wartende Arbeit (Netzwerk,
Datenbank) -> deutlich mehr, oder virtuelle Threads.

## 12.5 Virtuelle Threads (Java 21)

```java
try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var auftrag : auftraege) {
        pool.submit(() -> bearbeite(auftrag));   // Millionen davon sind ok
    }
}
```

Virtuelle Threads werden von der JVM verwaltet, nicht vom Betriebssystem. Sie
kosten wenige hundert Byte statt einem Megabyte. Damit wird der einfache
Stil — ein Thread pro Anfrage, blockierender Code — wieder tragfähig, ohne
auf asynchrone Callback-Ketten auszuweichen.

**Aber:** Sie lösen keine Race Conditions. Alles aus 12.2 gilt unverändert.

## 12.6 Nebenläufige Collections

```java
Map<String, Integer> m = new ConcurrentHashMap<>();   // statt HashMap
List<String> l = new CopyOnWriteArrayList<>();        // viele Leser, wenige Schreiber
BlockingQueue<Auftrag> q = new LinkedBlockingQueue<>();  // Erzeuger/Verbraucher
```

Eine normale `HashMap` aus mehreren Threads zu beschreiben kann sie in einen
kaputten Zustand versetzen — in älteren Java-Versionen sogar in eine
Endlosschleife.

`Collections.synchronizedMap(...)` synchronisiert jede Methode einzeln — das
schützt **nicht** vor zusammengesetzten Operationen:

```java
if (!map.containsKey(k)) map.put(k, v);    // zwei Aufrufe = zwei Luecken
map.putIfAbsent(k, v);                     // atomar - so ist es richtig
```

## 12.7 Deadlock

```java
// Thread 1: synchronized (a) { synchronized (b) { ... } }
// Thread 2: synchronized (b) { synchronized (a) { ... } }
```

Beide warten ewig aufeinander. Gegenmittel: Sperren **immer in derselben
Reihenfolge** nehmen, möglichst wenige gleichzeitig halten, und wo möglich
`tryLock` mit Zeitlimit verwenden (das kann `ReentrantLock` aus
`java.util.concurrent.locks`, `synchronized` nicht).

## 12.8 Regeln, die dich vor den meisten Fehlern bewahren

1. Teile so wenig veränderlichen Zustand wie möglich.
2. Bevorzuge unveränderliche Objekte.
3. Nimm `ExecutorService`, nicht `new Thread`.
4. Für Zähler: `Atomic*`. Für einfache Flags: `volatile` oder `AtomicBoolean`.
5. Für Maps: `ConcurrentHashMap` mit atomaren Operationen.
6. Synchronisiere **Lesen und Schreiben**, nicht nur Schreiben.
7. Miss nach. Nebenläufigkeit ist oft langsamer als eine gute Schleife.

---

# Teil B: Werkzeuge für echte Projekte

## 12.9 Projektstruktur

```
mein-projekt/
├── pom.xml                       (Maven)  oder build.gradle.kts (Gradle)
└── src/
    ├── main/java/de/firma/app/   Produktivcode
    ├── main/resources/           Konfiguration, Textdateien
    ├── test/java/de/firma/app/   Tests
    └── test/resources/
```

Diese Struktur ist Konvention — jedes Werkzeug und jede IDE erwartet sie.

### Pakete

```java
package de.firma.app.domain;      // MUSS die erste Zeile sein

import java.util.List;
import de.firma.app.util.Helfer;
```

Der Paketname spiegelt den Ordnerpfad. Konvention: umgekehrte Domain.
Pakete sind Javas eigentliche Modularisierung — package-private (kein
Modifier) ist die Sichtbarkeit "nur innerhalb dieses Pakets".

## 12.10 Maven

`pom.xml` — Beispiel in [`werkzeuge/pom.xml`](werkzeuge/pom.xml).

```bash
mvn compile           # uebersetzen
mvn test              # Tests laufen lassen
mvn package           # JAR bauen -> target/
mvn clean install     # aufraeumen, bauen, ins lokale Repository legen
```

Maven ist deklarativ: Du beschreibst *was*, nicht *wie*. Abhängigkeiten
kommen aus Maven Central und landen in `~/.m2/repository`.

## 12.11 Gradle

`build.gradle.kts` — Beispiel in [`werkzeuge/build.gradle.kts`](werkzeuge/build.gradle.kts).

```bash
./gradlew build
./gradlew test
./gradlew run
```

Gradle ist skriptbar (Kotlin oder Groovy), flexibler und bei grossen Projekten
schneller (inkrementelle Builds, Build-Cache). Dafür schwerer zu durchschauen.

**Womit anfangen?** Maven. Es ist langweiliger, und das ist bei Build-Werkzeugen
eine Tugend.

## 12.12 JUnit 5

Beispiel in [`werkzeuge/BeispielTest.java`](werkzeuge/BeispielTest.java).

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class KontoTest {

    @Test
    void einzahlenErhoehtDasGuthaben() {
        Konto konto = new Konto("Anna", 1000);
        konto.einzahlen(500);
        assertEquals(1500, konto.getGuthaben());
    }

    @Test
    void negativeEinzahlungWirdAbgelehnt() {
        Konto konto = new Konto("Anna", 1000);
        assertThrows(IllegalArgumentException.class, () -> konto.einzahlen(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void ungueltigeBetraegeWerdenAbgelehnt(int betrag) {
        Konto konto = new Konto("Anna", 1000);
        assertThrows(IllegalArgumentException.class, () -> konto.einzahlen(betrag));
    }
}
```

Das `Pruef`-Framework dieses Kurses ist eine Miniaturausgabe davon —
`Pruef.gleich` ist `assertEquals`, `Pruef.wirft` ist `assertThrows`.

Testnamen als **ganze Sätze**: Ein Testname soll beschreiben, was gelten
soll, nicht welche Methode aufgerufen wird.

Weitere Bausteine: `@BeforeEach`, `@AfterEach`, `@DisplayName`, `@Disabled`,
`@Nested`. Zum Nachschlagen: <https://junit.org/junit5/docs/current/user-guide/>

## 12.13 Weiteres Werkzeug

```bash
javadoc -d doku src/*.java           # HTML-Dokumentation erzeugen
jar cf app.jar -C build .            # Archiv packen
javap -c -p Klasse                   # Bytecode ansehen (lehrreich!)
jshell                               # interaktive Java-Konsole zum Ausprobieren
```

**jshell** lohnt sich sofort: Java-Ausdrücke direkt eintippen, ohne Klasse
und ohne `main`. Ideal, um eine API-Frage in 20 Sekunden zu klären statt in
einer Wegwerf-Datei.

**Debugger statt `System.out.println`:** Breakpoint setzen, Programm anhalten,
Variablen ansehen, Schritt für Schritt weitergehen. Jede IDE kann das; es ist
die größte einzelne Produktivitätssteigerung beim Fehlersuchen.

## 12.14 Wie es weitergeht

Wenn dieses Kapitel sitzt, hast du die Sprache. Danach kommt das Ökosystem:

| Thema | Warum |
|-------|-------|
| **JUnit + Mockito** vertiefen | Tests sind die Grundlage von allem Weiteren |
| **Spring Boot** | der De-facto-Standard für Java-Backends |
| **JDBC / JPA / Hibernate** | Datenbanken |
| **`java.time`** | Datum und Zeit richtig (nie `Date` oder `Calendar`) |
| **Logging** (SLF4J + Logback) | statt `System.out.println` |
| **Jackson** | JSON |
| **Effective Java** (Joshua Bloch) | das Buch, das aus Java-Kennern Java-Könnern macht |

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Zwei Dateien in [`src/`](src/) — prüfen mit `./lerne.sh 12`.
`Demo.java` ist fertig und zeigt die Race Condition live:
`./lerne.sh 12 -r Demo`.

### `Zaehler.java`

Ein thread-sicherer Zähler mit `synchronized`. `erhoehen()`, `wert()`,
`erhoeheUm(int)`. Denk daran: **auch das Lesen** muss synchronisiert sein.

Ehrlicher Hinweis: Die Tests prüfen `erhoehen()` und `erhoeheUm()` mit vielen
gleichzeitigen Threads — fehlt dort `synchronized`, gehen Erhöhungen verloren
und der Test wird rot. Ob `wert()` synchronisiert ist, kann dagegen **kein
Test zuverlässig feststellen**: Die Tests lesen erst nach `join()`, und
`join()` sorgt schon selbst für Sichtbarkeit. Ein Sichtbarkeitsfehler tritt
nur unter bestimmten Bedingungen auf, und dann meist erst in Produktion.
Hier musst du dich auf das Verständnis verlassen, nicht auf grüne Haken.
Dasselbe gilt dafür, ob deine Threads wirklich *parallel* laufen und ob du
den `ExecutorService` schliesst — beides sieht man dem Ergebnis nicht an.

### `Aufgaben.java`

1. **`zaehleMitZaehler(int threads, int proThread)`** -> `int`.
   Starte die Threads, lass jeden `proThread`-mal erhöhen, warte auf alle,
   gib den Endstand zurück. Muss **exakt** `threads * proThread` sein.
2. **`zaehleMitAtomic(int threads, int proThread)`** — dasselbe mit
   `AtomicInteger` statt `synchronized`.
3. **`summeParallel(long bis, int threads)`** -> `long`.
   Summe von 1 bis `bis` (einschliesslich), aufgeteilt auf höchstens
   `threads` Teilaufgaben über einen `ExecutorService`. Ergebnis muss
   `bis * (bis + 1) / 2` sein. Tipp: Abschnittslänge aufrunden,
   `(bis + threads - 1) / threads`, sonst bleibt ein Rest-Abschnitt übrig.
4. **`laengenParallel(List<String>)`** -> `List<Integer>`.
   Jede Länge in einer eigenen Aufgabe berechnen, **Reihenfolge erhalten**.
   Tipp: `invokeAll` garantiert genau das.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
Thread t = new Thread(() -> System.out.print(
        Thread.currentThread().getName().equals("main") ? "main " : "neu "));
t.run();
t.start();
t.join();
```

<details><summary>Auflösung</summary>

`main neu` — `run()` ist ein ganz normaler Methodenaufruf im **aktuellen** Thread. Erst `start()` erzeugt wirklich einen neuen. Der klassische Fehler aus 12.1.

</details>

**2.**

```java
AtomicInteger z = new AtomicInteger(5);
System.out.println(z.incrementAndGet() + " " + z.getAndIncrement() + " " + z.get());
```

<details><summary>Auflösung</summary>

`6 6 7` — `incrementAndGet` erhöht und liefert den neuen Wert (wie `++z`), `getAndIncrement` liefert den alten und erhöht danach (wie `z++`).

</details>

**3.**

```java
try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
    Future<Integer> f = pool.submit(() -> 1 / 0);
    try {
        f.get();
    } catch (ExecutionException e) {
        System.out.println(e.getCause().getClass().getSimpleName());
    }
}
```

<details><summary>Auflösung</summary>

`ArithmeticException` — Die Exception entsteht im Pool-Thread und wird dort aufbewahrt. `get()` wirft sie verpackt als `ExecutionException` wieder, das Original steckt in `getCause()`. Ohne `get()` hättest du von dem Fehler nie etwas erfahren.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum ist `zaehler++` nicht atomar?
- Warum muss auch `wert()` synchronisiert sein?
- Wann `AtomicInteger`, wann `synchronized`?
- Was garantiert `invokeAll` bezüglich der Reihenfolge?
- Warum ist Unveränderlichkeit die beste Nebenläufigkeitsstrategie?
