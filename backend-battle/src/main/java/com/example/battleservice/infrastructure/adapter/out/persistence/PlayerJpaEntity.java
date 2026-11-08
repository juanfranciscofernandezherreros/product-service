package com.example.battleservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for battle players.
 */
@Entity
@Table(name = "battle_players")
@IdClass(PlayerId.class)
public class PlayerJpaEntity {

    @Id
    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "alias", nullable = false, length = 30)
    private String alias;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    /** Default constructor required by JPA. */
    protected PlayerJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param roomId   room id
     * @param userId   user id
     * @param alias    alias
     * @param status   status
     * @param joinedAt joined at
     */
    public PlayerJpaEntity(final UUID roomId, final UUID userId, final String alias,
                           final String status, final Instant joinedAt) {
        this.roomId = roomId;
        this.userId = userId;
        this.alias = alias;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    /** @return room id */
    public UUID getRoomId() {
        return roomId;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return alias */
    public String getAlias() {
        return alias;
    }

    /** @return status */
    public String getStatus() {
        return status;
    }

    /** @return joined at */
    public Instant getJoinedAt() {
        return joinedAt;
    }
}
