"""Contrato OpenAPI 3.0.3 derivado del modelo (mismo origen que el codigo: no pueden divergir)."""
from __future__ import annotations

import json

from .model import SCALARS, Column, Service, Table


def schema_of(c: Column) -> dict:
    _, kind, fmt = SCALARS.get(c.base, SCALARS["text"])
    schema: dict = {"type": kind}
    if fmt:
        schema["format"] = fmt
    if c.length:
        schema["maxLength"] = c.length
    if c.is_json:
        schema = {"description": "JSON libre (columna jsonb)"}
    if c.is_array:
        schema = {"type": "array", "items": schema}
    if not c.not_null:
        schema["nullable"] = True
    return schema


def _object(columns: list[Column], required: list[str], example: dict) -> dict:
    schema: dict = {"type": "object", "properties": {c.field: schema_of(c) for c in columns}}
    if required:
        schema["required"] = required
    if example:
        schema["example"] = example
    return schema


def _param(c: Column, where: str, t: Table) -> dict:
    schema = schema_of(c)
    schema.pop("nullable", None)
    return {"name": c.field, "in": where, "required": where == "path", "schema": schema,
            "example": c.sample(t.name)}


def _error(description: str) -> dict:
    return {"description": description,
            "content": {"application/json": {"schema": {"$ref": "#/components/schemas/Error"}}}}


def _json_body(ref: str) -> dict:
    return {"required": True, "content": {"application/json": {"schema": {"$ref": f"#/components/schemas/{ref}"}}}}


def _ok(description: str, schema: dict) -> dict:
    return {"description": description, "content": {"application/json": {"schema": schema}}}


def build(s: Service) -> dict:
    paths: dict = {}
    schemas: dict = {
        "Error": {"type": "object", "required": ["code", "message"],
                  "properties": {"code": {"type": "string"}, "message": {"type": "string"}},
                  "example": {"code": "RESOURCE_NOT_FOUND", "message": "recurso no encontrado"}},
    }
    for t in s.tables:
        ref = {"$ref": f"#/components/schemas/{t.cls}"}
        schemas[t.cls] = _object(t.columns, [c.field for c in t.columns if c.not_null], t.resource())
        schemas[f"{t.cls}Request"] = _object(
            t.create_columns, [c.field for c in t.create_columns if c.required], t.body(0))
        page = {"type": "object", "properties": {
            "items": {"type": "array", "items": ref}, "total": {"type": "integer", "format": "int64"},
            "page": {"type": "integer"}, "size": {"type": "integer"}}}
        paging = [
            {"name": "page", "in": "query", "required": False, "schema": {"type": "integer", "default": 0}},
            {"name": "size", "in": "query", "required": False,
             "schema": {"type": "integer", "default": 20, "maximum": 100}},
        ]
        create_responses = {"201": _ok("Creado", ref), "400": _error("Peticion no valida"),
                            "409": _error("Ya existe o incumple una restriccion")}
        paths[t.path] = {
            "post": {"tags": [t.name], "operationId": f"create{t.cls}", "summary": f"Crea un registro de {t.name}",
                     "requestBody": _json_body(f"{t.cls}Request"), "responses": create_responses},
            "get": {"tags": [t.name], "operationId": f"list{t.cls}", "summary": f"Lista {t.name} (paginado)",
                    "parameters": [_param(c, "query", t) for c in t.filter_columns] + paging,
                    "responses": {"200": _ok("Pagina de resultados", page), "400": _error("Parametro no valido")}},
        }
        key = [_param(c, "path", t) for c in t.pk_columns]
        item: dict = {
            "get": {"tags": [t.name], "operationId": f"get{t.cls}", "summary": f"Obtiene un registro de {t.name}",
                    "parameters": key, "responses": {"200": _ok("Encontrado", ref), "404": _error("No existe")}},
        }
        if t.update_columns:
            schemas[f"{t.cls}UpdateRequest"] = _object(
                t.update_columns, [c.field for c in t.update_columns if c.required], t.body(1, update=True))
            item["put"] = {
                "tags": [t.name], "operationId": f"update{t.cls}", "summary": f"Modifica un registro de {t.name}",
                "parameters": key, "requestBody": _json_body(f"{t.cls}UpdateRequest"),
                "responses": {"200": _ok("Modificado", ref), "400": _error("Peticion no valida"),
                              "404": _error("No existe"), "409": _error("Incumple una restriccion")}}
        item["delete"] = {
            "tags": [t.name], "operationId": f"delete{t.cls}", "summary": f"Borra un registro de {t.name}",
            "parameters": key, "responses": {"204": {"description": "Borrado"}, "404": _error("No existe"),
                                             "409": _error("Lo impide una restriccion")}}
        paths[t.item_path] = item
    return {
        "openapi": "3.0.3",
        "info": {"title": f"{s.name} API", "version": "1.0.0",
                 "description": f"CRUD de las tablas de {s.name}, derivado del esquema de base de datos."},
        "servers": [{"url": "http://localhost:8080", "description": "Local"}],
        "tags": [{"name": t.name} for t in s.tables],
        "paths": paths,
        "components": {"schemas": schemas},
    }


def to_yaml(value, indent: int = 0) -> str:
    """Emisor YAML minimo (dict, list y escalares): evita depender de PyYAML para generar."""
    pad = "  " * indent
    if isinstance(value, dict):
        if not value:
            return "{}"
        out = []
        for key, item in value.items():
            key_text = key if isinstance(key, str) and key.replace("_", "").replace("-", "").isalnum() \
                and not key.isdigit() else json.dumps(str(key))
            if isinstance(item, (dict, list)) and item:
                out.append(f"{pad}{key_text}:\n{to_yaml(item, indent + 1)}")
            else:
                out.append(f"{pad}{key_text}: {to_yaml(item, indent + 1)}")
        return "\n".join(out)
    if isinstance(value, list):
        if not value:
            return "[]"
        out = []
        for item in value:
            if isinstance(item, (dict, list)) and item:
                rendered = to_yaml(item, indent + 1)
                out.append(f"{pad}- {rendered.lstrip()}")
            else:
                out.append(f"{pad}- {to_yaml(item, indent + 1)}")
        return "\n".join(out)
    return json.dumps(value, ensure_ascii=False)


def render(s: Service, port: int = 8080) -> str:
    document = build(s)
    document["servers"][0]["url"] = f"http://localhost:{port}"
    return ("# Generado desde el DDL por el generador de servicios. No editar a mano.\n"
            + to_yaml(document) + "\n")
