package com.example.battleservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for battle rooms.
 */
@Entity
@Table(name = "battle_rooms")
public class RoomJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 16)
    private String code;

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @Column(name = "level", nullable = false, length = 2)
    private String level;

    @Column(name = "topic", nullable = false, length = 16)
    private String topic;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "deadline_at")
    private Instant deadlineAt;

    /** Default constructor required by JPA. */
    protected RoomJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id              id
     * @param code            code
     * @param hostId          host id
     * @param level           level
     * @param topic           topic
     * @param durationSeconds duration
     * @param capacity        capacity
     * @param status          status
     * @param createdAt       created at
     * @param startedAt       started at
     * @param deadlineAt      deadline at
     */
    public RoomJpaEntity(final UUID id, final String code, final UUID hostId, final String level,
                         final String topic, final int durationSeconds, final int capacity,
                         final String status, final Instant createdAt, final Instant startedAt,
                         final Instant deadlineAt) {
        this.id = id;
        this.code = code;
        this.hostId = hostId;
        this.level = level;
        this.topic = topic;
        this.durationSeconds = durationSeconds;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.deadlineAt = deadlineAt;
    }

    /** @return id */
    public UUID getId() {
        return id;
    }

    /** @return code */
    public String getCode() {
        return code;
    }

    /** @return host id */
    public UUID getHostId() {
        return hostId;
    }

    /** @return level */
    public String getLevel() {
        return level;
    }

    /** @return topic */
    public String getTopic() {
        return topic;
    }

    /** @return duration */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /** @return capacity */
    public int getCapacity() {
        return capacity;
    }

    /** @return status */
    public String getStatus() {
        return status;
    }

    /** @return created at */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /** @return started at */
    public Instant getStartedAt() {
        return startedAt;
    }

    /** @return deadline at */
    public Instant getDeadlineAt() {
        return deadlineAt;
    }
}
