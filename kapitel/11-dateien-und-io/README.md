# Kapitel 11 — Dateien und IO

**Ziel:** Du liest und schreibst Dateien mit der modernen `java.nio.file`-API,
gehst korrekt mit Zeichensätzen um und verarbeitest Zeilen als Stream.

---

## 11.1 `Path` statt `File`

```java
import java.nio.file.Path;
import java.nio.file.Files;

Path p = Path.of("daten", "messwerte.csv");     // relativ zum Arbeitsverzeichnis
Path abs = Path.of("/home/mord/daten.csv");     // absolut
Path kind = p.resolve("unterordner");           // anhaengen
Path eltern = p.getParent();
String name = p.getFileName().toString();
```

Die alte Klasse `java.io.File` gibt es seit 1996 und sie ist voller Fallen:
`delete()` gibt `false` zurück, ohne zu sagen warum; `mkdirs()` ebenso.
`java.nio.file` (seit Java 7) wirft stattdessen aussagekräftige Exceptions.

**Nimm `Path` und `Files`. `File` nur, wenn eine alte Bibliothek es verlangt.**

`Path.of("daten/x.csv")` funktioniert plattformübergreifend — Java setzt den
richtigen Trenner ein. Schreibe nie `"ordner\\datei"` von Hand.

## 11.2 Die einfachen Fälle

```java
// Ganze Datei lesen (nur bei kleinen Dateien!)
String inhalt = Files.readString(pfad);                    // seit Java 11
List<String> zeilen = Files.readAllLines(pfad);

// Schreiben (legt an oder ueberschreibt)
Files.writeString(pfad, "Hallo");
Files.write(pfad, List.of("Zeile 1", "Zeile 2"));

// Anhaengen
Files.writeString(pfad, "mehr\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);

// Pruefen und verwalten
Files.exists(pfad);
Files.size(pfad);
Files.createDirectories(pfad.getParent());
Files.deleteIfExists(pfad);
Files.copy(quelle, ziel, StandardCopyOption.REPLACE_EXISTING);
```

Alle diese Methoden werfen `IOException` — eine **checked** Exception
(Kapitel 7). Deine Methode braucht also `throws IOException` oder ein `catch`.

## 11.3 Zeichensätze — die stille Fehlerquelle

```java
Files.readString(pfad);                           // UTF-8 - bei Files.* schon immer
Files.readString(pfad, StandardCharsets.UTF_8);   // explizit - noch besser
new String(bytes);                                // Plattform-Standard - bis Java 17 GEFAEHRLICH
new FileReader(datei);                            // ebenso
```

Ein Zeichensatz übersetzt zwischen Bytes auf der Platte und `char`s im
Speicher. Die `Files`-Methoden ohne Zeichensatz-Argument nehmen seit jeher
UTF-8. Die *älteren* APIs (`FileReader`, `new String(bytes)`, `Scanner` auf
einer Datei …) nahmen dagegen bis Java 17 den Plattform-Standard — dieselbe
Datei las sich unter Linux korrekt und unter Windows als `Ã¤` statt `ä`.

Seit Java 18 ist UTF-8 auch dort überall Standard. Trotzdem: **Schreib den
Zeichensatz hin.** Es kostet nichts, dokumentiert die Absicht und schützt
dich, falls der Code doch einmal auf einer älteren JVM läuft.

Wirft eine Datei `MalformedInputException`, ist sie nicht UTF-8 kodiert —
häufig `ISO-8859-1` (Latin-1) aus alten Windows-Systemen.

## 11.4 Grosse Dateien: `Files.lines`

`readAllLines` lädt alles in den Speicher. Bei einer 2-GB-Logdatei ist das
ein `OutOfMemoryError`. Die faule Variante:

```java
try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) {
    long treffer = zeilen.filter(z -> z.contains("ERROR")).count();
}
```

> **Wichtig:** `Files.lines` hält die Datei offen. Dieser Stream **muss** in
> try-with-resources stehen — anders als sonstige Streams, die man einfach
> stehen lässt. Vergisst du es, läuft dir irgendwann das
> Dateideskriptor-Limit voll ("too many open files").

Dasselbe gilt für `Files.list(verzeichnis)` und `Files.walk(verzeichnis)`.

## 11.5 Der klassische Weg mit Readern

```java
try (BufferedReader r = Files.newBufferedReader(pfad, StandardCharsets.UTF_8)) {
    String zeile;
    while ((zeile = r.readLine()) != null) {      // null = Dateiende
        verarbeite(zeile);
    }
}

try (BufferedWriter w = Files.newBufferedWriter(pfad, StandardCharsets.UTF_8)) {
    w.write("Zeile");
    w.newLine();
}
```

Warum "Buffered"? Ohne Puffer geht jeder einzelne `read()` ans Betriebssystem.
Der Puffer holt einen ganzen Block auf einmal — das ist Größenordnungen
schneller.

Diese Form brauchst du, wenn du Zustand über Zeilen hinweg mitführst
(z. B. mehrzeilige Datensätze) — dafür sind Streams unhandlich.

## 11.6 CSV von Hand

```java
for (String zeile : zeilen) {
    if (zeile.isBlank()) continue;
    String[] felder = zeile.split(";", -1);      // -1: leere Endfelder behalten!
    ...
}
```

Das `-1` als zweites Argument von `split` ist wichtig: Ohne es wirft Java
leere Felder am Ende weg. `"a;b;;"` ergibt normal `["a","b"]`, mit `-1`
korrekt `["a","b","",""]`.

