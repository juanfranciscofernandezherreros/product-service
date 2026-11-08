package com.example.battleservice.application.port.in;

import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.domain.model.RoomTopic;
import java.util.List;
import java.util.UUID;

/**
 * Input records for battle operations.
 */
public final class BattleCommands {

    private BattleCommands() {
    }

    /**
     * Create/update question command.
     *
     * @param level        level
     * @param topic        topic
     * @param prompt       prompt
     * @param options      options
     * @param correctIndex correct index
     * @param explanation  explanation
     */
    public record QuestionCommand(Level level, QuestionTopic topic, String prompt, List<String> options,
                                  int correctIndex, String explanation) {
    }

    /**
     * List questions query.
     *
     * @param level  optional level
     * @param topic  optional topic
     * @param limit  page size
     * @param offset results to skip
     */
    public record ListQuestionsQuery(Level level, QuestionTopic topic, int limit, int offset) {
    }

    /**
     * Paged questions result.
     *
     * @param total total
     * @param items items
     */
    public record PagedQuestions(long total, List<BattleQuestion> items) {
    }

    /**
     * Create room command.
     *
     * @param hostId          host id
     * @param level           level
     * @param topic           topic
     * @param durationSeconds duration
     * @param capacity        capacity
     */
    public record CreateRoomCommand(UUID hostId, Level level, RoomTopic topic, int durationSeconds, int capacity) {
    }

    /**
     * Join room command.
     *
     * @param roomId room id
     * @param userId user id
     * @param alias  alias
     */
    public record JoinRoomCommand(UUID roomId, UUID userId, String alias) {
    }
}
