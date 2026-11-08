package com.example.examsservice.infrastructure.adapter.in.web.dto;

import com.example.examsservice.domain.model.Modalidad;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Web DTOs for the exams API.
 */
public final class ExamsDtos {

    private ExamsDtos() {
    }

    /** Exam create request. */
    public record ExamRequest(@NotBlank String codigo, @NotBlank String titulo, LocalDate fecha, String nivel,
                              String descripcion, boolean publicado, String articuloSlug) {
    }

    /** Exam update request. */
    public record ExamUpdateRequest(String titulo, LocalDate fecha, String nivel, String descripcion,
                                    Boolean publicado, String articuloSlug) {
    }

    /** Exam response. */
    public record ExamResponse(Long id, String codigo, String titulo, LocalDate fecha, String nivel,
                               String descripcion, boolean publicado, String articuloSlug, Instant createdAt) {
    }

    /** Paged exams response. */
    public record PagedExamsResponse(long total, List<ExamResponse> items) {
    }

    /** Question create request. */
    public record QuestionRequest(@Min(1) int orden, @NotBlank String enunciado,
                                  @NotNull @Size(min = 2, max = 8) List<@NotBlank String> opciones,
                                  String fuenteUrl) {
    }

    /** Question response. */
    public record QuestionResponse(Long id, long examenId, int orden, String enunciado, List<String> opciones,
                                   String fuenteUrl) {
    }

    /** Result create request. */
    public record ResultRequest(@NotNull UUID usuarioId, long examenId, @NotNull JsonNode respuestas,
                                @Min(0) int aciertos, @Min(1) int total, @NotNull BigDecimal nota) {
    }

    /** Result response. */
    public record ResultResponse(Long id, UUID usuarioId, long examenId, @JsonRawValue String respuestas,
                                 int aciertos, int total, BigDecimal nota, Instant fecha) {
    }

    /** Diagnostic create request. */
    public record DiagnosticRequest(@NotNull UUID usuarioId, int aciertos, int total, JsonNode desglose) {
    }

    /** Diagnostic response. */
    public record DiagnosticResponse(Long id, UUID usuarioId, int aciertos, int total,
                                     @JsonRawValue String desglose, Instant creadoEn) {
    }

    /** Simulation create request. */
    public record SimulationRequest(@NotNull UUID usuarioId, @NotNull Modalidad modalidad, int aciertos,
                                    int total, JsonNode respuestas, int duracionSegundos) {
    }

    /** Simulation response. */
    public record SimulationResponse(Long id, UUID usuarioId, Modalidad modalidad, int aciertos, int total,
                                     @JsonRawValue String respuestas, int duracionSegundos, Instant creadoEn) {
    }

    /** Error payload. */
    public record ErrorResponse(String code, String message) {
    }
}
