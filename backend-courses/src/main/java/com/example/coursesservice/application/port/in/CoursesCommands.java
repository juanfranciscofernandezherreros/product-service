package com.example.coursesservice.application.port.in;

import java.util.UUID;

/**
 * Command records carrying validated input into the use cases.
 */
public final class CoursesCommands {

    private CoursesCommands() {
    }

    /** Command to create or update a course. */
    public record SaveCourse(Long id, String nombre, String descripcion, Boolean activo) {
    }

    /** Command to create a unit. */
    public record CreateUnit(String slug, String titulo, String descripcion, String nivel,
                             Integer orden, Boolean publicado) {
    }

    /** Command to create an activity. */
    public record CreateActivity(String slug, String titulo, String descripcion, String destreza,
                                 Long unidadId, String articuloSlug, Integer orden, Boolean publicado) {
    }

    /** Command to add a question to an activity. */
    public record CreateQuestion(Long actividadId, int orden, String tipo, String enunciado,
                                 String configuracion, String medioTipo, String medioUrl,
                                 String medioAlt) {
    }

    /** Command to register an activity attempt. */
    public record CreateAttempt(Long actividadId, UUID usuarioId, String respuestas, String detalle,
                                int aciertos, int evaluables, int pendientesRevision) {
    }
}
