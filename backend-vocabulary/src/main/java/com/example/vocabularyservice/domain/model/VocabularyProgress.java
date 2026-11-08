package com.example.vocabularyservice.domain.model;

import com.example.vocabularyservice.domain.exception.InvalidVocabularyException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Immutable domain representation of a user's progress on a vocabulary item.
 */
public final class VocabularyProgress {

    private static final int MAX_GRADE = 2;

    private final UUID userId;
    private final long itemId;
    private final int streak;
    private final int lastGrade;
    private final Instant nextReviewAt;
    private final LocalDate firstSeenDay;

    /**
     * Creates a validated progress record.
     *
     * @param userId       owning user
     * @param itemId       vocabulary item id
     * @param streak       current streak (&ge; 0)
     * @param lastGrade    last grade (0..2)
     * @param nextReviewAt next review instant
     * @param firstSeenDay first seen day
     * @throws InvalidVocabularyException if an invariant is violated
     */
    public VocabularyProgress(final UUID userId, final long itemId, final int streak, final int lastGrade,
                              final Instant nextReviewAt, final LocalDate firstSeenDay) {
        if (userId == null) {
            throw new InvalidVocabularyException("userId is required");
        }
        if (streak < 0) {
            throw new InvalidVocabularyException("streak must not be negative");
        }
        if (lastGrade < 0 || lastGrade > MAX_GRADE) {
            throw new InvalidVocabularyException("lastGrade must be between 0 and " + MAX_GRADE);
        }
        this.userId = userId;
        this.itemId = itemId;
        this.streak = streak;
        this.lastGrade = lastGrade;
        this.nextReviewAt = nextReviewAt == null ? Instant.now() : nextReviewAt;
        this.firstSeenDay = firstSeenDay == null ? LocalDate.now() : firstSeenDay;
    }

    /** @return the user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return the item id */
    public long getItemId() {
        return itemId;
    }

    /** @return the streak */
    public int getStreak() {
        return streak;
    }

    /** @return the last grade */
    public int getLastGrade() {
        return lastGrade;
    }

    /** @return the next review instant */
    public Instant getNextReviewAt() {
        return nextReviewAt;
    }

    /** @return the first seen day */
    public LocalDate getFirstSeenDay() {
        return firstSeenDay;
    }
}
