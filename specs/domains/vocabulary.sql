-- database: vocabulary_db
-- Dominio Vocabulary (subconjunto del esquema Supabase, autocontenido).
-- Se eliminan las FK a auth.users (otro schema): user_id queda como uuid simple.
-- id pasa a IDENTITY para permitir inserciones desde el servicio.

CREATE TABLE vocabulary_items (
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    en      VARCHAR(255) NOT NULL,
    es      VARCHAR(255) NOT NULL,
    example VARCHAR(2000),
    level   VARCHAR(2)   NOT NULL,
    CONSTRAINT vocabulary_items_en_key UNIQUE (en),
    CONSTRAINT vocabulary_items_level_check CHECK (level IN ('A1','A2','B1','B2','C1'))
);

CREATE TABLE vocabulary_progress (
    user_id        UUID        NOT NULL,
    item_id        BIGINT      NOT NULL REFERENCES vocabulary_items(id) ON DELETE CASCADE,
    streak         INTEGER     NOT NULL DEFAULT 0,
    last_grade     INTEGER     NOT NULL DEFAULT 0,
    next_review_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    first_seen_day DATE        NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT vocabulary_progress_pkey PRIMARY KEY (user_id, item_id),
    CONSTRAINT vocabulary_progress_streak_check CHECK (streak >= 0),
    CONSTRAINT vocabulary_progress_last_grade_check CHECK (last_grade >= 0 AND last_grade <= 2)
);
