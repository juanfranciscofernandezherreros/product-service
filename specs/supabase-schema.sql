-- =====================================================================
--  Export de esquema PostgreSQL (Supabase)
--  Proyecto: wnluofonzteaqtlfshgy  ·  Schema: public  ·  56 tablas
--  Generado desde pg_catalog (columnas, tipos, PK/FK, UNIQUE, CHECK).
--  Nota: varias FK referencian auth.users(id) (schema de Supabase Auth),
--        que no forma parte de este export. Mantenlas o ajústalas según
--        dónde se cree el esquema.
-- =====================================================================

CREATE TABLE actividad_diaria (
    usuario_id uuid NOT NULL,
    dia date NOT NULL,
    primera_actividad timestamp with time zone NOT NULL DEFAULT now(),
    origen text NOT NULL DEFAULT 'web'::text,
    CONSTRAINT actividad_diaria_pkey PRIMARY KEY (usuario_id, dia),
    CONSTRAINT actividad_diaria_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT actividad_fallos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT actividad_fallos_pregunta_id_fkey FOREIGN KEY (pregunta_id) REFERENCES actividad_preguntas(id) ON DELETE CASCADE
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
    CONSTRAINT actividad_intentos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT actividad_intentos_actividad_id_fkey FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE
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
    CONSTRAINT articulos_categoria_slug_fkey FOREIGN KEY (categoria_slug) REFERENCES categorias_blog(slug),
    CONSTRAINT articulos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id)
);

