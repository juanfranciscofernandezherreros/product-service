package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for vocabulary items.
 */
@Entity
@Table(name = "vocabulary_items")
public class VocabularyItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "en", nullable = false)
    private String en;

    @Column(name = "es", nullable = false)
    private String es;

    @Column(name = "example")
    private String example;

    @Column(name = "level", nullable = false, length = 2)
    private String level;

    /**
     * Default constructor required by JPA.
     */
    protected VocabularyItemJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id      id
     * @param en      English term
     * @param es      Spanish translation
     * @param example example
     * @param level   level name
     */
    public VocabularyItemJpaEntity(final Long id, final String en, final String es,
                                   final String example, final String level) {
        this.id = id;
        this.en = en;
        this.es = es;
        this.example = example;
        this.level = level;
    }

    /** @return the id */
    public Long getId() {
        return id;
    }

    /** @return the English term */
    public String getEn() {
        return en;
    }

    /** @return the Spanish translation */
    public String getEs() {
        return es;
    }

    /** @return the example */
    public String getExample() {
        return example;
    }

    /** @return the level name */
    public String getLevel() {
        return level;
    }
}
