package com.example.miscservice.application.port.out;

import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.domain.model.WritingDraft;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output ports (persistence contracts) for the Misc domain.
 */
public final class MiscOutPorts {

    private MiscOutPorts() {
    }

    /**
     * Persistence port for suggestions.
     */
    public interface SuggestionPort {
        Suggestion save(Suggestion suggestion);

        Optional<Suggestion> findById(Long id);

        List<Suggestion> findAll(SuggestionStatus estado, int offset, int limit);

        long count(SuggestionStatus estado);
    }

    /**
     * Persistence port for logbook categories.
     */
    public interface LogbookCategoryPort {
        LogbookCategory save(LogbookCategory category);

        Optional<LogbookCategory> findByIdAndUser(UUID id, UUID usuarioId);

        List<LogbookCategory> findByUser(UUID usuarioId, int offset, int limit);

        long countByUser(UUID usuarioId);
    }

    /**
     * Persistence port for logbook notes.
     */
    public interface LogbookNotePort {
        LogbookNote save(LogbookNote note);

        List<LogbookNote> findByUser(UUID usuarioId, int offset, int limit);

        long countByUser(UUID usuarioId);
    }

    /**
     * Persistence port for writing drafts.
     */
    public interface WritingDraftPort {
        WritingDraft save(WritingDraft draft);

        List<WritingDraft> findByUser(UUID usuarioId, int offset, int limit);

        long countByUser(UUID usuarioId);
    }
}
