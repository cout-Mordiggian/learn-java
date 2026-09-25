#!/usr/bin/env bash
# Kompiliert das Projekt nach build/ und startet es.
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"
# Frisch bauen: sonst blieben .class-Dateien geloeschter/umbenannter Klassen liegen.
rm -rf build
mkdir -p build
javac -g -encoding UTF-8 -d build src/*.java
java -cp build Main "$@"
