package com.example.examsservice.application.port.in;

import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Modalidad;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Input records for exams operations.
 */
public final class ExamsCommands {

    private ExamsCommands() {
    }

    /**
     * Create exam command.
     *
     * @param codigo       code
     * @param titulo       title
     * @param fecha        date
     * @param nivel        level
     * @param descripcion  description
     * @param publicado    published
     * @param articuloSlug article slug
     */
    public record CreateExamCommand(String codigo, String titulo, LocalDate fecha, String nivel,
                                    String descripcion, boolean publicado, String articuloSlug) {
    }

    /**
     * Update exam command.
     *
     * @param titulo       title
     * @param fecha        date
     * @param nivel        level
     * @param descripcion  description
     * @param publicado    published
     * @param articuloSlug article slug
     */
    public record UpdateExamCommand(String titulo, LocalDate fecha, String nivel, String descripcion,
                                    Boolean publicado, String articuloSlug) {
    }

    /**
     * List exams query.
     *
     * @param nivel     optional level
     * @param publicado optional published
     * @param limit     page size
     * @param offset    results to skip
     */
    public record ListExamsQuery(String nivel, Boolean publicado, int limit, int offset) {
    }

    /**
     * Paged exams result.
     *
     * @param total total
     * @param items items
     */
    public record PagedExams(long total, List<Exam> items) {
    }

    /**
     * Add question command.
     *
     * @param orden     order
     * @param enunciado prompt
     * @param opciones  options
     * @param fuenteUrl source url
     */
    public record AddQuestionCommand(int orden, String enunciado, List<String> opciones, String fuenteUrl) {
    }

    /**
     * Create result command.
     *
     * @param usuarioId  user id
     * @param examenId   exam id
     * @param respuestas answers JSON
     * @param aciertos   correct count
     * @param total      total
     * @param nota       score
     */
    public record CreateResultCommand(UUID usuarioId, long examenId, String respuestas, int aciertos,
                                      int total, BigDecimal nota) {
    }

    /**
     * Create diagnostic command.
     *
     * @param usuarioId user id
     * @param aciertos  correct count
     * @param total     total
     * @param desglose  breakdown JSON
     */
    public record CreateDiagnosticCommand(UUID usuarioId, int aciertos, int total, String desglose) {
    }

    /**
     * Create simulation command.
     *
     * @param usuarioId        user id
     * @param modalidad        modality
     * @param aciertos         correct count
     * @param total            total
     * @param respuestas       answers JSON
     * @param duracionSegundos duration
     */
    public record CreateSimulationCommand(UUID usuarioId, Modalidad modalidad, int aciertos, int total,
                                          String respuestas, int duracionSegundos) {
    }
}
