package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import com.example.vocabularyservice.domain.model.VocabularyProgress;

/**
 * Explicit mapper between domain models and JPA entities.
 */
public final class VocabularyPersistenceMapper {

    private VocabularyPersistenceMapper() {
    }

    /**
     * Converts a domain item to an entity.
     *
     * @param item domain item
     * @return the entity
     */
    public static VocabularyItemJpaEntity toEntity(final VocabularyItem item) {
        return new VocabularyItemJpaEntity(item.getId(), item.getEn(), item.getEs(),
                item.getExample(), item.getLevel().name());
    }

    /**
     * Converts an entity to a domain item.
     *
     * @param entity entity
     * @return the domain item
     */
    public static VocabularyItem toDomain(final VocabularyItemJpaEntity entity) {
        return new VocabularyItem(entity.getId(), entity.getEn(), entity.getEs(),
                entity.getExample(), Level.valueOf(entity.getLevel()));
    }

    /**
     * Converts a domain progress to an entity.
     *
     * @param progress domain progress
     * @return the entity
     */
    public static VocabularyProgressJpaEntity toEntity(final VocabularyProgress progress) {
        return new VocabularyProgressJpaEntity(progress.getUserId(), progress.getItemId(),
                progress.getStreak(), progress.getLastGrade(), progress.getNextReviewAt(),
                progress.getFirstSeenDay());
    }

    /**
     * Converts an entity to a domain progress.
     *
     * @param entity entity
     * @return the domain progress
     */
    public static VocabularyProgress toDomain(final VocabularyProgressJpaEntity entity) {
        return new VocabularyProgress(entity.getUserId(), entity.getItemId(), entity.getStreak(),
                entity.getLastGrade(), entity.getNextReviewAt(), entity.getFirstSeenDay());
    }
}
