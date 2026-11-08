CREATE TABLE categorias_blog (
    slug        VARCHAR(255) PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL,
    descripcion TEXT,
    orden       INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE articulos (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ruta               VARCHAR(255) NOT NULL UNIQUE,
    slug               VARCHAR(255) NOT NULL UNIQUE,
    titulo             VARCHAR(255) NOT NULL,
    descripcion        TEXT,
    html               TEXT NOT NULL,
    url_publica        VARCHAR(500) NOT NULL,
    origen             VARCHAR(50)  NOT NULL DEFAULT 'github',
    actualizado_en     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    categoria_slug     VARCHAR(255),
    fecha_publicacion  DATE,
    nivel              VARCHAR(50),
    contenido_texto    TEXT,
    palabras           INTEGER,
    es_articulo_indice BOOLEAN NOT NULL DEFAULT FALSE,
    usuario_id         UUID
);

CREATE TABLE comentarios_articulos (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    articulo_slug VARCHAR(255) NOT NULL,
    padre_id      BIGINT,
    autor_id      UUID,
    contenido     TEXT NOT NULL,
    creado_en     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    eliminado     BOOLEAN NOT NULL DEFAULT FALSE,
    oculto        BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE colecciones_material (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug                    VARCHAR(255) NOT NULL UNIQUE,
    titulo                  VARCHAR(255) NOT NULL,
    descripcion             TEXT,
    tipo                    VARCHAR(20)  NOT NULL,
    incluir_todos_articulos BOOLEAN NOT NULL DEFAULT FALSE,
    nombre_archivo          VARCHAR(255) NOT NULL UNIQUE,
    fecha_publicacion       DATE,
    publicado               BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT colecciones_tipo_check CHECK (tipo IN ('articulos','vocabulario'))
);

CREATE TABLE coleccion_articulos (
    coleccion_id  BIGINT NOT NULL,
    articulo_slug VARCHAR(255) NOT NULL,
    orden         INTEGER NOT NULL DEFAULT 100,
    CONSTRAINT coleccion_articulos_pkey PRIMARY KEY (coleccion_id, articulo_slug)
);

CREATE TABLE paginas_blog (
    ruta           VARCHAR(255) PRIMARY KEY,
    titulo         VARCHAR(255) NOT NULL,
    descripcion    TEXT,
    html           TEXT NOT NULL,
    url_publica    VARCHAR(500) NOT NULL,
    actualizado_en TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE recursos_descargables (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug              VARCHAR(255) NOT NULL UNIQUE,
    titulo            VARCHAR(255) NOT NULL,
    descripcion       TEXT,
    categoria         VARCHAR(100) NOT NULL DEFAULT 'apuntes',
    fecha_publicacion DATE,
    url_archivo       VARCHAR(500) NOT NULL,
    url_lectura       VARCHAR(500),
    orden             INTEGER NOT NULL DEFAULT 100,
    publicado         BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE movie_subtitle_phrases (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    movie_slug      VARCHAR(255) NOT NULL,
    movie_title     VARCHAR(255) NOT NULL,
    subtitle_number INTEGER NOT NULL,
    timecode        VARCHAR(50)  NOT NULL,
    phrase          TEXT NOT NULL,
    tense_labels    TEXT NOT NULL DEFAULT '',
    notes           TEXT NOT NULL DEFAULT '',
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT movie_phrase_number_positive CHECK (subtitle_number > 0),
    CONSTRAINT movie_phrase_unique UNIQUE (movie_slug, subtitle_number)
);
