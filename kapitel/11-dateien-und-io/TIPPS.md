# Kapitel 11 — Tipps und Antworten

> Erst selbst probieren. Klappe immer nur die **nächste** Stufe auf — jede verrät mehr.
> Die Tests in `tests/Tests.java` zeigen dir ausserdem genau, welche Eingabe welches Ergebnis erwartet.

## Aufgabe 1: `zeilenZaehlen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 11.4 (`Files.lines`), Kapitel 7.7 (try-with-resources) und 9.5
(Terminaloperationen). Du hast
einen Stream von Zeilen — welche Terminaloperation zählt? Und frag dich: Wer
schliesst die Datei, wenn du fertig bist?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`Files.lines(pfad, StandardCharsets.UTF_8)` im Kopf eines
try-with-resources-Blocks öffnen, im Block `count()` aufrufen und das Ergebnis
direkt zurückgeben. Ein `return` innerhalb von `try (...)` ist völlig in
Ordnung — der Stream wird trotzdem geschlossen.

Die Randfälle (leere Datei -> `0`, letzte Zeile ohne `\n` -> zählt trotzdem)
erledigt `Files.lines` von selbst. Nicht per Hand die `\n` zählen, dann
stimmt "eine Zeile ohne Umbruch am Ende" nicht.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) {
    return ...;
}
```

</details>

## Aufgabe 2: `schreibeZeilen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 11.2 (Schreiben) und 11.3 (Zeichensätze). Frag dich: Welcher
Zeilentrenner soll in der Datei stehen — und entscheidest du das selbst oder
das Betriebssystem?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Bau den kompletten Inhalt als einen `String` (z. B. mit einem `StringBuilder`),
wobei **jede** Zeile ein `'\n'` bekommt, und schreib ihn mit
`Files.writeString(pfad, inhalt, StandardCharsets.UTF_8)`. Ohne weitere Optionen
legt `writeString` die Datei an oder überschreibt sie — genau das verlangt der
Test "überschreibt".

Denkfallen:

- `Files.write(pfad, zeilen, ...)` hängt den **Plattform**-Zeilentrenner an —
  unter Linux `\n`, unter Windows `\r\n`. Dann sind die Tests nur auf manchen
  Systemen grün.
- `String.join("\n", zeilen) + "\n"` klingt praktisch, liefert für die leere
  Liste aber `"\n"` statt `""`. Der Test "leere Liste" fällt dann durch.
- Den Zeichensatz explizit angeben — der Test schreibt Umlaute hin und zurück.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
StringBuilder sb = new StringBuilder();
for (String z : zeilen) {
    sb.append(...).append(...);
}
Files.writeString(pfad, ..., StandardCharsets.UTF_8);
```

</details>

## Aufgabe 3: `nichtLeereZeilen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 11.4 (`Files.lines`) plus Kapitel 9.5 (`map`, `filter`). Frag dich:
Ist `"   "` eine leere Zeile? Und in welcher Reihenfolge musst du strippen und
filtern, damit sie verschwindet?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Wie in Aufgabe 1 den Stream in try-with-resources öffnen, dann
`map(String::strip)`, dann alles Leere herausfiltern, dann `toList()`.

Denkfalle Reihenfolge: Filterst du *vor* dem Strippen mit `isEmpty()`, bleibt
`"   "` stehen (es ist nicht leer) und wird danach zu `""` — im Ergebnis steht
dann ein leerer String. Entweder erst strippen und dann auf leer prüfen, oder
mit `isBlank()` filtern und danach strippen.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
try (Stream<String> zeilen = Files.lines(pfad, StandardCharsets.UTF_8)) {
    return zeilen.map(...)
            .filter(z -> ...)
            .toList();
}
```

</details>

## Aufgabe 4: `sicherLesen`

<details><summary>Tipp 1 — Richtung</summary>

Kapitel 7.3 (`try`/`catch`) trifft Kapitel 9.6 (`Optional`). Die Methode hat
**kein** `throws IOException` — die Exception muss also hier drin gefangen
werden. Frag dich: Was gibst du im Erfolgsfall zurück, was im Fehlerfall?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Im `try` die Datei mit `Files.readString(pfad, StandardCharsets.UTF_8)` lesen
und das Ergebnis in ein `Optional` verpacken; im `catch (IOException e)`
`Optional.empty()` zurückgeben. Eine fehlende Datei meldet Java als
`NoSuchFileException` — das ist eine Unterklasse von `IOException`, wird also
mitgefangen.

Fang nicht `Exception` ab: Das würde auch echte Programmierfehler
(`NullPointerException` usw.) still verschlucken.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
try {
    return Optional.of(...);
} catch (IOException e) {
    return ...;
}
```

