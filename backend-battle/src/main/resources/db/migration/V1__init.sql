CREATE TABLE battle_questions (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    level         VARCHAR(2)   NOT NULL,
    topic         VARCHAR(16)  NOT NULL,
    prompt        VARCHAR(2000) NOT NULL,
    options       VARCHAR(4000) NOT NULL,
    correct_index INTEGER      NOT NULL,
    explanation   VARCHAR(2000) NOT NULL,
    CONSTRAINT battle_questions_level_check CHECK (level IN ('A1','A2','B1','B2')),
    CONSTRAINT battle_questions_topic_check CHECK (topic IN ('daily','travel','work')),
    CONSTRAINT battle_questions_correct_index_check CHECK (correct_index >= 0 AND correct_index <= 3),
    CONSTRAINT battle_questions_unique UNIQUE (level, topic, prompt)
);

CREATE TABLE battle_rooms (
    id               UUID        PRIMARY KEY,
    code             VARCHAR(16) NOT NULL,
    host_id          UUID        NOT NULL,
    level            VARCHAR(2)  NOT NULL,
    topic            VARCHAR(16) NOT NULL,
    duration_seconds INTEGER     NOT NULL,
    capacity         INTEGER     NOT NULL,
    status           VARCHAR(16) NOT NULL DEFAULT 'waiting',
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    started_at       TIMESTAMP WITH TIME ZONE,
    deadline_at      TIMESTAMP WITH TIME ZONE,
    CONSTRAINT battle_rooms_code_key UNIQUE (code),
    CONSTRAINT battle_rooms_level_check CHECK (level IN ('A1','A2','B1','B2')),
    CONSTRAINT battle_rooms_topic_check CHECK (topic IN ('mixed','daily','travel','work')),
    CONSTRAINT battle_rooms_duration_check CHECK (duration_seconds IN (120,180,300)),
    CONSTRAINT battle_rooms_capacity_check CHECK (capacity >= 2 AND capacity <= 12),
    CONSTRAINT battle_rooms_status_check CHECK (status IN ('waiting','playing','finished','cancelled'))
);

CREATE TABLE battle_players (
    room_id   UUID        NOT NULL REFERENCES battle_rooms(id) ON DELETE CASCADE,
    user_id   UUID        NOT NULL,
    alias     VARCHAR(30) NOT NULL,
    status    VARCHAR(16) NOT NULL DEFAULT 'pending',
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT battle_players_pkey PRIMARY KEY (room_id, user_id),
    CONSTRAINT battle_players_alias_check CHECK (char_length(alias) >= 2 AND char_length(alias) <= 30),
    CONSTRAINT battle_players_status_check CHECK (status IN ('pending','accepted','rejected'))
);
