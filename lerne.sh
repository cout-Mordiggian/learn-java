#!/usr/bin/env bash
#
# Lern-Runner fuer den Java-Kurs.
#
#   ./lerne.sh              Kapiteluebersicht
#   ./lerne.sh 03           Kapitel 03 kompilieren und pruefen
#   ./lerne.sh 03 -l        gegen die Musterloesung pruefen
#   ./lerne.sh alle         alle Kapitel pruefen
#   ./lerne.sh 03 -r Demo   Klasse "Demo" aus dem Kapitel ausfuehren
#
set -uo pipefail
BASE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

uebersicht() {
  echo
  echo "  Java von Grund auf - Kapiteluebersicht"
  echo "  ======================================"
  for d in "$BASE"/kapitel/*/; do
    name="$(basename "$d")"
    titel="$(sed -n '1s/^#\s*//p' "$d/README.md" 2>/dev/null | sed 's/^Kapitel [0-9]* — //')"
    printf "   %s  %s\n" "${name%%-*}" "${titel:-$name}"
  done
  echo
  echo "  Start:  ./lerne.sh 01        Loesung: ./lerne.sh 01 -l"
  echo
}

kapitel_pfad() {
  local nr="$1"
  local treffer
  treffer="$(find "$BASE/kapitel" -maxdepth 1 -type d -name "${nr}-*" | head -1)"
  if [ -z "$treffer" ]; then
    echo "Kein Kapitel '$nr' gefunden. './lerne.sh' zeigt alle Kapitel." >&2
    return 1
  fi
  echo "$treffer"
}

pruefe() {
  local nr="$1" modus="$2" hauptklasse="${3:-Tests}"
  local dir out quelle
  dir="$(kapitel_pfad "$nr")" || return 1
  if [ "$modus" = "loesung" ]; then
    quelle="$dir/loesungen"
    out="$BASE/build/$nr-loesung"
  else
    quelle="$dir/src"
    out="$BASE/build/$nr"
  fi

  rm -rf "$out"; mkdir -p "$out"
  echo
  echo "  == $(basename "$dir")  [${modus}] =="

  # shellcheck disable=SC2046
  # -g: Debug-Infos, damit NullPointerExceptions echte Variablennamen nennen
  if ! javac -g -Xlint:-serial -encoding UTF-8 -d "$out" \
        $(find "$BASE/lib" "$quelle" "$dir/tests" -name '*.java' 2>/dev/null); then
    echo
    echo "  Kompilierfehler - lies die Meldung von oben nach unten, der erste Fehler zaehlt."
    return 1
  fi
  java -cp "$out" "$hauptklasse"
}

case "${1:-}" in
  ""|-h|--help|hilfe) uebersicht ;;
  alle)
    modus="aufgabe"
    [ "${2:-}" = "-l" ] || [ "${2:-}" = "--loesung" ] && modus="loesung"
    fehler=0
    for d in "$BASE"/kapitel/*/; do
      nr="$(basename "$d")"; nr="${nr%%-*}"
      pruefe "$nr" "$modus" || fehler=1
    done
    echo
    if [ $fehler -eq 0 ]; then
      echo "  Alle Kapitel bestanden."
    else
      echo "  Mindestens ein Kapitel ist noch offen."
    fi
    exit $fehler ;;
  *)
    nr="$1"; shift
    modus="aufgabe"; klasse="Tests"
    while [ $# -gt 0 ]; do
      case "$1" in
        -l|--loesung) modus="loesung" ;;
        -r|--run)     shift; klasse="${1:-Tests}" ;;
      esac
      shift
    done
    pruefe "$nr" "$modus" "$klasse" ;;
esac
