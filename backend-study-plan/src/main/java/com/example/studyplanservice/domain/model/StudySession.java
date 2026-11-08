package com.example.studyplanservice.domain.model;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Immutable domain representation of a completed study session.
 */
public final class StudySession {

    private final UUID userId;
    private final LocalDate dia;
    private final Tarea tarea;
    private final Instant completadaEn;

    /**
     * Creates a validated session.
     *
     * @param userId       user id
     * @param dia          day
     * @param tarea        task
     * @param completadaEn completion instant
     */
    public StudySession(final UUID userId, final LocalDate dia, final Tarea tarea, final Instant completadaEn) {
        if (userId == null) {
            throw new InvalidStudyPlanException("userId is required");
        }
        if (tarea == null) {
            throw new InvalidStudyPlanException("tarea is required");
        }
        this.userId = userId;
        this.dia = dia == null ? LocalDate.now() : dia;
        this.tarea = tarea;
        this.completadaEn = completadaEn == null ? Instant.now() : completadaEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return day */
    public LocalDate getDia() {
        return dia;
    }

    /** @return task */
    public Tarea getTarea() {
        return tarea;
    }

    /** @return completion instant */
    public Instant getCompletadaEn() {
        return completadaEn;
    }
}
