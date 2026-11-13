#!/usr/bin/env bash
# Genera los servicios del manifiesto, compila, deja que la IA corrija un build roto y sube cada uno
# a su remoto. Toda la logica esta en scripts/generate.py; aqui solo se fijan las opciones habituales.
#   ./run-ai-crud.sh                      todos los servicios
#   ./run-ai-crud.sh -s access-service    solo uno
#   ./run-ai-crud.sh --mode ai            pipeline antiguo (todo el codigo lo escribe la IA)
set -eu
cd "$(dirname "${BASH_SOURCE[0]}")"
PYTHON="$(command -v python3 || command -v python)" || { echo "[ERROR] Hace falta Python 3."; exit 1; }
exec "$PYTHON" scripts/generate.py --build --ai-fix 2 --ai-rules --push "$@"
