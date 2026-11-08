package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import com.example.vocabularyservice.application.port.out.VocabularyItemRepositoryPort;
import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Outbound adapter implementing {@link VocabularyItemRepositoryPort}.
 */
public class VocabularyItemPersistenceAdapter implements VocabularyItemRepositoryPort {

    private final VocabularyItemJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public VocabularyItemPersistenceAdapter(final VocabularyItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public VocabularyItem save(final VocabularyItem item) {
        return VocabularyPersistenceMapper.toDomain(
                jpaRepository.save(VocabularyPersistenceMapper.toEntity(item)));
    }

    @Override
    public Optional<VocabularyItem> findById(final long id) {
        return jpaRepository.findById(id).map(VocabularyPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(final long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByEn(final String en) {
        return jpaRepository.existsByEn(en);
    }

    @Override
    public List<VocabularyItem> findByFilter(final Level level, final int limit, final int offset) {
        final Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("id").ascending());
        final String levelName = level == null ? null : level.name();
        return jpaRepository.findByFilter(levelName, pageable).stream()
                .map(VocabularyPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByFilter(final Level level) {
        return jpaRepository.countByFilter(level == null ? null : level.name());
    }

    @Override
    public void deleteById(final long id) {
        jpaRepository.deleteById(id);
    }
}