CREATE TABLE asignaciones_alumno (
    curso_id bigint NOT NULL,
    alumno_id uuid NOT NULL,
    profesor_id uuid NOT NULL,
    asignado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT asignaciones_alumno_pkey PRIMARY KEY (curso_id, alumno_id, profesor_id),
    CONSTRAINT asignaciones_alumno_curso_id_fkey FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    CONSTRAINT asignaciones_alumno_alumno_id_fkey FOREIGN KEY (alumno_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT asignaciones_alumno_curso_id_profesor_id_fkey FOREIGN KEY (curso_id, profesor_id) REFERENCES cursos_profesores(curso_id, profesor_id) ON DELETE CASCADE
);

CREATE TABLE battle_answers (
    room_id uuid NOT NULL,
    user_id uuid NOT NULL,
    answers jsonb NOT NULL DEFAULT '{}'::jsonb,
    submitted_at timestamp with time zone,
    score integer,
    elapsed_ms integer,
    CONSTRAINT battle_answers_pkey PRIMARY KEY (room_id, user_id),
    CONSTRAINT battle_answers_room_id_fkey FOREIGN KEY (room_id) REFERENCES battle_rooms(id) ON DELETE CASCADE,
    CONSTRAINT battle_answers_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE battle_players (
    room_id uuid NOT NULL,
    user_id uuid NOT NULL DEFAULT auth.uid(),
    alias text NOT NULL,
    status text NOT NULL DEFAULT 'pending'::text,
    joined_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT battle_players_alias_check CHECK (((char_length(alias) >= 2) AND (char_length(alias) <= 30))),
    CONSTRAINT battle_players_status_check CHECK ((status = ANY (ARRAY['pending'::text, 'accepted'::text, 'rejected'::text]))),
    CONSTRAINT battle_players_pkey PRIMARY KEY (room_id, user_id),
    CONSTRAINT battle_players_room_id_fkey FOREIGN KEY (room_id) REFERENCES battle_rooms(id) ON DELETE CASCADE,
    CONSTRAINT battle_players_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE battle_questions (
    id bigint NOT NULL,
    level text NOT NULL,
    topic text NOT NULL,
    prompt text NOT NULL,
    options jsonb NOT NULL,
    correct_index integer NOT NULL,
    explanation text NOT NULL,
    CONSTRAINT battle_questions_level_check CHECK ((level = ANY (ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text]))),
    CONSTRAINT battle_questions_topic_check CHECK ((topic = ANY (ARRAY['daily'::text, 'travel'::text, 'work'::text]))),
    CONSTRAINT battle_questions_options_check CHECK (((jsonb_typeof(options) = 'array'::text) AND (jsonb_array_length(options) = 4))),
    CONSTRAINT battle_questions_correct_index_check CHECK (((correct_index >= 0) AND (correct_index <= 3))),
    CONSTRAINT battle_questions_pkey PRIMARY KEY (id),
    CONSTRAINT battle_questions_level_topic_prompt_key UNIQUE (level, topic, prompt)
);

CREATE TABLE battle_rooms (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    code text NOT NULL DEFAULT upper(substr(replace((gen_random_uuid())::text, '-'::text, ''::text), 1, 10)),
    host_id uuid NOT NULL DEFAULT auth.uid(),
    level text NOT NULL,
    topic text NOT NULL,
    duration_seconds integer NOT NULL,
    capacity integer NOT NULL,
    status text NOT NULL DEFAULT 'waiting'::text,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    started_at timestamp with time zone,
    deadline_at timestamp with time zone,
    CONSTRAINT battle_rooms_level_check CHECK ((level = ANY (ARRAY['A1'::text, 'A2'::text, 'B1'::text, 'B2'::text]))),
    CONSTRAINT battle_rooms_topic_check CHECK ((topic = ANY (ARRAY['mixed'::text, 'daily'::text, 'travel'::text, 'work'::text]))),
    CONSTRAINT battle_rooms_duration_seconds_check CHECK ((duration_seconds = ANY (ARRAY[120, 180, 300]))),
    CONSTRAINT battle_rooms_capacity_check CHECK (((capacity >= 2) AND (capacity <= 12))),
    CONSTRAINT battle_rooms_status_check CHECK ((status = ANY (ARRAY['waiting'::text, 'playing'::text, 'finished'::text, 'cancelled'::text]))),
    CONSTRAINT battle_rooms_pkey PRIMARY KEY (id),
    CONSTRAINT battle_rooms_code_key UNIQUE (code),
    CONSTRAINT battle_rooms_host_id_fkey FOREIGN KEY (host_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE battle_round_questions (
    room_id uuid NOT NULL,
    ordinal smallint NOT NULL,
    question_id bigint NOT NULL,
    CONSTRAINT battle_round_questions_ordinal_check CHECK (((ordinal >= 1) AND (ordinal <= 10))),
    CONSTRAINT battle_round_questions_pkey PRIMARY KEY (room_id, ordinal),
    CONSTRAINT battle_round_questions_room_id_question_id_key UNIQUE (room_id, question_id),
    CONSTRAINT battle_round_questions_room_id_fkey FOREIGN KEY (room_id) REFERENCES battle_rooms(id) ON DELETE CASCADE,
    CONSTRAINT battle_round_questions_question_id_fkey FOREIGN KEY (question_id) REFERENCES battle_questions(id)
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
    CONSTRAINT bitacora_anotaciones_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT bitacora_categoria_mismo_usuario FOREIGN KEY (categoria_id, usuario_id) REFERENCES bitacora_categorias(id, usuario_id) ON UPDATE RESTRICT ON DELETE SET NULL (categoria_id)
);

CREATE TABLE bitacora_categorias (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    usuario_id uuid NOT NULL,
    nombre text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT bitacora_categorias_nombre_check CHECK (((char_length(btrim(nombre)) >= 1) AND (char_length(btrim(nombre)) <= 60))),
    CONSTRAINT bitacora_categorias_pkey PRIMARY KEY (id),
    CONSTRAINT bitacora_categorias_id_usuario_id_key UNIQUE (id, usuario_id),
    CONSTRAINT bitacora_categorias_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE categorias_blog (
    slug text NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    orden integer NOT NULL DEFAULT 0,
    CONSTRAINT categorias_blog_pkey PRIMARY KEY (slug)
);

CREATE TABLE coleccion_articulos (
    coleccion_id bigint NOT NULL,
    articulo_slug text NOT NULL,
    orden integer NOT NULL DEFAULT 100,
    CONSTRAINT coleccion_articulos_pkey PRIMARY KEY (coleccion_id, articulo_slug),
    CONSTRAINT coleccion_articulos_coleccion_id_fkey FOREIGN KEY (coleccion_id) REFERENCES colecciones_material(id) ON DELETE CASCADE,
    CONSTRAINT coleccion_articulos_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON UPDATE CASCADE ON DELETE CASCADE
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
    CONSTRAINT comentarios_articulos_padre_id_fkey FOREIGN KEY (padre_id) REFERENCES comentarios_articulos(id) ON DELETE RESTRICT,
    CONSTRAINT comentarios_articulos_autor_id_fkey FOREIGN KEY (autor_id) REFERENCES auth.users(id) ON DELETE SET NULL
);

CREATE TABLE cursos (
    id bigint NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    activo boolean NOT NULL DEFAULT true,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT cursos_nombre_check CHECK ((length(TRIM(BOTH FROM nombre)) > 0)),
    CONSTRAINT cursos_pkey PRIMARY KEY (id)
);

CREATE TABLE cursos_profesores (
    curso_id bigint NOT NULL,
    profesor_id uuid NOT NULL,
    CONSTRAINT cursos_profesores_pkey PRIMARY KEY (curso_id, profesor_id),
    CONSTRAINT cursos_profesores_curso_id_fkey FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    CONSTRAINT cursos_profesores_profesor_id_fkey FOREIGN KEY (profesor_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT diagnosticos_b2_pkey PRIMARY KEY (id),
    CONSTRAINT diagnosticos_b2_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

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
    CONSTRAINT examenes_codigo_key UNIQUE (codigo),
    CONSTRAINT examenes_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON UPDATE CASCADE ON DELETE SET NULL
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
    CONSTRAINT examenes_lecturas_pkey PRIMARY KEY (id),
    CONSTRAINT examenes_lecturas_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE lecturas (
    usuario_id uuid NOT NULL,
    articulo_url text NOT NULL,
    leido_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT lecturas_articulo_url_check CHECK (((length(articulo_url) <= 240) AND (articulo_url ~ '^post/[a-zA-Z0-9_./-]+[.]html$'::text) AND (articulo_url !~ '[.][.]'::text))),
    CONSTRAINT lecturas_pkey PRIMARY KEY (usuario_id, articulo_url),
    CONSTRAINT lecturas_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE mensajes_privados (
    id bigint NOT NULL,
    remitente uuid NOT NULL,
    destinatario uuid NOT NULL,
    contenido text NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT mensajes_privados_contenido_check CHECK (((char_length(btrim(contenido)) >= 1) AND (char_length(btrim(contenido)) <= 2000))),
    CONSTRAINT mensajes_usuarios_distintos CHECK ((remitente <> destinatario)),
    CONSTRAINT mensajes_privados_pkey PRIMARY KEY (id),
    CONSTRAINT mensajes_privados_remitente_fkey FOREIGN KEY (remitente) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT mensajes_privados_destinatario_fkey FOREIGN KEY (destinatario) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE mensajes_vistos (
    usuario_id uuid NOT NULL,
    visto_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT mensajes_vistos_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT mensajes_vistos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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

CREATE TABLE objetivos_semanales (
    usuario_id uuid NOT NULL,
    tipo text NOT NULL,
    cantidad integer NOT NULL,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT objetivos_semanales_tipo_check CHECK ((tipo = ANY (ARRAY['unidades'::text, 'examenes'::text, 'actividades'::text]))),
    CONSTRAINT objetivos_semanales_cantidad_check CHECK (((cantidad >= 1) AND (cantidad <= 20))),
    CONSTRAINT objetivos_semanales_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT objetivos_semanales_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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

CREATE TABLE perfiles (
    usuario_id uuid NOT NULL,
    nombre text NOT NULL DEFAULT ''::text,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT perfiles_nombre_check CHECK ((char_length(nombre) <= 50)),
    CONSTRAINT perfiles_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT perfiles_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT perfiles_publicos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT perfiles_publicos_biografia_check CHECK ((char_length(biografia) <= 180)),
    CONSTRAINT perfiles_publicos_avatar_emoji_check CHECK ((avatar_emoji = ANY (ARRAY['📘'::text, '🎓'::text, '⭐'::text, '🧠'::text, '🌍'::text, '🚀'::text, '📚'::text])))
);

CREATE TABLE permisos (
    codigo text NOT NULL,
    descripcion text NOT NULL,
    CONSTRAINT permisos_pkey PRIMARY KEY (codigo)
);

CREATE TABLE planes_estudio_b2 (
    usuario_id uuid NOT NULL,
    fecha_examen date,
    minutos_dia integer NOT NULL DEFAULT 30,
    dias_semana integer[] NOT NULL DEFAULT ARRAY[1, 2, 3, 4, 5],
    prioridad text NOT NULL DEFAULT 'auto'::text,
    hora_recordatorio time without time zone,
    recordatorio_activo boolean NOT NULL DEFAULT false,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT planes_estudio_b2_minutos_dia_check CHECK (((minutos_dia >= 10) AND (minutos_dia <= 180))),
    CONSTRAINT planes_estudio_b2_prioridad_check CHECK ((prioridad = ANY (ARRAY['auto'::text, 'grammar'::text, 'vocabulary'::text, 'reading'::text, 'writing'::text, 'listening'::text, 'speaking'::text]))),
    CONSTRAINT valid_days CHECK (((dias_semana <@ ARRAY[0, 1, 2, 3, 4, 5, 6]) AND ((cardinality(dias_semana) >= 1) AND (cardinality(dias_semana) <= 7)))),
    CONSTRAINT planes_estudio_b2_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT planes_estudio_b2_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE planes_ia_b2 (
    usuario_id uuid NOT NULL,
    semanas integer NOT NULL,
    plan jsonb NOT NULL,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT planes_ia_b2_semanas_check CHECK ((semanas = ANY (ARRAY[4, 8, 12, 16, 24]))),
    CONSTRAINT planes_ia_b2_pkey PRIMARY KEY (usuario_id),
    CONSTRAINT planes_ia_b2_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT preguntas_falladas_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT preguntas_falladas_pregunta_id_fkey FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE
);

CREATE TABLE progreso_curso (
    usuario_id uuid NOT NULL,
    unidad_slug text NOT NULL,
    completada_en timestamp with time zone DEFAULT now(),
    estado text NOT NULL DEFAULT 'completada'::text,
    iniciada_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT progreso_curso_pkey PRIMARY KEY (usuario_id, unidad_slug),
    CONSTRAINT progreso_curso_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT progreso_curso_unidad_slug_fkey FOREIGN KEY (unidad_slug) REFERENCES unidades_curso(slug) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT progreso_curso_estado_valido CHECK (((estado = ANY (ARRAY['en_curso'::text, 'completada'::text])) AND ((estado = 'en_curso'::text) OR (completada_en IS NOT NULL))))
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

CREATE TABLE recursos_guardados (
    usuario_id uuid NOT NULL,
    tipo text NOT NULL,
    referencia text NOT NULL,
    guardado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT recursos_guardados_tipo_check CHECK ((tipo = ANY (ARRAY['articulo'::text, 'unidad'::text]))),
    CONSTRAINT recursos_guardados_referencia_check CHECK ((referencia ~ '^[a-z0-9][a-z0-9-]{1,119}$'::text)),
    CONSTRAINT recursos_guardados_pkey PRIMARY KEY (usuario_id, tipo, referencia),
    CONSTRAINT recursos_guardados_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT resultados_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT resultados_examen_id_fkey FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE CASCADE
);

CREATE TABLE roles (
    codigo text NOT NULL,
    descripcion text NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (codigo)
);

CREATE TABLE roles_permisos (
    rol text NOT NULL,
    permiso text NOT NULL,
    CONSTRAINT roles_permisos_pkey PRIMARY KEY (rol, permiso),
    CONSTRAINT roles_permisos_rol_fkey FOREIGN KEY (rol) REFERENCES roles(codigo) ON DELETE CASCADE,
    CONSTRAINT roles_permisos_permiso_fkey FOREIGN KEY (permiso) REFERENCES permisos(codigo) ON DELETE CASCADE
);

CREATE TABLE seguimiento_semanal (
    usuario_id uuid NOT NULL,
    semana_inicio date NOT NULL,
    semana_fin date NOT NULL,
    informe jsonb NOT NULL,
    generado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT seguimiento_semanal_informe_check CHECK ((jsonb_typeof(informe) = 'object'::text)),
    CONSTRAINT seguimiento_semanal_check CHECK ((semana_fin >= semana_inicio)),
    CONSTRAINT seguimiento_semanal_pkey PRIMARY KEY (usuario_id, semana_inicio),
    CONSTRAINT seguimiento_semanal_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);

CREATE TABLE seguimientos (
    seguidor uuid NOT NULL,
    seguido uuid NOT NULL,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT distinto_usuario CHECK ((seguidor <> seguido)),
    CONSTRAINT seguimientos_pkey PRIMARY KEY (seguidor, seguido),
    CONSTRAINT seguimientos_seguidor_fkey FOREIGN KEY (seguidor) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT seguimientos_seguido_fkey FOREIGN KEY (seguido) REFERENCES perfiles_publicos(usuario_id) ON DELETE CASCADE
);

CREATE TABLE sesiones_estudio_b2 (
    usuario_id uuid NOT NULL,
    dia date NOT NULL DEFAULT CURRENT_DATE,
    tarea text NOT NULL,
    completada_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT sesiones_estudio_b2_pkey PRIMARY KEY (usuario_id, dia, tarea),
    CONSTRAINT sesiones_estudio_b2_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT sesiones_estudio_b2_tarea_check CHECK ((tarea = ANY (ARRAY['unidad'::text, 'repaso'::text, 'vocabulario'::text, 'reading'::text, 'writing'::text, 'listening'::text, 'speaking'::text])))
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
    CONSTRAINT simulacros_b2_pkey PRIMARY KEY (id),
    CONSTRAINT simulacros_b2_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT solicitudes_mensaje_remitente_fkey FOREIGN KEY (remitente) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT solicitudes_mensaje_destinatario_fkey FOREIGN KEY (destinatario) REFERENCES perfiles_publicos(usuario_id) ON DELETE CASCADE
);

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

CREATE TABLE unidades_articulos (
    unidad_id bigint NOT NULL,
    articulo_slug text NOT NULL,
    orden integer NOT NULL DEFAULT 100,
    CONSTRAINT unidades_articulos_pkey PRIMARY KEY (unidad_id, articulo_slug),
    CONSTRAINT unidades_articulos_unidad_id_fkey FOREIGN KEY (unidad_id) REFERENCES unidades_curso(id) ON DELETE CASCADE,
    CONSTRAINT unidades_articulos_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON UPDATE CASCADE ON DELETE CASCADE
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

CREATE TABLE usuarios_roles (
    usuario_id uuid NOT NULL,
    rol text NOT NULL,
    asignado_por uuid,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT usuarios_roles_pkey PRIMARY KEY (usuario_id, rol),
    CONSTRAINT usuarios_roles_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT usuarios_roles_rol_fkey FOREIGN KEY (rol) REFERENCES roles(codigo) ON DELETE CASCADE,
    CONSTRAINT usuarios_roles_asignado_por_fkey FOREIGN KEY (asignado_por) REFERENCES auth.users(id) ON DELETE SET NULL
);

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
    CONSTRAINT vocabulario_termino_categoria_unicos UNIQUE (termino_en, categoria),
    CONSTRAINT vocabulario_articulo_slug_fkey FOREIGN KEY (articulo_slug) REFERENCES articulos(slug) ON UPDATE CASCADE ON DELETE SET NULL
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
    CONSTRAINT vocabulario_preferencias_pkey PRIMARY KEY (user_id),
    CONSTRAINT vocabulario_preferencias_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE
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
    CONSTRAINT vocabulario_progreso_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    CONSTRAINT vocabulario_progreso_item_id_fkey FOREIGN KEY (item_id) REFERENCES vocabulario_items(id) ON DELETE CASCADE
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
    CONSTRAINT writing_borradores_pkey PRIMARY KEY (id),
    CONSTRAINT writing_borradores_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES auth.users(id) ON DELETE CASCADE
);
