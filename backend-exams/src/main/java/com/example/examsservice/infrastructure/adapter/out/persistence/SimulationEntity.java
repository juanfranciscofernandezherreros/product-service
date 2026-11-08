package com.example.examsservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for B2 simulations. */
@Entity
@Table(name = "simulacros_b2")
public class SimulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "modalidad", nullable = false, length = 32)
    private String modalidad;

    @Column(name = "aciertos", nullable = false)
    private int aciertos;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "respuestas", nullable = false, length = 8000)
    private String respuestas;

    @Column(name = "duracion_segundos", nullable = false)
    private int duracionSegundos;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    /** Default constructor required by JPA. */
    protected SimulationEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id               id
     * @param usuarioId        user id
     * @param modalidad        modality name
     * @param aciertos         correct count
     * @param total            total
     * @param respuestas       answers JSON
     * @param duracionSegundos duration
     * @param creadoEn         instant
     */
    public SimulationEntity(final Long id, final UUID usuarioId, final String modalidad, final int aciertos,
                            final int total, final String respuestas, final int duracionSegundos,
                            final Instant creadoEn) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.modalidad = modalidad;
        this.aciertos = aciertos;
        this.total = total;
        this.respuestas = respuestas;
        this.duracionSegundos = duracionSegundos;
        this.creadoEn = creadoEn;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return modality name */
    public String getModalidad() {
        return modalidad;
    }

    /** @return correct count */
    public int getAciertos() {
        return aciertos;
    }

    /** @return total */
    public int getTotal() {
        return total;
    }

    /** @return answers JSON */
    public String getRespuestas() {
        return respuestas;
    }

    /** @return duration */
    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    /** @return instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
