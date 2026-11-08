package com.example.miscservice.application.usecase;

import com.example.miscservice.application.port.in.MiscCommands;
import com.example.miscservice.application.port.out.MiscOutPorts.LogbookCategoryPort;
import com.example.miscservice.application.port.out.MiscOutPorts.LogbookNotePort;
import com.example.miscservice.application.port.out.MiscOutPorts.SuggestionPort;
import com.example.miscservice.application.port.out.MiscOutPorts.WritingDraftPort;
import com.example.miscservice.domain.exception.ResourceNotFoundException;
import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.domain.model.WritingDraft;
import com.example.miscservice.domain.model.WritingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MiscServiceTest {

    private SuggestionPort suggestionPort;
    private LogbookCategoryPort categoryPort;
    private LogbookNotePort notePort;
    private WritingDraftPort draftPort;
    private MiscService service;

    @BeforeEach
    void setUp() {
        suggestionPort = mock(SuggestionPort.class);
        categoryPort = mock(LogbookCategoryPort.class);
        notePort = mock(LogbookNotePort.class);
        draftPort = mock(WritingDraftPort.class);
        service = new MiscService(suggestionPort, categoryPort, notePort, draftPort);
    }

    @Test
    void createSuggestionPersistsPending() {
        when(suggestionPort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Suggestion result = service.createSuggestion(
                new MiscCommands.CreateSuggestion("Una idea", "Descripcion suficientemente larga"));
        assertThat(result.estado()).isEqualTo(SuggestionStatus.PENDIENTE);
        verify(suggestionPort).save(any());
    }

    @Test
    void getSuggestionReturnsExisting() {
        final Suggestion s = new Suggestion(1L, "Titulo ok", "Descripcion larga ok",
                SuggestionStatus.PENDIENTE, OffsetDateTime.now());
        when(suggestionPort.findById(1L)).thenReturn(Optional.of(s));
        assertThat(service.getSuggestion(1L).id()).isEqualTo(1L);
    }

    @Test
    void getSuggestionMissingThrows() {
        when(suggestionPort.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getSuggestion(9L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listAndCountSuggestions() {
        when(suggestionPort.findAll(null, 0, 10)).thenReturn(List.of());
        when(suggestionPort.count(SuggestionStatus.PUBLICADA)).thenReturn(3L);
        assertThat(service.listSuggestions(null, 0, 10)).isEmpty();
        assertThat(service.countSuggestions(SuggestionStatus.PUBLICADA)).isEqualTo(3L);
    }

    @Test
    void updateSuggestionStatusChangesState() {
        final Suggestion s = new Suggestion(1L, "Titulo ok", "Descripcion larga ok",
                SuggestionStatus.PENDIENTE, OffsetDateTime.now());
        when(suggestionPort.findById(1L)).thenReturn(Optional.of(s));
        when(suggestionPort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Suggestion result = service.updateSuggestionStatus(
                new MiscCommands.UpdateSuggestionStatus(1L, "publicada"));
        assertThat(result.estado()).isEqualTo(SuggestionStatus.PUBLICADA);
    }

    @Test
    void createCategoryAndListCount() {
        final UUID user = UUID.randomUUID();
        when(categoryPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(categoryPort.findByUser(user, 0, 10)).thenReturn(List.of());
        when(categoryPort.countByUser(user)).thenReturn(2L);
        final LogbookCategory c = service.createCategory(
                new MiscCommands.CreateLogbookCategory(user, "Gramatica"));
        assertThat(c.nombre()).isEqualTo("Gramatica");
        assertThat(service.listCategories(user, 0, 10)).isEmpty();
        assertThat(service.countCategories(user)).isEqualTo(2L);
    }

    @Test
    void createNoteWithoutCategory() {
        final UUID user = UUID.randomUUID();
        when(notePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final LogbookNote n = service.createNote(
                new MiscCommands.CreateLogbookNote(user, null, "Una anotacion"));
        assertThat(n.categoriaId()).isNull();
    }

    @Test
    void createNoteWithValidCategory() {
        final UUID user = UUID.randomUUID();
        final UUID cat = UUID.randomUUID();
        when(categoryPort.findByIdAndUser(cat, user)).thenReturn(
                Optional.of(new LogbookCategory(cat, user, "Cat", OffsetDateTime.now())));
        when(notePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final LogbookNote n = service.createNote(
                new MiscCommands.CreateLogbookNote(user, cat, "Anotacion"));
        assertThat(n.categoriaId()).isEqualTo(cat);
    }

    @Test
    void createNoteWithMissingCategoryThrows() {
        final UUID user = UUID.randomUUID();
        final UUID cat = UUID.randomUUID();
        when(categoryPort.findByIdAndUser(cat, user)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.createNote(
                new MiscCommands.CreateLogbookNote(user, cat, "Anotacion")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listAndCountNotes() {
        final UUID user = UUID.randomUUID();
        when(notePort.findByUser(user, 0, 5)).thenReturn(List.of());
        when(notePort.countByUser(user)).thenReturn(7L);
        assertThat(service.listNotes(user, 0, 5)).isEmpty();
        assertThat(service.countNotes(user)).isEqualTo(7L);
    }

    @Test
    void createDraftAndListCount() {
        final UUID user = UUID.randomUUID();
        when(draftPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(draftPort.findByUser(user, 0, 10)).thenReturn(List.of());
        when(draftPort.countByUser(user)).thenReturn(4L);
        final WritingDraft d = service.createDraft(
                new MiscCommands.CreateWritingDraft(user, "essay", "Titulo", "Contenido"));
        assertThat(d.tipo()).isEqualTo(WritingType.ESSAY);
        assertThat(service.listDrafts(user, 0, 10)).isEmpty();
        assertThat(service.countDrafts(user)).isEqualTo(4L);
    }
}
