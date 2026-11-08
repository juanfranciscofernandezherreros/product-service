package com.example.coursesservice.domain.model;

import com.example.coursesservice.domain.exception.InvalidCourseException;

import java.util.regex.Pattern;

/**
 * Immutable domain model for an activity question.
 *
 * @param id            identifier (nullable before persistence)
 * @param actividadId   owning activity id
 * @param orden         order (positive)
 * @param tipo          question type
 * @param enunciado     statement
 * @param configuracion JSON configuration (object)
 * @param medioTipo     optional media type ({@code audio}/{@code imagen})
 * @param medioUrl      optional media URL (https)
 * @param medioAlt      optional media alt text
 */
public record ActivityQuestion(Long id, Long actividadId, int orden, QuestionType tipo,
                               String enunciado, String configuracion, String medioTipo,
                               String medioUrl, String medioAlt) {

    private static final Pattern URL = Pattern.compile("^https://\\S+$");

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public ActivityQuestion {
        if (actividadId == null) {
            throw new InvalidCourseException("actividadId is required");
        }
        if (orden <= 0) {
            throw new InvalidCourseException("orden must be positive");
        }
        if (tipo == null) {
            throw new InvalidCourseException("tipo is required");
        }
        if (enunciado == null || enunciado.strip().isEmpty()) {
            throw new InvalidCourseException("enunciado is required");
        }
        if (medioTipo != null && !medioTipo.equals("audio") && !medioTipo.equals("imagen")) {
            throw new InvalidCourseException("medioTipo must be audio or imagen");
        }
        if (medioTipo != null && (medioUrl == null || !URL.matcher(medioUrl).matches())) {
            throw new InvalidCourseException("medioUrl must be an https URL when medioTipo is set");
        }
        if (medioTipo == null && medioUrl != null) {
            throw new InvalidCourseException("medioUrl requires medioTipo");
        }
    }

    /**
     * Factory for a new question (configuration defaults to an empty object).
     *
     * @param actividadId   activity id
     * @param orden         order
     * @param tipo          type
     * @param enunciado     statement
     * @param configuracion JSON configuration or null
     * @param medioTipo     media type
     * @param medioUrl      media URL
     * @param medioAlt      media alt text
     * @return a new question
     */
    public static ActivityQuestion create(final Long actividadId, final int orden, final QuestionType tipo,
                                          final String enunciado, final String configuracion,
                                          final String medioTipo, final String medioUrl,
                                          final String medioAlt) {
        return new ActivityQuestion(null, actividadId, orden, tipo, enunciado,
                (configuracion == null || configuracion.isBlank()) ? "{}" : configuracion,
                medioTipo, medioUrl, medioAlt);
    }
}
