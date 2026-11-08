package com.example.examsservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** JPA entity for exam questions (options as JSON text). */
@Entity
@Table(name = "preguntas")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "examen_id", nullable = false)
    private Long examenId;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "enunciado", nullable = false, length = 2000)
    private String enunciado;

    @Column(name = "opciones", nullable = false, length = 4000)
    private String opciones;

    @Column(name = "fuente_url", length = 500)
    private String fuenteUrl;

    /** Default constructor required by JPA. */
    protected QuestionEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id        id
     * @param examenId  exam id
     * @param orden     order
     * @param enunciado prompt
     * @param opciones  options JSON
     * @param fuenteUrl source url
     */
    public QuestionEntity(final Long id, final Long examenId, final int orden, final String enunciado,
                          final String opciones, final String fuenteUrl) {
        this.id = id;
        this.examenId = examenId;
        this.orden = orden;
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.fuenteUrl = fuenteUrl;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return exam id */
    public Long getExamenId() {
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

    /** @return options JSON */
    public String getOpciones() {
        return opciones;
    }

    /** @return source url */
    public String getFuenteUrl() {
        return fuenteUrl;
    }
}
