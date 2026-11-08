package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Immutable domain model for a downloadable resource.
 *
 * @param id               identifier (nullable before persistence)
 * @param slug             unique slug
 * @param titulo           title
 * @param descripcion      optional description
 * @param categoria        category (defaults to {@code apuntes})
 * @param fechaPublicacion optional publication date
 * @param urlArchivo       file URL
 * @param urlLectura       optional reading URL
 * @param orden            display order
 * @param publicado        published flag
 * @param creadoEn         creation timestamp
 */
public record Downloadable(Long id, String slug, String titulo, String descripcion, String categoria,
                           LocalDate fechaPublicacion, String urlArchivo, String urlLectura,
                           int orden, boolean publicado, OffsetDateTime creadoEn) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Downloadable {
        if (slug == null || slug.strip().isEmpty()) {
            throw new InvalidContentException("slug is required");
        }
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new InvalidContentException("titulo is required");
        }
        if (urlArchivo == null || urlArchivo.strip().isEmpty()) {
            throw new InvalidContentException("urlArchivo is required");
        }
    }

    /**
     * Factory for a new downloadable (category defaults to {@code apuntes}).
     *
     * @param slug             slug
     * @param titulo           title
     * @param descripcion      description
     * @param categoria        category
     * @param fechaPublicacion publication date
     * @param urlArchivo       file URL
     * @param urlLectura       reading URL
     * @return a new downloadable
     */
    public static Downloadable create(final String slug, final String titulo, final String descripcion,
                                      final String categoria, final LocalDate fechaPublicacion,
                                      final String urlArchivo, final String urlLectura) {
        return new Downloadable(null, slug, titulo, descripcion,
                (categoria == null || categoria.isBlank()) ? "apuntes" : categoria,
                fechaPublicacion, urlArchivo, urlLectura, 100, true, null);
    }
}
