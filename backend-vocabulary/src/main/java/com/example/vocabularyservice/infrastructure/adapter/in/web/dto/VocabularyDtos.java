package com.example.vocabularyservice.infrastructure.adapter.in.web.dto;

import com.example.vocabularyservice.domain.model.Level;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Web DTOs for the vocabulary API.
 */
public final class VocabularyDtos {

    private VocabularyDtos() {
    }

    /**
     * Create item request.
     *
     * @param en      English term
     * @param es      Spanish translation
     * @param example example
     * @param level   level
     */
    public record ItemRequest(
            @NotBlank String en,
            @NotBlank String es,
            String example,
            @NotNull Level level) {
    }

    /**
     * Update item request (null fields keep current value).
     *
     * @param es      translation
     * @param example example
     * @param level   level
     */
    public record ItemUpdateRequest(String es, String example, Level level) {
    }

    /**
     * Item response.
     *
     * @param id      id
     * @param en      English term
     * @param es      Spanish translation
     * @param example example
     * @param level   level
     */
    public record ItemResponse(Long id, String en, String es, String example, Level level) {
    }

    /**
     * Paged items response.
     *
     * @param total total
     * @param items items
     */
    public record PagedItemsResponse(long total, List<ItemResponse> items) {
    }

    /**
     * Progress upsert request.
     *
     * @param itemId       item id
     * @param streak       streak
     * @param lastGrade    last grade (0..2)
     * @param nextReviewAt next review instant
     */
    public record ProgressUpsertRequest(
            @NotNull Long itemId,
            @Min(0) Integer streak,
            @Min(0) @Max(2) int lastGrade,
            Instant nextReviewAt) {
    }

    /**
     * Progress response.
     *
     * @param userId       user id
     * @param itemId       item id
     * @param streak       streak
     * @param lastGrade    last grade
     * @param nextReviewAt next review instant
     * @param firstSeenDay first seen day
     */
    public record ProgressResponse(UUID userId, long itemId, int streak, int lastGrade,
                                   Instant nextReviewAt, LocalDate firstSeenDay) {
    }

    /**
     * Error payload.
     *
     * @param code    code
     * @param message message
     */
    public record ErrorResponse(String code, String message) {
    }
}
