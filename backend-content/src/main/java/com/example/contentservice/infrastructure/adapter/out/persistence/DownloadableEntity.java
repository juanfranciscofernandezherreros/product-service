package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * JPA entity mapped to the {@code recursos_descargables} table.
 */
@Entity
@Table(name = "recursos_descargables")
public class DownloadableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "url_archivo", nullable = false, length = 500)
    private String urlArchivo;

    @Column(name = "url_lectura", length = 500)
    private String urlLectura;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(final String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(final LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(final String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getUrlLectura() {
        return urlLectura;
    }

    public void setUrlLectura(final String urlLectura) {
        this.urlLectura = urlLectura;
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
