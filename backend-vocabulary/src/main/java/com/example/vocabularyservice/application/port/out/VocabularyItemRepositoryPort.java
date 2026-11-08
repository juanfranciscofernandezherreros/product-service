package com.example.vocabularyservice.application.port.out;

import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for vocabulary item persistence.
 */
public interface VocabularyItemRepositoryPort {

    /**
     * Persists (creates or updates) an item.
     *
     * @param item item to store
     * @return the stored item
     */
    VocabularyItem save(VocabularyItem item);

    /**
     * Finds an item by id.
     *
     * @param id item id
     * @return the item if present
     */
    Optional<VocabularyItem> findById(long id);

    /**
     * Indicates whether an item exists.
     *
     * @param id item id
     * @return {@code true} if present
     */
    boolean existsById(long id);

    /**
     * Indicates whether an item with the given English term exists.
     *
     * @param en English term
     * @return {@code true} if present
     */
    boolean existsByEn(String en);

    /**
     * Returns a page of items, optionally filtered by level.
     *
     * @param level  optional level ({@code null} = no filter)
     * @param limit  page size
     * @param offset results to skip
     * @return matching items
     */
    List<VocabularyItem> findByFilter(Level level, int limit, int offset);

    /**
     * Counts items, optionally filtered by level.
     *
     * @param level optional level
     * @return number of matching items
     */
    long countByFilter(Level level);

    /**
     * Deletes an item by id.
     *
     * @param id item id
     */
    void deleteById(long id);
}
