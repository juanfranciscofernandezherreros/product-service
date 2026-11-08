package com.example.vocabularyservice.application.port.in;

import java.time.Instant;
import java.util.UUID;

/**
 * Data for creating or updating a user's progress on an item.
 *
 * @param userId       owning user
 * @param itemId       vocabulary item id
 * @param streak       current streak
 * @param lastGrade    last grade (0..2)
 * @param nextReviewAt next review instant (optional)
 */
public record UpsertProgressCommand(UUID userId, long itemId, Integer streak, int lastGrade,
                                    Instant nextReviewAt) {
}
