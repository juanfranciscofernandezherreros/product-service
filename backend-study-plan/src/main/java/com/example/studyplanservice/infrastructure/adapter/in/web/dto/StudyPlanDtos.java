package com.example.studyplanservice.infrastructure.adapter.in.web.dto;

import com.example.studyplanservice.domain.model.GoalType;
import com.example.studyplanservice.domain.model.Prioridad;
import com.example.studyplanservice.domain.model.Tarea;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Web DTOs for the study-plan API.
 */
public final class StudyPlanDtos {

    private StudyPlanDtos() {
    }

    /**
     * Plan upsert request.
     *
     * @param fechaExamen        exam date
     * @param minutosDia         minutes per day
     * @param diasSemana         days of week
     * @param prioridad          priority
     * @param horaRecordatorio   reminder time
     * @param recordatorioActivo reminder active
     */
    public record PlanUpsertRequest(LocalDate fechaExamen, @Min(10) @Max(180) int minutosDia,
                                    @NotNull List<Integer> diasSemana, @NotNull Prioridad prioridad,
                                    LocalTime horaRecordatorio, boolean recordatorioActivo) {
    }

    /**
     * Plan response.
     *
     * @param userId             user id
     * @param fechaExamen        exam date
     * @param minutosDia         minutes per day
     * @param diasSemana         days of week
     * @param prioridad          priority
     * @param horaRecordatorio   reminder time
     * @param recordatorioActivo reminder active
     * @param actualizadoEn      updated at
     */
    public record PlanResponse(UUID userId, LocalDate fechaExamen, int minutosDia, List<Integer> diasSemana,
                               Prioridad prioridad, LocalTime horaRecordatorio, boolean recordatorioActivo,
                               Instant actualizadoEn) {
    }

    /**
     * AI plan upsert request.
     *
     * @param semanas weeks
     * @param plan    plan JSON
     */
    public record AiPlanUpsertRequest(int semanas, @NotNull JsonNode plan) {
    }

    /**
     * AI plan response.
     *
     * @param userId        user id
     * @param semanas       weeks
     * @param plan          plan JSON (raw)
     * @param actualizadoEn updated at
     */
    public record AiPlanResponse(UUID userId, int semanas, @JsonRawValue String plan, Instant actualizadoEn) {
    }

    /**
     * Goal upsert request.
     *
     * @param tipo     type
     * @param cantidad amount
     */
    public record GoalUpsertRequest(@NotNull GoalType tipo, @Min(1) @Max(20) int cantidad) {
    }

    /**
     * Goal response.
     *
     * @param userId        user id
     * @param tipo          type
     * @param cantidad      amount
     * @param actualizadoEn updated at
     */
    public record GoalResponse(UUID userId, GoalType tipo, int cantidad, Instant actualizadoEn) {
    }

    /**
     * Session create request.
     *
     * @param dia   day
     * @param tarea task
     */
    public record SessionCreateRequest(LocalDate dia, @NotNull Tarea tarea) {
    }

    /**
     * Session response.
     *
     * @param userId       user id
     * @param dia          day
     * @param tarea        task
     * @param completadaEn completion instant
     */
    public record SessionResponse(UUID userId, LocalDate dia, Tarea tarea, Instant completadaEn) {
    }

    /**
     * Weekly report response.
     *
     * @param userId       user id
     * @param semanaInicio week start
     * @param semanaFin    week end
     * @param informe      report JSON (raw)
     * @param generadoEn   generation instant
     */
    public record ReportResponse(UUID userId, LocalDate semanaInicio, LocalDate semanaFin,
                                 @JsonRawValue String informe, Instant generadoEn) {
    }

    /**
     * Error payload.
     *
     * @param code    code
     * @param message message
     */
    public record ErrorResponse(String code, String message) {
    }
}
