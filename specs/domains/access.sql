-- database: access_db
-- Dominio access: 4 tablas copiadas tal cual de specs/supabase-schema.sql.
-- Generado por scripts/split_domains.py a partir de specs/domain-map.txt. No editar a mano.
-- Claves foraneas omitidas (apuntan a tablas de fuera del servicio; en Supabase si existen):
--   usuarios_roles.usuarios_roles_usuario_id_fkey -> auth.users(id)
--   usuarios_roles.usuarios_roles_asignado_por_fkey -> auth.users(id)

CREATE TABLE roles (
    codigo text NOT NULL,
    descripcion text NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (codigo)
);

CREATE TABLE permisos (
    codigo text NOT NULL,
    descripcion text NOT NULL,
    CONSTRAINT permisos_pkey PRIMARY KEY (codigo)
);

CREATE TABLE roles_permisos (
    rol text NOT NULL,
    permiso text NOT NULL,
    CONSTRAINT roles_permisos_pkey PRIMARY KEY (rol, permiso),
    CONSTRAINT roles_permisos_rol_fkey FOREIGN KEY (rol) REFERENCES roles(codigo) ON DELETE CASCADE,
    CONSTRAINT roles_permisos_permiso_fkey FOREIGN KEY (permiso) REFERENCES permisos(codigo) ON DELETE CASCADE
);

CREATE TABLE usuarios_roles (
    usuario_id uuid NOT NULL,
    rol text NOT NULL,
    asignado_por uuid,
    creado_en timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT usuarios_roles_pkey PRIMARY KEY (usuario_id, rol),
    CONSTRAINT usuarios_roles_rol_fkey FOREIGN KEY (rol) REFERENCES roles(codigo) ON DELETE CASCADE
);
