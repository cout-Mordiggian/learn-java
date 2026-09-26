# Glossar

**Annotation** — Metadaten am Code, z. B. `@Override`, `@FunctionalInterface`.
Manche prüft der Compiler, andere werten Bibliotheken zur Laufzeit aus.

**Autoboxing** — Automatische Umwandlung zwischen `int` und `Integer`.
Bequem, aber nicht gratis: In heissen Schleifen erzeugt es viele Objekte.

**Bytecode** — Das Zwischenformat in `.class`-Dateien. Plattformunabhängig,
wird von der JVM ausgeführt bzw. per JIT in Maschinencode übersetzt.

**Checked Exception** — Ausnahme, die der Compiler erzwingt zu behandeln.
Alles unter `Exception` ausser `RuntimeException`. Siehe Kapitel 7.

**Classpath** — Die Liste der Orte, an denen die JVM Klassen sucht (`-cp`).

**Comparable / Comparator** — Natürliche Ordnung *in* der Klasse
(`compareTo`) gegen alternative Ordnungen *ausserhalb*.

**Deadlock** — Zwei Threads warten gegenseitig auf Sperren, die der jeweils
andere hält. Beide stehen für immer.

**Diamond Operator** — Das `<>` in `new ArrayList<>()`. Der Compiler liest die
Typargumente von der linken Seite ab.

**Dynamischer Typ** — Der tatsächliche Typ des Objekts zur Laufzeit; im
Gegensatz zum **statischen Typ** der Variablen. Entscheidet beim
Methodenaufruf (Polymorphie).

**Effektiv final** — Eine lokale Variable, die nach der Initialisierung nicht
mehr verändert wird. Nur solche darf ein Lambda benutzen.

**Enum** — Typ mit einer festen Menge benannter Werte (`ROT, GELB, GRUEN`).
Vollwertige Klasse: darf Felder und Methoden haben.

**Erasure (Typlöschung)** — Generics existieren nur zur Compile-Zeit; zur
Laufzeit ist `List<String>` einfach `List`.

**Garbage Collector** — Gibt Objekte frei, die nicht mehr erreichbar sind.
Du gibst in Java nichts von Hand frei.

**Generics** — Typparameter wie in `List<String>`: Der Compiler prüft den
Elementtyp, Casts entfallen. Siehe Kapitel 8.

**Guard Clause** — Früher `return` für Sonderfälle am Methodenanfang, damit
der Hauptfall flach bleibt.

**Heap / Stack** — Objekte leben auf dem Heap, lokale Variablen und
Methodenaufrufe auf dem Stack. Zu tiefe Rekursion -> `StackOverflowError`.

**Idempotent** — Mehrfaches Ausführen hat dieselbe Wirkung wie einmaliges.
Wichtige Eigenschaft von `close()`.

**Immutable (unveränderlich)** — Der Zustand steht nach der Erzeugung fest.
`String`, `Integer`, `record`, `List.of(...)`. Automatisch thread-sicher.

**Interface** — Vertrag über Fähigkeiten, ohne Zustand. Eine Klasse kann
beliebig viele implementieren.

**Invariante** — Eine Zusage, die für ein Objekt immer gilt
("das Guthaben ist nie negativ"). Konstruktoren stellen sie her, Methoden
erhalten sie.

**JDK / JRE / JVM** — Entwicklungswerkzeuge / Laufzeitumgebung / die
virtuelle Maschine, die Bytecode ausführt.

**JIT** — Just-in-Time-Compiler. Übersetzt häufig durchlaufenen Bytecode
zur Laufzeit in Maschinencode. Grund dafür, dass Java "warm" schnell ist.

**jshell** — Interaktive Java-Konsole: Ausdrücke eintippen, Ergebnis sofort
sehen. Ideal zum Ausprobieren.

**Kapselung** — Interne Daten sind `private`; Änderungen laufen über
Methoden, die die Invarianten prüfen.

