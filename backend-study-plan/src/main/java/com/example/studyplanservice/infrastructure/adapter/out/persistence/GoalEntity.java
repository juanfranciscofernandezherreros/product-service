package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for weekly goals. */
@Entity
@Table(name = "weekly_goals")
public class GoalEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "tipo", nullable = false, length = 16)
    private String tipo;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    /** Default constructor required by JPA. */
    protected GoalEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId        user id
     * @param tipo          type
     * @param cantidad      amount
     * @param actualizadoEn updated at
     */
    public GoalEntity(final UUID userId, final String tipo, final int cantidad, final Instant actualizadoEn) {
        this.userId = userId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.actualizadoEn = actualizadoEn;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return type */
    public String getTipo() {
        return tipo;
    }

    /** @return amount */
    public int getCantidad() {
        return cantidad;
    }

    /** @return updated at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