</details>

## Aufgabe 5: `csvLesen`

<details><summary>Tipp 1 — Richtung</summary>

Abschnitt 11.6 (CSV von Hand), 11.2 (`readAllLines`) und Kapitel 7.6
(Ursachen verketten). Frag dich zuerst: Woher weisst du in der Schleife die
**Zeilennummer**? Und welche zwei Dinge können bei einer Zeile schiefgehen?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

- `Files.readAllLines(pfad, StandardCharsets.UTF_8)` liefert eine `List<String>`.
  Durchlaufe sie mit einer **Index-Schleife**, die bei `1` beginnt — so ist die
  Kopfzeile (Index 0) übersprungen. Die Zeilennummer in der Datei ist dann
  `i + 1`.
- Leere Zeilen mit `isBlank()` überspringen (`continue`). Sie zählen trotzdem
  mit, weil der Index weiterläuft — das prüft der Test "Zeile 4".
- `split(";", -1)`: Nur mit `-1` hat `"temp;21.5;"` drei Felder. Ohne es fallen
  leere Endfelder weg, die Zeile sähe gültig aus.
- Prüfe `felder.length != 2`, dann `Double.parseDouble(...)`. Dessen
  `NumberFormatException` fängst du und wirfst eine eigene
  `IllegalArgumentException` mit der richtigen Nachricht (gern mit `e` als
  Ursache).

Randfälle: Eine leere Datei ergibt eine leere Liste — die Schleife ab `1` läuft
dann gar nicht. Und: `NumberFormatException` *ist* schon eine
`IllegalArgumentException`. Lässt du sie einfach durch, stimmt der Typ, aber
die Nachricht nicht, und der Test wird rot. Die Nachricht enthält die
**Originalzeile**: `"Ungueltige Zeile 2: temp;21.5;"`.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
List<String> zeilen = Files.readAllLines(pfad, StandardCharsets.UTF_8);
List<Messwert> ergebnis = new ArrayList<>();
for (int i = 1; i < zeilen.size(); i++) {
    String zeile = zeilen.get(i);
    if (...) continue;                               // Leerzeile

    String[] felder = zeile.split(";", -1);
    if (...) {
        throw new IllegalArgumentException("Ungueltige Zeile " + ... + ": " + ...);
    }
    try {
        double wert = ...;
        ergebnis.add(new Messwert(..., wert));
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException(..., e);
    }
}
return ergebnis;
```

</details>

## Aufgabe 6: `durchschnittProSensor`

<details><summary>Tipp 1 — Richtung</summary>

Kapitel 9, Abschnitt 9.5, Collectors: `groupingBy` mit Downstream-Collector.
Frag dich: Wonach wird gruppiert — und was soll pro Gruppe herauskommen statt
einer Liste?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

Klassifizierer ist die Record-Zugriffsmethode `Messwert::sensor` (ohne `get`,
Kapitel 10.2). Als Downstream nimmst du `Collectors.averagingDouble(...)`, das
aus jeder Gruppe einen `Double`-Durchschnitt macht. Leere Liste ergibt eine
leere Map, ohne Sonderfall.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
return werte.stream()
        .collect(Collectors.groupingBy(
                ...,
                Collectors.averagingDouble(...)));
```

</details>

## Aufgabe 7: `schreibeBericht`

<details><summary>Tipp 1 — Richtung</summary>

Kapitel 8.5 (`TreeMap`), Abschnitt 11.2 (Schreiben) und Kapitel 6
(`Locale.ROOT` beim Formatieren). Frag dich: Wie bekommst du die Einträge
**alphabetisch**, wenn die übergebene Map (`Map.of(...)`) keine Reihenfolge
garantiert?

</details>

<details><summary>Tipp 2 — Ansatz</summary>

`new TreeMap<>(durchschnitte)` sortiert nach Schlüssel. Über deren
`entrySet()` laufen, jede Zeile mit
`String.format(Locale.ROOT, "%s=%.2f", ...)` bauen und ein `'\n'` anhängen,
am Ende alles mit `Files.writeString(..., StandardCharsets.UTF_8)` schreiben.

