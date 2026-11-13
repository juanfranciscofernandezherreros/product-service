-- database: content_db
-- Dominio content: 10 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   articulos.articulos_usuario_id_fkey -> auth.users(id)
--   comentarios_articulos.comentarios_articulos_autor_id_fkey -> auth.users(id)
--   lecturas.lecturas_usuario_id_fkey -> auth.users(id)
--   recursos_guardados.recursos_guardados_usuario_id_fkey -> auth.users(id)

CREATE TABLE categorias_blog (
    slug text NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    orden integer NOT NULL DEFAULT 0,
    CONSTRAINT categorias_blog_pkey PRIMARY KEY (slug)
);

CREATE TABLE articulos (
    id bigint NOT NULL,
    ruta text NOT NULL,
    slug text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    html text NOT NULL,
    url_publica text NOT NULL,
    origen text NOT NULL DEFAULT 'github'::text,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    categoria_slug text,
    fecha_publicacion date,
    nivel text,
    contenido_texto text,
    palabras integer,
    es_articulo_indice boolean NOT NULL DEFAULT false,
    usuario_id uuid,
    CONSTRAINT articulos_ruta_formato CHECK ((ruta ~ '^post/[a-z0-9-]+[.]html$'::text)),
    CONSTRAINT articulos_pkey PRIMARY KEY (id),
    CONSTRAINT articulos_ruta_key UNIQUE (ruta),
    CONSTRAINT articulos_slug_key UNIQUE (slug),
    CONSTRAINT articulos_categoria_slug_fkey FOREIGN KEY (categoria_slug) REFERENCES categorias_blog(slug)
);

CREATE TABLE comentarios_articulos (
    id bigint NOT NULL,
    articulo_slug text NOT NULL,
    padre_id bigint,
    autor_id uuid,
    contenido text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    eliminado boolean NOT NULL DEFAULT false,
    oculto boolean NOT NULL DEFAULT false,
    CONSTRAINT comentario_texto_valido CHECK (((char_length(btrim(contenido)) >= 1) AND (char_length(btrim(contenido)) <= 2000))),
    CONSTRAINT comentario_no_auto_respuesta CHECK (((padre_id IS NULL) OR (padre_id <> id))),
    CONSTRAINT comentarios_articulos_pkey PRIMARY KEY (id),
    CONSTRAINT comentarios_articulos_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON DELETE CASCADE,
    CONSTRAINT comentarios_articulos_padre_id_fkey FOREIGN KEY (padre_id) REFERENCES comentarios_articulos(id) ON DELETE RESTRICT
);

CREATE TABLE colecciones_material (
    id bigint NOT NULL,
    slug text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    tipo text NOT NULL,
    incluir_todos_articulos boolean NOT NULL DEFAULT false,
    nombre_archivo text NOT NULL,
    fecha_publicacion date,
    publicado boolean NOT NULL DEFAULT true,
    CONSTRAINT colecciones_material_tipo_check CHECK ((tipo = ANY (ARRAY['articulos'::text, 'vocabulario'::text]))),
    CONSTRAINT coleccion_nombre_pdf CHECK ((nombre_archivo ~ '^[a-zA-Z0-9_-]+[.]pdf$'::text)),
    CONSTRAINT colecciones_material_pkey PRIMARY KEY (id),
    CONSTRAINT colecciones_material_slug_key UNIQUE (slug),
    CONSTRAINT colecciones_material_nombre_archivo_key UNIQUE (nombre_archivo)
);

CREATE TABLE coleccion_articulos (
    coleccion_id bigint NOT NULL,
    articulo_slug text NOT NULL,
    orden integer NOT NULL DEFAULT 100,
    CONSTRAINT coleccion_articulos_pkey PRIMARY KEY (coleccion_id, articulo_slug),
    CONSTRAINT coleccion_articulos_coleccion_id_fkey FOREIGN KEY (coleccion_id) REFERENCES colecciones_material(id) ON DELETE CASCADE,
    CONSTRAINT coleccion_articulos_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE paginas_blog (
    ruta text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    html text NOT NULL,
    url_publica text NOT NULL,
    actualizado_en timestamp with time zone DEFAULT now(),
    CONSTRAINT paginas_blog_pkey PRIMARY KEY (ruta)
);

CREATE TABLE recursos_descargables (
    id bigint NOT NULL,
    slug text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    categoria text NOT NULL DEFAULT 'apuntes'::text,
    fecha_publicacion date,
    url_archivo text NOT NULL,
    url_lectura text,
    orden integer NOT NULL DEFAULT 100,
    publicado boolean NOT NULL DEFAULT true,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT recurso_url_no_vacia CHECK ((length(TRIM(BOTH FROM url_archivo)) > 0)),
    CONSTRAINT recursos_descargables_pkey PRIMARY KEY (id),
    CONSTRAINT recursos_descargables_slug_key UNIQUE (slug)
);

CREATE TABLE movie_subtitle_phrases (
    id bigint NOT NULL,
    movie_slug text NOT NULL,
    movie_title text NOT NULL,
    subtitle_number integer NOT NULL,
    timecode text NOT NULL,
    phrase text NOT NULL,
    tense_labels text NOT NULL DEFAULT ''::text,
    notes text NOT NULL DEFAULT ''::text,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT movie_subtitle_phrases_subtitle_number_positive CHECK ((subtitle_number > 0)),
    CONSTRAINT movie_subtitle_phrases_pkey PRIMARY KEY (id),
    CONSTRAINT movie_subtitle_phrases_movie_subtitle_unique UNIQUE (movie_slug, subtitle_number)
);

CREATE TABLE lecturas (
    usuario_id uuid NOT NULL,
    articulo_url text NOT NULL,
    leido_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT lecturas_articulo_url_check CHECK (((length(articulo_url) <= 240) AND (articulo_url ~ '^post/[a-zA-Z0-9_./-]+[.]html$'::text) AND (articulo_url !~ '[.][.]'::text))),
    CONSTRAINT lecturas_pkey PRIMARY KEY (usuario_id, articulo_url)
);

CREATE TABLE recursos_guardados (
    usuario_id uuid NOT NULL,
    tipo text NOT NULL,
    referencia text NOT NULL,
    guardado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT recursos_guardados_tipo_check CHECK ((tipo = ANY (ARRAY['articulo'::text, 'unidad'::text]))),
    CONSTRAINT recursos_guardados_referencia_check CHECK ((referencia ~ '^[a-z0-9][a-z0-9-]{1,119}$'::text)),
    CONSTRAINT recursos_guardados_pkey PRIMARY KEY (usuario_id, tipo, referencia)
);
