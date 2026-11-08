package com.example.miscservice.application.usecase;

import com.example.miscservice.application.port.in.MiscCommands;
import com.example.miscservice.application.port.in.MiscUseCase;
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

import java.util.List;
import java.util.UUID;

/**
 * Application service orchestrating the Misc domain use cases.
 */
public class MiscService implements MiscUseCase {

    private final SuggestionPort suggestionPort;
    private final LogbookCategoryPort categoryPort;
    private final LogbookNotePort notePort;
    private final WritingDraftPort draftPort;

    /**
     * Constructor injection of all output ports.
     *
     * @param suggestionPort suggestions persistence port
     * @param categoryPort   categories persistence port
     * @param notePort       notes persistence port
     * @param draftPort      drafts persistence port
     */
    public MiscService(final SuggestionPort suggestionPort, final LogbookCategoryPort categoryPort,
                       final LogbookNotePort notePort, final WritingDraftPort draftPort) {
        this.suggestionPort = suggestionPort;
        this.categoryPort = categoryPort;
        this.notePort = notePort;
        this.draftPort = draftPort;
    }

    @Override
    public Suggestion createSuggestion(final MiscCommands.CreateSuggestion command) {
        return suggestionPort.save(Suggestion.create(command.titulo(), command.descripcion()));
    }

    @Override
    public Suggestion getSuggestion(final Long id) {
        return suggestionPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found: " + id));
    }

    @Override
    public List<Suggestion> listSuggestions(final SuggestionStatus estado, final int offset, final int limit) {
        return suggestionPort.findAll(estado, offset, limit);
    }

    @Override
    public long countSuggestions(final SuggestionStatus estado) {
        return suggestionPort.count(estado);
    }

    @Override
    public Suggestion updateSuggestionStatus(final MiscCommands.UpdateSuggestionStatus command) {
        final Suggestion existing = getSuggestion(command.id());
        return suggestionPort.save(existing.withStatus(SuggestionStatus.fromValue(command.estado())));
    }

    @Override
    public LogbookCategory createCategory(final MiscCommands.CreateLogbookCategory command) {
        return categoryPort.save(LogbookCategory.create(command.usuarioId(), command.nombre()));
    }

    @Override
    public List<LogbookCategory> listCategories(final UUID usuarioId, final int offset, final int limit) {
        return categoryPort.findByUser(usuarioId, offset, limit);
    }

    @Override
    public long countCategories(final UUID usuarioId) {
        return categoryPort.countByUser(usuarioId);
    }

    @Override
    public LogbookNote createNote(final MiscCommands.CreateLogbookNote command) {
        if (command.categoriaId() != null) {
            categoryPort.findByIdAndUser(command.categoriaId(), command.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found: " + command.categoriaId()));
        }
        return notePort.save(LogbookNote.create(command.usuarioId(), command.categoriaId(), command.contenido()));
    }

    @Override
    public List<LogbookNote> listNotes(final UUID usuarioId, final int offset, final int limit) {
        return notePort.findByUser(usuarioId, offset, limit);
    }

    @Override
    public long countNotes(final UUID usuarioId) {
        return notePort.countByUser(usuarioId);
    }

    @Override
    public WritingDraft createDraft(final MiscCommands.CreateWritingDraft command) {
        return draftPort.save(WritingDraft.create(
                command.usuarioId(), WritingType.fromValue(command.tipo()),
                command.titulo(), command.contenido()));
    }

    @Override
    public List<WritingDraft> listDrafts(final UUID usuarioId, final int offset, final int limit) {
        return draftPort.findByUser(usuarioId, offset, limit);
    }

    @Override
    public long countDrafts(final UUID usuarioId) {
        return draftPort.countByUser(usuarioId);
    }
}
