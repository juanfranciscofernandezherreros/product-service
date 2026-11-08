package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for {@link VocabularyItemJpaEntity}.
 */
public interface VocabularyItemJpaRepository extends JpaRepository<VocabularyItemJpaEntity, Long> {

    /**
     * Indicates whether an item with the given English term exists.
     *
     * @param en English term
     * @return {@code true} if present
     */
    boolean existsByEn(String en);

    /**
     * Finds items filtered by optional level.
     *
     * @param level    optional level name ({@code null} = ignore)
     * @param pageable pagination
     * @return matching entities
     */
    @Query("select i from VocabularyItemJpaEntity i where (:level is null or i.level = :level)")
    List<VocabularyItemJpaEntity> findByFilter(@Param("level") String level, Pageable pageable);

    /**
     * Counts items filtered by optional level.
     *
     * @param level optional level name
     * @return number of matching entities
     */
    @Query("select count(i) from VocabularyItemJpaEntity i where (:level is null or i.level = :level)")
    long countByFilter(@Param("level") String level);
}
