-- database: courses_db
-- Dominio courses: 10 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   actividad_intentos.actividad_intentos_usuario_id_fkey -> auth.users(id)
--   actividad_fallos.actividad_fallos_usuario_id_fkey -> auth.users(id)
--   cursos_profesores.cursos_profesores_profesor_id_fkey -> auth.users(id)
--   asignaciones_alumno.asignaciones_alumno_alumno_id_fkey -> auth.users(id)
--   progreso_curso.progreso_curso_usuario_id_fkey -> auth.users(id)
--   unidades_articulos.unidades_articulos_articulo_slug_fkey -> articulos(slug)

CREATE TABLE cursos (
    id bigint NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    activo boolean NOT NULL DEFAULT true,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT cursos_nombre_check CHECK ((length(TRIM(BOTH FROM nombre)) > 0)),
    CONSTRAINT cursos_pkey PRIMARY KEY (id)
);

CREATE TABLE unidades_curso (
    id bigint NOT NULL,
    slug text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    nivel text NOT NULL,
    orden integer NOT NULL DEFAULT 100,
    publicado boolean NOT NULL DEFAULT true,
    CONSTRAINT unidades_curso_pkey PRIMARY KEY (id),
    CONSTRAINT unidades_curso_slug_key UNIQUE (slug)
);

CREATE TABLE actividades (
    id bigint NOT NULL,
    slug text NOT NULL,
    titulo text NOT NULL,
    descripcion text,
    destreza text NOT NULL DEFAULT 'use_of_english'::text,
    unidad_id bigint,
    articulo_slug text,
    orden integer NOT NULL DEFAULT 1,
    publicado boolean NOT NULL DEFAULT false,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT actividades_slug_check CHECK ((slug ~ '^[a-z0-9][a-z0-9-]{2,99}$'::text)),
    CONSTRAINT actividades_destreza_check CHECK ((destreza = ANY (ARRAY['reading'::text, 'use_of_english'::text, 'writing'::text, 'listening'::text, 'speaking'::text, 'vocabulary'::text, 'grammar'::text]))),
    CONSTRAINT actividades_pkey PRIMARY KEY (id),
    CONSTRAINT actividades_slug_key UNIQUE (slug),
    CONSTRAINT actividades_unidad_id_fkey FOREIGN KEY (unidad_id) REFERENCES unidades_curso(id) ON DELETE SET NULL
);

CREATE TABLE actividad_preguntas (
    id bigint NOT NULL,
    actividad_id bigint NOT NULL,
    orden integer NOT NULL,
    tipo text NOT NULL,
    enunciado text NOT NULL,
    configuracion jsonb NOT NULL DEFAULT '{}'::jsonb,
    medio_tipo text,
    medio_url text,
    medio_alt text,
    CONSTRAINT actividad_preguntas_orden_check CHECK ((orden > 0)),
    CONSTRAINT actividad_preguntas_tipo_check CHECK ((tipo = ANY (ARRAY['seleccion'::text, 'texto_corto'::text, 'huecos'::text, 'transformacion'::text, 'redaccion'::text]))),
    CONSTRAINT actividad_preguntas_configuracion_check CHECK ((jsonb_typeof(configuracion) = 'object'::text)),
    CONSTRAINT actividad_preguntas_medio_tipo_check CHECK ((medio_tipo = ANY (ARRAY['audio'::text, 'imagen'::text]))),
    CONSTRAINT actividad_preguntas_check CHECK ((((medio_tipo IS NULL) AND (medio_url IS NULL)) OR ((medio_tipo IS NOT NULL) AND (medio_url ~ '^https://[^[:space:]]+$'::text)))),
    CONSTRAINT actividad_preguntas_pkey PRIMARY KEY (id),
    CONSTRAINT actividad_preguntas_actividad_id_orden_key UNIQUE (actividad_id, orden),
    CONSTRAINT actividad_preguntas_actividad_id_fkey FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE
);

