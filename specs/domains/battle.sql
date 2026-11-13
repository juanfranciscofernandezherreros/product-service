-- database: battle_db
-- Dominio battle: 5 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   battle_rooms.battle_rooms_host_id_fkey -> auth.users(id)
--   battle_players.battle_players_user_id_fkey -> auth.users(id)
--   battle_answers.battle_answers_user_id_fkey -> auth.users(id)

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
    CONSTRAINT battle_rooms_code_key UNIQUE (code)
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
    CONSTRAINT battle_players_room_id_fkey FOREIGN KEY (room_id) REFERENCES battle_rooms(id) ON DELETE CASCADE
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

CREATE TABLE battle_answers (
    room_id uuid NOT NULL,
    user_id uuid NOT NULL,
    answers jsonb NOT NULL DEFAULT '{}'::jsonb,
    submitted_at timestamp with time zone,
    score integer,
    elapsed_ms integer,
    CONSTRAINT battle_answers_pkey PRIMARY KEY (room_id, user_id),
    CONSTRAINT battle_answers_room_id_fkey FOREIGN KEY (room_id) REFERENCES battle_rooms(id) ON DELETE CASCADE
);
