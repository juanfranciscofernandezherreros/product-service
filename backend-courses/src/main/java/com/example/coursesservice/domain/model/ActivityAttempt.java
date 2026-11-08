package com.example.coursesservice.domain.model;

import com.example.coursesservice.domain.exception.InvalidCourseException;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Immutable domain model for an activity attempt.
 *
 * @param id                 identifier
 * @param usuarioId          user id
 * @param actividadId        activity id
 * @param respuestas         JSON answers (object)
 * @param detalle            JSON detail (object)
 * @param aciertos           correct count (>= 0)
 * @param evaluables         evaluable count (>= 0)
 * @param pendientesRevision pending-review count (>= 0)
 * @param fecha              timestamp
 */
public record ActivityAttempt(UUID id, UUID usuarioId, Long actividadId, String respuestas,
                              String detalle, int aciertos, int evaluables, int pendientesRevision,
                              OffsetDateTime fecha) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public ActivityAttempt {
        if (id == null) {
            throw new InvalidCourseException("id is required");
        }
        if (usuarioId == null) {
            throw new InvalidCourseException("usuarioId is required");
        }
        if (actividadId == null) {
            throw new InvalidCourseException("actividadId is required");
        }
        if (respuestas == null || respuestas.isBlank()) {
            throw new InvalidCourseException("respuestas is required");
        }
        if (detalle == null || detalle.isBlank()) {
            throw new InvalidCourseException("detalle is required");
        }
        if (aciertos < 0 || evaluables < 0 || pendientesRevision < 0) {
            throw new InvalidCourseException("counters must be non-negative");
        }
    }

    /**
     * Factory for a new attempt with a generated id.
     *
     * @param usuarioId          user id
     * @param actividadId        activity id
     * @param respuestas         JSON answers
     * @param detalle            JSON detail
     * @param aciertos           correct count
     * @param evaluables         evaluable count
     * @param pendientesRevision pending-review count
     * @return a new attempt
     */
    public static ActivityAttempt create(final UUID usuarioId, final Long actividadId,
                                         final String respuestas, final String detalle,
                                         final int aciertos, final int evaluables,
                                         final int pendientesRevision) {
        return new ActivityAttempt(UUID.randomUUID(), usuarioId, actividadId, respuestas, detalle,
                aciertos, evaluables, pendientesRevision, null);
    }
}
