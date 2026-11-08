package com.example.examsservice.domain.model;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of an exam result.
 */
public final class Result {

    private static final BigDecimal MAX_NOTE = new BigDecimal("100");

    private final Long id;
    private final UUID usuarioId;
    private final long examenId;
    private final String respuestas;
    private final int aciertos;
    private final int total;
    private final BigDecimal nota;
    private final Instant fecha;

    /**
     * Creates a validated result.
     *
     * @param id         identifier (nullable before persistence)
     * @param usuarioId  user id
     * @param examenId   exam id
     * @param respuestas answers JSON
     * @param aciertos   correct count (&ge; 0)
     * @param total      total (&gt; 0)
     * @param nota       score (0..100)
     * @param fecha      instant
     */
    public Result(final Long id, final UUID usuarioId, final long examenId, final String respuestas,
                  final int aciertos, final int total, final BigDecimal nota, final Instant fecha) {
        if (usuarioId == null) {
            throw new InvalidExamException("usuarioId is required");
        }
        if (respuestas == null || respuestas.trim().isEmpty()) {
            throw new InvalidExamException("respuestas is required");
        }
        if (aciertos < 0) {
            throw new InvalidExamException("aciertos must not be negative");
        }
        if (total <= 0) {
            throw new InvalidExamException("total must be greater than 0");
        }
        if (nota == null || nota.signum() < 0 || nota.compareTo(MAX_NOTE) > 0) {
            throw new InvalidExamException("nota must be between 0 and 100");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.examenId = examenId;
        this.respuestas = respuestas;
        this.aciertos = aciertos;
        this.total = total;
        this.nota = nota;
        this.fecha = fecha == null ? Instant.now() : fecha;
    }

    /**
     * Factory for a new result.
     *
     * @param usuarioId  user id
     * @param examenId   exam id
     * @param respuestas answers JSON
     * @param aciertos   correct count
     * @param total      total
     * @param nota       score
     * @return a new result
     */
    public static Result record(final UUID usuarioId, final long examenId, final String respuestas,
                                final int aciertos, final int total, final BigDecimal nota) {
        return new Result(null, usuarioId, examenId, respuestas, aciertos, total, nota, Instant.now());
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
    public long getExamenId() {
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
