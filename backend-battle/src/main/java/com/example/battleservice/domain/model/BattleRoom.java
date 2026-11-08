package com.example.battleservice.domain.model;

import com.example.battleservice.domain.exception.InvalidBattleException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain representation of a battle room.
 */
public final class BattleRoom {

    private static final List<Integer> DURATIONS = List.of(120, 180, 300);
    private static final int MIN_CAPACITY = 2;
    private static final int MAX_CAPACITY = 12;
    private static final int CODE_LENGTH = 10;

    private final UUID id;
    private final String code;
    private final UUID hostId;
    private final Level level;
    private final RoomTopic topic;
    private final int durationSeconds;
    private final int capacity;
    private final RoomStatus status;
    private final Instant createdAt;
    private final Instant startedAt;
    private final Instant deadlineAt;

    /**
     * Creates a validated room.
     *
     * @param id              identifier
     * @param code            room code
     * @param hostId          host user id
     * @param level           level
     * @param topic           topic
     * @param durationSeconds duration (120/180/300)
     * @param capacity        capacity (2..12)
     * @param status          status
     * @param createdAt       creation instant
     * @param startedAt       start instant (nullable)
     * @param deadlineAt      deadline instant (nullable)
     */
    public BattleRoom(final UUID id, final String code, final UUID hostId, final Level level,
                      final RoomTopic topic, final int durationSeconds, final int capacity,
                      final RoomStatus status, final Instant createdAt, final Instant startedAt,
                      final Instant deadlineAt) {
        if (id == null || hostId == null || level == null || topic == null || status == null) {
            throw new InvalidBattleException("id, hostId, level, topic and status are required");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidBattleException("code is required");
        }
        if (!DURATIONS.contains(durationSeconds)) {
            throw new InvalidBattleException("durationSeconds must be one of " + DURATIONS);
        }
        if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
            throw new InvalidBattleException("capacity must be between 2 and 12");
        }
        this.id = id;
        this.code = code.trim();
        this.hostId = hostId;
        this.level = level;
        this.topic = topic;
        this.durationSeconds = durationSeconds;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.startedAt = startedAt;
        this.deadlineAt = deadlineAt;
    }

    /**
     * Factory for a new waiting room with a generated id and code.
     *
     * @param hostId          host user id
     * @param level           level
     * @param topic           topic
     * @param durationSeconds duration
     * @param capacity        capacity
     * @return a new room
     */
    public static BattleRoom open(final UUID hostId, final Level level, final RoomTopic topic,
                                  final int durationSeconds, final int capacity) {
        final String code = UUID.randomUUID().toString().replace("-", "")
                .substring(0, CODE_LENGTH).toUpperCase(java.util.Locale.ROOT);
        return new BattleRoom(UUID.randomUUID(), code, hostId, level, topic, durationSeconds, capacity,
                RoomStatus.WAITING, Instant.now(), null, null);
    }

    /** @return the id */
    public UUID getId() {
        return id;
    }

    /** @return the code */
    public String getCode() {
        return code;
    }

    /** @return the host id */
    public UUID getHostId() {
        return hostId;
    }

    /** @return the level */
    public Level getLevel() {
        return level;
    }

    /** @return the topic */
    public RoomTopic getTopic() {
        return topic;
    }

    /** @return the duration */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /** @return the capacity */
    public int getCapacity() {
        return capacity;
    }

    /** @return the status */
    public RoomStatus getStatus() {
        return status;
    }

    /** @return the creation instant */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /** @return the start instant */
    public Instant getStartedAt() {
        return startedAt;
    }

    /** @return the deadline instant */
    public Instant getDeadlineAt() {
        return deadlineAt;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof BattleRoom && Objects.equals(id, ((BattleRoom) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
