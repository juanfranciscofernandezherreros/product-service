package com.example.miscservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code bitacora_anotaciones} table.
 */
@Entity
@Table(name = "bitacora_anotaciones")
public class LogbookNoteEntity {

    @Id
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "categoria_id")
    private UUID categoriaId;

    @Column(nullable = false, length = 10000)
    private String contenido;

    @Column(name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(final UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(final UUID categoriaId) {
        this.categoriaId = categoriaId;
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

    public OffsetDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(final OffsetDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}
