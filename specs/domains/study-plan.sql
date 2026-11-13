-- database: study_plan_db
-- Dominio study-plan: 6 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   planes_estudio_b2.planes_estudio_b2_usuario_id_fkey -> auth.users(id)
--   planes_ia_b2.planes_ia_b2_usuario_id_fkey -> auth.users(id)
--   objetivos_semanales.objetivos_semanales_usuario_id_fkey -> auth.users(id)
--   sesiones_estudio_b2.sesiones_estudio_b2_usuario_id_fkey -> auth.users(id)
--   seguimiento_semanal.seguimiento_semanal_usuario_id_fkey -> auth.users(id)
--   actividad_diaria.actividad_diaria_usuario_id_fkey -> auth.users(id)

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
    CONSTRAINT planes_estudio_b2_pkey PRIMARY KEY (usuario_id)
);

CREATE TABLE planes_ia_b2 (
    usuario_id uuid NOT NULL,
    semanas integer NOT NULL,
    plan jsonb NOT NULL,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT planes_ia_b2_semanas_check CHECK ((semanas = ANY (ARRAY[4, 8, 12, 16, 24]))),
    CONSTRAINT planes_ia_b2_pkey PRIMARY KEY (usuario_id)
);

CREATE TABLE objetivos_semanales (
    usuario_id uuid NOT NULL,
    tipo text NOT NULL,
    cantidad integer NOT NULL,
    actualizado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT objetivos_semanales_tipo_check CHECK ((tipo = ANY (ARRAY['unidades'::text, 'examenes'::text, 'actividades'::text]))),
    CONSTRAINT objetivos_semanales_cantidad_check CHECK (((cantidad >= 1) AND (cantidad <= 20))),
    CONSTRAINT objetivos_semanales_pkey PRIMARY KEY (usuario_id)
);

CREATE TABLE sesiones_estudio_b2 (
    usuario_id uuid NOT NULL,
    dia date NOT NULL DEFAULT CURRENT_DATE,
    tarea text NOT NULL,
    completada_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT sesiones_estudio_b2_pkey PRIMARY KEY (usuario_id, dia, tarea),
    CONSTRAINT sesiones_estudio_b2_tarea_check CHECK ((tarea = ANY (ARRAY['unidad'::text, 'repaso'::text, 'vocabulario'::text, 'reading'::text, 'writing'::text, 'listening'::text, 'speaking'::text])))
);

CREATE TABLE seguimiento_semanal (
    usuario_id uuid NOT NULL,
    semana_inicio date NOT NULL,
    semana_fin date NOT NULL,
    informe jsonb NOT NULL,
    generado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT seguimiento_semanal_informe_check CHECK ((jsonb_typeof(informe) = 'object'::text)),
    CONSTRAINT seguimiento_semanal_check CHECK ((semana_fin >= semana_inicio)),
    CONSTRAINT seguimiento_semanal_pkey PRIMARY KEY (usuario_id, semana_inicio)
);

CREATE TABLE actividad_diaria (
    usuario_id uuid NOT NULL,
    dia date NOT NULL,
    primera_actividad timestamp with time zone NOT NULL DEFAULT now(),
    origen text NOT NULL DEFAULT 'web'::text,
    CONSTRAINT actividad_diaria_pkey PRIMARY KEY (usuario_id, dia)
);
