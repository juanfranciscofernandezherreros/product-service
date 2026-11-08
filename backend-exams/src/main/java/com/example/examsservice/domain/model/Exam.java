package com.example.examsservice.domain.model;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable domain representation of an exam.
 */
public final class Exam {

    private static final String DEFAULT_LEVEL = "B1-B2";

    private final Long id;
    private final String codigo;
    private final String titulo;
    private final LocalDate fecha;
    private final String nivel;
    private final String descripcion;
    private final boolean publicado;
    private final String articuloSlug;
    private final Instant createdAt;

    /**
     * Creates a validated exam.
     *
     * @param id           identifier (nullable before persistence)
     * @param codigo       unique code
     * @param titulo       title
     * @param fecha        date (nullable)
     * @param nivel        level
     * @param descripcion  description (nullable)
     * @param publicado    published flag
     * @param articuloSlug optional article slug
     * @param createdAt    creation instant
     */
    public Exam(final Long id, final String codigo, final String titulo, final LocalDate fecha,
                final String nivel, final String descripcion, final boolean publicado,
                final String articuloSlug, final Instant createdAt) {
        this.codigo = require(codigo, "codigo");
        this.titulo = require(titulo, "titulo");
        this.id = id;
        this.fecha = fecha;
        this.nivel = nivel == null || nivel.trim().isEmpty() ? DEFAULT_LEVEL : nivel.trim();
        this.descripcion = descripcion;
        this.publicado = publicado;
        this.articuloSlug = articuloSlug;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    /**
     * Factory for a new exam.
     *
     * @param codigo       code
     * @param titulo       title
     * @param fecha        date
     * @param nivel        level
     * @param descripcion  description
     * @param publicado    published
     * @param articuloSlug article slug
     * @return a new exam
     */
    public static Exam create(final String codigo, final String titulo, final LocalDate fecha,
                              final String nivel, final String descripcion, final boolean publicado,
                              final String articuloSlug) {
        return new Exam(null, codigo, titulo, fecha, nivel, descripcion, publicado, articuloSlug, Instant.now());
    }

    /**
     * Returns an updated copy keeping id/codigo/createdAt.
     *
     * @param newTitulo      new title (keeps current when {@code null})
     * @param newFecha       new date
     * @param newNivel       new level
     * @param newDescripcion new description
     * @param newPublicado   new published flag
     * @param newArticulo    new article slug
     * @return updated exam
     */
    public Exam withUpdate(final String newTitulo, final LocalDate newFecha, final String newNivel,
                           final String newDescripcion, final Boolean newPublicado, final String newArticulo) {
        return new Exam(this.id, this.codigo,
                newTitulo == null ? this.titulo : newTitulo,
                newFecha == null ? this.fecha : newFecha,
                newNivel == null ? this.nivel : newNivel,
                newDescripcion == null ? this.descripcion : newDescripcion,
                newPublicado == null ? this.publicado : newPublicado,
                newArticulo == null ? this.articuloSlug : newArticulo,
                this.createdAt);
    }

    private static String require(final String value, final String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidExamException(field + " is required");
        }
        return value.trim();
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

    /** @return creation instant */
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Exam && Objects.equals(id, ((Exam) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
