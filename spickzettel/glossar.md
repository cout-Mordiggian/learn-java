# Glossar

**Annotation** — Metadaten am Code, z. B. `@Override`, `@FunctionalInterface`.
Manche prueft der Compiler, andere werten Bibliotheken zur Laufzeit aus.

**Autoboxing** — Automatische Umwandlung zwischen `int` und `Integer`.
Bequem, aber nicht gratis: In heissen Schleifen erzeugt es viele Objekte.

**Bytecode** — Das Zwischenformat in `.class`-Dateien. Plattformunabhaengig,
wird von der JVM ausgefuehrt bzw. per JIT in Maschinencode uebersetzt.

**Checked Exception** — Ausnahme, die der Compiler erzwingt zu behandeln.
Alles unter `Exception` ausser `RuntimeException`. Siehe Kapitel 7.

**Classpath** — Die Liste der Orte, an denen die JVM Klassen sucht (`-cp`).

**Comparable / Comparator** — Natuerliche Ordnung *in* der Klasse
(`compareTo`) gegen alternative Ordnungen *ausserhalb*.

**Deadlock** — Zwei Threads warten gegenseitig auf Sperren, die der jeweils
andere haelt. Beide stehen fuer immer.

**Diamond Operator** — Das `<>` in `new ArrayList<>()`. Der Compiler liest die
Typargumente von der linken Seite ab.

**Dynamischer Typ** — Der tatsaechliche Typ des Objekts zur Laufzeit; im
Gegensatz zum **statischen Typ** der Variablen. Entscheidet beim
Methodenaufruf (Polymorphie).

**Effektiv final** — Eine lokale Variable, die nach der Initialisierung nicht
mehr veraendert wird. Nur solche darf ein Lambda benutzen.

**Enum** — Typ mit einer festen Menge benannter Werte (`ROT, GELB, GRUEN`).
Vollwertige Klasse: darf Felder und Methoden haben.

**Erasure (Typloeschung)** — Generics existieren nur zur Compile-Zeit; zur
Laufzeit ist `List<String>` einfach `List`.

**Garbage Collector** — Gibt Objekte frei, die nicht mehr erreichbar sind.
Du gibst in Java nichts von Hand frei.

**Generics** — Typparameter wie in `List<String>`: Der Compiler prueft den
Elementtyp, Casts entfallen. Siehe Kapitel 8.

**Guard Clause** — Frueher `return` fuer Sonderfaelle am Methodenanfang, damit
der Hauptfall flach bleibt.

**Heap / Stack** — Objekte leben auf dem Heap, lokale Variablen und
Methodenaufrufe auf dem Stack. Zu tiefe Rekursion -> `StackOverflowError`.

**Idempotent** — Mehrfaches Ausfuehren hat dieselbe Wirkung wie einmaliges.
Wichtige Eigenschaft von `close()`.

**Immutable (unveraenderlich)** — Der Zustand steht nach der Erzeugung fest.
`String`, `Integer`, `record`, `List.of(...)`. Automatisch thread-sicher.

**Interface** — Vertrag ueber Faehigkeiten, ohne Zustand. Eine Klasse kann
beliebig viele implementieren.

**Invariante** — Eine Zusage, die fuer ein Objekt immer gilt
("das Guthaben ist nie negativ"). Konstruktoren stellen sie her, Methoden
erhalten sie.

**JDK / JRE / JVM** — Entwicklungswerkzeuge / Laufzeitumgebung / die
virtuelle Maschine, die Bytecode ausfuehrt.

**JIT** — Just-in-Time-Compiler. Uebersetzt haeufig durchlaufenen Bytecode
zur Laufzeit in Maschinencode. Grund dafuer, dass Java "warm" schnell ist.

**jshell** — Interaktive Java-Konsole: Ausdruecke eintippen, Ergebnis sofort
sehen. Ideal zum Ausprobieren.

**Kapselung** — Interne Daten sind `private`; Aenderungen laufen ueber
Methoden, die die Invarianten pruefen.

