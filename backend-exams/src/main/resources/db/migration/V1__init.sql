CREATE TABLE examenes (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo        VARCHAR(64)  NOT NULL,
    titulo        VARCHAR(255) NOT NULL,
    fecha         DATE,
    nivel         VARCHAR(16)  NOT NULL DEFAULT 'B1-B2',
    descripcion   VARCHAR(2000),
    publicado     BOOLEAN      NOT NULL DEFAULT false,
    articulo_slug VARCHAR(255),
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT examenes_codigo_key UNIQUE (codigo)
);

CREATE TABLE preguntas (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    examen_id  BIGINT NOT NULL REFERENCES examenes(id) ON DELETE CASCADE,
    orden      INTEGER NOT NULL,
    enunciado  VARCHAR(2000) NOT NULL,
    opciones   VARCHAR(4000) NOT NULL,
    fuente_url VARCHAR(500),
    CONSTRAINT preguntas_orden_check CHECK (orden > 0),
    CONSTRAINT preguntas_examen_orden_key UNIQUE (examen_id, orden)
);

CREATE TABLE resultados (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id UUID   NOT NULL,
    examen_id  BIGINT NOT NULL REFERENCES examenes(id) ON DELETE CASCADE,
    respuestas VARCHAR(8000) NOT NULL,
    aciertos   INTEGER NOT NULL,
    total      INTEGER NOT NULL,
    nota       NUMERIC(5,2) NOT NULL,
    fecha      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT resultados_total_check CHECK (total > 0),
    CONSTRAINT resultados_nota_check CHECK (nota >= 0 AND nota <= 100)
);

CREATE TABLE diagnosticos_b2 (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id UUID    NOT NULL,
    aciertos   INTEGER NOT NULL,
    total      INTEGER NOT NULL,
    desglose   VARCHAR(8000) NOT NULL DEFAULT '{}',
    creado_en  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT diagnosticos_aciertos_check CHECK (aciertos >= 0 AND aciertos <= 40),
    CONSTRAINT diagnosticos_total_check CHECK (total >= 1 AND total <= 40)
);

CREATE TABLE simulacros_b2 (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id        UUID    NOT NULL,
    modalidad         VARCHAR(32) NOT NULL,
    aciertos          INTEGER NOT NULL,
    total             INTEGER NOT NULL,
    respuestas        VARCHAR(8000) NOT NULL DEFAULT '{}',
    duracion_segundos INTEGER NOT NULL DEFAULT 0,
    creado_en         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT simulacros_modalidad_check CHECK (modalidad IN ('use_of_english','listening')),
    CONSTRAINT simulacros_aciertos_check CHECK (aciertos >= 0 AND aciertos <= 40),
    CONSTRAINT simulacros_total_check CHECK (total >= 1 AND total <= 40),
    CONSTRAINT simulacros_duracion_check CHECK (duracion_segundos >= 0 AND duracion_segundos <= 7200)
);
