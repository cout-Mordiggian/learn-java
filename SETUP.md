# Setup und Werkzeuge

## 1. Was ist auf diesem Rechner installiert?

```bash
java -version    # die Laufzeitumgebung (JVM) - fuehrt Bytecode aus
javac -version   # der Compiler - macht aus .java Bytecode (.class)
```

Bei dir (Stand September 2026): **Temurin OpenJDK 27**. Das ist ein *JDK*
(Java Development Kit). Der Kurs verlangt mindestens Java 21 — alles Neuere
funktioniert ebenso, denn Java bleibt rückwärtskompatibel.
Ein *JRE* (nur Laufzeit) hätte kein `javac` — zum Entwickeln brauchst du das JDK.

| Begriff | Bedeutung |
|---------|-----------|
| **JDK** | Compiler + Werkzeuge + JRE. Das brauchst du. |
| **JRE** | Nur die Laufzeitumgebung. Reicht zum Ausführen, nicht zum Entwickeln. |
| **JVM** | Die virtuelle Maschine, die Bytecode ausführt. Teil des JRE. |
| **LTS** | Long Term Support. 8, 11, 17, 21, 25 sind LTS-Versionen. Dazwischen erscheint alle sechs Monate eine Version mit kurzer Lebensdauer (z. B. 26, 27). |

## 2. Der Kompilier-Zyklus von Hand

Damit du verstehst, was `./lerne.sh` für dich erledigt:

```bash
mkdir -p /tmp/javatest && cd /tmp/javatest
cat > Hallo.java <<'JAVA'
public class Hallo {
    public static void main(String[] args) {
        System.out.println("Hallo, Java!");
    }
}
JAVA

javac Hallo.java     # erzeugt Hallo.class (Bytecode)
java Hallo           # fuehrt aus - ACHTUNG: ohne .class und ohne .java!
```

Wichtige Regeln, die Anfänger oft stolpern lassen:

- Eine `public` Klasse muss **genauso heissen wie die Datei**: `Hallo` -> `Hallo.java`.
- `java Hallo` nimmt einen **Klassennamen**, keinen Dateinamen.
- `-d ziel/` sagt dem Compiler, wohin die `.class`-Dateien sollen.
- `-cp` (classpath) sagt der JVM, wo sie Klassen suchen soll.

Seit Java 11 geht auch der Schnellstart für eine einzelne Datei — praktisch zum Ausprobieren:

```bash
java Hallo.java      # kompiliert im Speicher und startet sofort
```

Seit **Java 25** darf ein kleines Programm sogar ganz ohne Klasse,
`public static` und `String[] args` auskommen (in Java 21 war das noch ein
Preview-Feature):

```java
void main() {
    IO.println("Hallo, Java!");
}
```

In diesem Kurs bleiben wir bei der klassischen Form, weil du sie in jedem
existierenden Projekt antreffen wirst — und weil sie zeigt, was wirklich
passiert (Klasse, statische Methode, Parameter).

## 3. Editor

Alles im Kurs funktioniert mit einem beliebigen Texteditor. Trotzdem lohnt sich
Werkzeugunterstützung — Java ist statisch typisiert, ein guter Editor sagt dir
Fehler *während* du tippst statt erst beim Kompilieren.

- **VS Code** + Extension Pack for Java (leichtgewichtig, guter Einstieg)
- **IntelliJ IDEA Community** (kostenlos, der De-facto-Standard in der Java-Welt)
- **Neovim/Helix** + `jdtls` per LSP

Für diesen Kurs: öffne den Kursordner (`~/dev/learn-java`) als Projektordner. Ohne Build-Datei
erkennt die IDE das als "einfaches Java-Projekt" — das reicht.

## 4. jshell — Java zum Ausprobieren

```bash
jshell
jshell> 7 / 2
$1 ==> 3
jshell> "Hallo".substring(1, 3)
$2 ==> "al"
jshell> /exit
```

`jshell` führt einzelne Java-Ausdrücke sofort aus — ohne Klasse, ohne `main`,
ohne Kompilieren. Ideal für Fragen wie "Was liefert `Math.round(-2.5)`?".
Gewöhne es dir ab Kapitel 1 an: Jedes Code-Schnipsel aus den Kapiteln kannst
du dort in Sekunden nachprüfen. `/help` zeigt die Befehle, Tab vervollständigt.

## 5. Fehlermeldungen lesen

Der Compiler meldet Fehler in dieser Form:

```
src/Aufgaben.java:12: error: cannot find symbol
        return laenge * breit;
                        ^
  symbol:   variable breit
  location: class Aufgaben
1 error
```

Lies sie von oben nach unten:

1. **Datei und Zeile** — `src/Aufgaben.java:12`
2. **Fehlerart** — `cannot find symbol` = ein Name, den der Compiler nicht kennt
3. **Das Zirkumflex `^`** zeigt exakt auf die Stelle
4. **`symbol:`** sagt, *welcher* Name unbekannt ist

Der **erste** Fehler ist der echte. Folgefehler verschwinden oft von allein,
wenn du den ersten behebst. Nicht von "37 errors" einschüchtern lassen.

Eine Sammlung typischer Meldungen mit Erklärung:
[`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md).

## 6. Wo du nachschlägst

- **Javadoc der Standardbibliothek**: <https://docs.oracle.com/en/java/javase/21/docs/api/>
  Das ist die Referenz. Gewöhne dir an, dort statt in Blogposts nachzusehen.
- `javap -cp build/01 Aufgaben` zeigt die Signaturen einer kompilierten Klasse.
- Die offizielle Sprachspezifikation (JLS) ist präzise, aber kein Lernmaterial.
