package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link VocabularyProgressJpaEntity}.
 */
public interface VocabularyProgressJpaRepository extends JpaRepository<VocabularyProgressJpaEntity, ProgressId> {

    /**
     * Finds progress records by user.
     *
     * @param userId user id
     * @return the user's progress
     */
    List<VocabularyProgressJpaEntity> findByUserId(UUID userId);
}
