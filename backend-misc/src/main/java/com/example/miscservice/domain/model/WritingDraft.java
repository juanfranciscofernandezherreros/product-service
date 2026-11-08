package com.example.miscservice.domain.model;

import com.example.miscservice.domain.exception.InvalidMiscException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Immutable domain model for a writing draft owned by a user.
 */
public final class WritingDraft {

    private static final String DEFAULT_TITLE = "Mi writing B2";

    private final UUID id;
    private final UUID usuarioId;
    private final WritingType tipo;
    private final String titulo;
    private final String contenido;
    private final OffsetDateTime creadoEn;
    private final OffsetDateTime actualizadoEn;

    /**
     * Full constructor enforcing domain invariants.
     *
     * @param id            identifier
     * @param usuarioId     owning user id
     * @param tipo          writing type
     * @param titulo        title (max 255 chars)
     * @param contenido     draft content
     * @param creadoEn      creation timestamp
     * @param actualizadoEn last update timestamp
     */
    public WritingDraft(final UUID id, final UUID usuarioId, final WritingType tipo,
                        final String titulo, final String contenido, final OffsetDateTime creadoEn,
                        final OffsetDateTime actualizadoEn) {
        if (id == null) {
            throw new InvalidMiscException("id is required");
        }
        if (usuarioId == null) {
            throw new InvalidMiscException("usuarioId is required");
        }
        if (tipo == null) {
            throw new InvalidMiscException("tipo is required");
        }
        if (titulo != null && titulo.length() > 255) {
            throw new InvalidMiscException("titulo must be at most 255 characters");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.titulo = (titulo == null || titulo.isBlank()) ? DEFAULT_TITLE : titulo;
        this.contenido = contenido == null ? "" : contenido;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Factory for a new draft with a generated id.
     *
     * @param usuarioId owning user id
     * @param tipo      writing type
     * @param titulo    optional title (defaults applied)
     * @param contenido optional content
     * @return a new draft
     */
    public static WritingDraft create(final UUID usuarioId, final WritingType tipo,
                                      final String titulo, final String contenido) {
        return new WritingDraft(UUID.randomUUID(), usuarioId, tipo, titulo, contenido, null, null);
    }

    public UUID id() {
        return id;
    }

    public UUID usuarioId() {
        return usuarioId;
    }

    public WritingType tipo() {
        return tipo;
    }

    public String titulo() {
        return titulo;
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
