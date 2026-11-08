package com.example.vocabularyservice.application.port.out;

import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.UUID;

/**
 * Outbound port for vocabulary progress persistence.
 */
public interface VocabularyProgressRepositoryPort {

    /**
     * Returns the progress records of a user.
     *
     * @param userId user id
     * @return the user's progress
     */
    List<VocabularyProgress> findByUser(UUID userId);

    /**
     * Creates or updates a progress record (upsert).
     *
     * @param progress progress to store
     * @return the stored progress
     */
    VocabularyProgress save(VocabularyProgress progress);
}
