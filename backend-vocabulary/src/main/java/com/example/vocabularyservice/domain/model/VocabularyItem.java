package com.example.vocabularyservice.domain.model;

import com.example.vocabularyservice.domain.exception.InvalidVocabularyException;
import java.util.Objects;

/**
 * Immutable domain representation of a vocabulary item.
 */
public final class VocabularyItem {

    private final Long id;
    private final String en;
    private final String es;
    private final String example;
    private final Level level;

    /**
     * Creates a validated vocabulary item.
     *
     * @param id      identifier (may be {@code null} before persistence)
     * @param en      English term (required)
     * @param es      Spanish translation (required)
     * @param example optional example sentence
     * @param level   CEFR level (required)
     * @throws InvalidVocabularyException if a required field is missing
     */
    public VocabularyItem(final Long id, final String en, final String es,
                          final String example, final Level level) {
        this.en = require(en, "en");
        this.es = require(es, "es");
        if (level == null) {
            throw new InvalidVocabularyException("level is required");
        }
        this.id = id;
        this.example = example;
        this.level = level;
    }

    /**
     * Factory for a new item (no id yet).
     *
     * @param en      English term
     * @param es      Spanish translation
     * @param example example sentence
     * @param level   CEFR level
     * @return a new validated item
     */
    public static VocabularyItem create(final String en, final String es,
                                        final String example, final Level level) {
        return new VocabularyItem(null, en, es, example, level);
    }

    /**
     * Returns a copy with updated mutable fields, keeping the id and English term.
     *
     * @param newEs      new translation (keeps current when {@code null})
     * @param newExample new example (keeps current when {@code null})
     * @param newLevel   new level (keeps current when {@code null})
     * @return the updated item
     */
    public VocabularyItem withUpdate(final String newEs, final String newExample, final Level newLevel) {
        return new VocabularyItem(this.id, this.en,
                newEs == null ? this.es : newEs,
                newExample == null ? this.example : newExample,
                newLevel == null ? this.level : newLevel);
    }

    private static String require(final String value, final String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidVocabularyException(field + " is required");
        }
        return value.trim();
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

    /** @return the example, may be {@code null} */
    public String getExample() {
        return example;
    }

    /** @return the level */
    public Level getLevel() {
        return level;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VocabularyItem)) {
            return false;
        }
        return Objects.equals(id, ((VocabularyItem) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
