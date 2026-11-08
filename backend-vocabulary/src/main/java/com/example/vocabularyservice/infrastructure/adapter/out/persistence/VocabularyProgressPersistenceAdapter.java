package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import com.example.vocabularyservice.application.port.out.VocabularyProgressRepositoryPort;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Outbound adapter implementing {@link VocabularyProgressRepositoryPort}.
 */
public class VocabularyProgressPersistenceAdapter implements VocabularyProgressRepositoryPort {

    private final VocabularyProgressJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public VocabularyProgressPersistenceAdapter(final VocabularyProgressJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<VocabularyProgress> findByUser(final UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(VocabularyPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public VocabularyProgress save(final VocabularyProgress progress) {
        return VocabularyPersistenceMapper.toDomain(
                jpaRepository.save(VocabularyPersistenceMapper.toEntity(progress)));
    }
}