**Konstruktor** — Spezielle Methode ohne Rueckgabetyp, die ein neues Objekt
initialisiert; heisst wie die Klasse und laeuft bei `new`.

**Kovarianter Rueckgabetyp** — Eine ueberschreibende Methode darf einen
spezielleren Typ zurueckgeben als die ueberschriebene.

**Lambda** — Anonyme Funktion `x -> x * 2`, nutzbar ueberall dort, wo ein
funktionales Interface erwartet wird.

**LTS** — Long Term Support. Java 8, 11, 17, 21, 25.

**Methodenreferenz** — Kurzform eines Lambdas, das nur eine Methode aufruft:
`String::length` statt `s -> s.length()`.

**NPE** — `NullPointerException`. Methodenaufruf auf einer `null`-Referenz.

**Optional** — Rueckgabetyp fuer "vielleicht kein Wert"; ersetzt `null` als
Rueckgabe. Siehe Kapitel 9.

**Overloading (Ueberladen)** — Gleicher Name, andere Parameter. Der Compiler
entscheidet.

**Overriding (Ueberschreiben)** — Unterklasse ersetzt eine geerbte Methode.
Die Laufzeit entscheidet.

**Paket (package)** — Namensraum und Sichtbarkeitsgrenze; spiegelt die
Ordnerstruktur.

**Pattern Matching** — Typ pruefen und Variable binden in einem Schritt:
`if (o instanceof String s)`, `case Kreis(double r) ->`.

**PECS** — "Producer extends, Consumer super". Merkregel fuer Wildcards:
`? extends T` zum Lesen, `? super T` zum Schreiben.

**Polymorphie** — Derselbe Aufruf, unterschiedliches Verhalten je nach
tatsaechlichem Objekttyp.

**Race Condition** — Das Ergebnis haengt davon ab, welcher Thread zufaellig
zuerst drankommt.

**Record** — (Flach) unveraenderlicher Datentraeger; der Compiler erzeugt
Konstruktor, Zugriffsmethoden, `equals`, `hashCode`, `toString`.

**Referenz** — Ein Verweis auf ein Objekt. Java-Variablen von Objekttypen
enthalten Referenzen, nie das Objekt selbst.

**Sealed** — Ein Typ, der nur von aufgezaehlten Untertypen abgeleitet werden
darf. Ermoeglicht vollstaendigkeitsgepruefte `switch`-Ausdruecke.

**Signatur** — Name plus Parametertypen einer Methode. **Nicht** der
Rueckgabetyp.

**Stacktrace** — Der Aufrufpfad zum Zeitpunkt einer Exception. Von oben lesen.

**Stream** — Pipeline ueber Daten, faul ausgewertet, einmal benutzbar.
Keine Datenstruktur.

**String-Pool** — Zwischenspeicher fuer String-Literale. Grund dafuer, dass
`==` bei Literalen scheinbar funktioniert — und warum man sich nie darauf
verlassen darf (bei Strings aus Eingaben oder Dateien klappt es nicht).

**Terminaloperation** — Die Operation, die eine Stream-Pipeline ausloest
(`toList`, `count`, `collect`, `forEach`).

**Thread-sicher** — Aus mehreren Threads gleichzeitig nutzbar, ohne dass
Daten kaputtgehen.

**Unboxing** — Gegenstueck zum Autoboxing (`Integer` -> `int`). Ist das
`Integer` `null`, gibt es eine `NullPointerException`.

**Unchecked Exception** — `RuntimeException` und Unterklassen. Der Compiler
verlangt keine Behandlung; sie signalisieren Programmierfehler.

**Varargs** — `int... zahlen`: beliebig viele Argumente, innen ein Array.

**volatile** — Feld-Modifier: Aenderungen werden sofort fuer alle Threads
sichtbar. Macht `x++` aber nicht atomar.

**Wrapper-Klasse** — Objektfassung eines primitiven Typs: `Integer`,
`Double`, `Boolean`, `Character`.
