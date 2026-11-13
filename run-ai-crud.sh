#!/usr/bin/env bash
# ======================================================================
#  AI CRUD GENERATOR - MULTI-MICROSERVICIO (un servicio = un repositorio)
#  Equivalente a run-ai-crud.bat para Linux / macOS / Git Bash.
#  Lee .ai/services.txt y genera CADA servicio en SU carpeta y SU remoto.
#
#  Variables opcionales:
#    MODEL        modelo de Claude (si se omite, el que use la CLI por defecto)
#    CLAUDE_ARGS  argumentos extra para la CLI (p. ej. --permission-mode acceptEdits)
# ======================================================================
set -u

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MANIFEST="$ROOT/.ai/services.txt"
MODEL="${MODEL:-}"
CLAUDE_ARGS="${CLAUDE_ARGS:-}"

echo "========================================================"
echo "  AI CRUD GENERATOR - MULTI-MICROSERVICIO"
echo "  Modelo activo: ${MODEL:-por defecto de la CLI}"
echo "  Manifiesto: $MANIFEST"
echo "========================================================"

command -v claude >/dev/null 2>&1 || { echo "[ERROR] No se encuentra 'claude' CLI en el PATH."; exit 1; }
command -v git >/dev/null 2>&1 || { echo "[ERROR] No se encuentra 'git' en el PATH."; exit 1; }
[ -f "$MANIFEST" ] || { echo "[ERROR] No existe el manifiesto .ai/services.txt."; exit 1; }

# Ejecuta una fase pasando su prompt a la CLI por la entrada estandar.
run_phase() {
    local prompt="$1"
    # shellcheck disable=SC2086
    claude ${MODEL:+--model "$MODEL"} $CLAUDE_ARGS -p < ".ai/prompts/$prompt"
}

process() {
    local service_name="$1" target_dir="$2" remote_url="$3" spec_file="$4" db_spec="$5"
    local mode="api-first"
    [ -z "$spec_file" ] && mode="db-first"

    echo
    echo "########################################################"
    echo "#  Microservicio: $service_name  [$mode]"
    echo "#  Carpeta : $target_dir"
    echo "#  Remoto  : $remote_url"
    echo "#  OpenAPI : $spec_file"
    echo "#  Esquema : $db_spec"
    echo "########################################################"

    [ -n "$target_dir" ] || { echo "[ERROR] TARGET_DIR vacio para $service_name."; return 1; }
    [ -n "$remote_url" ] || { echo "[ERROR] REMOTE_URL vacio para $service_name."; return 1; }
    [ -n "$db_spec" ] || { echo "[ERROR] DB_SPEC vacio para $service_name: indica el esquema SQL del servicio."; return 1; }
    [ -f "$ROOT/$db_spec" ] || { echo "[ERROR] No existe el esquema de BD $db_spec."; return 1; }
    if [ "$mode" = "api-first" ] && [ ! -f "$ROOT/$spec_file" ]; then
        echo "[ERROR] No existe el contrato $spec_file."; return 1
    fi

    # Falla pronto si el remoto no existe o no es accesible (antes de gastar las fases).
    git ls-remote "$remote_url" >/dev/null 2>&1 \
        || { echo "[ERROR] El remoto $remote_url no existe o no es accesible. Crea el repositorio antes de generar."; return 1; }

    # Las rutas relativas se resuelven desde la raiz del generador.
    case "$target_dir" in
        /*|[A-Za-z]:*) ;;
        *) target_dir="$ROOT/$target_dir" ;;
    esac
    mkdir -p "$target_dir/.ai" "$target_dir/specs" || return 1
    target_dir="$(cd "$target_dir" && pwd)"

    echo "Copiando reglas, skills, plantillas y especificaciones a \"$target_dir\"..."
    cp "$ROOT/CLAUDE.md" "$target_dir/"
    rm -rf "$target_dir/.claude" "$target_dir/.ai/prompts"
    cp -R "$ROOT/.claude" "$target_dir/.claude"
    cp -R "$ROOT/.ai/prompts" "$target_dir/.ai/prompts"
    cp "$ROOT/templates/checkstyle.xml" "$ROOT/templates/.editorconfig" "$target_dir/"
    mkdir -p "$target_dir/plans" && cp -R "$ROOT/templates/plans/." "$target_dir/plans/"
    cp "$ROOT/$db_spec" "$target_dir/specs/database-spec.sql"
    [ "$mode" = "api-first" ] && cp "$ROOT/$spec_file" "$target_dir/specs/crud-spec.yaml"
    [ -f "$ROOT/specs/acceptance/$service_name.md" ] \
        && cp "$ROOT/specs/acceptance/$service_name.md" "$target_dir/specs/acceptance-criteria.md"

    cat > "$target_dir/.ai/project-context.txt" <<EOF
SERVICE_NAME=$service_name
REMOTE_URL=$remote_url
TARGET_DIR=$target_dir
GENERATION_MODE=$mode
EOF

    (
        cd "$target_dir" || exit 1
        echo "Directorio de trabajo: $PWD"
        if [ "$mode" = "db-first" ]; then
            echo "[FASE 0/5] Database-First: derivando OpenAPI desde el esquema..."
            run_phase 00-db-to-openapi.md || { echo "[ERROR] Fase 0 fallo en $service_name."; exit 1; }
        fi
        echo "[FASE 1/5] Plan y tareas..."
        run_phase 01-plan-and-tasks.md || { echo "[ERROR] Fase 1 fallo en $service_name."; exit 1; }
        echo "[FASE 2/5] Dominio..."
        run_phase 02-domain.md || { echo "[ERROR] Fase 2 fallo en $service_name."; exit 1; }
        echo "[FASE 3/5] Aplicacion + tests..."
        run_phase 03-application.md || { echo "[ERROR] Fase 3 fallo en $service_name."; exit 1; }
        echo "[FASE 4/5] Adaptadores, Docker, Cucumber..."
        run_phase 04-adapters-and-tests.md || { echo "[ERROR] Fase 4 fallo en $service_name."; exit 1; }
        echo "[FASE 5/5] Verificacion, doc HTML y push a $remote_url..."
        run_phase 05-e2e-verification.md || { echo "[ERROR] Fase 5 fallo en $service_name."; exit 1; }
    ) || return 1

    echo "[OK] $service_name generado en $target_dir y sincronizado con $remote_url."
}

count=0
fail=0
# fd 3 para el manifiesto: asi la entrada estandar queda libre para la CLI.
while IFS=';' read -r -u 3 service_name target_dir remote_url spec_file db_spec || [ -n "${service_name:-}" ]; do
    service_name="${service_name%$'\r'}"; db_spec="${db_spec%$'\r'}"; spec_file="${spec_file%$'\r'}"
    remote_url="${remote_url%$'\r'}"; target_dir="${target_dir%$'\r'}"
    case "$service_name" in ''|'#'*) continue ;; esac
    count=$((count + 1))
    process "$service_name" "$target_dir" "$remote_url" "$spec_file" "$db_spec" || fail=$((fail + 1))
done 3< "$MANIFEST"

echo
echo "========================================================"
echo "  PROCESO COMPLETADO. Servicios procesados: $count  Fallidos: $fail"
echo "========================================================"
[ "$count" -eq 0 ] && echo "[AVISO] El manifiesto no tiene ninguna linea activa."
[ "$fail" -eq 0 ]
