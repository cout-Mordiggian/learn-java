# Werkzeuge — Beispieldateien

Diese Dateien werden vom Kurs-Runner **nicht** kompiliert. Sie sind Vorlagen
zum Abschreiben, wenn du dein erstes richtiges Projekt aufsetzt.

- `pom.xml` — Maven-Projekt mit Java 21 und JUnit 6 (Stand 2026; mit JUnit 5 sieht alles gleich aus)
- `build.gradle.kts` — dasselbe mit Gradle
- `BeispielTest.java` — wie ein JUnit-Test aussieht

## Ein Maven-Projekt in einer Minute

```bash
mkdir -p ~/dev/mein-projekt/src/main/java/de/beispiel
mkdir -p ~/dev/mein-projekt/src/test/java/de/beispiel
cp pom.xml ~/dev/mein-projekt/
cp BeispielTest.java ~/dev/mein-projekt/src/test/java/de/beispiel/
# Konto aus Kapitel 5 dazulegen - mit "package de.beispiel;" als erster Zeile:
(echo 'package de.beispiel;'; cat ../../05-oop-klassen-und-objekte/loesungen/Konto.java) \
    > ~/dev/mein-projekt/src/main/java/de/beispiel/Konto.java
cd ~/dev/mein-projekt
mvn test
```

Die Versionsnummern in `pom.xml` veralten: Die jeweils aktuelle findest du auf
<https://central.sonatype.com> (Suche nach `junit-jupiter`).

Oder mit dem Generator (laedt beim ersten Mal einiges nach):

```bash
mvn archetype:generate -DgroupId=de.beispiel -DartifactId=mein-projekt \
    -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
