package com.example.vocabularyservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.vocabularyservice.application.port.in.ItemCommands.CreateItemCommand;
import com.example.vocabularyservice.application.port.in.ItemCommands.ListItemsQuery;
import com.example.vocabularyservice.application.port.in.ItemCommands.UpdateItemCommand;
import com.example.vocabularyservice.application.port.in.PagedItems;
import com.example.vocabularyservice.application.port.in.UpsertProgressCommand;
import com.example.vocabularyservice.application.port.out.VocabularyItemRepositoryPort;
import com.example.vocabularyservice.application.port.out.VocabularyProgressRepositoryPort;
import com.example.vocabularyservice.domain.exception.DuplicateVocabularyItemException;
import com.example.vocabularyservice.domain.exception.VocabularyItemNotFoundException;
import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link VocabularyService}.
 */
@ExtendWith(MockitoExtension.class)
class VocabularyServiceTest {

    @Mock
    private VocabularyItemRepositoryPort itemRepository;
    @Mock
    private VocabularyProgressRepositoryPort progressRepository;

    private VocabularyService service;

    @BeforeEach
    void setUp() {
        service = new VocabularyService(itemRepository, progressRepository);
    }

    @Test
    void createItemPersistsWhenNew() {
        when(itemRepository.existsByEn("book")).thenReturn(false);
        when(itemRepository.save(any(VocabularyItem.class))).thenAnswer(inv -> inv.getArgument(0));

        final VocabularyItem result = service.createItem(new CreateItemCommand("book", "libro", null, Level.A1));

        assertThat(result.getEn()).isEqualTo("book");
    }

    @Test
    void createItemThrowsWhenDuplicate() {
        when(itemRepository.existsByEn("book")).thenReturn(true);

        assertThatThrownBy(() -> service.createItem(new CreateItemCommand("book", "libro", null, Level.A1)))
                .isInstanceOf(DuplicateVocabularyItemException.class);
        verify(itemRepository, never()).save(any(VocabularyItem.class));
    }

    @Test
    void getItemReturnsWhenPresent() {
        final VocabularyItem item = new VocabularyItem(1L, "book", "libro", null, Level.A1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThat(service.getItem(1L)).isEqualTo(item);
    }

    @Test
    void getItemThrowsWhenMissing() {
        when(itemRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getItem(9L)).isInstanceOf(VocabularyItemNotFoundException.class);
    }

    @Test
    void listItemsReturnsPage() {
        final VocabularyItem item = new VocabularyItem(1L, "book", "libro", null, Level.B2);
        when(itemRepository.findByFilter(eq(Level.B2), anyInt(), anyInt())).thenReturn(List.of(item));
        when(itemRepository.countByFilter(Level.B2)).thenReturn(1L);

        final PagedItems page = service.listItems(new ListItemsQuery(Level.B2, 20, 0));

        assertThat(page.total()).isEqualTo(1L);
        assertThat(page.items()).containsExactly(item);
    }

    @Test
    void updateItemMergesWhenPresent() {
        final VocabularyItem existing = new VocabularyItem(1L, "book", "libro", null, Level.A1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(itemRepository.save(any(VocabularyItem.class))).thenAnswer(inv -> inv.getArgument(0));

        final VocabularyItem result = service.updateItem(1L, new UpdateItemCommand("libro2", null, Level.C1));

        assertThat(result.getEs()).isEqualTo("libro2");
        assertThat(result.getLevel()).isEqualTo(Level.C1);
    }

    @Test
    void updateItemThrowsWhenMissing() {
        when(itemRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateItem(9L, new UpdateItemCommand(null, null, null)))
                .isInstanceOf(VocabularyItemNotFoundException.class);
    }

    @Test
    void deleteItemRemovesWhenPresent() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        service.deleteItem(1L);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    void deleteItemThrowsWhenMissing() {
        when(itemRepository.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteItem(9L)).isInstanceOf(VocabularyItemNotFoundException.class);
    }

    @Test
    void listProgressDelegates() {
        final UUID user = UUID.randomUUID();
        final VocabularyProgress p = new VocabularyProgress(user, 1L, 0, 0, null, null);
        when(progressRepository.findByUser(user)).thenReturn(List.of(p));
        assertThat(service.listProgress(user)).containsExactly(p);
    }

    @Test
    void upsertProgressPersistsWhenItemExists() {
        final UUID user = UUID.randomUUID();
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(progressRepository.save(any(VocabularyProgress.class))).thenAnswer(inv -> inv.getArgument(0));

        final VocabularyProgress result = service.upsertProgress(
                new UpsertProgressCommand(user, 1L, 2, 1, null));

        assertThat(result.getStreak()).isEqualTo(2);
        assertThat(result.getLastGrade()).isEqualTo(1);
    }

    @Test
    void upsertProgressThrowsWhenItemMissing() {
        when(itemRepository.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.upsertProgress(
                new UpsertProgressCommand(UUID.randomUUID(), 9L, null, 0, null)))
                .isInstanceOf(VocabularyItemNotFoundException.class);
    }
}
