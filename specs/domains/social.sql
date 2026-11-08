-- database: social_db
-- Dominio Social (perfiles publicos, seguimientos, mensajes, solicitudes). FK a auth.users eliminadas.
-- Las validaciones de patron (alias) se trasladan al dominio (portabilidad H2/Flyway).

CREATE TABLE perfiles_publicos (
    user_id      UUID        PRIMARY KEY,
    alias        VARCHAR(24) NOT NULL,
    creado_en    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    biografia    VARCHAR(180) NOT NULL DEFAULT '',
    avatar_emoji VARCHAR(8)  NOT NULL DEFAULT 'BOOK',
    CONSTRAINT perfiles_publicos_alias_key UNIQUE (alias)
);

CREATE TABLE seguimientos (
    seguidor  UUID NOT NULL,
    seguido   UUID NOT NULL,
    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT seguimientos_pkey PRIMARY KEY (seguidor, seguido),
    CONSTRAINT seguimientos_distinct CHECK (seguidor <> seguido)
);

CREATE TABLE mensajes_privados (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    remitente    UUID NOT NULL,
    destinatario UUID NOT NULL,
    contenido    VARCHAR(2000) NOT NULL,
    creado_en    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT mensajes_distinct CHECK (remitente <> destinatario)
);

CREATE TABLE solicitudes_mensaje (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    remitente       UUID NOT NULL,
    destinatario    UUID NOT NULL,
    mensaje_inicial VARCHAR(2000) NOT NULL,
    estado          VARCHAR(16) NOT NULL DEFAULT 'pendiente',
    creado_en       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    respondido_en   TIMESTAMP WITH TIME ZONE,
    CONSTRAINT solicitudes_distinct CHECK (remitente <> destinatario),
    CONSTRAINT solicitudes_estado_check CHECK (estado IN ('pendiente','aceptada','rechazada')),
    CONSTRAINT solicitudes_unique UNIQUE (remitente, destinatario)
);
