package com.example.socialservice.domain.model;

import com.example.socialservice.domain.exception.InvalidSocialException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a private message.
 */
public final class Message {

    private static final int MAX = 2000;

    private final Long id;
    private final UUID remitente;
    private final UUID destinatario;
    private final String contenido;
    private final Instant creadoEn;

    /**
     * Creates a validated message.
     *
     * @param id           identifier (nullable before persistence)
     * @param remitente    sender id
     * @param destinatario recipient id
     * @param contenido    content (1..2000 chars)
     * @param creadoEn     creation instant
     */
    public Message(final Long id, final UUID remitente, final UUID destinatario, final String contenido,
                   final Instant creadoEn) {
        if (remitente == null || destinatario == null) {
            throw new InvalidSocialException("remitente and destinatario are required");
        }
        if (remitente.equals(destinatario)) {
            throw new InvalidSocialException("remitente and destinatario must differ");
        }
        if (contenido == null || contenido.trim().isEmpty() || contenido.length() > MAX) {
            throw new InvalidSocialException("contenido must be between 1 and 2000 characters");
        }
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
    }

    /**
     * Factory for a new message.
     *
     * @param remitente    sender
     * @param destinatario recipient
     * @param contenido    content
     * @return a new message
     */
    public static Message send(final UUID remitente, final UUID destinatario, final String contenido) {
        return new Message(null, remitente, destinatario, contenido, Instant.now());
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
