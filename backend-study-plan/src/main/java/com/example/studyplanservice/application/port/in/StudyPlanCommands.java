package com.example.studyplanservice.application.port.in;

import com.example.studyplanservice.domain.model.GoalType;
import com.example.studyplanservice.domain.model.Prioridad;
import com.example.studyplanservice.domain.model.Tarea;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Input records for study-plan operations.
 */
public final class StudyPlanCommands {

    private StudyPlanCommands() {
    }

    /**
     * Upsert plan command.
     *
     * @param userId             user id
     * @param fechaExamen        exam date
     * @param minutosDia         minutes per day
     * @param diasSemana         days of week
     * @param prioridad          priority
     * @param horaRecordatorio   reminder time
     * @param recordatorioActivo reminder active
     */
    public record UpsertPlanCommand(UUID userId, LocalDate fechaExamen, int minutosDia, List<Integer> diasSemana,
                                    Prioridad prioridad, LocalTime horaRecordatorio, boolean recordatorioActivo) {
    }

    /**
     * Upsert AI plan command.
     *
     * @param userId  user id
     * @param semanas weeks
     * @param plan    plan JSON
     */
    public record UpsertAiPlanCommand(UUID userId, int semanas, String plan) {
    }

    /**
     * Upsert goal command.
     *
     * @param userId   user id
     * @param tipo     type
     * @param cantidad amount
     */
    public record UpsertGoalCommand(UUID userId, GoalType tipo, int cantidad) {
    }

    /**
     * Add session command.
     *
     * @param userId user id
     * @param dia    day
     * @param tarea  task
     */
    public record AddSessionCommand(UUID userId, LocalDate dia, Tarea tarea) {
    }
}
