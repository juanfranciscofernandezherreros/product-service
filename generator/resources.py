"""Ficheros no Java del servicio: build, configuracion, migraciones y features de Cucumber."""
from __future__ import annotations

import json
import re
from urllib.parse import quote

from .model import Service, Table

AUTH_STUB = """-- Sustituto local de Supabase Auth: en Supabase, auth.uid() devuelve el usuario del JWT.
CREATE SCHEMA IF NOT EXISTS auth;
CREATE OR REPLACE FUNCTION auth.uid() RETURNS uuid LANGUAGE sql STABLE AS 'SELECT NULL::uuid';

"""


def pom(s: Service) -> str:
    return f"""<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>{s.name}</artifactId>
    <version>1.0.0</version>
    <name>{s.name}</name>
    <description>{s.name}: API CRUD hexagonal generada desde el esquema de base de datos</description>

    <properties>
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <archunit.version>1.3.0</archunit.version>
        <cucumber.version>7.18.1</cucumber.version>
        <embedded-postgres.version>2.0.7</embedded-postgres.version>
        <checkstyle.plugin.version>3.6.0</checkstyle.plugin.version>
        <jacoco.version>0.8.12</jacoco.version>
        <coverage.minimum>0.80</coverage.minimum>
    </properties>

    <dependencies>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>
        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-actuator</artifactId></dependency>
        <dependency><groupId>org.flywaydb</groupId><artifactId>flyway-core</artifactId></dependency>
        <dependency><groupId>org.flywaydb</groupId><artifactId>flyway-database-postgresql</artifactId></dependency>
        <dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>

        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
        <dependency><groupId>io.zonky.test</groupId><artifactId>embedded-postgres</artifactId><version>${{embedded-postgres.version}}</version><scope>test</scope></dependency>
        <dependency><groupId>com.tngtech.archunit</groupId><artifactId>archunit-junit5</artifactId><version>${{archunit.version}}</version><scope>test</scope></dependency>
        <dependency><groupId>io.cucumber</groupId><artifactId>cucumber-java</artifactId><version>${{cucumber.version}}</version><scope>test</scope></dependency>
        <dependency><groupId>io.cucumber</groupId><artifactId>cucumber-spring</artifactId><version>${{cucumber.version}}</version><scope>test</scope></dependency>
        <dependency><groupId>io.cucumber</groupId><artifactId>cucumber-junit-platform-engine</artifactId><version>${{cucumber.version}}</version><scope>test</scope></dependency>
        <dependency><groupId>org.junit.platform</groupId><artifactId>junit-platform-suite</artifactId><scope>test</scope></dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin><groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId></plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>${{checkstyle.plugin.version}}</version>
                <configuration>
                    <configLocation>${{project.basedir}}/../checkstyle.xml</configLocation>
                    <consoleOutput>true</consoleOutput>
                    <failsOnError>true</failsOnError>
                    <violationSeverity>error</violationSeverity>
                    <includeTestSourceDirectory>true</includeTestSourceDirectory>
                </configuration>
                <executions><execution><id>checkstyle-validation</id><phase>validate</phase><goals><goal>check</goal></goals></execution></executions>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${{jacoco.version}}</version>
                <configuration><excludes><exclude>**/{s.app_class}.class</exclude></excludes></configuration>
                <executions>
                    <execution><id>jacoco-prepare-agent</id><goals><goal>prepare-agent</goal></goals></execution>
                    <execution><id>jacoco-report</id><phase>verify</phase><goals><goal>report</goal></goals></execution>
                    <execution>
                        <id>jacoco-check</id><phase>verify</phase><goals><goal>check</goal></goals>
                        <configuration><rules><rule><element>BUNDLE</element><limits><limit><counter>LINE</counter><value>COVEREDRATIO</value><minimum>${{coverage.minimum}}</minimum></limit></limits></rule></rules></configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
"""


def application_yml(s: Service, port: int) -> str:
    return f"""spring:
  application:
    name: {s.name}
  profiles:
    active: ${{SPRING_PROFILES_ACTIVE:dev}}
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
  mvc:
    format:
      date: iso
      date-time: iso
      time: iso

server:
  port: {port}

management:
  endpoints:
    web:
      exposure:
        include: health,info
"""


def application_local_yml(s: Service) -> str:
    return f"""# Perfil local: PostgreSQL en Docker (docker compose up -d). Flyway crea las tablas y JPA las valida.
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/{s.database}
    driver-class-name: org.postgresql.Driver
    username: ${{DB_USERNAME:postgres}}
    password: ${{DB_PASSWORD:postgres}}
  jpa:
    show-sql: true
"""


def application_dev_yml(supabase_ref: str | None) -> str:
    host = "${DB_HOST_DEV:db." + supabase_ref + ".supabase.co}" if supabase_ref else "${DB_HOST_DEV}"
    return f"""# Perfil dev: Supabase. El esquema ya existe y lo gestiona Supabase: el servicio ni lo crea ni lo valida.
# La contrasena llega por la variable de entorno DB_PASS_DEV; nunca va en el repositorio.
spring:
  datasource:
    url: jdbc:postgresql://{host}:5432/${{DB_NAME_DEV:postgres}}
    driver-class-name: org.postgresql.Driver
    username: ${{DB_USER_DEV:postgres}}
    password: ${{DB_PASS_DEV}}
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: none
  flyway:
    enabled: false
"""


def application_test_yml() -> str:
    return """# Perfil test: PostgreSQL embebido (ver EmbeddedPostgresConfiguration). Flyway aplica el esquema de
# pruebas (mismas columnas, tipos, PK y UNIQUE que Supabase, sin CHECK ni FK) y JPA lo valida.
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/testmigration
"""


