package com.example.socialservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for message requests. */
@Entity
@Table(name = "solicitudes_mensaje")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "remitente", nullable = false)
    private UUID remitente;

    @Column(name = "destinatario", nullable = false)
    private UUID destinatario;

    @Column(name = "mensaje_inicial", nullable = false, length = 2000)
    private String mensajeInicial;

    @Column(name = "estado", nullable = false, length = 16)
    private String estado;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "respondido_en")
    private Instant respondidoEn;

    /** Default constructor required by JPA. */
    protected RequestEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id             id
     * @param remitente      sender
     * @param destinatario   recipient
     * @param mensajeInicial initial message
     * @param estado         status
     * @param creadoEn       creation instant
     * @param respondidoEn   response instant
     */
    public RequestEntity(final Long id, final UUID remitente, final UUID destinatario,
                         final String mensajeInicial, final String estado, final Instant creadoEn,
                         final Instant respondidoEn) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensajeInicial = mensajeInicial;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.respondidoEn = respondidoEn;
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

    /** @return initial message */
    public String getMensajeInicial() {
        return mensajeInicial;
    }

    /** @return status */
    public String getEstado() {
        return estado;
    }

    /** @return creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }

    /** @return response instant */
    public Instant getRespondidoEn() {
        return respondidoEn;
    }
}
