package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code comentarios_articulos} table.
 */
@Entity
@Table(name = "comentarios_articulos")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "articulo_slug", nullable = false)
    private String articuloSlug;

    @Column(name = "padre_id")
    private Long padreId;

    @Column(name = "autor_id")
    private UUID autorId;

    @Column(nullable = false, columnDefinition = "text")
    private String contenido;

    @Column(name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    @Column(nullable = false)
    private boolean eliminado;

    @Column(nullable = false)
    private boolean oculto;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getArticuloSlug() {
        return articuloSlug;
    }

    public void setArticuloSlug(final String articuloSlug) {
        this.articuloSlug = articuloSlug;
    }

    public Long getPadreId() {
        return padreId;
    }

    public void setPadreId(final Long padreId) {
        this.padreId = padreId;
    }

    public UUID getAutorId() {
        return autorId;
    }

    public void setAutorId(final UUID autorId) {
        this.autorId = autorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(final String contenido) {
        this.contenido = contenido;
    }

    public OffsetDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(final OffsetDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(final boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isOculto() {
        return oculto;
    }

    public void setOculto(final boolean oculto) {
        this.oculto = oculto;
    }
}
