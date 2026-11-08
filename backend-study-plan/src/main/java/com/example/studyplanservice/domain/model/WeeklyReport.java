package com.example.studyplanservice.domain.model;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Immutable domain representation of a user's weekly report.
 */
public final class WeeklyReport {

    private final UUID userId;
    private final LocalDate semanaInicio;
    private final LocalDate semanaFin;
    private final String informe;
    private final Instant generadoEn;

    /**
     * Creates a validated report.
     *
     * @param userId       user id
     * @param semanaInicio week start
     * @param semanaFin    week end
     * @param informe      report JSON
     * @param generadoEn   generation instant
     */
    public WeeklyReport(final UUID userId, final LocalDate semanaInicio, final LocalDate semanaFin,
                        final String informe, final Instant generadoEn) {
        if (userId == null || semanaInicio == null || semanaFin == null) {
            throw new InvalidStudyPlanException("userId, semanaInicio and semanaFin are required");
        }
        if (semanaFin.isBefore(semanaInicio)) {
            throw new InvalidStudyPlanException("semanaFin must not be before semanaInicio");
        }
        if (informe == null || informe.trim().isEmpty()) {
            throw new InvalidStudyPlanException("informe is required");
        }
        this.userId = userId;
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaFin;
        this.informe = informe;
        this.generadoEn = generadoEn == null ? Instant.now() : generadoEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return week start */
    public LocalDate getSemanaInicio() {
        return semanaInicio;
    }

    /** @return week end */
    public LocalDate getSemanaFin() {
        return semanaFin;
    }

    /** @return report JSON */
    public String getInforme() {
        return informe;
    }

    /** @return generation instant */
    public Instant getGeneradoEn() {
        return generadoEn;
    }
}
