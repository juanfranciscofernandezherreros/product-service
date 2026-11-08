package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Immutable domain model for an article comment.
 *
 * @param id           identifier (nullable before persistence)
 * @param articuloSlug slug of the commented article
 * @param padreId      optional parent comment id
 * @param autorId      optional author id
 * @param contenido    content (1..2000 chars)
 * @param creadoEn     creation timestamp
 */
public record Comment(Long id, String articuloSlug, Long padreId, UUID autorId,
                      String contenido, OffsetDateTime creadoEn) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Comment {
        if (articuloSlug == null || articuloSlug.strip().isEmpty()) {
            throw new InvalidContentException("articuloSlug is required");
        }
        final String trimmed = contenido == null ? "" : contenido.strip();
        if (trimmed.isEmpty() || trimmed.length() > 2000) {
            throw new InvalidContentException("contenido must be between 1 and 2000 characters");
        }
    }

    /**
     * Factory for a new comment.
     *
     * @param articuloSlug article slug
     * @param padreId      optional parent id
     * @param autorId      optional author id
     * @param contenido    content
     * @return a new comment
     */
    public static Comment create(final String articuloSlug, final Long padreId, final UUID autorId,
                                 final String contenido) {
        return new Comment(null, articuloSlug, padreId, autorId, contenido, null);
    }
}
