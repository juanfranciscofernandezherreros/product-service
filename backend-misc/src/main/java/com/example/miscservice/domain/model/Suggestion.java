package com.example.miscservice.domain.model;

import com.example.miscservice.domain.exception.InvalidMiscException;

import java.time.OffsetDateTime;

/**
 * Immutable domain model for a user suggestion.
 */
public final class Suggestion {

    private final Long id;
    private final String titulo;
    private final String descripcion;
    private final SuggestionStatus estado;
    private final OffsetDateTime creadoEn;

    /**
     * Full constructor enforcing domain invariants.
     *
     * @param id          identifier (nullable before persistence)
     * @param titulo      title (5..100 chars)
     * @param descripcion description (10..1000 chars)
     * @param estado      lifecycle status
     * @param creadoEn    creation timestamp
     */
    public Suggestion(final Long id, final String titulo, final String descripcion,
                      final SuggestionStatus estado, final OffsetDateTime creadoEn) {
        if (titulo == null || titulo.strip().length() < 5 || titulo.strip().length() > 100) {
            throw new InvalidMiscException("titulo must be between 5 and 100 characters");
        }
        if (descripcion == null || descripcion.strip().length() < 10 || descripcion.strip().length() > 1000) {
            throw new InvalidMiscException("descripcion must be between 10 and 1000 characters");
        }
        if (estado == null) {
            throw new InvalidMiscException("estado is required");
        }
        this.id = id;
        this.titulo = titulo.strip();
        this.descripcion = descripcion.strip();
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    /**
     * Factory for a new pending suggestion.
     *
     * @param titulo      title
     * @param descripcion description
     * @return a new pending suggestion
     */
    public static Suggestion create(final String titulo, final String descripcion) {
        return new Suggestion(null, titulo, descripcion, SuggestionStatus.PENDIENTE, null);
    }

    /**
     * Returns a copy with a new status.
     *
     * @param newStatus the target status
     * @return a new suggestion with the updated status
     */
    public Suggestion withStatus(final SuggestionStatus newStatus) {
        return new Suggestion(id, titulo, descripcion, newStatus, creadoEn);
    }

    public Long id() {
        return id;
    }

    public String titulo() {
        return titulo;
    }

    public String descripcion() {
        return descripcion;
    }

    public SuggestionStatus estado() {
        return estado;
    }

    public OffsetDateTime creadoEn() {
        return creadoEn;
    }
}