CREATE TABLE actividad_intentos (
    id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    actividad_id bigint NOT NULL,
    respuestas jsonb NOT NULL,
    detalle jsonb NOT NULL,
    aciertos integer NOT NULL,
    evaluables integer NOT NULL,
    pendientes_revision integer NOT NULL,
    fecha timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT actividad_intentos_respuestas_check CHECK ((jsonb_typeof(respuestas) = 'object'::text)),
    CONSTRAINT actividad_intentos_detalle_check CHECK ((jsonb_typeof(detalle) = 'object'::text)),
    CONSTRAINT actividad_intentos_aciertos_check CHECK ((aciertos >= 0)),
    CONSTRAINT actividad_intentos_evaluables_check CHECK ((evaluables >= 0)),
    CONSTRAINT actividad_intentos_pendientes_revision_check CHECK ((pendientes_revision >= 0)),
    CONSTRAINT actividad_intentos_pkey PRIMARY KEY (id),
    CONSTRAINT actividad_intentos_actividad_id_fkey FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE
);

CREATE TABLE actividad_fallos (
    usuario_id uuid NOT NULL,
    pregunta_id bigint NOT NULL,
    veces_fallada integer NOT NULL DEFAULT 1,
    ultima_respuesta jsonb,
    ultimo_fallo timestamp with time zone NOT NULL DEFAULT now(),
    dominada boolean NOT NULL DEFAULT false,
    CONSTRAINT actividad_fallos_veces_fallada_check CHECK ((veces_fallada > 0)),
    CONSTRAINT actividad_fallos_pkey PRIMARY KEY (usuario_id, pregunta_id),
    CONSTRAINT actividad_fallos_pregunta_id_fkey FOREIGN KEY (pregunta_id) REFERENCES actividad_preguntas(id) ON DELETE CASCADE
);

CREATE TABLE cursos_profesores (
    curso_id bigint NOT NULL,
    profesor_id uuid NOT NULL,
    CONSTRAINT cursos_profesores_pkey PRIMARY KEY (curso_id, profesor_id),
    CONSTRAINT cursos_profesores_curso_id_fkey FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

CREATE TABLE asignaciones_alumno (
    curso_id bigint NOT NULL,
    alumno_id uuid NOT NULL,
    profesor_id uuid NOT NULL,
    asignado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT asignaciones_alumno_pkey PRIMARY KEY (curso_id, alumno_id, profesor_id),
    CONSTRAINT asignaciones_alumno_curso_id_fkey FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    CONSTRAINT asignaciones_alumno_curso_id_profesor_id_fkey FOREIGN KEY (curso_id, profesor_id) REFERENCES cursos_profesores(curso_id, profesor_id) ON DELETE CASCADE
);

CREATE TABLE progreso_curso (
    usuario_id uuid NOT NULL,
    unidad_slug text NOT NULL,
    completada_en timestamp with time zone DEFAULT now(),
    estado text NOT NULL DEFAULT 'completada'::text,
    iniciada_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT progreso_curso_pkey PRIMARY KEY (usuario_id, unidad_slug),
    CONSTRAINT progreso_curso_unidad_slug_fkey FOREIGN KEY (unidad_slug) REFERENCES unidades_curso(slug) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT progreso_curso_estado_valido CHECK (((estado = ANY (ARRAY['en_curso'::text, 'completada'::text])) AND ((estado = 'en_curso'::text) OR (completada_en IS NOT NULL))))
);

CREATE TABLE unidades_articulos (
    unidad_id bigint NOT NULL,
    articulo_slug text NOT NULL,
    orden integer NOT NULL DEFAULT 100,
    CONSTRAINT unidades_articulos_pkey PRIMARY KEY (unidad_id, articulo_slug),
    CONSTRAINT unidades_articulos_unidad_id_fkey FOREIGN KEY (unidad_id) REFERENCES unidades_curso(id) ON DELETE CASCADE
);
