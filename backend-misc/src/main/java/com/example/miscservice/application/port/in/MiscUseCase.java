package com.example.miscservice.application.port.in;

import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.domain.model.WritingDraft;

import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the Misc domain operations.
 */
public interface MiscUseCase {

    Suggestion createSuggestion(MiscCommands.CreateSuggestion command);

    Suggestion getSuggestion(Long id);

    List<Suggestion> listSuggestions(SuggestionStatus estado, int offset, int limit);

    long countSuggestions(SuggestionStatus estado);

    Suggestion updateSuggestionStatus(MiscCommands.UpdateSuggestionStatus command);

    LogbookCategory createCategory(MiscCommands.CreateLogbookCategory command);

    List<LogbookCategory> listCategories(UUID usuarioId, int offset, int limit);

    long countCategories(UUID usuarioId);

    LogbookNote createNote(MiscCommands.CreateLogbookNote command);

    List<LogbookNote> listNotes(UUID usuarioId, int offset, int limit);

    long countNotes(UUID usuarioId);

    WritingDraft createDraft(MiscCommands.CreateWritingDraft command);

    List<WritingDraft> listDrafts(UUID usuarioId, int offset, int limit);

    long countDrafts(UUID usuarioId);
}
