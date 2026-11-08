package com.example.socialservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for private messages. */
@Entity
@Table(name = "mensajes_privados")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "remitente", nullable = false)
    private UUID remitente;

    @Column(name = "destinatario", nullable = false)
    private UUID destinatario;

    @Column(name = "contenido", nullable = false, length = 2000)
    private String contenido;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    /** Default constructor required by JPA. */
    protected MessageEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id           id
     * @param remitente    sender
     * @param destinatario recipient
     * @param contenido    content
     * @param creadoEn     creation instant
     */
    public MessageEntity(final Long id, final UUID remitente, final UUID destinatario,
                         final String contenido, final Instant creadoEn) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.creadoEn = creadoEn;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return sender */
    public UUID getRemitente() {
        return remitente;
    }

    /** @return recipient */
    public UUID getDestinatario() {
        return destinatario;
    }

    /** @return content */
    public String getContenido() {
        return contenido;
    }

    /** @return creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
