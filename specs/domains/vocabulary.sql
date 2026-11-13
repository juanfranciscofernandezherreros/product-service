-- database: vocabulary_db
-- Dominio vocabulary: 4 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   vocabulario.vocabulario_articulo_slug_fkey -> articulos(slug)
--   vocabulario_progreso.vocabulario_progreso_user_id_fkey -> auth.users(id)
--   vocabulario_preferencias.vocabulario_preferencias_user_id_fkey -> auth.users(id)

CREATE TABLE vocabulario (
    id bigint NOT NULL,
    termino_en text NOT NULL,
    traduccion_es text NOT NULL,
    categoria text NOT NULL,
    ejemplo_en text,
    nivel text,
    articulo_slug text,
    orden integer NOT NULL DEFAULT 100,
    publicado boolean NOT NULL DEFAULT true,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT vocabulario_pkey PRIMARY KEY (id),
    CONSTRAINT vocabulario_termino_categoria_unicos UNIQUE (termino_en, categoria)
);

CREATE TABLE vocabulario_items (
    id bigint NOT NULL,
    en text NOT NULL,
    es text NOT NULL,
    example text,
    level text NOT NULL,
    CONSTRAINT vocabulario_items_level_check CHECK ((level = ANY (ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text, 'C1'::text]))),
    CONSTRAINT vocabulario_items_pkey PRIMARY KEY (id),
    CONSTRAINT vocabulario_items_en_key UNIQUE (en)
);

CREATE TABLE vocabulario_progreso (
    user_id uuid NOT NULL,
    item_id bigint NOT NULL,
    streak integer NOT NULL DEFAULT 0,
    last_grade integer NOT NULL DEFAULT 0,
    next_review_at timestamp with time zone NOT NULL DEFAULT now(),
    first_seen_day date NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT vocabulario_progreso_streak_check CHECK ((streak >= 0)),
    CONSTRAINT vocabulario_progreso_last_grade_check CHECK (((last_grade >= 0) AND (last_grade <= 2))),
    CONSTRAINT vocabulario_progreso_pkey PRIMARY KEY (user_id, item_id),
    CONSTRAINT vocabulario_progreso_item_id_fkey FOREIGN KEY (item_id) REFERENCES vocabulario_items(id) ON DELETE CASCADE
);

CREATE TABLE vocabulario_preferencias (
    user_id uuid NOT NULL,
    new_per_day integer NOT NULL DEFAULT 10,
    levels text[] NOT NULL DEFAULT ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text, 'C1'::text],
    priority text NOT NULL DEFAULT 'B2'::text,
    direction text NOT NULL DEFAULT 'en-es'::text,
    CONSTRAINT vocabulario_preferencias_new_per_day_check CHECK (((new_per_day >= 5) AND (new_per_day <= 50))),
    CONSTRAINT vocabulario_preferencias_priority_check CHECK ((priority = ANY (ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text, 'C1'::text]))),
    CONSTRAINT vocabulario_preferencias_direction_check CHECK ((direction = ANY (ARRAY['en-es'::text, 'es-en'::text, 'mixed'::text]))),
    CONSTRAINT valid_levels CHECK ((((array_length(levels, 1) >= 1) AND (array_length(levels, 1) <= 5)) AND (levels <@ ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text, 'C1'::text]))),
    CONSTRAINT vocabulario_preferencias_pkey PRIMARY KEY (user_id)
);
