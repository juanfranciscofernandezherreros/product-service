package com.example.vocabularyservice.application.usecase;

import com.example.vocabularyservice.application.port.in.ItemCommands.CreateItemCommand;
import com.example.vocabularyservice.application.port.in.ItemCommands.ListItemsQuery;
import com.example.vocabularyservice.application.port.in.ItemCommands.UpdateItemCommand;
import com.example.vocabularyservice.application.port.in.PagedItems;
import com.example.vocabularyservice.application.port.in.UpsertProgressCommand;
import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.application.port.out.VocabularyItemRepositoryPort;
import com.example.vocabularyservice.application.port.out.VocabularyProgressRepositoryPort;
import com.example.vocabularyservice.domain.exception.DuplicateVocabularyItemException;
import com.example.vocabularyservice.domain.exception.VocabularyItemNotFoundException;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the vocabulary use cases.
 */
public class VocabularyService implements VocabularyUseCases {

    private final VocabularyItemRepositoryPort itemRepository;
    private final VocabularyProgressRepositoryPort progressRepository;

    /**
     * Creates the service.
     *
     * @param itemRepository     item persistence port
     * @param progressRepository progress persistence port
     */
    public VocabularyService(final VocabularyItemRepositoryPort itemRepository,
                             final VocabularyProgressRepositoryPort progressRepository) {
        this.itemRepository = itemRepository;
        this.progressRepository = progressRepository;
    }

    @Override
    public VocabularyItem createItem(final CreateItemCommand command) {
        if (command.en() != null && itemRepository.existsByEn(command.en().trim())) {
            throw new DuplicateVocabularyItemException(command.en().trim());
        }
        return itemRepository.save(
                VocabularyItem.create(command.en(), command.es(), command.example(), command.level()));
    }

    @Override
    public VocabularyItem getItem(final long id) {
        return itemRepository.findById(id).orElseThrow(() -> new VocabularyItemNotFoundException(id));
    }

    @Override
    public PagedItems listItems(final ListItemsQuery query) {
        final List<VocabularyItem> items = itemRepository.findByFilter(query.level(), query.limit(), query.offset());
        return new PagedItems(itemRepository.countByFilter(query.level()), items);
    }

    @Override
    public VocabularyItem updateItem(final long id, final UpdateItemCommand command) {
        final VocabularyItem existing = itemRepository.findById(id)
                .orElseThrow(() -> new VocabularyItemNotFoundException(id));
        return itemRepository.save(existing.withUpdate(command.es(), command.example(), command.level()));
    }

    @Override
    public void deleteItem(final long id) {
        if (!itemRepository.existsById(id)) {
            throw new VocabularyItemNotFoundException(id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<VocabularyProgress> listProgress(final UUID userId) {
        return progressRepository.findByUser(userId);
    }

    @Override
    public VocabularyProgress upsertProgress(final UpsertProgressCommand command) {
        if (!itemRepository.existsById(command.itemId())) {
            throw new VocabularyItemNotFoundException(command.itemId());
        }
        final int streak = command.streak() == null ? 0 : command.streak();
        return progressRepository.save(new VocabularyProgress(
                command.userId(), command.itemId(), streak, command.lastGrade(),
                command.nextReviewAt(), null));
    }
}
