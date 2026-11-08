package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * JPA entity mapped to the {@code colecciones_material} table.
 */
@Entity
@Table(name = "colecciones_material")
public class CollectionEntity {

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
    private String tipo;

    @Column(name = "incluir_todos_articulos", nullable = false)
    private boolean incluirTodosArticulos;

    @Column(name = "nombre_archivo", nullable = false, unique = true)
    private String nombreArchivo;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(nullable = false)
    private boolean publicado;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public boolean isIncluirTodosArticulos() {
        return incluirTodosArticulos;
    }

    public void setIncluirTodosArticulos(final boolean incluirTodosArticulos) {
        this.incluirTodosArticulos = incluirTodosArticulos;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(final String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(final LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(final boolean publicado) {
        this.publicado = publicado;
    }
}
