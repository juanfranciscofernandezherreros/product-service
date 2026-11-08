package com.example.battleservice.application.port.out;

import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for battle question persistence.
 */
public interface QuestionRepositoryPort {

    /**
     * Persists a question.
     *
     * @param question question
     * @return stored question
     */
    BattleQuestion save(BattleQuestion question);

    /**
     * Finds a question by id.
     *
     * @param id id
     * @return the question if present
     */
    Optional<BattleQuestion> findById(long id);

    /**
     * Indicates whether a question exists.
     *
     * @param id id
     * @return {@code true} if present
     */
    boolean existsById(long id);

    /**
     * Indicates whether a question with the given natural key exists.
     *
     * @param level  level
     * @param topic  topic
     * @param prompt prompt
     * @return {@code true} if present
     */
    boolean existsByLevelTopicPrompt(Level level, QuestionTopic topic, String prompt);

    /**
     * Lists questions filtered by optional level/topic.
     *
     * @param level  optional level
     * @param topic  optional topic
     * @param limit  page size
     * @param offset results to skip
     * @return matching questions
     */
    List<BattleQuestion> findByFilter(Level level, QuestionTopic topic, int limit, int offset);

    /**
     * Counts questions filtered by optional level/topic.
     *
     * @param level optional level
     * @param topic optional topic
     * @return number of matching questions
     */
    long countByFilter(Level level, QuestionTopic topic);

    /**
     * Deletes a question by id.
     *
     * @param id id
     */
    void deleteById(long id);
}
