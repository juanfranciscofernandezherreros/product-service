#!/usr/bin/env python3
"""Valida el manifiesto .ai/services.txt y los contratos OpenAPI del generador.

Comprueba que:
  * cada linea activa del manifiesto tiene 5 campos y apunta a ficheros existentes;
  * no se repiten SERVICE_NAME, TARGET_DIR ni REMOTE_URL (un servicio = un repositorio);
  * TARGET_DIR no cae dentro de este repositorio;
  * cada DDL referenciado contiene al menos un CREATE TABLE;
  * todos los specs/openapi/*.yaml son OpenAPI validos.

Uso: python scripts/validate_specs.py   (requiere pyyaml y openapi-spec-validator)
"""
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MANIFEST = ROOT / ".ai" / "services.txt"
errors: list[str] = []


def check_manifest() -> int:
    seen: dict[str, dict[str, int]] = {"SERVICE_NAME": {}, "TARGET_DIR": {}, "REMOTE_URL": {}}
    active = 0
    for number, raw in enumerate(MANIFEST.read_text(encoding="utf-8").splitlines(), start=1):
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        active += 1
        where = f".ai/services.txt:{number}"
        fields = [field.strip() for field in line.split(";")]
        if len(fields) != 5:
            errors.append(f"{where}: se esperaban 5 campos separados por ';' y hay {len(fields)}")
            continue
        name, target, remote, spec, db_spec = fields
        if not re.fullmatch(r"[a-z][a-z0-9-]*", name):
            errors.append(f"{where}: SERVICE_NAME '{name}' debe ser kebab-case en minusculas")
        for label, value in (("SERVICE_NAME", name), ("TARGET_DIR", target), ("REMOTE_URL", remote)):
            if not value:
                errors.append(f"{where}: {label} vacio")
            elif value in seen[label]:
                errors.append(f"{where}: {label} '{value}' repetido (ya en la linea {seen[label][value]})")
            else:
                seen[label][value] = number
        if target and not re.match(r"^([A-Za-z]:|[\\/])", target):
            resolved = (ROOT / target).resolve()
            if resolved == ROOT or ROOT in resolved.parents:
                errors.append(f"{where}: TARGET_DIR '{target}' queda dentro del repositorio del generador")
        if spec and not (ROOT / spec).is_file():
            errors.append(f"{where}: no existe SPEC_FILE '{spec}'")
        if not db_spec:
            errors.append(f"{where}: DB_SPEC es obligatorio")
        elif not (ROOT / db_spec).is_file():
            errors.append(f"{where}: no existe DB_SPEC '{db_spec}'")
        elif not re.search(r"^\s*CREATE\s+TABLE", (ROOT / db_spec).read_text(encoding="utf-8"),
                           re.IGNORECASE | re.MULTILINE):
            errors.append(f"{where}: DB_SPEC '{db_spec}' no contiene ningun CREATE TABLE")
    return active


def check_openapi() -> int:
    import yaml
    from openapi_spec_validator import validate

    files = sorted((ROOT / "specs" / "openapi").glob("*.y*ml"))
    for path in files:
        rel = path.relative_to(ROOT)
        try:
            validate(yaml.safe_load(path.read_text(encoding="utf-8")))
        except Exception as exc:  # noqa: BLE001 - se informa cualquier fallo de validacion
            first_line = str(exc).strip().splitlines()[0] if str(exc).strip() else type(exc).__name__
            errors.append(f"{rel}: OpenAPI no valido: {first_line}")
    return len(files)


def main() -> int:
    services = check_manifest()
    contracts = check_openapi()
    for error in errors:
        print(f"ERROR {error}")
    print(f"{services} servicio(s) en el manifiesto, {contracts} contrato(s) OpenAPI, {len(errors)} error(es).")
    return 1 if errors else 0


if __name__ == "__main__":
    sys.exit(main())
