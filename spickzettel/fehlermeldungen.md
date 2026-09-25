# Die haeufigsten Fehlermeldungen — und was sie wirklich bedeuten

## Compilerfehler (das Programm startet gar nicht)

### `cannot find symbol`
```
symbol:   variable breit
location: class Aufgaben
```
Der Compiler kennt diesen Namen nicht. Ursachen: Tippfehler,
Gross-/Kleinschreibung, Variable ausserhalb ihres Blocks deklariert,
fehlender `import`, Methode existiert nicht (oder heisst anders).

### `incompatible types: String cannot be converted to int`
Zuweisung zwischen unpassenden Typen. Bei Zahlen hilft oft ein Cast, bei
Text `Integer.parseInt(...)`.

### `possible lossy conversion from double to int`
Einengende Umwandlung. Wenn du sie wirklich willst: `(int) wert`.
Zum Runden statt Abschneiden: `Math.round(...)`.

### `missing return statement`
Nicht jeder Pfad durch die Methode endet mit `return`. Meist fehlt der
`else`-Zweig oder ein `return` nach einer Schleife.

### `variable x might not have been initialized`
Lokale Variablen haben — anders als Felder — keinen Standardwert.

### `unreported exception IOException; must be caught or declared to be thrown`
Eine checked Exception (Kapitel 7). Entweder `try`/`catch` oder
`throws IOException` an die Methode.

### `non-static variable x cannot be referenced from a static context`
Du greifst aus einer `static`-Methode (oft `main`) auf ein Instanzfeld zu.
Entweder ein Objekt erzeugen oder das Feld `static` machen.

### `constructor Konto in class Konto cannot be applied to given types`
Die Argumente passen zu keinem Konstruktor. Haeufig nach dem Hinzufuegen
eines eigenen Konstruktors: Der parameterlose Standardkonstruktor ist damit
verschwunden.

### `class X is public, should be declared in a file named X.java`
Dateiname und Name der `public`-Klasse muessen uebereinstimmen.

### `method does not override or implement a method from a supertype`
`@Override` steht ueber einer Methode, die nichts ueberschreibt. Fast immer
ein Tippfehler oder eine falsche Parameterliste — genau dafuer ist die
Annotation da.

### `unreachable statement`
Code nach `return`, `break` oder einem allgemeineren `catch`. Bei `catch`:
spezifische Typen zuerst.

### `local variables referenced from a lambda expression must be final or effectively final`
Das Lambda benutzt eine lokale Variable, die sich spaeter noch aendert.
Kopiere sie vorher in eine neue Variable.

### `';' expected` / `class, interface, enum, or record expected`
Ein Semikolon fehlt — der Compiler meldet das oft erst in der **naechsten**
Zeile. Bei `class ... expected` ist meist eine `}` zu viel (oder zu wenig)
und Code steht ausserhalb der Klasse. Die Einrueckung deines Editors verraet,
wo die Klammern nicht mehr stimmen.

### `the switch expression does not cover all possible input values`
Ein `switch`-Ausdruck ueber ein Enum oder einen `sealed`-Typ vergisst einen
Fall. Fall ergaenzen — genau diese Meldung ist der Sinn von `sealed`.

## Laufzeitfehler (das Programm laeuft und bricht ab)

### `NullPointerException: Cannot invoke "String.length()" because "name" is null`
Methodenaufruf auf `null`. Seit Java 14 nennt die Meldung die genaue
Referenz — lies sie, sie sagt dir direkt, welche Variable leer war.
(Steht dort `"<local1>"`, wurde ohne `javac -g` kompiliert.)

### `Error: Could not find or load main class Aufgaben`
`java` findet die Klasse nicht. Meist: `java Aufgaben.class` statt
`java Aufgaben`, falscher `-cp`, oder die Klasse liegt in einem Paket und
muss mit vollem Namen (`java de.firma.Start`) gestartet werden.

### `ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5`
Gueltige Indizes gehen von `0` bis `length - 1`. Klassisch: `<=` statt `<`
in der Schleifenbedingung.

### `StringIndexOutOfBoundsException`
Dasselbe bei `charAt` oder `substring`. Beachte: `substring(von, bis)` hat
`bis` **exklusiv**.

### `NumberFormatException: For input string: "abc"`
`Integer.parseInt` auf etwas, das keine Zahl ist. Auch Leerzeichen zaehlen —
`strip()` hilft.

### `ArithmeticException: / by zero`
Nur bei **Ganzzahl**division. `1.0 / 0` ergibt `Infinity` ohne Exception.

### `ClassCastException: class A cannot be cast to class B`
Ein Downcast auf einen Typ, den das Objekt nicht hat. Vorher mit
`instanceof` pruefen.

### `ConcurrentModificationException`
Die Collection wurde waehrend einer `for-each`-Schleife veraendert.
Nimm `removeIf` oder einen expliziten `Iterator`.

### `StackOverflowError`
Rekursion ohne (erreichbaren) Basisfall. Sieh dir die sich wiederholenden
Zeilen im Stacktrace an.

### `OutOfMemoryError: Java heap space`
Zu viele Objekte gleichzeitig im Speicher. Bei Dateien: `Files.lines` statt
`readAllLines`.

### `UnsupportedOperationException`
Aenderung an einer unveraenderlichen Collection (`List.of(...)`,
`stream.toList()`, `Collections.unmodifiableList(...)`) — oder `add`/`remove`
auf einer `Arrays.asList(...)`-Liste (die hat feste Groesse).
Kopiere sie: `new ArrayList<>(liste)`.

### `IllegalStateException: Duplicate key Anna`
`Collectors.toMap` hat einen Schluessel zweimal gesehen. Dritter Parameter
(Merge-Funktion) angeben: `toMap(k, v, (alt, neu) -> alt)`.

### `IllegalStateException: stream has already been operated upon or closed`
Ein Stream wurde zweimal verwendet. Erzeuge einen neuen.

### `MalformedInputException: Input length = 1`
Die Datei ist nicht UTF-8 kodiert — oft `ISO-8859-1`.

## Wenn gar nichts geht

```bash
# Was genau kompiliert nicht?
javac -Xlint:all datei.java

# Welche Java-Version laeuft ueberhaupt?
java -version && javac -version

# Wo suche ich gerade?
java -cp build Klasse
```

Und die wichtigste Regel: **Immer den ERSTEN Fehler zuerst beheben.**
Alles darunter sind oft nur Folgefehler.
