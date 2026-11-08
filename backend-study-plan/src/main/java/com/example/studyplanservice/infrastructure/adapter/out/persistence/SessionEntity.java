package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** JPA entity for study sessions. */
@Entity
@Table(name = "study_sessions")
@IdClass(SessionId.class)
public class SessionEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "dia", nullable = false)
    private LocalDate dia;

    @Id
    @Column(name = "tarea", nullable = false, length = 16)
    private String tarea;

    @Column(name = "completada_en", nullable = false)
    private Instant completadaEn;

    /** Default constructor required by JPA. */
    protected SessionEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId       user id
     * @param dia          day
     * @param tarea        task
     * @param completadaEn completion instant
     */
    public SessionEntity(final UUID userId, final LocalDate dia, final String tarea, final Instant completadaEn) {
        this.userId = userId;
        this.dia = dia;
        this.tarea = tarea;
        this.completadaEn = completadaEn;
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
    public String getTarea() {
        return tarea;
    }

    /** @return completion instant */
    public Instant getCompletadaEn() {
        return completadaEn;
    }
}
