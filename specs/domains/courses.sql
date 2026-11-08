CREATE TABLE cursos (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL,
    descripcion TEXT,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE unidades_curso (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug        VARCHAR(255) NOT NULL UNIQUE,
    titulo      VARCHAR(255) NOT NULL,
    descripcion TEXT,
    nivel       VARCHAR(50)  NOT NULL,
    orden       INTEGER NOT NULL DEFAULT 100,
    publicado   BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE actividades (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug          VARCHAR(255) NOT NULL UNIQUE,
    titulo        VARCHAR(255) NOT NULL,
    descripcion   TEXT,
    destreza      VARCHAR(20) NOT NULL DEFAULT 'use_of_english',
    unidad_id     BIGINT,
    articulo_slug VARCHAR(255),
    orden         INTEGER NOT NULL DEFAULT 1,
    publicado     BOOLEAN NOT NULL DEFAULT FALSE,
    creado_en     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT actividades_destreza_check CHECK (destreza IN
        ('reading','use_of_english','writing','listening','speaking','vocabulary','grammar'))
);

CREATE TABLE actividad_preguntas (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    actividad_id  BIGINT NOT NULL,
    orden         INTEGER NOT NULL,
    tipo          VARCHAR(20) NOT NULL,
    enunciado     TEXT NOT NULL,
    configuracion TEXT NOT NULL DEFAULT '{}',
    medio_tipo    VARCHAR(10),
    medio_url     VARCHAR(500),
    medio_alt     TEXT,
    CONSTRAINT actividad_preguntas_orden_check CHECK (orden > 0),
    CONSTRAINT actividad_preguntas_tipo_check CHECK (tipo IN
        ('seleccion','texto_corto','huecos','transformacion','redaccion')),
    CONSTRAINT actividad_preguntas_medio_tipo_check CHECK (medio_tipo IS NULL OR medio_tipo IN ('audio','imagen')),
    CONSTRAINT actividad_preguntas_unique UNIQUE (actividad_id, orden)
);

CREATE TABLE actividad_intentos (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID NOT NULL,
    actividad_id        BIGINT NOT NULL,
    respuestas          TEXT NOT NULL,
    detalle             TEXT NOT NULL,
    aciertos            INTEGER NOT NULL,
    evaluables          INTEGER NOT NULL,
    pendientes_revision INTEGER NOT NULL,
    fecha               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT actividad_intentos_aciertos_check CHECK (aciertos >= 0),
    CONSTRAINT actividad_intentos_evaluables_check CHECK (evaluables >= 0),
    CONSTRAINT actividad_intentos_pendientes_check CHECK (pendientes_revision >= 0)
);
