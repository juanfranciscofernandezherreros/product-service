"""Codigo Java (produccion y pruebas) de un servicio, generado desde el modelo.

Los tipos se escriben con nombre completo salvo los imports fijos de cada plantilla: asi nunca
queda un import sin usar (Checkstyle) aunque cambien las columnas.
"""
from __future__ import annotations

import json

from .model import Column, Service, Table

JSON_NODE = "com.fasterxml.jackson.databind.JsonNode"


def literal(column: Column, table: Table, variant: int = 0, dto: bool = False) -> str:
    """Expresion Java para el valor de ejemplo de una columna."""
    value = column.sample(table.name, variant)
    if column.is_array:
        return "java.util.List.of(" + _scalar_literal(column, value[0]) + ")"
    if column.is_json:
        text = json.dumps(json.dumps(value, separators=(",", ":")))
        return f"JsonValues.toNode({text})" if dto else text
    return _scalar_literal(column, value)


def _scalar_literal(column: Column, value) -> str:
    base = column.base
    if base in ("text", "varchar"):
        return json.dumps(value)
    if base == "uuid":
        return f'java.util.UUID.fromString("{value}")'
    if base == "bigint":
        return f"{value}L"
    if base == "integer":
        return str(value)
    if base == "smallint":
        return f"(short) {value}"
    if base == "boolean":
        return "true" if value else "false"
    if base == "timestamptz":
        return f'java.time.OffsetDateTime.parse("{value}")'
    if base == "timestamp":
        return f'java.time.LocalDateTime.parse("{value}")'
    if base == "date":
        return f'java.time.LocalDate.parse("{value}")'
    if base == "time":
        return f'java.time.LocalTime.parse("{value}")'
    if base == "numeric":
        return f'new java.math.BigDecimal("{value}")'
    if base == "double precision":
        return str(value)
    if base == "real":
        return f"{value}f"
    return json.dumps(str(value))


def dto_type(column: Column) -> str:
    return JSON_NODE if column.is_json else column.java


def pk_params(table: Table, final: bool = True) -> str:
    prefix = "final " if final else ""
    return ", ".join(f"{prefix}{c.java} {c.field}" for c in table.pk_columns)


def pk_args(table: Table, source: str = "") -> str:
    if source:
        return ", ".join(f"{source}.{c.field}()" for c in table.pk_columns)
    return ", ".join(c.field for c in table.pk_columns)


def pk_literals(table: Table, variant: int = 0) -> str:
    return ", ".join(literal(c, table, variant) for c in table.pk_columns)


def id_type(table: Table) -> str:
    return table.pk_columns[0].java if len(table.pk) == 1 else f"{table.cls}EntityId"


def lines(items: list[str], indent: int, sep: str = ",") -> str:
    return (sep + "\n").join(" " * indent + item for item in items)


# --------------------------------------------------------------------------------------------
# Dominio y aplicacion
# --------------------------------------------------------------------------------------------

def domain_record(pkg: str, t: Table) -> str:
    components = lines([f"{c.java} {c.field}" for c in t.columns], 8)
    return f"""package {pkg}.domain.model;

/**
 * Fila de la tabla {{@code {t.name}}}.
 */
public record {t.cls}(
{components}) {{
}}
"""


def repository_port(pkg: str, t: Table) -> str:
    return f"""package {pkg}.application.port.out;

import {pkg}.domain.model.{t.cls};

/**
 * Puerto de salida: persistencia de {{@code {t.name}}}.
 */
public interface {t.cls}RepositoryPort {{

    {t.cls} save({t.cls} value);

    java.util.Optional<{t.cls}> findById({pk_params(t, final=False)});

    boolean existsById({pk_params(t, final=False)});

    void deleteById({pk_params(t, final=False)});

    java.util.List<{t.cls}> findAll(java.util.Map<String, Object> filters, int page, int size);

    long count(java.util.Map<String, Object> filters);
}}
"""


