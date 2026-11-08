package com.example.vocabularyservice.application.port.in;

import com.example.vocabularyservice.application.port.in.ItemCommands.CreateItemCommand;
import com.example.vocabularyservice.application.port.in.ItemCommands.ListItemsQuery;
import com.example.vocabularyservice.application.port.in.ItemCommands.UpdateItemCommand;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the vocabulary use cases.
 */
public interface VocabularyUseCases {

    /**
     * Creates an item.
     *
     * @param command attributes
     * @return the created item
     */
    VocabularyItem createItem(CreateItemCommand command);

    /**
     * Gets an item by id.
     *
     * @param id item id
     * @return the item
     */
    VocabularyItem getItem(long id);

    /**
     * Lists items.
     *
     * @param query pagination and filter
     * @return a page of items
     */
    PagedItems listItems(ListItemsQuery query);

    /**
     * Updates an item.
     *
     * @param id      item id
     * @param command new attributes
     * @return the updated item
     */
    VocabularyItem updateItem(long id, UpdateItemCommand command);

    /**
     * Deletes an item.
     *
     * @param id item id
     */
    void deleteItem(long id);

    /**
     * Lists a user's progress.
     *
     * @param userId user id
     * @return the user's progress
     */
    List<VocabularyProgress> listProgress(UUID userId);

    /**
     * Creates or updates a user's progress on an item.
     *
     * @param command progress data
     * @return the stored progress
     */
    VocabularyProgress upsertProgress(UpsertProgressCommand command);
}
