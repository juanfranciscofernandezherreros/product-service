package com.example.coursesservice.domain.model;

import com.example.coursesservice.domain.exception.InvalidCourseException;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

/**
 * Immutable domain model for a course activity.
 *
 * @param id           identifier (nullable before persistence)
 * @param slug         unique slug (matches {@code ^[a-z0-9][a-z0-9-]{2,99}$})
 * @param titulo       title
 * @param descripcion  optional description
 * @param destreza     skill
 * @param unidadId     optional unit id
 * @param articuloSlug optional linked article slug
 * @param orden        display order
 * @param publicado    published flag
 * @param creadoEn     creation timestamp
 */
public record Activity(Long id, String slug, String titulo, String descripcion, Destreza destreza,
                       Long unidadId, String articuloSlug, int orden, boolean publicado,
                       OffsetDateTime creadoEn) {

    private static final Pattern SLUG = Pattern.compile("^[a-z0-9][a-z0-9-]{2,99}$");

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Activity {
        if (slug == null || !SLUG.matcher(slug).matches()) {
            throw new InvalidCourseException("slug must match ^[a-z0-9][a-z0-9-]{2,99}$");
        }
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new InvalidCourseException("titulo is required");
        }
        if (destreza == null) {
            throw new InvalidCourseException("destreza is required");
        }
    }

    /**
     * Factory for a new activity (order defaults to 1, unpublished by default).
     *
     * @param slug         slug
     * @param titulo       title
     * @param descripcion  description
     * @param destreza     skill
     * @param unidadId     unit id
     * @param articuloSlug article slug
     * @param orden        order or null
     * @param publicado    published or null
     * @return a new activity
     */
    public static Activity create(final String slug, final String titulo, final String descripcion,
                                  final Destreza destreza, final Long unidadId, final String articuloSlug,
                                  final Integer orden, final Boolean publicado) {
        return new Activity(null, slug, titulo, descripcion, destreza, unidadId, articuloSlug,
                orden != null ? orden : 1, publicado != null && publicado, null);
    }
}