def use_case(pkg: str, t: Table) -> str:
    update = f"\n    {t.cls} update({t.cls} changes);\n" if t.update_columns else ""
    return f"""package {pkg}.application.port.in;

import {pkg}.domain.model.PageResult;
import {pkg}.domain.model.{t.cls};

/**
 * Puerto de entrada: casos de uso CRUD de {{@code {t.name}}}.
 */
public interface {t.cls}UseCase {{

    {t.cls} create({t.cls} value);

    {t.cls} get({pk_params(t, final=False)});

    PageResult<{t.cls}> list(java.util.Map<String, Object> filters, int page, int size);
{update}
    void delete({pk_params(t, final=False)});
}}
"""


def service(pkg: str, t: Table) -> str:
    duplicate = ""
    exists_import = ""
    if not t.generated_key:
        exists_import = f"import {pkg}.domain.exception.ResourceAlreadyExistsException;\n"
        duplicate = f"""        if (repository.existsById({pk_args(t, "value")})) {{
            throw new ResourceAlreadyExistsException("{t.name} ya existe");
        }}
"""
    update = ""
    if t.update_columns:
        merged = []
        for c in t.columns:
            if c in t.update_columns and not (c.not_null and c.default is not None):
                merged.append(f"changes.{c.field}()")
            elif c in t.update_columns:
                merged.append(f"changes.{c.field}() != null ? changes.{c.field}() : current.{c.field}()")
            elif c.pk:
                merged.append(f"changes.{c.field}()")
            else:
                merged.append(f"current.{c.field}()")
        update = f"""
    @Override
    public {t.cls} update(final {t.cls} changes) {{
        final {t.cls} current = get({pk_args(t, "changes")});
        return repository.save(new {t.cls}(
{lines(merged, 16)}));
    }}
"""
    return f"""package {pkg}.application.usecase;

import {pkg}.application.port.in.{t.cls}UseCase;
import {pkg}.application.port.out.{t.cls}RepositoryPort;
{exists_import}import {pkg}.domain.exception.ResourceNotFoundException;
import {pkg}.domain.model.PageResult;
import {pkg}.domain.model.{t.cls};

/**
 * Casos de uso CRUD de {{@code {t.name}}}.
 */
public final class {t.cls}Service implements {t.cls}UseCase {{

    private static final int MAX_SIZE = 100;

    private final {t.cls}RepositoryPort repository;

    public {t.cls}Service(final {t.cls}RepositoryPort repository) {{
        this.repository = repository;
    }}

    @Override
    public {t.cls} create(final {t.cls} value) {{
{duplicate}        return repository.save(value);
    }}

    @Override
    public {t.cls} get({pk_params(t)}) {{
        return repository.findById({pk_args(t)})
                .orElseThrow(() -> new ResourceNotFoundException("{t.name} no encontrado"));
    }}

    @Override
    public PageResult<{t.cls}> list(final java.util.Map<String, Object> filters, final int page, final int size) {{
        final int safePage = Math.max(page, 0);
        final int safeSize = Math.min(Math.max(size, 1), MAX_SIZE);
        return new PageResult<>(repository.findAll(filters, safePage, safeSize), repository.count(filters),
                safePage, safeSize);
    }}
{update}
    @Override
    public void delete({pk_params(t)}) {{
        if (!repository.existsById({pk_args(t)})) {{
            throw new ResourceNotFoundException("{t.name} no encontrado");
        }}
        repository.deleteById({pk_args(t)});
    }}
}}
"""


# --------------------------------------------------------------------------------------------
# Adaptador web
# --------------------------------------------------------------------------------------------

def _dto_component(c: Column, validate: bool) -> str:
    annotations = ""
    if validate and c.required:
        annotations += "@jakarta.validation.constraints.NotNull "
    if validate and c.length and not c.is_array:
        annotations += f"@jakarta.validation.constraints.Size(max = {c.length}) "
    return f"{annotations}{dto_type(c)} {c.field}"


def dto(pkg: str, t: Table, kind: str) -> str:
    if kind == "Response":
        columns, validate, doc = t.columns, False, "Respuesta"
    elif kind == "Request":
        columns, validate, doc = t.create_columns, True, "Cuerpo del alta"
    else:
        columns, validate, doc = t.update_columns, True, "Cuerpo de la modificacion"
    components = lines([_dto_component(c, validate) for c in columns], 8)
    return f"""package {pkg}.infrastructure.adapter.in.web.dto;

/**
 * {doc} de {{@code {t.name}}}.
 */
public record {t.cls}{kind}(
{components}) {{
}}
"""


