package com.example.studyplanservice.domain.model;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a user's weekly goal.
 */
public final class WeeklyGoal {

    private static final int MIN = 1;
    private static final int MAX = 20;

    private final UUID userId;
    private final GoalType tipo;
    private final int cantidad;
    private final Instant actualizadoEn;

    /**
     * Creates a validated goal.
     *
     * @param userId        user id
     * @param tipo          goal type
     * @param cantidad      amount (1..20)
     * @param actualizadoEn updated-at instant
     */
    public WeeklyGoal(final UUID userId, final GoalType tipo, final int cantidad, final Instant actualizadoEn) {
        if (userId == null) {
            throw new InvalidStudyPlanException("userId is required");
        }
        if (tipo == null) {
            throw new InvalidStudyPlanException("tipo is required");
        }
        if (cantidad < MIN || cantidad > MAX) {
            throw new InvalidStudyPlanException("cantidad must be between 1 and 20");
        }
        this.userId = userId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.actualizadoEn = actualizadoEn == null ? Instant.now() : actualizadoEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return type */
    public GoalType getTipo() {
        return tipo;
    }

    /** @return amount */
    public int getCantidad() {
        return cantidad;
    }

    /** @return updated-at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
