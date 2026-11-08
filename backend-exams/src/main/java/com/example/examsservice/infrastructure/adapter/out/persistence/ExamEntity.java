package com.example.examsservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/** JPA entity for exams. */
@Entity
@Table(name = "examenes")
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 64)
    private String codigo;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "nivel", nullable = false, length = 16)
    private String nivel;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @Column(name = "publicado", nullable = false)
    private boolean publicado;

    @Column(name = "articulo_slug")
    private String articuloSlug;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /** Default constructor required by JPA. */
    protected ExamEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id           id
     * @param codigo       code
     * @param titulo       title
     * @param fecha        date
     * @param nivel        level
     * @param descripcion  description
     * @param publicado    published
     * @param articuloSlug article slug
     * @param createdAt    created at
     */
    public ExamEntity(final Long id, final String codigo, final String titulo, final LocalDate fecha,
                      final String nivel, final String descripcion, final boolean publicado,
                      final String articuloSlug, final Instant createdAt) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.fecha = fecha;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.publicado = publicado;
        this.articuloSlug = articuloSlug;
        this.createdAt = createdAt;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return code */
    public String getCodigo() {
        return codigo;
    }

    /** @return title */
    public String getTitulo() {
        return titulo;
    }

    /** @return date */
    public LocalDate getFecha() {
        return fecha;
    }

    /** @return level */
    public String getNivel() {
        return nivel;
    }

    /** @return description */
    public String getDescripcion() {
        return descripcion;
    }

    /** @return published */
    public boolean isPublicado() {
        return publicado;
    }

    /** @return article slug */
    public String getArticuloSlug() {
        return articuloSlug;
    }

    /** @return created at */
    public Instant getCreatedAt() {
        return createdAt;
    }
}
