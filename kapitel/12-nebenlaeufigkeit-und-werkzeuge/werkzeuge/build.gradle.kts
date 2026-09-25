// Gradle mit Kotlin-DSL - dasselbe Projekt wie in pom.xml

plugins {
    java
    application
}

group = "de.beispiel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        // Gradle laedt bei Bedarf selbst ein passendes JDK herunter
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "de.beispiel.Main"
}

tasks.test {
    useJUnitPlatform()      // ohne diese Zeile werden JUnit-Tests ignoriert
    testLogging {
        events("passed", "skipped", "failed")
    }
}
