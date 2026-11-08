package com.example.socialservice.domain.model;

import com.example.socialservice.domain.exception.InvalidSocialException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a message request.
 */
public final class MessageRequest {

    private static final int MAX = 2000;

    private final Long id;
    private final UUID remitente;
    private final UUID destinatario;
    private final String mensajeInicial;
    private final RequestStatus estado;
    private final Instant creadoEn;
    private final Instant respondidoEn;

    /**
     * Creates a validated request.
     *
     * @param id             identifier (nullable before persistence)
     * @param remitente      sender id
     * @param destinatario   recipient id
     * @param mensajeInicial initial message (1..2000)
     * @param estado         status
     * @param creadoEn       creation instant
     * @param respondidoEn   response instant (nullable)
     */
    public MessageRequest(final Long id, final UUID remitente, final UUID destinatario,
                          final String mensajeInicial, final RequestStatus estado, final Instant creadoEn,
                          final Instant respondidoEn) {
        if (remitente == null || destinatario == null) {
            throw new InvalidSocialException("remitente and destinatario are required");
        }
        if (remitente.equals(destinatario)) {
            throw new InvalidSocialException("remitente and destinatario must differ");
        }
        if (mensajeInicial == null || mensajeInicial.trim().isEmpty() || mensajeInicial.length() > MAX) {
            throw new InvalidSocialException("mensajeInicial must be between 1 and 2000 characters");
        }
        if (estado == null) {
            throw new InvalidSocialException("estado is required");
        }
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensajeInicial = mensajeInicial;
        this.estado = estado;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
        this.respondidoEn = respondidoEn;
    }

    /**
     * Factory for a new pending request.
     *
     * @param remitente      sender
     * @param destinatario   recipient
     * @param mensajeInicial initial message
     * @return a new request
     */
    public static MessageRequest create(final UUID remitente, final UUID destinatario,
                                        final String mensajeInicial) {
        return new MessageRequest(null, remitente, destinatario, mensajeInicial, RequestStatus.PENDIENTE,
                Instant.now(), null);
    }

    /**
     * Returns a responded copy.
     *
     * @param newStatus accepted/rejected status
     * @return updated request
     */
    public MessageRequest respond(final RequestStatus newStatus) {
        if (newStatus == null || newStatus == RequestStatus.PENDIENTE) {
            throw new InvalidSocialException("response status must be ACEPTADA or RECHAZADA");
        }
        return new MessageRequest(this.id, this.remitente, this.destinatario, this.mensajeInicial,
                newStatus, this.creadoEn, Instant.now());
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
    public RequestStatus getEstado() {
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
