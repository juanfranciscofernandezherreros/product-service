package com.example.miscservice.infrastructure.adapter.in.web;

import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.WritingDraft;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CategoryResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.DraftResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.NoteResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.SuggestionResponse;

/**
 * Maps domain models to web response DTOs.
 */
public final class WebMapper {

    private WebMapper() {
    }

    public static SuggestionResponse toResponse(final Suggestion s) {
        return new SuggestionResponse(s.id(), s.titulo(), s.descripcion(), s.estado().value(), s.creadoEn());
    }

    public static CategoryResponse toResponse(final LogbookCategory c) {
        return new CategoryResponse(c.id(), c.usuarioId(), c.nombre(), c.creadoEn());
    }

    public static NoteResponse toResponse(final LogbookNote n) {
        return new NoteResponse(n.id(), n.usuarioId(), n.categoriaId(), n.contenido(),
                n.creadoEn(), n.actualizadoEn());
    }

    public static DraftResponse toResponse(final WritingDraft d) {
        return new DraftResponse(d.id(), d.usuarioId(), d.tipo().value(), d.titulo(), d.contenido(),
                d.creadoEn(), d.actualizadoEn());
    }
}
