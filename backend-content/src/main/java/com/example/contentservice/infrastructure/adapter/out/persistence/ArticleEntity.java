package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code articulos} table.
 */
@Entity
@Table(name = "articulos")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruta;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, columnDefinition = "text")
    private String html;

    @Column(name = "url_publica", nullable = false, length = 500)
    private String urlPublica;

    @Column(nullable = false, length = 50)
    private String origen;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @Column(name = "categoria_slug")
    private String categoriaSlug;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(length = 50)
    private String nivel;

    @Column(name = "contenido_texto", columnDefinition = "text")
    private String contenidoTexto;

    private Integer palabras;

    @Column(name = "es_articulo_indice", nullable = false)
    private boolean esArticuloIndice;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(final String ruta) {
        this.ruta = ruta;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(final String html) {
        this.html = html;
    }

    public String getUrlPublica() {
        return urlPublica;
    }

    public void setUrlPublica(final String urlPublica) {
        this.urlPublica = urlPublica;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(final String origen) {
        this.origen = origen;
    }

    public OffsetDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(final OffsetDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    public String getCategoriaSlug() {
        return categoriaSlug;
    }

    public void setCategoriaSlug(final String categoriaSlug) {
        this.categoriaSlug = categoriaSlug;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(final LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(final String nivel) {
        this.nivel = nivel;
    }

    public String getContenidoTexto() {
        return contenidoTexto;
    }

    public void setContenidoTexto(final String contenidoTexto) {
        this.contenidoTexto = contenidoTexto;
    }

    public Integer getPalabras() {
        return palabras;
    }

    public void setPalabras(final Integer palabras) {
        this.palabras = palabras;
    }

    public boolean isEsArticuloIndice() {
        return esArticuloIndice;
    }

    public void setEsArticuloIndice(final boolean esArticuloIndice) {
        this.esArticuloIndice = esArticuloIndice;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(final UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}
