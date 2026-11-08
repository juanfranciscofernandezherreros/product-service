package com.example.vocabularyservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.vocabularyservice.domain.exception.InvalidVocabularyException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link VocabularyProgress}.
 */
class VocabularyProgressTest {

    @Test
    void createsValidProgressWithDefaults() {
        final VocabularyProgress p = new VocabularyProgress(UUID.randomUUID(), 1L, 0, 0, null, null);
        assertThat(p.getNextReviewAt()).isNotNull();
        assertThat(p.getFirstSeenDay()).isNotNull();
    }

    @Test
    void rejectsNegativeStreak() {
        assertThatThrownBy(() -> new VocabularyProgress(UUID.randomUUID(), 1L, -1, 0, null, null))
                .isInstanceOf(InvalidVocabularyException.class);
    }

    @Test
    void rejectsGradeOutOfRange() {
        assertThatThrownBy(() -> new VocabularyProgress(UUID.randomUUID(), 1L, 0, 3, null, null))
                .isInstanceOf(InvalidVocabularyException.class);
    }

    @Test
    void rejectsNullUser() {
        assertThatThrownBy(() -> new VocabularyProgress(null, 1L, 0, 0, null, null))
                .isInstanceOf(InvalidVocabularyException.class);
    }
}