**Konstruktor** — Spezielle Methode ohne Rückgabetyp, die ein neues Objekt
initialisiert; heisst wie die Klasse und läuft bei `new`.

**Kovarianter Rückgabetyp** — Eine überschreibende Methode darf einen
spezielleren Typ zurückgeben als die überschriebene.

**Lambda** — Anonyme Funktion `x -> x * 2`, nutzbar überall dort, wo ein
funktionales Interface erwartet wird.

**LTS** — Long Term Support. Java 8, 11, 17, 21, 25.

**Methodenreferenz** — Kurzform eines Lambdas, das nur eine Methode aufruft:
`String::length` statt `s -> s.length()`.

**NPE** — `NullPointerException`. Methodenaufruf auf einer `null`-Referenz.

**Optional** — Rückgabetyp für "vielleicht kein Wert"; ersetzt `null` als
Rückgabe. Siehe Kapitel 9.

**Overloading (Überladen)** — Gleicher Name, andere Parameter. Der Compiler
entscheidet.

**Overriding (Überschreiben)** — Unterklasse ersetzt eine geerbte Methode.
Die Laufzeit entscheidet.

**Paket (package)** — Namensraum und Sichtbarkeitsgrenze; spiegelt die
Ordnerstruktur.

**Pattern Matching** — Typ prüfen und Variable binden in einem Schritt:
`if (o instanceof String s)`, `case Kreis(double r) ->`.

**PECS** — "Producer extends, Consumer super". Merkregel für Wildcards:
`? extends T` zum Lesen, `? super T` zum Schreiben.

**Polymorphie** — Derselbe Aufruf, unterschiedliches Verhalten je nach
tatsächlichem Objekttyp.

**Race Condition** — Das Ergebnis hängt davon ab, welcher Thread zufällig
zuerst drankommt.

**Record** — (Flach) unveränderlicher Datenträger; der Compiler erzeugt
Konstruktor, Zugriffsmethoden, `equals`, `hashCode`, `toString`.

**Referenz** — Ein Verweis auf ein Objekt. Java-Variablen von Objekttypen
enthalten Referenzen, nie das Objekt selbst.

**Sealed** — Ein Typ, der nur von aufgezählten Untertypen abgeleitet werden
darf. Ermöglicht vollständigkeitsgeprüfte `switch`-Ausdrücke.

**Signatur** — Name plus Parametertypen einer Methode. **Nicht** der
Rückgabetyp.

**Stacktrace** — Der Aufrufpfad zum Zeitpunkt einer Exception. Von oben lesen.

**Stream** — Pipeline über Daten, faul ausgewertet, einmal benutzbar.
Keine Datenstruktur.

**String-Pool** — Zwischenspeicher für String-Literale. Grund dafür, dass
`==` bei Literalen scheinbar funktioniert — und warum man sich nie darauf
verlassen darf (bei Strings aus Eingaben oder Dateien klappt es nicht).

**Terminaloperation** — Die Operation, die eine Stream-Pipeline auslöst
(`toList`, `count`, `collect`, `forEach`).

**Thread-sicher** — Aus mehreren Threads gleichzeitig nutzbar, ohne dass
Daten kaputtgehen.

**Unboxing** — Gegenstück zum Autoboxing (`Integer` -> `int`). Ist das
`Integer` `null`, gibt es eine `NullPointerException`.

**Unchecked Exception** — `RuntimeException` und Unterklassen. Der Compiler
verlangt keine Behandlung; sie signalisieren Programmierfehler.

**Varargs** — `int... zahlen`: beliebig viele Argumente, innen ein Array.

**volatile** — Feld-Modifier: Änderungen werden sofort für alle Threads
sichtbar. Macht `x++` aber nicht atomar.

**Wrapper-Klasse** — Objektfassung eines primitiven Typs: `Integer`,
`Double`, `Boolean`, `Character`.
