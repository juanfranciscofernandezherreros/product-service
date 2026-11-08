package com.example.miscservice.application.port.in;

import java.util.UUID;

/**
 * Command records carrying validated input into the use cases.
 */
public final class MiscCommands {

    private MiscCommands() {
    }

    /**
     * Command to create a suggestion.
     *
     * @param titulo      title
     * @param descripcion description
     */
    public record CreateSuggestion(String titulo, String descripcion) {
    }

    /**
     * Command to change a suggestion status.
     *
     * @param id     suggestion id
     * @param estado target status value
     */
    public record UpdateSuggestionStatus(Long id, String estado) {
    }

    /**
     * Command to create a logbook category.
     *
     * @param usuarioId owning user id
     * @param nombre    category name
     */
    public record CreateLogbookCategory(UUID usuarioId, String nombre) {
    }

    /**
     * Command to create a logbook note.
     *
     * @param usuarioId   owning user id
     * @param categoriaId optional category id
     * @param contenido   note content
     */
    public record CreateLogbookNote(UUID usuarioId, UUID categoriaId, String contenido) {
    }

    /**
     * Command to create a writing draft.
     *
     * @param usuarioId owning user id
     * @param tipo      writing type value
     * @param titulo    optional title
     * @param contenido optional content
     */
    public record CreateWritingDraft(UUID usuarioId, String tipo, String titulo, String contenido) {
    }
}
