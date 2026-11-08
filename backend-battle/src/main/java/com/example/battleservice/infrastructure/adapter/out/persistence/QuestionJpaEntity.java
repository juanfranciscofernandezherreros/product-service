package com.example.battleservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for battle questions (options stored as JSON text).
 */
@Entity
@Table(name = "battle_questions")
public class QuestionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "level", nullable = false, length = 2)
    private String level;

    @Column(name = "topic", nullable = false, length = 16)
    private String topic;

    @Column(name = "prompt", nullable = false, length = 2000)
    private String prompt;

    @Column(name = "options", nullable = false, length = 4000)
    private String options;

    @Column(name = "correct_index", nullable = false)
    private int correctIndex;

    @Column(name = "explanation", nullable = false, length = 2000)
    private String explanation;

    /** Default constructor required by JPA. */
    protected QuestionJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id           id
     * @param level        level
     * @param topic        topic
     * @param prompt       prompt
     * @param options      options as JSON
     * @param correctIndex correct index
     * @param explanation  explanation
     */
    public QuestionJpaEntity(final Long id, final String level, final String topic, final String prompt,
                             final String options, final int correctIndex, final String explanation) {
        this.id = id;
        this.level = level;
        this.topic = topic;
        this.prompt = prompt;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return level */
    public String getLevel() {
        return level;
    }

    /** @return topic */
    public String getTopic() {
        return topic;
    }

    /** @return prompt */
    public String getPrompt() {
        return prompt;
    }

    /** @return options JSON */
    public String getOptions() {
        return options;
    }

    /** @return correct index */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /** @return explanation */
    public String getExplanation() {
        return explanation;
    }
}
