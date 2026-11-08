package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * JPA entity mapped to the {@code paginas_blog} table.
 */
@Entity
@Table(name = "paginas_blog")
public class PageEntity {

    @Id
    private String ruta;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, columnDefinition = "text")
    private String html;

    @Column(name = "url_publica", nullable = false, length = 500)
    private String urlPublica;

    @Column(name = "actualizado_en")
    private OffsetDateTime actualizadoEn;

    public String getRuta() {
        return ruta;
    }

    public void setRuta(final String ruta) {
        this.ruta = ruta;
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

    public OffsetDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(final OffsetDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}
