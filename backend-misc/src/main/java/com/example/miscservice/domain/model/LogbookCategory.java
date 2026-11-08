package com.example.miscservice.domain.model;

import com.example.miscservice.domain.exception.InvalidMiscException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Immutable domain model for a logbook category owned by a user.
 */
public final class LogbookCategory {

    private final UUID id;
    private final UUID usuarioId;
    private final String nombre;
    private final OffsetDateTime creadoEn;

    /**
     * Full constructor enforcing domain invariants.
     *
     * @param id        identifier
     * @param usuarioId owning user id
     * @param nombre    category name (1..60 chars)
     * @param creadoEn  creation timestamp
     */
    public LogbookCategory(final UUID id, final UUID usuarioId, final String nombre,
                           final OffsetDateTime creadoEn) {
        if (id == null) {
            throw new InvalidMiscException("id is required");
        }
        if (usuarioId == null) {
            throw new InvalidMiscException("usuarioId is required");
        }
        if (nombre == null || nombre.strip().isEmpty() || nombre.strip().length() > 60) {
            throw new InvalidMiscException("nombre must be between 1 and 60 characters");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre.strip();
        this.creadoEn = creadoEn;
    }

    /**
     * Factory for a new category with a generated id.
     *
     * @param usuarioId owning user id
     * @param nombre    category name
     * @return a new category
     */
    public static LogbookCategory create(final UUID usuarioId, final String nombre) {
        return new LogbookCategory(UUID.randomUUID(), usuarioId, nombre, null);
    }

    public UUID id() {
        return id;
    }

    public UUID usuarioId() {
        return usuarioId;
    }

    public String nombre() {
        return nombre;
    }

    public OffsetDateTime creadoEn() {
        return creadoEn;
    }
}
