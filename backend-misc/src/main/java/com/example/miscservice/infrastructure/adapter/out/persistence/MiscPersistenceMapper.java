package com.example.miscservice.infrastructure.adapter.out.persistence;

import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.domain.model.WritingDraft;
import com.example.miscservice.domain.model.WritingType;

/**
 * Maps between domain models and JPA entities.
 */
public final class MiscPersistenceMapper {

    private MiscPersistenceMapper() {
    }

    public static Suggestion toDomain(final SuggestionEntity e) {
        return new Suggestion(e.getId(), e.getTitulo(), e.getDescripcion(),
                SuggestionStatus.fromValue(e.getEstado()), e.getCreadoEn());
    }

    public static LogbookCategory toDomain(final LogbookCategoryEntity e) {
        return new LogbookCategory(e.getId(), e.getUsuarioId(), e.getNombre(), e.getCreadoEn());
    }

    public static LogbookNote toDomain(final LogbookNoteEntity e) {
        return new LogbookNote(e.getId(), e.getUsuarioId(), e.getCategoriaId(),
                e.getContenido(), e.getCreadoEn(), e.getActualizadoEn());
    }

    public static WritingDraft toDomain(final WritingDraftEntity e) {
        return new WritingDraft(e.getId(), e.getUsuarioId(), WritingType.fromValue(e.getTipo()),
                e.getTitulo(), e.getContenido(), e.getCreadoEn(), e.getActualizadoEn());
    }
}
