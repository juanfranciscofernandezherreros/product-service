-- database: access_db
-- Dominio Access (roles, permisos, asignaciones). FK a auth.users eliminadas (uuid simple).

CREATE TABLE roles (
    codigo      VARCHAR(64)  PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE permisos (
    codigo      VARCHAR(64)  PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE roles_permisos (
    rol     VARCHAR(64) NOT NULL REFERENCES roles(codigo) ON DELETE CASCADE,
    permiso VARCHAR(64) NOT NULL REFERENCES permisos(codigo) ON DELETE CASCADE,
    CONSTRAINT roles_permisos_pkey PRIMARY KEY (rol, permiso)
);

CREATE TABLE usuarios_roles (
    usuario_id   UUID        NOT NULL,
    rol          VARCHAR(64) NOT NULL REFERENCES roles(codigo) ON DELETE CASCADE,
    asignado_por UUID,
    creado_en    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT usuarios_roles_pkey PRIMARY KEY (usuario_id, rol)
);