Denkfallen:

- `%.2f` rundet: `1013.456` wird `1013.46` — genau das will der Test.
- `%n` statt `'\n'` setzt den Plattform-Zeilentrenner ein (Windows: `\r\n`).
- Leere Map: Die Datei muss trotzdem geschrieben werden (leer). Der Test ruft
  die Methode zweimal auf dieselbe Datei auf — schreibst du bei leerer Map
  nichts, steht dort noch der alte Bericht.

</details>

<details><summary>Tipp 3 — Gerüst</summary>

```java
Map<String, Double> sortiert = new TreeMap<>(...);
StringBuilder sb = new StringBuilder();
for (Map.Entry<String, Double> e : sortiert.entrySet()) {
    sb.append(String.format(Locale.ROOT, "%s=%.2f", ..., ...)).append(...);
}
Files.writeString(pfad, ..., StandardCharsets.UTF_8);
```

</details>

---

## Selbstcheck — Antworten

<details><summary>Warum `Path`/`Files` statt `File`?</summary>

`java.io.File` meldet Fehler oft nur mit `false` (`delete()`, `mkdirs()`) und
sagt nicht, **warum** etwas schiefging. `Files` wirft stattdessen
aussagekräftige Exceptions wie `NoSuchFileException` oder
`AccessDeniedException`. Dazu kommen bequeme Methoden (`readString`,
`writeString`, `lines`), UTF-8 als Standard und plattformunabhängige Pfade
über `Path.of(...)`. `File` brauchst du nur noch, wenn eine alte Bibliothek es
verlangt — `pfad.toFile()` und `file.toPath()` wandeln um.

</details>

<details><summary>Warum **muss** `Files.lines` in try-with-resources stehen?</summary>

`Files.lines` liest **faul**: Die Datei bleibt geöffnet, solange der Stream
lebt, damit er weitere Zeilen nachladen kann. Ein normaler Stream über eine
`List` hält keine Ressource, `Files.lines` dagegen einen Dateideskriptor des
Betriebssystems. Streams sind `AutoCloseable`, und try-with-resources ruft
`close()` sicher auf — auch bei einer Exception. Ohne das sammeln sich offene
Dateien an, bis "too many open files" kommt (und unter Windows lässt sich die
Datei so lange nicht löschen).

</details>

<details><summary>Was passiert ohne expliziten Zeichensatz — und warum ist das heute weniger schlimm als früher?</summary>

Die `Files`-Methoden nehmen ohne Angabe **immer UTF-8**. Ältere APIs wie
`FileReader`, `new String(bytes)` oder `Scanner` auf einer Datei nahmen bis
Java 17 den **Plattform-Standard** — unter Windows oft `windows-1252` statt
UTF-8. Dieselbe Datei las sich dann je nach Rechner richtig oder mit kaputten
Umlauten. Seit Java 18 ist UTF-8 auch dort der Standard, deshalb ist das Risiko
kleiner. Den Zeichensatz hinzuschreiben bleibt trotzdem gut: Es dokumentiert
die Absicht und schützt auf älteren JVMs.

</details>

<details><summary>Wozu das `-1` bei `split(";", -1)`?</summary>

Das zweite Argument ist das `limit`. Ohne Angabe (bzw. mit `0`) entfernt
`split` **leere Felder am Ende**, ein negativer Wert behält sie. Das ist für
CSV wichtig, weil ein leeres letztes Feld sonst einfach verschwindet und eine
kaputte Zeile gültig aussieht:

```java
"a;b;;".split(";")        // [a, b]
"a;b;;".split(";", -1)    // [a, b, , ]
"temp;21.5;".split(";", -1).length   // 3
```

</details>

<details><summary>Worauf bezieht sich ein relativer Pfad?</summary>

Auf das **Arbeitsverzeichnis** des Java-Prozesses — also das Verzeichnis, in
dem das Programm gestartet wurde. Nicht auf den Ort der `.java`- oder
`.class`-Datei. Startest du dasselbe Programm aus einem anderen Ordner, findet
`Path.of("daten.csv")` eine andere (oder gar keine) Datei. Wo du gerade bist,
zeigt `Path.of("").toAbsolutePath()`.

</details>
