CREATE TABLE sugerencias (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo      VARCHAR(100)  NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    estado      VARCHAR(16)   NOT NULL DEFAULT 'pendiente',
    creado_en   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT sugerencias_estado_check CHECK (estado IN ('pendiente','publicada','rechazada'))
);

CREATE TABLE bitacora_categorias (
    id         UUID        PRIMARY KEY,
    usuario_id UUID        NOT NULL,
    nombre     VARCHAR(60) NOT NULL,
    creado_en  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE bitacora_anotaciones (
    id             UUID         PRIMARY KEY,
    usuario_id     UUID         NOT NULL,
    categoria_id   UUID,
    contenido      VARCHAR(10000) NOT NULL,
    creado_en      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE writing_borradores (
    id             UUID        PRIMARY KEY,
    usuario_id     UUID        NOT NULL,
    tipo           VARCHAR(20) NOT NULL,
    titulo         VARCHAR(255) NOT NULL DEFAULT 'Mi writing B2',
    contenido      TEXT        NOT NULL DEFAULT '',
    creado_en      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT writing_tipo_check CHECK (tipo IN ('essay','article','review','email_informal','email_formal','report','story'))
);
