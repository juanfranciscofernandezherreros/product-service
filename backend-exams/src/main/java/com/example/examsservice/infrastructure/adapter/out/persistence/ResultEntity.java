package com.example.examsservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for exam results. */
@Entity
@Table(name = "resultados")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "examen_id", nullable = false)
    private Long examenId;

    @Column(name = "respuestas", nullable = false, length = 8000)
    private String respuestas;

    @Column(name = "aciertos", nullable = false)
    private int aciertos;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "nota", nullable = false)
    private BigDecimal nota;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    /** Default constructor required by JPA. */
    protected ResultEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id         id
     * @param usuarioId  user id
     * @param examenId   exam id
     * @param respuestas answers JSON
     * @param aciertos   correct count
     * @param total      total
     * @param nota       score
     * @param fecha      instant
     */
    public ResultEntity(final Long id, final UUID usuarioId, final Long examenId, final String respuestas,
                        final int aciertos, final int total, final BigDecimal nota, final Instant fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.examenId = examenId;
        this.respuestas = respuestas;
        this.aciertos = aciertos;
        this.total = total;
        this.nota = nota;
        this.fecha = fecha;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return exam id */
    public Long getExamenId() {
        return examenId;
    }

    /** @return answers JSON */
    public String getRespuestas() {
        return respuestas;
    }

    /** @return correct count */
    public int getAciertos() {
        return aciertos;
    }

    /** @return total */
    public int getTotal() {
        return total;
    }

    /** @return score */
    public BigDecimal getNota() {
        return nota;
    }

    /** @return instant */
    public Instant getFecha() {
        return fecha;
    }
}
