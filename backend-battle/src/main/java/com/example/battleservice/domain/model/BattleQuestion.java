package com.example.battleservice.domain.model;

import com.example.battleservice.domain.exception.InvalidBattleException;
import java.util.List;
import java.util.Objects;

/**
 * Immutable domain representation of a battle question.
 */
public final class BattleQuestion {

    private static final int OPTIONS = 4;
    private static final int MAX_INDEX = 3;

    private final Long id;
    private final Level level;
    private final QuestionTopic topic;
    private final String prompt;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    /**
     * Creates a validated question.
     *
     * @param id           identifier (may be {@code null})
     * @param level        level
     * @param topic        topic
     * @param prompt       prompt text
     * @param options      exactly four options
     * @param correctIndex correct option index (0..3)
     * @param explanation  explanation text
     */
    public BattleQuestion(final Long id, final Level level, final QuestionTopic topic, final String prompt,
                          final List<String> options, final int correctIndex, final String explanation) {
        if (level == null || topic == null) {
            throw new InvalidBattleException("level and topic are required");
        }
        this.prompt = requireText(prompt, "prompt");
        this.explanation = requireText(explanation, "explanation");
        if (options == null || options.size() != OPTIONS) {
            throw new InvalidBattleException("options must contain exactly " + OPTIONS + " entries");
        }
        if (correctIndex < 0 || correctIndex > MAX_INDEX) {
            throw new InvalidBattleException("correctIndex must be between 0 and " + MAX_INDEX);
        }
        this.id = id;
        this.level = level;
        this.topic = topic;
        this.options = List.copyOf(options);
        this.correctIndex = correctIndex;
    }

    private static String requireText(final String value, final String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidBattleException(field + " is required");
        }
        return value.trim();
    }

    /** @return the id */
    public Long getId() {
        return id;
    }

    /** @return the level */
    public Level getLevel() {
        return level;
    }

    /** @return the topic */
    public QuestionTopic getTopic() {
        return topic;
    }

    /** @return the prompt */
    public String getPrompt() {
        return prompt;
    }

    /** @return the options */
    public List<String> getOptions() {
        return options;
    }

    /** @return the correct index */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /** @return the explanation */
    public String getExplanation() {
        return explanation;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof BattleQuestion && Objects.equals(id, ((BattleQuestion) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
