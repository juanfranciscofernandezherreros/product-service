package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

/**
 * Immutable domain model for a blog category.
 *
 * @param slug        primary key slug
 * @param nombre      display name
 * @param descripcion optional description
 * @param orden       display order
 */
public record Category(String slug, String nombre, String descripcion, int orden) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Category {
        if (slug == null || slug.strip().isEmpty()) {
            throw new InvalidContentException("slug is required");
        }
        if (nombre == null || nombre.strip().isEmpty()) {
            throw new InvalidContentException("nombre is required");
        }
    }

    /**
     * Factory applying the default order when none is given.
     *
     * @param slug        slug
     * @param nombre      name
     * @param descripcion description
     * @param orden       optional order
     * @return a new category
     */
    public static Category create(final String slug, final String nombre, final String descripcion,
                                  final Integer orden) {
        return new Category(slug, nombre, descripcion, orden != null ? orden : 0);
    }
}
