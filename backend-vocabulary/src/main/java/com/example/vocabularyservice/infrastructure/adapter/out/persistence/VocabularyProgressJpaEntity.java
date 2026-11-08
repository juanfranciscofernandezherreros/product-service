package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity for vocabulary progress (composite key user_id + item_id).
 */
@Entity
@Table(name = "vocabulary_progress")
@IdClass(ProgressId.class)
public class VocabularyProgressJpaEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "streak", nullable = false)
    private int streak;

    @Column(name = "last_grade", nullable = false)
    private int lastGrade;

    @Column(name = "next_review_at", nullable = false)
    private Instant nextReviewAt;

    @Column(name = "first_seen_day", nullable = false)
    private LocalDate firstSeenDay;

    /**
     * Default constructor required by JPA.
     */
    protected VocabularyProgressJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId       user id
     * @param itemId       item id
     * @param streak       streak
     * @param lastGrade    last grade
     * @param nextReviewAt next review instant
     * @param firstSeenDay first seen day
     */
    public VocabularyProgressJpaEntity(final UUID userId, final Long itemId, final int streak,
                                       final int lastGrade, final Instant nextReviewAt,
                                       final LocalDate firstSeenDay) {
        this.userId = userId;
        this.itemId = itemId;
        this.streak = streak;
        this.lastGrade = lastGrade;
        this.nextReviewAt = nextReviewAt;
        this.firstSeenDay = firstSeenDay;
    }

    /** @return the user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return the item id */
    public Long getItemId() {
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