Für echtes CSV (Anführungszeichen, eingebettete Trenner, Zeilenumbrüche in
Feldern) nimm eine Bibliothek — Apache Commons CSV oder OpenCSV. Selbstgebaute
CSV-Parser sind ein klassischer Fehler.

## 11.7 Eingabe von der Tastatur

```java
Scanner scanner = new Scanner(System.in);
System.out.print("Name: ");
String name = scanner.nextLine();
System.out.print("Alter: ");
int alter = scanner.nextInt();
```

> **Klassische Falle:** `nextInt()` liest die Zahl, lässt aber den
> Zeilenumbruch stehen. Das nächste `nextLine()` liefert dann sofort einen
> leeren String. Lösung: nach `nextInt()` ein zusätzliches `nextLine()` —
> oder konsequent nur `nextLine()` verwenden und selbst parsen.

`System.in` soll man **nicht** schliessen — danach ist die Eingabe für das
ganze Programm zu.

## 11.8 Wo liegt eigentlich "daten.csv"?

Relative Pfade beziehen sich auf das **Arbeitsverzeichnis** des Prozesses,
nicht auf den Ort der `.class`-Datei:

```java
System.out.println(Path.of("").toAbsolutePath());   // wo bin ich?
```

Für Tests und temporäre Daten:

```java
Path tempOrdner = Files.createTempDirectory("test");
Path tempDatei = Files.createTempFile("daten", ".csv");
```

---

## Aufgaben

> Hängst du fest? Gestufte Hinweise zu jeder Aufgabe stehen in
> [`TIPPS.md`](TIPPS.md) — erst Tipp 1, dann wieder selbst probieren.

Zwei Dateien in [`src/`](src/) — prüfen mit `./lerne.sh 11`.
`Messwert.java` ist fertig vorgegeben (ein `record` aus Kapitel 10).

Die Tests legen sich ihre Dateien selbst in einem temporären Ordner an — es
wird nichts in deinem Projekt angefasst.

1. **`zeilenZaehlen(Path)`** -> `long`. Nutze `Files.lines` **in
   try-with-resources**.
2. **`schreibeZeilen(Path, List<String>)`** — jede Zeile mit `\n`, UTF-8.
   Bestehende Dateien werden überschrieben.
3. **`nichtLeereZeilen(Path)`** -> `List<String>`, ohne leere und
   Whitespace-Zeilen, jede Zeile `strip()`-behandelt.
4. **`sicherLesen(Path)`** -> `Optional<String>`. Bei `IOException`
   (z. B. Datei fehlt) `Optional.empty()` statt einer Exception.
   Das ist die Brücke von Kapitel 7 zu Kapitel 9.
5. **`csvLesen(Path)`** -> `List<Messwert>`. Format `sensor;wert`,
   Trennzeichen `;`. Erste Zeile ist eine Kopfzeile und wird übersprungen,
   leere Zeilen ebenfalls; eine leere Datei ergibt eine leere Liste. Bei
   kaputten Zeilen (falsche Feldzahl — auch `temp;21.5;` hat drei Felder —
   oder unparsbare Zahl) eine `IllegalArgumentException` mit der
   **Zeilennummer** in der Nachricht: `"Ungueltige Zeile 3: kaputt"`.
   Gemeint ist die Zeilennummer in der Datei (1-basiert, Kopf- und Leerzeilen
   zählen mit).
6. **`durchschnittProSensor(List<Messwert>)`** -> `Map<String, Double>`.
   Kapitel 9 lässt grüßen: `groupingBy` mit `averagingDouble`.
7. **`schreibeBericht(Path, Map<String,Double>)`** — je Zeile
   `sensor=12.50`, alphabetisch nach Sensorname, `Locale.ROOT`.

## Was gibt das aus?

Erst überlegen, am besten mit Stift und Papier, dann aufklappen. Danach
kannst du es in `jshell` nachprüfen. Code lesen und vorhersagen trainiert
genau das Verständnis, das du zum Schreiben brauchst.

**1.**

```java
System.out.println("a;b;;".split(";").length);
System.out.println("a;b;;".split(";", -1).length);
```

<details><summary>Auflösung</summary>

`2`, dann `4` — Ohne das Limit wirft `split` leere Felder **am Ende** weg. Mit `-1` bleiben sie erhalten: `["a", "b", "", ""]`.

</details>

**2.**

```java
System.out.println(Path.of("daten", "x.csv").getFileName());
System.out.println(Path.of("/home/anna/daten/x.csv").getParent());
```

<details><summary>Auflösung</summary>

`x.csv`, dann `/home/anna/daten` — `Path` zerlegt den Pfad in seine Teile, ohne dass die Datei existieren muss. Es ist reine Textarbeit, bis du `Files.*` aufrufst.

</details>

**3.**

```java
try {
    Files.readString(Path.of("gibtsnicht.txt"));
} catch (IOException e) {
    System.out.println(e.getClass().getSimpleName());
}
```

<details><summary>Auflösung</summary>

`NoSuchFileException` — Nicht `FileNotFoundException`! Die `java.nio.file`-API hat eigene, genauere Untertypen von `IOException`. Deshalb fängt man meist `IOException` und nicht einen bestimmten Untertyp.

</details>

## Selbstcheck

Erst selbst antworten, dann vergleichen: Die Antworten stehen am Ende von
[`TIPPS.md`](TIPPS.md).

- Warum `Path`/`Files` statt `File`?
- Warum **muss** `Files.lines` in try-with-resources stehen?
- Was passiert ohne expliziten Zeichensatz — und warum ist das heute weniger schlimm als früher?
- Wozu das `-1` bei `split(";", -1)`?
- Worauf bezieht sich ein relativer Pfad?
