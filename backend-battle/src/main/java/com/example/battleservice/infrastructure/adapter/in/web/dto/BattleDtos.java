package com.example.battleservice.infrastructure.adapter.in.web.dto;

import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.domain.model.RoomTopic;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Web DTOs for the battle API.
 */
public final class BattleDtos {

    private BattleDtos() {
    }

    /**
     * Question request.
     *
     * @param level        level
     * @param topic        topic
     * @param prompt       prompt
     * @param options      options (exactly 4)
     * @param correctIndex correct index (0..3)
     * @param explanation  explanation
     */
    public record QuestionRequest(
            @NotNull Level level,
            @NotNull QuestionTopic topic,
            @NotBlank String prompt,
            @NotNull @Size(min = 4, max = 4) List<@NotBlank String> options,
            @Min(0) @Max(3) int correctIndex,
            @NotBlank String explanation) {
    }

    /**
     * Question response.
     *
     * @param id           id
     * @param level        level
     * @param topic        topic
     * @param prompt       prompt
     * @param options      options
     * @param correctIndex correct index
     * @param explanation  explanation
     */
    public record QuestionResponse(Long id, Level level, QuestionTopic topic, String prompt,
                                   List<String> options, int correctIndex, String explanation) {
    }

    /**
     * Paged questions response.
     *
     * @param total total
     * @param items items
     */
    public record PagedQuestionsResponse(long total, List<QuestionResponse> items) {
    }

    /**
     * Room request.
     *
     * @param hostId          host id
     * @param level           level
     * @param topic           topic
     * @param durationSeconds duration
     * @param capacity        capacity
     */
    public record RoomRequest(
            @NotNull UUID hostId,
            @NotNull Level level,
            @NotNull RoomTopic topic,
            int durationSeconds,
            int capacity) {
    }

    /**
     * Room response.
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
     */
    public record RoomResponse(UUID id, String code, UUID hostId, Level level, RoomTopic topic,
                               int durationSeconds, int capacity, String status, Instant createdAt) {
    }

    /**
     * Join request.
     *
     * @param userId user id
     * @param alias  alias
     */
    public record JoinRequest(@NotNull UUID userId, @NotBlank @Size(min = 2, max = 30) String alias) {
    }

    /**
     * Player response.
     *
     * @param roomId   room id
     * @param userId   user id
     * @param alias    alias
     * @param status   status
     * @param joinedAt joined at
     */
    public record PlayerResponse(UUID roomId, UUID userId, String alias, String status, Instant joinedAt) {
    }

    /**
     * Error payload.
     *
     * @param code    code
     * @param message message
     */
    public record ErrorResponse(String code, String message) {
    }
}
