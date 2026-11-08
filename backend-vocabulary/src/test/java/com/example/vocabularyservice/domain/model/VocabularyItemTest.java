package com.example.vocabularyservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.vocabularyservice.domain.exception.InvalidVocabularyException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link VocabularyItem}.
 */
class VocabularyItemTest {

    @Test
    void createsValidItem() {
        final VocabularyItem item = VocabularyItem.create("book", "libro", "a book", Level.A1);
        assertThat(item.getEn()).isEqualTo("book");
        assertThat(item.getLevel()).isEqualTo(Level.A1);
        assertThat(item.getId()).isNull();
    }

    @Test
    void rejectsBlankEn() {
        assertThatThrownBy(() -> VocabularyItem.create(" ", "libro", null, Level.A1))
                .isInstanceOf(InvalidVocabularyException.class);
    }

    @Test
    void rejectsNullLevel() {
        assertThatThrownBy(() -> VocabularyItem.create("book", "libro", null, null))
                .isInstanceOf(InvalidVocabularyException.class);
    }

    @Test
    void updateKeepsIdAndEnMergesNulls() {
        final VocabularyItem item = new VocabularyItem(5L, "book", "libro", "ex", Level.A1);
        final VocabularyItem updated = item.withUpdate("libro2", null, Level.B2);
        assertThat(updated.getId()).isEqualTo(5L);
        assertThat(updated.getEn()).isEqualTo("book");
        assertThat(updated.getEs()).isEqualTo("libro2");
        assertThat(updated.getExample()).isEqualTo("ex");
        assertThat(updated.getLevel()).isEqualTo(Level.B2);
    }

    @Test
    void equalsBasedOnId() {
        final VocabularyItem a = new VocabularyItem(1L, "a", "a", null, Level.A1);
        final VocabularyItem b = new VocabularyItem(1L, "b", "b", null, Level.C1);
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }
}
