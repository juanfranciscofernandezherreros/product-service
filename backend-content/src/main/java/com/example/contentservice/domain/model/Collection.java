package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Immutable domain model for a material collection.
 *
 * @param id                    identifier (nullable before persistence)
 * @param slug                  unique slug
 * @param titulo                title
 * @param descripcion           optional description
 * @param tipo                  collection type
 * @param incluirTodosArticulos whether all articles are included
 * @param nombreArchivo         PDF file name (matches {@code <name>.pdf})
 * @param fechaPublicacion      optional publication date
 * @param publicado             published flag
 */
public record Collection(Long id, String slug, String titulo, String descripcion, CollectionType tipo,
                         boolean incluirTodosArticulos, String nombreArchivo, LocalDate fechaPublicacion,
                         boolean publicado) {

    private static final Pattern FILE = Pattern.compile("^[a-zA-Z0-9_-]+[.]pdf$");

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Collection {
        if (slug == null || slug.strip().isEmpty()) {
            throw new InvalidContentException("slug is required");
        }
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new InvalidContentException("titulo is required");
        }
        if (tipo == null) {
            throw new InvalidContentException("tipo is required");
        }
        if (nombreArchivo == null || !FILE.matcher(nombreArchivo).matches()) {
            throw new InvalidContentException("nombreArchivo must match <name>.pdf");
        }
    }

    /**
     * Factory for a new collection (published by default).
     *
     * @param slug                  slug
     * @param titulo                title
     * @param descripcion           description
     * @param tipo                  type
     * @param incluirTodosArticulos include-all flag
     * @param nombreArchivo         file name
     * @param fechaPublicacion      publication date
     * @return a new collection
     */
    public static Collection create(final String slug, final String titulo, final String descripcion,
                                    final CollectionType tipo, final Boolean incluirTodosArticulos,
                                    final String nombreArchivo, final LocalDate fechaPublicacion) {
        return new Collection(null, slug, titulo, descripcion, tipo,
                incluirTodosArticulos != null && incluirTodosArticulos, nombreArchivo,
                fechaPublicacion, true);
    }
}
