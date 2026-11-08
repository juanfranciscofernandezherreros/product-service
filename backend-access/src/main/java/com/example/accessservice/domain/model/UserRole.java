package com.example.accessservice.domain.model;

import com.example.accessservice.domain.exception.InvalidAccessException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a role assigned to a user.
 */
public final class UserRole {

    private final UUID usuarioId;
    private final String rol;
    private final UUID asignadoPor;
    private final Instant creadoEn;

    /**
     * Creates a validated assignment.
     *
     * @param usuarioId   user id
     * @param rol         role code
     * @param asignadoPor assigning user id (may be {@code null})
     * @param creadoEn    creation instant
     */
    public UserRole(final UUID usuarioId, final String rol, final UUID asignadoPor, final Instant creadoEn) {
        if (usuarioId == null) {
            throw new InvalidAccessException("usuarioId is required");
        }
        if (rol == null || rol.trim().isEmpty()) {
            throw new InvalidAccessException("rol is required");
        }
        this.usuarioId = usuarioId;
        this.rol = rol.trim();
        this.asignadoPor = asignadoPor;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
    }

    /** @return the user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return the role code */
    public String getRol() {
        return rol;
    }

    /** @return the assigning user id, may be {@code null} */
    public UUID getAsignadoPor() {
        return asignadoPor;
    }

    /** @return the creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
