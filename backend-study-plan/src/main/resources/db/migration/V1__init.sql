CREATE TABLE study_plans (
    user_id             UUID        PRIMARY KEY,
    fecha_examen        DATE,
    minutos_dia         INTEGER     NOT NULL DEFAULT 30,
    dias_semana         VARCHAR(32) NOT NULL DEFAULT '1,2,3,4,5',
    prioridad           VARCHAR(16) NOT NULL DEFAULT 'auto',
    hora_recordatorio   TIME,
    recordatorio_activo BOOLEAN     NOT NULL DEFAULT false,
    actualizado_en      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT study_plans_minutos_check CHECK (minutos_dia >= 10 AND minutos_dia <= 180),
    CONSTRAINT study_plans_prioridad_check CHECK (prioridad IN ('auto','grammar','vocabulary','reading','writing','listening','speaking'))
);

CREATE TABLE ai_plans (
    user_id        UUID         PRIMARY KEY,
    semanas        INTEGER      NOT NULL,
    plan           VARCHAR(8000) NOT NULL,
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT ai_plans_semanas_check CHECK (semanas IN (4,8,12,16,24))
);

CREATE TABLE weekly_goals (
    user_id        UUID        PRIMARY KEY,
    tipo           VARCHAR(16) NOT NULL,
    cantidad       INTEGER     NOT NULL,
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT weekly_goals_tipo_check CHECK (tipo IN ('unidades','examenes','actividades')),
    CONSTRAINT weekly_goals_cantidad_check CHECK (cantidad >= 1 AND cantidad <= 20)
);

CREATE TABLE study_sessions (
    user_id       UUID        NOT NULL,
    dia           DATE        NOT NULL DEFAULT CURRENT_DATE,
    tarea         VARCHAR(16) NOT NULL,
    completada_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT study_sessions_pkey PRIMARY KEY (user_id, dia, tarea),
    CONSTRAINT study_sessions_tarea_check CHECK (tarea IN ('unidad','repaso','vocabulario','reading','writing','listening','speaking'))
);

CREATE TABLE weekly_reports (
    user_id        UUID         NOT NULL,
    semana_inicio  DATE         NOT NULL,
    semana_fin     DATE         NOT NULL,
    informe        VARCHAR(8000) NOT NULL,
    generado_en    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT weekly_reports_pkey PRIMARY KEY (user_id, semana_inicio),
    CONSTRAINT weekly_reports_week_check CHECK (semana_fin >= semana_inicio)
);
