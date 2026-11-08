package com.example.miscservice.domain.model;

import com.example.miscservice.domain.exception.InvalidMiscException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Immutable domain model for a logbook note owned by a user.
 */
public final class LogbookNote {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID categoriaId;
    private final String contenido;
    private final OffsetDateTime creadoEn;
    private final OffsetDateTime actualizadoEn;

    /**
     * Full constructor enforcing domain invariants.
     *
     * @param id            identifier
     * @param usuarioId     owning user id
     * @param categoriaId   optional category id
     * @param contenido     note content (1..10000 chars)
     * @param creadoEn      creation timestamp
     * @param actualizadoEn last update timestamp
     */
    public LogbookNote(final UUID id, final UUID usuarioId, final UUID categoriaId,
                       final String contenido, final OffsetDateTime creadoEn,
                       final OffsetDateTime actualizadoEn) {
        if (id == null) {
            throw new InvalidMiscException("id is required");
        }
        if (usuarioId == null) {
            throw new InvalidMiscException("usuarioId is required");
        }
        if (contenido == null || contenido.strip().isEmpty() || contenido.strip().length() > 10000) {
            throw new InvalidMiscException("contenido must be between 1 and 10000 characters");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.contenido = contenido.strip();
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Factory for a new note with a generated id.
     *
     * @param usuarioId   owning user id
     * @param categoriaId optional category id
     * @param contenido   note content
     * @return a new note
     */
    public static LogbookNote create(final UUID usuarioId, final UUID categoriaId, final String contenido) {
        return new LogbookNote(UUID.randomUUID(), usuarioId, categoriaId, contenido, null, null);
    }

    public UUID id() {
        return id;
    }

    public UUID usuarioId() {
        return usuarioId;
    }

    public UUID categoriaId() {
        return categoriaId;
    }

    public String contenido() {
        return contenido;
    }

    public OffsetDateTime creadoEn() {
        return creadoEn;
    }

    public OffsetDateTime actualizadoEn() {
        return actualizadoEn;
    }
}
