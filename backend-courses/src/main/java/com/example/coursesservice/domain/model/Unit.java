package com.example.coursesservice.domain.model;

import com.example.coursesservice.domain.exception.InvalidCourseException;

/**
 * Immutable domain model for a course unit.
 *
 * @param id          identifier (nullable before persistence)
 * @param slug        unique slug
 * @param titulo      title
 * @param descripcion optional description
 * @param nivel       level
 * @param orden       display order
 * @param publicado   published flag
 */
public record Unit(Long id, String slug, String titulo, String descripcion, String nivel,
                   int orden, boolean publicado) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Unit {
        if (slug == null || slug.strip().isEmpty()) {
            throw new InvalidCourseException("slug is required");
        }
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new InvalidCourseException("titulo is required");
        }
        if (nivel == null || nivel.strip().isEmpty()) {
            throw new InvalidCourseException("nivel is required");
        }
    }

    /**
     * Factory for a new unit (order defaults to 100, published by default).
     *
     * @param slug        slug
     * @param titulo      title
     * @param descripcion description
     * @param nivel       level
     * @param orden       order or null
     * @param publicado   published or null
     * @return a new unit
     */
    public static Unit create(final String slug, final String titulo, final String descripcion,
                              final String nivel, final Integer orden, final Boolean publicado) {
        return new Unit(null, slug, titulo, descripcion, nivel,
                orden != null ? orden : 100, publicado == null || publicado);
    }
}
