package com.example.studyplanservice.domain.model;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Immutable domain representation of a user's AI study plan.
 */
public final class AiPlan {

    private static final List<Integer> WEEKS = List.of(4, 8, 12, 16, 24);

    private final UUID userId;
    private final int semanas;
    private final String plan;
    private final Instant actualizadoEn;

    /**
     * Creates a validated AI plan.
     *
     * @param userId        user id
     * @param semanas       weeks (4/8/12/16/24)
     * @param plan          plan JSON
     * @param actualizadoEn updated-at instant
     */
    public AiPlan(final UUID userId, final int semanas, final String plan, final Instant actualizadoEn) {
        if (userId == null) {
            throw new InvalidStudyPlanException("userId is required");
        }
        if (!WEEKS.contains(semanas)) {
            throw new InvalidStudyPlanException("semanas must be one of " + WEEKS);
        }
        if (plan == null || plan.trim().isEmpty()) {
            throw new InvalidStudyPlanException("plan is required");
        }
        this.userId = userId;
        this.semanas = semanas;
        this.plan = plan;
        this.actualizadoEn = actualizadoEn == null ? Instant.now() : actualizadoEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return weeks */
    public int getSemanas() {
        return semanas;
    }

    /** @return plan JSON */
    public String getPlan() {
        return plan;
    }

    /** @return updated-at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
