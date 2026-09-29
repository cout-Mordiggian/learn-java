# Entwurfsmuster auf einen Blick

Ein **Entwurfsmuster** ist eine bewährte, benannte Lösung für ein
wiederkehrendes Entwurfsproblem: Name, Problem, Lösung, Preis. Die klassische
Sammlung ist *Design Patterns* (Gamma, Helm, Johnson, Vlissides, 1994, die
"Gang of Four") mit 23 Mustern in drei Gruppen. Im Kurs stehen die Muster dort,
wo ihre Voraussetzungen zum ersten Mal da sind. Hier ist alles an einem Ort.

## Die Muster im Kurs

| Muster | Problem | Lösung in einem Satz | im Kurs | im JDK |
|--------|---------|-----------------------|---------|--------|
| **Statische Fabrikmethode** | `new` hat keinen Namen und liefert immer ein neues Objekt | eine statische Methode erzeugt das Objekt, darf cachen und Untertypen liefern | 5.10, 9.8 | `List.of`, `Integer.valueOf`, `Path.of` |
| **Singleton** | genau ein Objekt für das ganze Programm | privater Konstruktor + `static final`-Instanz, am besten ein `enum` | 5.10, 10.9, 12.3 | `Runtime.getRuntime()` |
| **Fabrik** | welche Klasse, steht erst zur Laufzeit fest | eine Methode entscheidet und liefert nur das Interface | 9.8 | `Collectors.toCollection(TreeSet::new)` |
| **Builder** | viele, oft freiwillige Angaben | ein Hilfsobjekt sammelt, `build()` prüft und baut | 10.9 | `StringBuilder`, `HttpRequest.newBuilder()` |
| **Strategie** | Verhalten soll austauschbar sein | Verhalten hinter ein Interface, der Kontext ruft es auf | 6.9, 9.8 | `Comparator` für `sort` |
| **Dekorierer** | Zusatzverhalten in beliebiger Kombination | Hülle mit demselben Interface, reicht an das Innere weiter | 6.9, 11.9 | `BufferedReader`, `Collections.unmodifiableList` |
| **Adapter** | vorhandene Klasse passt nicht zum erwarteten Interface | ein Zwischenstück übersetzt | 6.9, 11.9 | `Arrays.asList`, `InputStreamReader` |
| **Schablonenmethode** | Ablauf fest, einzelne Schritte variieren | `final`-Methode in der Oberklasse ruft abstrakte Schritte | 6.9 | `AbstractList` |
| **Beobachter** | andere informieren, ohne sie zu kennen | Liste angemeldeter Beobachter, alle benachrichtigen | 9.9 | Listener in GUIs, `Flow` |
| **Iterator** | Elemente durchlaufen, ohne das Innere zu kennen | `Iterable` + `Iterator`, dann klappt for-each | 14.9 | jede `Collection` |
| **Befehl** | Aktionen speichern, später ausführen, rückgängig machen | jede Aktion wird ein Objekt | 14.10 | `Runnable`/`Callable` im `ExecutorService` |
| **Dependency Injection** | versteckte Abhängigkeiten, schwer testbar | Abhängigkeiten per Konstruktor hineinreichen | 13.8 | Frameworks wie Spring |

## Die Grundform im Code

```java
// Singleton
public enum IdGenerator { INSTANZ; /* Felder, Methoden */ }

// Statische Fabrikmethode mit Cache
private static final Rabatt KEINER = betrag -> betrag;
public static Rabatt keiner() { return KEINER; }

// Builder
Pizza p = Pizza.builder(Groesse.MITTEL).belag("Salami").extraKaese().build();

// Strategie
kasse.setzeRabatt(betrag -> betrag * 90 / 100);
liste.sort(Comparator.comparing(Person::getName));

// Dekorierer: dasselbe Interface, innen ein anderes Exemplar
class MitZeilennummer implements Protokoll {
    private final Protokoll innen;
    public void schreibe(String z) { innen.schreibe(++nr + ": " + z); }
}

// Beobachter
lager.anmelden((artikel, bestand) -> System.out.println(artikel + " knapp"));
for (LagerBeobachter b : List.copyOf(beobachter)) b.knapp(artikel, bestand);   // Kopie!

// Befehl
interface Befehl { void ausfuehren(); void rueckgaengig(); }
```

## Weitere Muster, die dir begegnen werden

| Muster | Idee | in Java |
|--------|------|---------|
| **Kompositum** (*Composite*) | Einzelteil und Gruppe haben dasselbe Interface, es entsteht ein Baum | Ordner und Dateien, GUI-Container. Projekt M6: Unteraufgaben |
| **Fassade** (*Facade*) | eine einfache Schnittstelle vor einem komplizierten Teilsystem | `Files.readString(pfad)` (11.9) |
| **Stellvertreter** (*Proxy*) | gleiches Interface, kontrolliert den Zugriff: später laden, Rechte, Netzwerk | Hibernate, Spring |
| **Zustand** (*State*) | das Verhalten wechselt mit dem Zustand | `enum` mit Methoden je Konstante (10.9) |
| **Besucher** (*Visitor*) | neue Operation über eine feste Typ-Hierarchie | heute: `sealed` + `switch` mit Typmustern (10.5, 10.9) |
| **Prototyp** (*Prototype*) | neue Objekte als Kopie eines Vorbilds | `clone()`, besser Kopierkonstruktor oder `record` |
| **Abstrakte Fabrik** | eine Familie zusammenpassender Objekte erzeugen | JDBC: `Connection`, `Statement`, `ResultSet` eines Treibers |
| **Wertobjekt** (*Value Object*) | klein, unveränderlich, gleich bei gleichem Inhalt | `record`, `LocalDate`, `String` |

Einige Muster sind in die Sprache gewandert: Iterator in for-each, Besucher in
`sealed` + `switch`, Strategie und Befehl in Lambdas.

## Wann Muster schaden

- **Erst das Problem, dann das Muster.** Eine Kasse mit genau einer Rabattart
  braucht keine Strategie. Faustregel: beim dritten ähnlichen Fall
  verallgemeinern (*rule of three*).
- **Jedes Muster kostet** Klassen und Umwege beim Lesen. Fünf Schichten
  Dekorierer beantworten "Wo passiert das eigentlich?" nicht mehr auf einen Blick.
- **Muster sind keine Klassennamen.** In modernem Java ist eine Strategie oft ein
  Lambda, eine Fabrik eine statische Methode, ein Befehl ein `Runnable`. Das
  Spring-Framework hat eine echte Klasse `AbstractSingletonProxyFactoryBean`.
  Dort ist sie sinnvoll, als Vorbild für eigenen Code eher nicht.
- **Singleton sparsam.** Er ist globaler Zustand: versteckte Abhängigkeiten,
  Tests, die sich gegenseitig beeinflussen (13.8).

Die Prüfung vor jedem Muster: **Welches Problem löst es hier, konkret, jetzt?**