def web_mapper(pkg: str, t: Table) -> str:
    def from_request(c: Column, source: str) -> str:
        return f"JsonValues.toText({source}.{c.field}())" if c.is_json else f"{source}.{c.field}()"

    create_args = [from_request(c, "request") if c in t.create_columns else "null" for c in t.columns]
    response_args = [f"JsonValues.toNode(value.{c.field}())" if c.is_json else f"value.{c.field}()"
                     for c in t.columns]
    update_method = ""
    update_import = ""
    if t.update_columns:
        update_import = f"import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}UpdateRequest;\n"
        update_args = []
        for c in t.columns:
            if c.pk:
                update_args.append(c.field)
            elif c in t.update_columns:
                update_args.append(from_request(c, "request"))
            else:
                update_args.append("null")
        update_method = f"""
    static {t.cls} toDomain({pk_params(t)}, final {t.cls}UpdateRequest request) {{
        return new {t.cls}(
{lines(update_args, 16)});
    }}
"""
    return f"""package {pkg}.infrastructure.adapter.in.web;

import {pkg}.domain.model.{t.cls};
import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Request;
import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Response;
{update_import}
/**
 * Conversion entre los DTO web y el dominio de {{@code {t.name}}}.
 */
final class {t.cls}WebMapper {{

    private {t.cls}WebMapper() {{
    }}

    static {t.cls} toDomain(final {t.cls}Request request) {{
        return new {t.cls}(
{lines(create_args, 16)});
    }}
{update_method}
    static {t.cls}Response toResponse(final {t.cls} value) {{
        return new {t.cls}Response(
{lines(response_args, 16)});
    }}
}}
"""


def controller(pkg: str, t: Table) -> str:
    item = "".join("/{" + c.field + "}" for c in t.pk_columns)
    path_vars = ",\n".join(
        f'            @PathVariable("{c.field}") final {c.java} {c.field}' for c in t.pk_columns)
    filter_params = "".join(
        f'            @RequestParam(name = "{c.field}", required = false) final {c.java} {c.field},\n'
        for c in t.filter_columns)
    filter_puts = "".join(
        f"""        if ({c.field} != null) {{
            filters.put("{c.field}", {c.field});
        }}
""" for c in t.filter_columns)
    update = ""
    update_imports = ""
    if t.update_columns:
        update_imports = (f"import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}UpdateRequest;\n"
                          "import org.springframework.web.bind.annotation.PutMapping;\n")
        update = f"""
    @PutMapping("{item}")
    public {t.cls}Response update(
{path_vars},
            @Valid @RequestBody final {t.cls}UpdateRequest request) {{
        return {t.cls}WebMapper.toResponse(useCase.update({t.cls}WebMapper.toDomain({pk_args(t)}, request)));
    }}
"""
    imports = sorted([
        f"import {pkg}.application.port.in.{t.cls}UseCase;",
        f"import {pkg}.domain.model.PageResult;",
        f"import {pkg}.domain.model.{t.cls};",
        f"import {pkg}.infrastructure.adapter.in.web.dto.PageResponse;",
        f"import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Request;",
        f"import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Response;",
    ] + [line for line in update_imports.splitlines() if pkg in line])
    spring_put = "import org.springframework.web.bind.annotation.PutMapping;\n" if t.update_columns else ""
    return f"""package {pkg}.infrastructure.adapter.in.web;

{chr(10).join(imports)}
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
{spring_put}import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de {{@code {t.name}}}.
 */
@RestController
@RequestMapping("{t.path}")
public class {t.cls}Controller {{

    private final {t.cls}UseCase useCase;

    public {t.cls}Controller(final {t.cls}UseCase useCase) {{
        this.useCase = useCase;
    }}

    @PostMapping
    public ResponseEntity<{t.cls}Response> create(@Valid @RequestBody final {t.cls}Request request) {{
        final {t.cls} created = useCase.create({t.cls}WebMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body({t.cls}WebMapper.toResponse(created));
    }}

    @GetMapping
    public PageResponse<{t.cls}Response> list(
{filter_params}            @RequestParam(name = "page", defaultValue = "0") final int page,
            @RequestParam(name = "size", defaultValue = "20") final int size) {{
        final java.util.Map<String, Object> filters = new java.util.LinkedHashMap<>();
{filter_puts}        final PageResult<{t.cls}> result = useCase.list(filters, page, size);
        return new PageResponse<>(result.items().stream().map({t.cls}WebMapper::toResponse).toList(),
                result.total(), result.page(), result.size());
    }}

    @GetMapping("{item}")
    public {t.cls}Response get(
{path_vars}) {{
        return {t.cls}WebMapper.toResponse(useCase.get({pk_args(t)}));
    }}
{update}
    @DeleteMapping("{item}")
    public ResponseEntity<Void> delete(
{path_vars}) {{
        useCase.delete({pk_args(t)});
        return ResponseEntity.noContent().build();
    }}
}}
"""


