package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** JPA entity for weekly reports. */
@Entity
@Table(name = "weekly_reports")
@IdClass(ReportId.class)
public class ReportEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio;

    @Column(name = "semana_fin", nullable = false)
    private LocalDate semanaFin;

    @Column(name = "informe", nullable = false, length = 8000)
    private String informe;

    @Column(name = "generado_en", nullable = false)
    private Instant generadoEn;

    /** Default constructor required by JPA. */
    protected ReportEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId       user id
     * @param semanaInicio week start
     * @param semanaFin    week end
     * @param informe      report JSON
     * @param generadoEn   generation instant
     */
    public ReportEntity(final UUID userId, final LocalDate semanaInicio, final LocalDate semanaFin,
                        final String informe, final Instant generadoEn) {
        this.userId = userId;
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaFin;
        this.informe = informe;
        this.generadoEn = generadoEn;
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
