package com.example.socialservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Composite id for {@link FollowEntity}. */
public class FollowId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID seguidor;
    private UUID seguido;

    /** Default constructor required by JPA. */
    public FollowId() {
    }

    /**
     * Creates the id.
     *
     * @param seguidor follower
     * @param seguido  followed
     */
    public FollowId(final UUID seguidor, final UUID seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
    }

    /** @return follower */
    public UUID getSeguidor() {
        return seguidor;
    }

    /** @return followed */
    public UUID getSeguido() {
        return seguido;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FollowId)) {
            return false;
        }
        final FollowId that = (FollowId) other;
        return Objects.equals(seguidor, that.seguidor) && Objects.equals(seguido, that.seguido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seguidor, seguido);
    }
}