# --------------------------------------------------------------------------------------------
# Adaptador de persistencia
# --------------------------------------------------------------------------------------------

def _accessor(c: Column) -> str:
    return c.field[:1].upper() + c.field[1:]


def entity(pkg: str, t: Table) -> str:
    composite = len(t.pk) > 1
    fields = []
    for c in t.columns:
        annotations = []
        if c.pk:
            annotations.append("@Id")
            if c.generated:
                strategy = "UUID" if c.base == "uuid" else "IDENTITY"
                annotations.append(f"@GeneratedValue(strategy = GenerationType.{strategy})")
        if c.is_array:
            annotations.append("@JdbcTypeCode(SqlTypes.ARRAY)")
        elif c.is_json:
            annotations.append("@JdbcTypeCode(SqlTypes.JSON)")
        extra = ", insertable = false, updatable = false" if c.stored else ""
        annotations.append(f'@Column(name = "{c.name}"{extra})')
        fields.append("".join(f"    {a}\n" for a in annotations) + f"    private {c.java} {c.field};\n")
    accessors = "".join(f"""
    public {c.java} get{_accessor(c)}() {{
        return {c.field};
    }}

    public void set{_accessor(c)}(final {c.java} {c.field}) {{
        this.{c.field} = {c.field};
    }}
""" for c in t.columns)
    uses_jdbc_type = any(c.is_array or c.is_json for c in t.columns)
    imports = ["import jakarta.persistence.Column;", "import jakarta.persistence.Entity;",
               "import jakarta.persistence.Id;", "import jakarta.persistence.Table;",
               "import org.hibernate.annotations.DynamicInsert;"]
    if t.generated_key:
        imports += ["import jakarta.persistence.GeneratedValue;", "import jakarta.persistence.GenerationType;"]
    if composite:
        imports.append("import jakarta.persistence.IdClass;")
    if uses_jdbc_type:
        imports += ["import org.hibernate.annotations.JdbcTypeCode;", "import org.hibernate.type.SqlTypes;"]
    id_class = f"@IdClass({t.cls}EntityId.class)\n" if composite else ""
    return f"""package {pkg}.infrastructure.adapter.out.persistence;

{chr(10).join(sorted(imports))}

/**
 * Entidad JPA de la tabla {{@code {t.name}}}.
 */
@Entity
@Table(name = "{t.name}")
@DynamicInsert
{id_class}public class {t.cls}Entity {{

{chr(10).join(fields)}{accessors}}}
"""


