package com.example.miscservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Request and response DTOs for the web adapter.
 */
public final class MiscDtos {

    private MiscDtos() {
    }

    /**
     * Request body to create a suggestion.
     *
     * @param titulo      title (5..100)
     * @param descripcion description (10..1000)
     */
    public record CreateSuggestionRequest(
            @NotBlank @Size(min = 5, max = 100) String titulo,
            @NotBlank @Size(min = 10, max = 1000) String descripcion) {
    }

    /**
     * Request body to change a suggestion status.
     *
     * @param estado target status
     */
    public record UpdateSuggestionStatusRequest(@NotBlank String estado) {
    }

    /**
     * Response body for a suggestion.
     *
     * @param id          id
     * @param titulo      title
     * @param descripcion description
     * @param estado      status
     * @param creadoEn    creation timestamp
     */
    public record SuggestionResponse(Long id, String titulo, String descripcion,
                                     String estado, OffsetDateTime creadoEn) {
    }

    /**
     * Request body to create a logbook category.
     *
     * @param nombre name (1..60)
     */
    public record CreateCategoryRequest(@NotBlank @Size(min = 1, max = 60) String nombre) {
    }

    /**
     * Response body for a logbook category.
     *
     * @param id        id
     * @param usuarioId owning user
     * @param nombre    name
     * @param creadoEn  creation timestamp
     */
    public record CategoryResponse(UUID id, UUID usuarioId, String nombre, OffsetDateTime creadoEn) {
    }

    /**
     * Request body to create a logbook note.
     *
     * @param categoriaId optional category id
     * @param contenido   content (1..10000)
     */
    public record CreateNoteRequest(
            UUID categoriaId,
            @NotBlank @Size(min = 1, max = 10000) String contenido) {
    }

    /**
     * Response body for a logbook note.
     *
     * @param id            id
     * @param usuarioId     owning user
     * @param categoriaId   optional category
     * @param contenido     content
     * @param creadoEn      creation timestamp
     * @param actualizadoEn update timestamp
     */
    public record NoteResponse(UUID id, UUID usuarioId, UUID categoriaId, String contenido,
                               OffsetDateTime creadoEn, OffsetDateTime actualizadoEn) {
    }

    /**
     * Request body to create a writing draft.
     *
     * @param tipo      writing type
     * @param titulo    optional title
     * @param contenido optional content
     */
    public record CreateDraftRequest(
            @NotNull String tipo,
            @Size(max = 255) String titulo,
            String contenido) {
    }

    /**
     * Response body for a writing draft.
     *
     * @param id            id
     * @param usuarioId     owning user
     * @param tipo          type
     * @param titulo        title
     * @param contenido     content
     * @param creadoEn      creation timestamp
     * @param actualizadoEn update timestamp
     */
    public record DraftResponse(UUID id, UUID usuarioId, String tipo, String titulo, String contenido,
                                OffsetDateTime creadoEn, OffsetDateTime actualizadoEn) {
    }

    /**
     * Generic paginated response wrapper.
     *
     * @param <T>     item type
     * @param items   page items
     * @param total   total element count
     * @param offset  applied offset
     * @param limit   applied limit
     */
    public record PageResponse<T>(List<T> items, long total, int offset, int limit) {
    }

    /**
     * Error payload.
     *
     * @param code    machine-readable error code
     * @param message human-readable detail
     */
    public record ErrorResponse(String code, String message) {
    }
}
