package com.example.coursesservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * JPA entity mapped to the {@code actividades} table.
 */
@Entity
@Table(name = "actividades")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String destreza;

    @Column(name = "unidad_id")
    private Long unidadId;

    @Column(name = "articulo_slug")
    private String articuloSlug;

    @Column(nullable = false)
    private int orden;

    @Column(nullable = false)
    private boolean publicado;

    @Column(name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(final String slug) {
        this.slug = slug;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDestreza() {
        return destreza;
    }

    public void setDestreza(final String destreza) {
        this.destreza = destreza;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(final Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getArticuloSlug() {
        return articuloSlug;
    }

    public void setArticuloSlug(final String articuloSlug) {
        this.articuloSlug = articuloSlug;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(final int orden) {
        this.orden = orden;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(final boolean publicado) {
        this.publicado = publicado;
    }

    public OffsetDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(final OffsetDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