def entity_id(pkg: str, t: Table) -> str:
    keys = t.pk_columns
    fields = "".join(f"    private {c.java} {c.field};\n" for c in keys)
    assigns = "".join(f"        this.{c.field} = {c.field};\n" for c in keys)
    equals = "\n                && ".join(f"java.util.Objects.equals({c.field}, that.{c.field})" for c in keys)
    return f"""package {pkg}.infrastructure.adapter.out.persistence;

/**
 * Clave primaria compuesta de {{@code {t.name}}}.
 */
public class {t.cls}EntityId implements java.io.Serializable {{

    private static final long serialVersionUID = 1L;

{fields}
    public {t.cls}EntityId() {{
    }}

    public {t.cls}EntityId({pk_params(t)}) {{
{assigns}    }}

    @Override
    public boolean equals(final Object other) {{
        if (this == other) {{
            return true;
        }}
        if (!(other instanceof {t.cls}EntityId)) {{
            return false;
        }}
        final {t.cls}EntityId that = ({t.cls}EntityId) other;
        return {equals};
    }}

    @Override
    public int hashCode() {{
        return java.util.Objects.hash({pk_args(t)});
    }}
}}
"""


def jpa_repository(pkg: str, t: Table) -> str:
    return f"""package {pkg}.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio Spring Data de {{@code {t.name}}}.
 */
public interface {t.cls}JpaRepository
        extends JpaRepository<{t.cls}Entity, {id_type(t)}>, JpaSpecificationExecutor<{t.cls}Entity> {{
}}
"""


def persistence_adapter(pkg: str, t: Table) -> str:
    key = pk_args(t) if len(t.pk) == 1 else f"new {t.cls}EntityId({pk_args(t)})"
    setters = "".join(f"        entity.set{_accessor(c)}(value.{c.field}());\n" for c in t.columns)
    getters = lines([f"entity.get{_accessor(c)}()" for c in t.columns], 16)
    sort = ", ".join(f'"{c.field}"' for c in t.pk_columns)
    return f"""package {pkg}.infrastructure.adapter.out.persistence;

import {pkg}.application.port.out.{t.cls}RepositoryPort;
import {pkg}.domain.model.{t.cls};
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador de salida: implementa el puerto de {{@code {t.name}}} con Spring Data JPA.
 */
@Component
public class {t.cls}PersistenceAdapter implements {t.cls}RepositoryPort {{

    private final {t.cls}JpaRepository repository;
    private final EntityManager entityManager;

    public {t.cls}PersistenceAdapter(final {t.cls}JpaRepository repository, final EntityManager entityManager) {{
        this.repository = repository;
        this.entityManager = entityManager;
    }}

    @Override
    @Transactional
    public {t.cls} save(final {t.cls} value) {{
        final {t.cls}Entity saved = repository.saveAndFlush(toEntity(value));
        entityManager.refresh(saved);
        return toDomain(saved);
    }}

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<{t.cls}> findById({pk_params(t)}) {{
        return repository.findById({key}).map({t.cls}PersistenceAdapter::toDomain);
    }}

    @Override
    @Transactional(readOnly = true)
    public boolean existsById({pk_params(t)}) {{
        return repository.existsById({key});
    }}

    @Override
    @Transactional
    public void deleteById({pk_params(t)}) {{
        repository.deleteById({key});
    }}

    @Override
    @Transactional(readOnly = true)
    public java.util.List<{t.cls}> findAll(final java.util.Map<String, Object> filters, final int page,
            final int size) {{
        return repository.findAll(FilterSpecification.of(filters), PageRequest.of(page, size, Sort.by({sort})))
                .map({t.cls}PersistenceAdapter::toDomain).getContent();
    }}

    @Override
    @Transactional(readOnly = true)
    public long count(final java.util.Map<String, Object> filters) {{
        return repository.count(FilterSpecification.of(filters));
    }}

    private static {t.cls}Entity toEntity(final {t.cls} value) {{
        final {t.cls}Entity entity = new {t.cls}Entity();
{setters}        return entity;
    }}

    private static {t.cls} toDomain(final {t.cls}Entity entity) {{
        return new {t.cls}(
{getters});
    }}
}}
"""


def bean_configuration(pkg: str, s: Service) -> str:
    imports = sorted(
        [f"import {pkg}.application.port.in.{t.cls}UseCase;" for t in s.tables]
        + [f"import {pkg}.application.port.out.{t.cls}RepositoryPort;" for t in s.tables]
        + [f"import {pkg}.application.usecase.{t.cls}Service;" for t in s.tables])
    beans = "".join(f"""
    @Bean
    public {t.cls}UseCase {t.var}UseCase(final {t.cls}RepositoryPort repository) {{
        return new {t.cls}Service(repository);
    }}
""" for t in s.tables)
    return f"""package {pkg}.infrastructure.config;

{chr(10).join(imports)}
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado de los casos de uso: la capa de aplicacion no conoce Spring.
 */
@Configuration
public class BeanConfiguration {{
{beans}}}
"""


