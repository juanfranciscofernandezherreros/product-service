package com.example.battleservice.domain.model;

import com.example.battleservice.domain.exception.InvalidBattleException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a player within a room.
 */
public final class BattlePlayer {

    private static final int MIN_ALIAS = 2;
    private static final int MAX_ALIAS = 30;

    private final UUID roomId;
    private final UUID userId;
    private final String alias;
    private final PlayerStatus status;
    private final Instant joinedAt;

    /**
     * Creates a validated player.
     *
     * @param roomId   room id
     * @param userId   user id
     * @param alias    alias (2..30 chars)
     * @param status   status
     * @param joinedAt join instant
     */
    public BattlePlayer(final UUID roomId, final UUID userId, final String alias,
                        final PlayerStatus status, final Instant joinedAt) {
        if (roomId == null || userId == null || status == null) {
            throw new InvalidBattleException("roomId, userId and status are required");
        }
        if (alias == null || alias.trim().length() < MIN_ALIAS || alias.trim().length() > MAX_ALIAS) {
            throw new InvalidBattleException("alias must be between 2 and 30 characters");
        }
        this.roomId = roomId;
        this.userId = userId;
        this.alias = alias.trim();
        this.status = status;
        this.joinedAt = joinedAt == null ? Instant.now() : joinedAt;
    }

    /**
     * Factory for a new pending player.
     *
     * @param roomId room id
     * @param userId user id
     * @param alias  alias
     * @return a new player
     */
    public static BattlePlayer join(final UUID roomId, final UUID userId, final String alias) {
        return new BattlePlayer(roomId, userId, alias, PlayerStatus.PENDING, Instant.now());
    }

    /** @return the room id */
    public UUID getRoomId() {
        return roomId;
    }

    /** @return the user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return the alias */
    public String getAlias() {
        return alias;
    }

    /** @return the status */
    public PlayerStatus getStatus() {
        return status;
    }

    /** @return the join instant */
    public Instant getJoinedAt() {
        return joinedAt;
    }
}
