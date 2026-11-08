package com.example.coursesservice.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Request and response DTOs for the Courses web adapter.
 */
public final class CoursesDtos {

    private CoursesDtos() {
    }

    /** Create/update course request. */
    public record CourseRequest(@NotBlank String nombre, String descripcion, Boolean activo) {
    }

    /** Course response. */
    public record CourseResponse(Long id, String nombre, String descripcion, boolean activo,
                                 OffsetDateTime creadoEn) {
    }

    /** Create unit request. */
    public record CreateUnitRequest(@NotBlank String slug, @NotBlank String titulo, String descripcion,
                                    @NotBlank String nivel, Integer orden, Boolean publicado) {
    }

    /** Unit response. */
    public record UnitResponse(Long id, String slug, String titulo, String descripcion, String nivel,
                               int orden, boolean publicado) {
    }

    /** Create activity request. */
    public record CreateActivityRequest(@NotBlank String slug, @NotBlank String titulo,
                                        String descripcion, @NotBlank String destreza, Long unidadId,
                                        String articuloSlug, Integer orden, Boolean publicado) {
    }

    /** Activity response. */
    public record ActivityResponse(Long id, String slug, String titulo, String descripcion,
                                   String destreza, Long unidadId, String articuloSlug, int orden,
                                   boolean publicado, OffsetDateTime creadoEn) {
    }

    /** Create question request. */
    public record CreateQuestionRequest(@Min(1) int orden, @NotBlank String tipo,
                                        @NotBlank String enunciado, JsonNode configuracion,
                                        String medioTipo, String medioUrl, String medioAlt) {
    }

    /** Question response (configuracion is emitted as raw JSON). */
    public record QuestionResponse(Long id, Long actividadId, int orden, String tipo, String enunciado,
                                   @JsonRawValue String configuracion, String medioTipo, String medioUrl,
                                   String medioAlt) {
    }

    /** Create attempt request. */
    public record CreateAttemptRequest(@NotNull UUID usuarioId, @NotNull JsonNode respuestas,
                                       @NotNull JsonNode detalle, @PositiveOrZero int aciertos,
                                       @PositiveOrZero int evaluables,
                                       @PositiveOrZero int pendientesRevision) {
    }

    /** Attempt response (respuestas/detalle emitted as raw JSON). */
    public record AttemptResponse(UUID id, UUID usuarioId, Long actividadId,
                                  @JsonRawValue String respuestas, @JsonRawValue String detalle,
                                  int aciertos, int evaluables, int pendientesRevision,
                                  OffsetDateTime fecha) {
    }

    /** Generic paginated wrapper. */
    public record PagedResponse<T>(List<T> items, long total, int offset, int limit) {
    }

    /** Error payload. */
    public record ErrorResponse(String code, String message) {
    }
}
