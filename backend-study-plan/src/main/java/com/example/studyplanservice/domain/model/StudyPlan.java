package com.example.studyplanservice.domain.model;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Immutable domain representation of a user's study plan.
 */
public final class StudyPlan {

    private static final int MIN_MIN = 10;
    private static final int MAX_MIN = 180;

    private final UUID userId;
    private final LocalDate fechaExamen;
    private final int minutosDia;
    private final List<Integer> diasSemana;
    private final Prioridad prioridad;
    private final LocalTime horaRecordatorio;
    private final boolean recordatorioActivo;
    private final Instant actualizadoEn;

    /**
     * Creates a validated study plan.
     *
     * @param userId             user id
     * @param fechaExamen        exam date (nullable)
     * @param minutosDia         minutes per day (10..180)
     * @param diasSemana         days of week (0..6, 1..7 entries)
     * @param prioridad          priority
     * @param horaRecordatorio   reminder time (nullable)
     * @param recordatorioActivo reminder active
     * @param actualizadoEn      updated-at instant
     */
    public StudyPlan(final UUID userId, final LocalDate fechaExamen, final int minutosDia,
                     final List<Integer> diasSemana, final Prioridad prioridad, final LocalTime horaRecordatorio,
                     final boolean recordatorioActivo, final Instant actualizadoEn) {
        if (userId == null) {
            throw new InvalidStudyPlanException("userId is required");
        }
        if (minutosDia < MIN_MIN || minutosDia > MAX_MIN) {
            throw new InvalidStudyPlanException("minutosDia must be between 10 and 180");
        }
        if (diasSemana == null || diasSemana.isEmpty() || diasSemana.size() > 7) {
            throw new InvalidStudyPlanException("diasSemana must contain between 1 and 7 entries");
        }
        for (final int d : diasSemana) {
            if (d < 0 || d > 6) {
                throw new InvalidStudyPlanException("diasSemana entries must be between 0 and 6");
            }
        }
        if (prioridad == null) {
            throw new InvalidStudyPlanException("prioridad is required");
        }
        this.userId = userId;
        this.fechaExamen = fechaExamen;
        this.minutosDia = minutosDia;
        this.diasSemana = List.copyOf(diasSemana);
        this.prioridad = prioridad;
        this.horaRecordatorio = horaRecordatorio;
        this.recordatorioActivo = recordatorioActivo;
        this.actualizadoEn = actualizadoEn == null ? Instant.now() : actualizadoEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return exam date */
    public LocalDate getFechaExamen() {
        return fechaExamen;
    }

    /** @return minutes per day */
    public int getMinutosDia() {
        return minutosDia;
    }

    /** @return days of week */
    public List<Integer> getDiasSemana() {
        return diasSemana;
    }

    /** @return priority */
    public Prioridad getPrioridad() {
        return prioridad;
    }

    /** @return reminder time */
    public LocalTime getHoraRecordatorio() {
        return horaRecordatorio;
    }

    /** @return reminder active */
    public boolean isRecordatorioActivo() {
        return recordatorioActivo;
    }

    /** @return updated-at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
