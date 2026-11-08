package com.example.battleservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite id for {@link PlayerJpaEntity}.
 */
public class PlayerId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID roomId;
    private UUID userId;

    /** Default constructor required by JPA. */
    public PlayerId() {
    }

    /**
     * Creates the id.
     *
     * @param roomId room id
     * @param userId user id
     */
    public PlayerId(final UUID roomId, final UUID userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    /** @return room id */
    public UUID getRoomId() {
        return roomId;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlayerId)) {
            return false;
        }
        final PlayerId that = (PlayerId) other;
        return Objects.equals(roomId, that.roomId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, userId);
    }
}
