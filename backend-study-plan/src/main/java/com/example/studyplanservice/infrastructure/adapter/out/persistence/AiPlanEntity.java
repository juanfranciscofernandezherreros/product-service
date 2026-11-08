package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for AI plans. */
@Entity
@Table(name = "ai_plans")
public class AiPlanEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "semanas", nullable = false)
    private int semanas;

    @Column(name = "plan", nullable = false, length = 8000)
    private String plan;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    /** Default constructor required by JPA. */
    protected AiPlanEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId        user id
     * @param semanas       weeks
     * @param plan          plan JSON
     * @param actualizadoEn updated at
     */
    public AiPlanEntity(final UUID userId, final int semanas, final String plan, final Instant actualizadoEn) {
        this.userId = userId;
        this.semanas = semanas;
        this.plan = plan;
        this.actualizadoEn = actualizadoEn;
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

    /** @return updated at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
