package com.example.examsservice.domain.model;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.util.List;

/**
 * Immutable domain representation of an exam question.
 */
public final class Question {

    private static final int MIN_OPTS = 2;
    private static final int MAX_OPTS = 8;

    private final Long id;
    private final long examenId;
    private final int orden;
    private final String enunciado;
    private final List<String> opciones;
    private final String fuenteUrl;

    /**
     * Creates a validated question.
     *
     * @param id        identifier (nullable before persistence)
     * @param examenId  exam id
     * @param orden     order (&gt; 0)
     * @param enunciado prompt
     * @param opciones  options (2..8)
     * @param fuenteUrl optional source url
     */
    public Question(final Long id, final long examenId, final int orden, final String enunciado,
                    final List<String> opciones, final String fuenteUrl) {
        if (orden <= 0) {
            throw new InvalidExamException("orden must be greater than 0");
        }
        if (enunciado == null || enunciado.trim().isEmpty()) {
            throw new InvalidExamException("enunciado is required");
        }
        if (opciones == null || opciones.size() < MIN_OPTS || opciones.size() > MAX_OPTS) {
            throw new InvalidExamException("opciones must contain between 2 and 8 entries");
        }
        this.id = id;
        this.examenId = examenId;
        this.orden = orden;
        this.enunciado = enunciado.trim();
        this.opciones = List.copyOf(opciones);
        this.fuenteUrl = fuenteUrl;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return exam id */
    public long getExamenId() {
        return examenId;
    }

    /** @return order */
    public int getOrden() {
        return orden;
    }

    /** @return prompt */
    public String getEnunciado() {
        return enunciado;
    }

    /** @return options */
    public List<String> getOpciones() {
        return opciones;
    }

    /** @return source url */
    public String getFuenteUrl() {
        return fuenteUrl;
    }
}