def docker_compose(s: Service) -> str:
    volume = s.database + "_data"
    return f"""services:
  postgres:
    image: postgres:16
    container_name: {s.name}-postgres
    environment:
      POSTGRES_DB: {s.database}
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - {volume}:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d {s.database}"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  {volume}:
"""


def _column_line(t: Table, line: str) -> str:
    """Anade la identidad a las claves enteras que el export antiguo dejo sin GENERATED ... AS IDENTITY."""
    if t.generated_key:
        key = t.pk_columns[0]
        if " ".join(line.split()) == key.raw and key.base != "uuid" and not key.identity and key.default is None:
            return line + " GENERATED BY DEFAULT AS IDENTITY"
    return line


def migration(s: Service, for_tests: bool) -> str:
    """V1__init.sql. En pruebas se omiten CHECK y FK para poder insertar datos de ejemplo genericos."""
    blocks = []
    for t in s.tables:
        kept = []
        for line in t.lines:
            is_constraint = re.match(r"CONSTRAINT\s", line, re.I)
            if for_tests and is_constraint and re.search(r"\s(CHECK|FOREIGN KEY)\b", " ".join(line.split()), re.I):
                continue
            kept.append("    " + (line if is_constraint else _column_line(t, line)))
        blocks.append(f"CREATE TABLE {t.name} (\n" + ",\n".join(kept) + "\n);\n")
    purpose = ("-- Esquema de pruebas: columnas, tipos, PK y UNIQUE de specs/database-spec.sql, sin CHECK ni FK.\n"
               if for_tests else "-- Esquema del servicio: copia de specs/database-spec.sql (fuente de verdad).\n")
    head = "-- Generado por el generador de servicios. No editar a mano.\n" + purpose + "\n"
    return head + (AUTH_STUB if s.uses_auth_uid else "") + "\n".join(blocks)


def _json(value: dict) -> str:
    return json.dumps(value, ensure_ascii=False)


def _literal_item_path(t: Table, variant: int) -> str:
    return t.path + "".join("/" + quote(str(c.sample(t.name, variant)), safe="") for c in t.pk_columns)


def feature(t: Table) -> str:
    def body(payload: dict) -> str:
        return '      """\n      ' + _json(payload) + '\n      """\n'

    item = t.item_path
    missing = _literal_item_path(t, 2)
    filter_query = ""
    if t.filter_columns:
        column = t.filter_columns[0]
        filter_query = f"&{column.field}={{{column.field}}}"
    steps = f"""  Scenario: Ciclo CRUD completo de {t.name}
    When envio POST a "{t.path}" con el cuerpo
{body(t.body(0))}    Then la respuesta tiene estado 201
    And la respuesta contiene el campo "{t.pk_columns[0].field}"
    When envio GET a "{item}"
    Then la respuesta tiene estado 200
    When envio GET a "{t.path}?page=0&size=5{filter_query}"
    Then la respuesta tiene estado 200
    And la respuesta contiene el campo "items"
"""
    if t.update_columns:
        steps += f"""    When envio PUT a "{item}" con el cuerpo
{body(t.body(1, update=True))}    Then la respuesta tiene estado 200
"""
    steps += f"""    When envio DELETE a "{item}"
    Then la respuesta tiene estado 204
    When envio GET a "{item}"
    Then la respuesta tiene estado 404
"""
    scenarios = [steps]
    if not t.generated_key:
        scenarios.append(f"""  Scenario: Alta duplicada de {t.name}
    When envio POST a "{t.path}" con el cuerpo
{body(t.body(0))}    Then la respuesta tiene estado 201
    When envio POST a "{t.path}" con el cuerpo
{body(t.body(0))}    Then la respuesta tiene estado 409
    When envio DELETE a "{item}"
    Then la respuesta tiene estado 204
""")
    invalid = f"""  Scenario: Peticiones no validas de {t.name}
    When envio POST a "{t.path}" con el cuerpo
      \"\"\"
      {{ esto no es json
      \"\"\"
    Then la respuesta tiene estado 400
"""
    if any(c.required for c in t.create_columns):
        invalid += f"""    When envio POST a "{t.path}" con el cuerpo
      \"\"\"
      {{}}
      \"\"\"
    Then la respuesta tiene estado 400
"""
    scenarios.append(invalid)
    absent = f"""  Scenario: Recurso inexistente de {t.name}
    When envio GET a "{missing}"
    Then la respuesta tiene estado 404
    When envio DELETE a "{missing}"
    Then la respuesta tiene estado 404
"""
    if t.update_columns:
        absent += f"""    When envio PUT a "{missing}" con el cuerpo
{body(t.body(1, update=True))}    Then la respuesta tiene estado 404
"""
    scenarios.append(absent)
    return f"Feature: CRUD de {t.name} ({t.path})\n\n" + "\n".join(scenarios)


def resource_files(s: Service, port: int, supabase_ref: str | None = None) -> dict[str, str]:
    files = {
        "pom.xml": pom(s),
        "docker-compose.yml": docker_compose(s),
        "src/main/resources/application.yml": application_yml(s, port),
        "src/main/resources/application-local.yml": application_local_yml(s),
        "src/main/resources/application-dev.yml": application_dev_yml(supabase_ref),
        "src/main/resources/db/migration/V1__init.sql": migration(s, for_tests=False),
        "src/test/resources/application-test.yml": application_test_yml(),
        "src/test/resources/db/testmigration/V1__init.sql": migration(s, for_tests=True),
    }
    for t in s.tables:
        files[f"src/test/resources/features/{t.name}.feature"] = feature(t)
    return files
