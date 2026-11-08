package com.example.coursesservice.domain.model;

import com.example.coursesservice.domain.exception.InvalidCourseException;

import java.time.OffsetDateTime;

/**
 * Immutable domain model for a course.
 *
 * @param id          identifier (nullable before persistence)
 * @param nombre      course name (non-blank)
 * @param descripcion optional description
 * @param activo      active flag
 * @param creadoEn    creation timestamp
 */
public record Course(Long id, String nombre, String descripcion, boolean activo,
                     OffsetDateTime creadoEn) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Course {
        if (nombre == null || nombre.strip().isEmpty()) {
            throw new InvalidCourseException("nombre is required");
        }
    }

    /**
     * Factory for a new course (active by default when not specified).
     *
     * @param nombre      name
     * @param descripcion description
     * @param activo      active flag or null
     * @return a new course
     */
    public static Course create(final String nombre, final String descripcion, final Boolean activo) {
        return new Course(null, nombre, descripcion, activo == null || activo, null);
    }

    /**
     * Returns a copy with mutable fields replaced.
     *
     * @param newNombre      new name
     * @param newDescripcion new description
     * @param newActivo      new active flag or null (keeps current)
     * @return an updated course
     */
    public Course update(final String newNombre, final String newDescripcion, final Boolean newActivo) {
        return new Course(id, newNombre, newDescripcion, newActivo == null ? activo : newActivo, creadoEn);
    }
}
