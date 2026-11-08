package com.example.socialservice.domain.model;

import com.example.socialservice.domain.exception.InvalidSocialException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a follow relationship.
 */
public final class Follow {

    private final UUID seguidor;
    private final UUID seguido;
    private final Instant creadoEn;

    /**
     * Creates a validated follow.
     *
     * @param seguidor follower id
     * @param seguido  followed id
     * @param creadoEn creation instant
     */
    public Follow(final UUID seguidor, final UUID seguido, final Instant creadoEn) {
        if (seguidor == null || seguido == null) {
            throw new InvalidSocialException("seguidor and seguido are required");
        }
        if (seguidor.equals(seguido)) {
            throw new InvalidSocialException("seguidor and seguido must differ");
        }
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
    }

    /** @return follower id */
    public UUID getSeguidor() {
        return seguidor;
    }

    /** @return followed id */
    public UUID getSeguido() {
        return seguido;
    }

    /** @return creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
