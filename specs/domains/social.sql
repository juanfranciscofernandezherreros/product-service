-- database: social_db
-- Dominio social: 6 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   perfiles.perfiles_usuario_id_fkey -> auth.users(id)
--   perfiles_publicos.perfiles_publicos_usuario_id_fkey -> auth.users(id)
--   seguimientos.seguimientos_seguidor_fkey -> auth.users(id)
--   mensajes_privados.mensajes_privados_remitente_fkey -> auth.users(id)
--   mensajes_privados.mensajes_privados_destinatario_fkey -> auth.users(id)
--   solicitudes_mensaje.solicitudes_mensaje_remitente_fkey -> auth.users(id)
--   mensajes_vistos.mensajes_vistos_usuario_id_fkey -> auth.users(id)

CREATE TABLE perfiles (
    usuario_id uuid NOT NULL,
    nombre text NOT NULL DEFAULT ''::text,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT perfiles_nombre_check CHECK ((char_length(nombre) <= 50)),
    CONSTRAINT perfiles_pkey PRIMARY KEY (usuario_id)
);

CREATE TABLE perfiles_publicos (
    usuario_id uuid NOT NULL,
    alias text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    biografia text NOT NULL DEFAULT ''::text,
    avatar_emoji text NOT NULL DEFAULT '📘'::text,
    CONSTRAINT alias_formato CHECK ((alias ~ '^[a-z0-9_]{3,24}$'::text)),
    CONSTRAINT perfiles_publicos_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT perfiles_publicos_alias_key UNIQUE (alias),
    CONSTRAINT perfiles_publicos_biografia_check CHECK ((char_length(biografia) <= 180)),
    CONSTRAINT perfiles_publicos_avatar_emoji_check CHECK ((avatar_emoji = ANY (ARRAY['📘'::text, '🎓'::text, '⭐'::text, '🧠'::text, '🌍'::text, '🚀'::text, '📚'::text])))
);

CREATE TABLE seguimientos (
    seguidor uuid NOT NULL,
    seguido uuid NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT distinto_usuario CHECK ((seguidor <> seguido)),
    CONSTRAINT seguimientos_pkey PRIMARY KEY (seguidor, seguido),
    CONSTRAINT seguimientos_seguido_fkey FOREIGN KEY (seguido) REFERENCES perfiles_publicos(usuario_id) ON DELETE CASCADE
);

CREATE TABLE mensajes_privados (
    id bigint NOT NULL,
    remitente uuid NOT NULL,
    destinatario uuid NOT NULL,
    contenido text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT mensajes_privados_contenido_check CHECK (((char_length(btrim(contenido)) >= 1) AND (char_length(btrim(contenido)) <= 2000))),
    CONSTRAINT mensajes_usuarios_distintos CHECK ((remitente <> destinatario)),
    CONSTRAINT mensajes_privados_pkey PRIMARY KEY (id)
);

CREATE TABLE solicitudes_mensaje (
    id bigint NOT NULL,
    remitente uuid NOT NULL,
    destinatario uuid NOT NULL,
    mensaje_inicial text NOT NULL,
    estado text NOT NULL DEFAULT 'pendiente'::text,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    respondido_en timestamp with time zone,
    CONSTRAINT solicitudes_mensaje_mensaje_inicial_check CHECK (((char_length(btrim(mensaje_inicial)) >= 1) AND (char_length(btrim(mensaje_inicial)) <= 2000))),
    CONSTRAINT solicitudes_mensaje_estado_check CHECK ((estado = ANY (ARRAY['pendiente'::text, 'aceptada'::text, 'rechazada'::text]))),
    CONSTRAINT solicitudes_mensaje_check CHECK ((remitente <> destinatario)),
    CONSTRAINT solicitudes_mensaje_pkey PRIMARY KEY (id),
    CONSTRAINT solicitudes_mensaje_remitente_destinatario_key UNIQUE (remitente, destinatario),
    CONSTRAINT solicitudes_mensaje_destinatario_fkey FOREIGN KEY (destinatario) REFERENCES perfiles_publicos(usuario_id) ON DELETE CASCADE
);

CREATE TABLE mensajes_vistos (
    usuario_id uuid NOT NULL,
    visto_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT mensajes_vistos_pkey PRIMARY KEY (usuario_id)
);
