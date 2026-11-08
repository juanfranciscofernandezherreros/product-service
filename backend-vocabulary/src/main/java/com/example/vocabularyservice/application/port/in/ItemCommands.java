package com.example.vocabularyservice.application.port.in;

import com.example.vocabularyservice.domain.model.Level;

/**
 * Input records for vocabulary item operations.
 */
public final class ItemCommands {

    private ItemCommands() {
    }

    /**
     * Data for creating an item.
     *
     * @param en      English term
     * @param es      Spanish translation
     * @param example example sentence
     * @param level   CEFR level
     */
    public record CreateItemCommand(String en, String es, String example, Level level) {
    }

    /**
     * Data for updating an item (null fields keep the current value).
     *
     * @param es      new translation
     * @param example new example
     * @param level   new level
     */
    public record UpdateItemCommand(String es, String example, Level level) {
    }

    /**
     * Query for listing items.
     *
     * @param level  optional level filter
     * @param limit  page size
     * @param offset results to skip
     */
    public record ListItemsQuery(Level level, int limit, int offset) {
    }
}
