-- database: misc_db
-- Dominio misc: 4 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   bitacora_categorias.bitacora_categorias_usuario_id_fkey -> auth.users(id)
--   bitacora_anotaciones.bitacora_anotaciones_usuario_id_fkey -> auth.users(id)
--   writing_borradores.writing_borradores_usuario_id_fkey -> auth.users(id)

CREATE TABLE sugerencias (
    id bigint NOT NULL,
    titulo text NOT NULL,
    descripcion text NOT NULL,
    estado text NOT NULL DEFAULT 'pendiente'::text,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT sugerencias_titulo_check CHECK (((char_length(btrim(titulo)) >= 5) AND (char_length(btrim(titulo)) <= 100))),
    CONSTRAINT sugerencias_descripcion_check CHECK (((char_length(btrim(descripcion)) >= 10) AND (char_length(btrim(descripcion)) <= 1000))),
    CONSTRAINT sugerencias_estado_check CHECK ((estado = ANY (ARRAY['pendiente'::text, 'publicada'::text, 'rechazada'::text]))),
    CONSTRAINT sugerencias_pkey PRIMARY KEY (id)
);

CREATE TABLE bitacora_categorias (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    usuario_id uuid NOT NULL,
    nombre text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT bitacora_categorias_nombre_check CHECK (((char_length(btrim(nombre)) >= 1) AND (char_length(btrim(nombre)) <= 60))),
    CONSTRAINT bitacora_categorias_pkey PRIMARY KEY (id),
    CONSTRAINT bitacora_categorias_id_usuario_id_key UNIQUE (id, usuario_id)
);

CREATE TABLE bitacora_anotaciones (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    usuario_id uuid NOT NULL,
    categoria_id uuid,
    contenido text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT bitacora_anotaciones_contenido_check CHECK (((char_length(btrim(contenido)) >= 1) AND (char_length(btrim(contenido)) <= 10000))),
    CONSTRAINT bitacora_anotaciones_pkey PRIMARY KEY (id),
    CONSTRAINT bitacora_categoria_mismo_usuario FOREIGN KEY (categoria_id, usuario_id) REFERENCES bitacora_categorias(id, usuario_id) ON UPDATE RESTRICT ON DELETE SET NULL (categoria_id)
);

CREATE TABLE writing_borradores (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    usuario_id uuid NOT NULL,
    tipo text NOT NULL,
    titulo text NOT NULL DEFAULT 'Mi writing B2'::text,
    contenido text NOT NULL DEFAULT ''::text,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT writing_borradores_tipo_check CHECK ((tipo = ANY (ARRAY['essay'::text, 'article'::text, 'review'::text, 'email_informal'::text, 'email_formal'::text, 'report'::text, 'story'::text]))),
    CONSTRAINT writing_borradores_pkey PRIMARY KEY (id)
);
