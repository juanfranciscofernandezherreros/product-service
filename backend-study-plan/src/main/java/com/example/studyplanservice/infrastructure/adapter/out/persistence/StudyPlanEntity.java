package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/** JPA entity for study plans. */
@Entity
@Table(name = "study_plans")
public class StudyPlanEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "fecha_examen")
    private LocalDate fechaExamen;

    @Column(name = "minutos_dia", nullable = false)
    private int minutosDia;

    @Column(name = "dias_semana", nullable = false, length = 32)
    private String diasSemana;

    @Column(name = "prioridad", nullable = false, length = 16)
    private String prioridad;

    @Column(name = "hora_recordatorio")
    private LocalTime horaRecordatorio;

    @Column(name = "recordatorio_activo", nullable = false)
    private boolean recordatorioActivo;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    /** Default constructor required by JPA. */
    protected StudyPlanEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId             user id
     * @param fechaExamen        exam date
     * @param minutosDia         minutes per day
     * @param diasSemana         days CSV
     * @param prioridad          priority
     * @param horaRecordatorio   reminder time
     * @param recordatorioActivo reminder active
     * @param actualizadoEn      updated at
     */
    public StudyPlanEntity(final UUID userId, final LocalDate fechaExamen, final int minutosDia,
                           final String diasSemana, final String prioridad, final LocalTime horaRecordatorio,
                           final boolean recordatorioActivo, final Instant actualizadoEn) {
        this.userId = userId;
        this.fechaExamen = fechaExamen;
        this.minutosDia = minutosDia;
        this.diasSemana = diasSemana;
        this.prioridad = prioridad;
        this.horaRecordatorio = horaRecordatorio;
        this.recordatorioActivo = recordatorioActivo;
        this.actualizadoEn = actualizadoEn;
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

    /** @return days CSV */
    public String getDiasSemana() {
        return diasSemana;
    }

    /** @return priority */
    public String getPrioridad() {
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

    /** @return updated at */
    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
