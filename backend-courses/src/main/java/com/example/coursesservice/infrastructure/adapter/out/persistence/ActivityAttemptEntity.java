package com.example.coursesservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code actividad_intentos} table.
 */
@Entity
@Table(name = "actividad_intentos")
public class ActivityAttemptEntity {

    @Id
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;

    @Column(nullable = false, columnDefinition = "text")
    private String respuestas;

    @Column(nullable = false, columnDefinition = "text")
    private String detalle;

    @Column(nullable = false)
    private int aciertos;

    @Column(nullable = false)
    private int evaluables;

    @Column(name = "pendientes_revision", nullable = false)
    private int pendientesRevision;

    @Column(nullable = false)
    private OffsetDateTime fecha;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(final UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(final Long actividadId) {
        this.actividadId = actividadId;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(final String respuestas) {
        this.respuestas = respuestas;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(final String detalle) {
        this.detalle = detalle;
    }

    public int getAciertos() {
        return aciertos;
    }

    public void setAciertos(final int aciertos) {
        this.aciertos = aciertos;
    }

    public int getEvaluables() {
        return evaluables;
    }

    public void setEvaluables(final int evaluables) {
        this.evaluables = evaluables;
    }

    public int getPendientesRevision() {
        return pendientesRevision;
    }

    public void setPendientesRevision(final int pendientesRevision) {
        this.pendientesRevision = pendientesRevision;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public void setFecha(final OffsetDateTime fecha) {
        this.fecha = fecha;
    }
}
