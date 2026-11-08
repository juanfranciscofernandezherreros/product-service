package com.example.battleservice.infrastructure.adapter.out.persistence;

import com.example.battleservice.domain.exception.InvalidBattleException;
import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.PlayerStatus;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.domain.model.RoomStatus;
import com.example.battleservice.domain.model.RoomTopic;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Locale;

/**
 * Explicit mapper between domain models and JPA entities (enums as DB strings, options as JSON).
 */
public final class BattlePersistenceMapper {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<List<String>> LIST = new TypeReference<>() { };

    private BattlePersistenceMapper() {
    }

    private static String toJson(final List<String> options) {
        try {
            return JSON.writeValueAsString(options);
        } catch (Exception ex) {
            throw new InvalidBattleException("options cannot be serialised");
        }
    }

    private static List<String> fromJson(final String json) {
        try {
            return JSON.readValue(json, LIST);
        } catch (Exception ex) {
            throw new InvalidBattleException("options cannot be read");
        }
    }

    /**
     * Converts a domain question to an entity.
     *
     * @param q domain question
     * @return the entity
     */
    public static QuestionJpaEntity toEntity(final BattleQuestion q) {
        return new QuestionJpaEntity(q.getId(), q.getLevel().name(),
                q.getTopic().name().toLowerCase(Locale.ROOT), q.getPrompt(), toJson(q.getOptions()),
                q.getCorrectIndex(), q.getExplanation());
    }

    /**
     * Converts an entity to a domain question.
     *
     * @param e entity
     * @return the domain question
     */
    public static BattleQuestion toDomain(final QuestionJpaEntity e) {
        return new BattleQuestion(e.getId(), Level.valueOf(e.getLevel()),
                QuestionTopic.valueOf(e.getTopic().toUpperCase(Locale.ROOT)), e.getPrompt(),
                fromJson(e.getOptions()), e.getCorrectIndex(), e.getExplanation());
    }

    /**
     * Converts a domain room to an entity.
     *
     * @param r domain room
     * @return the entity
     */
    public static RoomJpaEntity toEntity(final BattleRoom r) {
        return new RoomJpaEntity(r.getId(), r.getCode(), r.getHostId(), r.getLevel().name(),
                r.getTopic().name().toLowerCase(Locale.ROOT), r.getDurationSeconds(), r.getCapacity(),
                r.getStatus().name().toLowerCase(Locale.ROOT), r.getCreatedAt(), r.getStartedAt(),
                r.getDeadlineAt());
    }

    /**
     * Converts an entity to a domain room.
     *
     * @param e entity
     * @return the domain room
     */
    public static BattleRoom toDomain(final RoomJpaEntity e) {
        return new BattleRoom(e.getId(), e.getCode(), e.getHostId(), Level.valueOf(e.getLevel()),
                RoomTopic.valueOf(e.getTopic().toUpperCase(Locale.ROOT)), e.getDurationSeconds(),
                e.getCapacity(), RoomStatus.valueOf(e.getStatus().toUpperCase(Locale.ROOT)),
                e.getCreatedAt(), e.getStartedAt(), e.getDeadlineAt());
    }

    /**
     * Converts a domain player to an entity.
     *
     * @param p domain player
     * @return the entity
     */
    public static PlayerJpaEntity toEntity(final BattlePlayer p) {
        return new PlayerJpaEntity(p.getRoomId(), p.getUserId(), p.getAlias(),
                p.getStatus().name().toLowerCase(Locale.ROOT), p.getJoinedAt());
    }

    /**
     * Converts an entity to a domain player.
     *
     * @param e entity
     * @return the domain player
     */
    public static BattlePlayer toDomain(final PlayerJpaEntity e) {
        return new BattlePlayer(e.getRoomId(), e.getUserId(), e.getAlias(),
                PlayerStatus.valueOf(e.getStatus().toUpperCase(Locale.ROOT)), e.getJoinedAt());
    }
}