# --------------------------------------------------------------------------------------------
# Pruebas
# --------------------------------------------------------------------------------------------

def _sample(t: Table, variant: int = 0) -> str:
    return f"new {t.cls}(\n" + lines([literal(c, t, variant) for c in t.columns], 16) + ")"


def service_test(pkg: str, t: Table) -> str:
    duplicate_import = ""
    duplicate = ""
    if not t.generated_key:
        duplicate_import = f"import {pkg}.domain.exception.ResourceAlreadyExistsException;\n"
        duplicate = f"""
    @Test
    void createRejectsDuplicate() {{
        when(repository.existsById({pk_literals(t)})).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(sample()));
    }}
"""
    update = ""
    if t.update_columns:
        update = f"""
    @Test
    void updateSavesMergedValue() {{
        when(repository.findById({pk_literals(t)})).thenReturn(java.util.Optional.of(sample()));
        when(repository.save(any())).thenReturn(sample());
        assertEquals(sample(), service.update(sample()));
    }}

    @Test
    void updateFailsWhenMissing() {{
        assertThrows(ResourceNotFoundException.class, () -> service.update(sample()));
    }}
"""
    return f"""package {pkg}.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import {pkg}.application.port.out.{t.cls}RepositoryPort;
{duplicate_import}import {pkg}.domain.exception.ResourceNotFoundException;
import {pkg}.domain.model.PageResult;
import {pkg}.domain.model.{t.cls};
import org.junit.jupiter.api.Test;

class {t.cls}ServiceTest {{

    private final {t.cls}RepositoryPort repository = mock({t.cls}RepositoryPort.class);
    private final {t.cls}Service service = new {t.cls}Service(repository);

    private static {t.cls} sample() {{
        return {_sample(t)};
    }}

    @Test
    void createSaves() {{
        when(repository.save(any())).thenReturn(sample());
        assertEquals(sample(), service.create(sample()));
    }}
{duplicate}
    @Test
    void getReturnsExisting() {{
        when(repository.findById({pk_literals(t)})).thenReturn(java.util.Optional.of(sample()));
        assertEquals(sample(), service.get({pk_literals(t)}));
    }}

    @Test
    void getFailsWhenMissing() {{
        assertThrows(ResourceNotFoundException.class, () -> service.get({pk_literals(t)}));
    }}

    @Test
    void listNormalizesPaging() {{
        final java.util.Map<String, Object> filters = java.util.Map.of();
        when(repository.findAll(filters, 0, 100)).thenReturn(java.util.List.of(sample()));
        when(repository.count(filters)).thenReturn(1L);
        final PageResult<{t.cls}> result = service.list(filters, -1, 500);
        assertEquals(1, result.items().size());
        assertEquals(1L, result.total());
        assertEquals(0, result.page());
        assertEquals(100, result.size());
    }}
{update}
    @Test
    void deleteRemovesExisting() {{
        when(repository.existsById({pk_literals(t)})).thenReturn(true);
        service.delete({pk_literals(t)});
        verify(repository).deleteById({pk_literals(t)});
    }}

    @Test
    void deleteFailsWhenMissing() {{
        assertThrows(ResourceNotFoundException.class, () -> service.delete({pk_literals(t)}));
    }}
}}
"""


