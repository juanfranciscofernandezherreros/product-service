-- database: exams_db
-- Dominio exams: 7 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   examenes.examenes_articulo_slug_fkey -> articulos(slug)
--   resultados.resultados_usuario_id_fkey -> auth.users(id)
--   diagnosticos_b2.diagnosticos_b2_usuario_id_fkey -> auth.users(id)
--   simulacros_b2.simulacros_b2_usuario_id_fkey -> auth.users(id)
--   examenes_lecturas.examenes_lecturas_usuario_id_fkey -> auth.users(id)
--   preguntas_falladas.preguntas_falladas_usuario_id_fkey -> auth.users(id)

CREATE TABLE examenes (
    id bigint NOT NULL,
    codigo text NOT NULL,
    titulo text NOT NULL,
    fecha date,
    nivel text NOT NULL DEFAULT 'B1-B2'::text,
    descripcion text,
    publicado boolean NOT NULL DEFAULT false,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    lectura text,
    articulo_slug text,
    CONSTRAINT examenes_pkey PRIMARY KEY (id),
    CONSTRAINT examenes_codigo_key UNIQUE (codigo)
);

CREATE TABLE preguntas (
    id bigint NOT NULL,
    examen_id bigint NOT NULL,
    orden integer NOT NULL,
    enunciado text NOT NULL,
    opciones jsonb NOT NULL,
    fuente_url text,
    CONSTRAINT preguntas_orden_check CHECK ((orden > 0)),
    CONSTRAINT preguntas_opciones_check CHECK (((jsonb_typeof(opciones) = 'array'::text) AND ((jsonb_array_length(opciones) >= 2) AND (jsonb_array_length(opciones) <= 8)))),
    CONSTRAINT preguntas_pkey PRIMARY KEY (id),
    CONSTRAINT preguntas_examen_id_orden_key UNIQUE (examen_id, orden),
    CONSTRAINT preguntas_id_examen_id_key UNIQUE (id, examen_id),
    CONSTRAINT preguntas_examen_id_fkey FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE CASCADE
);

CREATE TABLE resultados (
    id bigint NOT NULL,
    usuario_id uuid NOT NULL,
    examen_id bigint NOT NULL,
    respuestas jsonb NOT NULL,
    aciertos integer NOT NULL,
    total integer NOT NULL,
    nota numeric(5,2) NOT NULL,
    fecha timestamp with time zone NOT NULL DEFAULT now(),
    intento_id uuid,
    CONSTRAINT resultados_total_check CHECK ((total > 0)),
    CONSTRAINT resultados_nota_check CHECK (((nota >= (0)::numeric) AND (nota <= (100)::numeric))),
    CONSTRAINT resultados_pkey PRIMARY KEY (id),
    CONSTRAINT resultados_examen_id_fkey FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE CASCADE
);

CREATE TABLE diagnosticos_b2 (
    id bigint NOT NULL,
    usuario_id uuid NOT NULL,
    aciertos integer NOT NULL,
    total integer NOT NULL,
    desglose jsonb NOT NULL DEFAULT '{}'::jsonb,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT diagnosticos_b2_aciertos_check CHECK (((aciertos >= 0) AND (aciertos <= 40))),
    CONSTRAINT diagnosticos_b2_total_check CHECK (((total >= 1) AND (total <= 40))),
    CONSTRAINT diagnosticos_b2_desglose_check CHECK ((jsonb_typeof(desglose) = 'object'::text)),
    CONSTRAINT diagnostico_score CHECK ((aciertos <= total)),
    CONSTRAINT diagnosticos_b2_pkey PRIMARY KEY (id)
);

CREATE TABLE simulacros_b2 (
    id bigint NOT NULL,
    usuario_id uuid NOT NULL,
    modalidad text NOT NULL,
    aciertos integer NOT NULL,
    total integer NOT NULL,
    respuestas jsonb NOT NULL DEFAULT '{}'::jsonb,
    duracion_segundos integer NOT NULL DEFAULT 0,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    intento_id uuid,
    CONSTRAINT simulacros_b2_modalidad_check CHECK ((modalidad = ANY (ARRAY['use_of_english'::text, 'listening'::text]))),
    CONSTRAINT simulacros_b2_aciertos_check CHECK (((aciertos >= 0) AND (aciertos <= 40))),
    CONSTRAINT simulacros_b2_total_check CHECK (((total >= 1) AND (total <= 40))),
    CONSTRAINT simulacros_b2_respuestas_check CHECK ((jsonb_typeof(respuestas) = 'object'::text)),
    CONSTRAINT simulacros_b2_duracion_segundos_check CHECK (((duracion_segundos >= 0) AND (duracion_segundos <= 7200))),
    CONSTRAINT simulacro_score CHECK ((aciertos <= total)),
    CONSTRAINT simulacros_b2_pkey PRIMARY KEY (id)
);

CREATE TABLE examenes_lecturas (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    usuario_id uuid NOT NULL,
    pregunta_ids bigint[] NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    terminado_en timestamp with time zone,
    respuestas jsonb,
    aciertos integer,
    total integer,
    CONSTRAINT examen_lecturas_puntuacion CHECK ((((terminado_en IS NULL) AND (respuestas IS NULL) AND (aciertos IS NULL) AND (total IS NULL)) OR ((terminado_en IS NOT NULL) AND (respuestas IS NOT NULL) AND (aciertos >= 0) AND (total > 0) AND (aciertos <= total)))),
    CONSTRAINT examenes_lecturas_pkey PRIMARY KEY (id)
);

CREATE TABLE preguntas_falladas (
    usuario_id uuid NOT NULL,
    pregunta_id bigint NOT NULL,
    veces_fallada integer NOT NULL DEFAULT 1,
    ultima_respuesta integer,
    primer_fallo timestamp with time zone NOT NULL DEFAULT now(),
    ultimo_fallo timestamp with time zone NOT NULL DEFAULT now(),
    proximo_repaso timestamp with time zone NOT NULL DEFAULT now(),
    dominada boolean NOT NULL DEFAULT false,
    CONSTRAINT preguntas_falladas_veces_fallada_check CHECK ((veces_fallada >= 1)),
    CONSTRAINT preguntas_falladas_pkey PRIMARY KEY (usuario_id, pregunta_id),
    CONSTRAINT preguntas_falladas_pregunta_id_fkey FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE
);