def controller_test(pkg: str, t: Table) -> str:
    request = f"new {t.cls}Request(" + ", ".join(literal(c, t, 0, dto=True) for c in t.create_columns) + ")"
    filters = "".join(literal(c, t) + ", " for c in t.filter_columns)
    update = ""
    update_import = ""
    if t.update_columns:
        update_import = f"import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}UpdateRequest;\n"
        body = f"new {t.cls}UpdateRequest(" + ", ".join(literal(c, t, 1, dto=True) for c in t.update_columns) + ")"
        update = f"""
    @Test
    void updateReturnsResource() {{
        when(useCase.update(any())).thenReturn(sample());
        final {t.cls}Response response = controller.update({pk_literals(t)},
                {body});
        assertNotNull(response);
    }}
"""
    return f"""package {pkg}.infrastructure.adapter.in.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import {pkg}.application.port.in.{t.cls}UseCase;
import {pkg}.domain.model.PageResult;
import {pkg}.domain.model.{t.cls};
import {pkg}.infrastructure.adapter.in.web.dto.PageResponse;
import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Request;
import {pkg}.infrastructure.adapter.in.web.dto.{t.cls}Response;
{update_import}import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class {t.cls}ControllerTest {{

    private final {t.cls}UseCase useCase = mock({t.cls}UseCase.class);
    private final {t.cls}Controller controller = new {t.cls}Controller(useCase);

    private static {t.cls} sample() {{
        return {_sample(t)};
    }}

    @Test
    void createReturns201() {{
        when(useCase.create(any())).thenReturn(sample());
        final ResponseEntity<{t.cls}Response> response = controller.create(
                {request});
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }}

    @Test
    void listReturnsPage() {{
        when(useCase.list(any(), anyInt(), anyInt()))
                .thenReturn(new PageResult<>(java.util.List.of(sample()), 1L, 0, 20));
        final PageResponse<{t.cls}Response> page = controller.list({filters}0, 20);
        assertEquals(1, page.items().size());
        assertEquals(1L, page.total());
    }}

    @Test
    void getReturnsResource() {{
        when(useCase.get({pk_literals(t)})).thenReturn(sample());
        assertNotNull(controller.get({pk_literals(t)}));
    }}
{update}
    @Test
    void deleteReturns204() {{
        final ResponseEntity<Void> response = controller.delete({pk_literals(t)});
        assertEquals(204, response.getStatusCode().value());
        verify(useCase).delete({pk_literals(t)});
    }}
}}
"""


def java_sources(s: Service) -> dict[str, str]:
    """Ficheros Java por tabla: ruta relativa a la carpeta del servicio -> contenido."""
    pkg = s.package
    main = "src/main/java/" + pkg.replace(".", "/")
    test = "src/test/java/" + pkg.replace(".", "/")
    files = {f"{main}/infrastructure/config/BeanConfiguration.java": bean_configuration(pkg, s)}
    for t in s.tables:
        web = f"{main}/infrastructure/adapter/in/web"
        persistence = f"{main}/infrastructure/adapter/out/persistence"
        files[f"{main}/domain/model/{t.cls}.java"] = domain_record(pkg, t)
        files[f"{main}/application/port/out/{t.cls}RepositoryPort.java"] = repository_port(pkg, t)
        files[f"{main}/application/port/in/{t.cls}UseCase.java"] = use_case(pkg, t)
        files[f"{main}/application/usecase/{t.cls}Service.java"] = service(pkg, t)
        files[f"{web}/dto/{t.cls}Request.java"] = dto(pkg, t, "Request")
        files[f"{web}/dto/{t.cls}Response.java"] = dto(pkg, t, "Response")
        if t.update_columns:
            files[f"{web}/dto/{t.cls}UpdateRequest.java"] = dto(pkg, t, "UpdateRequest")
        files[f"{web}/{t.cls}WebMapper.java"] = web_mapper(pkg, t)
        files[f"{web}/{t.cls}Controller.java"] = controller(pkg, t)
        files[f"{persistence}/{t.cls}Entity.java"] = entity(pkg, t)
        if len(t.pk) > 1:
            files[f"{persistence}/{t.cls}EntityId.java"] = entity_id(pkg, t)
        files[f"{persistence}/{t.cls}JpaRepository.java"] = jpa_repository(pkg, t)
        files[f"{persistence}/{t.cls}PersistenceAdapter.java"] = persistence_adapter(pkg, t)
        files[f"{test}/application/usecase/{t.cls}ServiceTest.java"] = service_test(pkg, t)
        files[f"{test}/infrastructure/adapter/in/web/{t.cls}ControllerTest.java"] = controller_test(pkg, t)
    return files
