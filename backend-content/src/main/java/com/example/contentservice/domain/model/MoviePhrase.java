package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.OffsetDateTime;

/**
 * Immutable domain model for a movie subtitle phrase.
 *
 * @param id             identifier (nullable before persistence)
 * @param movieSlug      movie slug
 * @param movieTitle     movie title
 * @param subtitleNumber subtitle number (positive)
 * @param timecode       timecode
 * @param phrase         subtitle phrase
 * @param tenseLabels    optional tense labels
 * @param notes          optional notes
 * @param createdAt      creation timestamp
 */
public record MoviePhrase(Long id, String movieSlug, String movieTitle, int subtitleNumber,
                          String timecode, String phrase, String tenseLabels, String notes,
                          OffsetDateTime createdAt) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public MoviePhrase {
        if (movieSlug == null || movieSlug.strip().isEmpty()) {
            throw new InvalidContentException("movieSlug is required");
        }
        if (movieTitle == null || movieTitle.strip().isEmpty()) {
            throw new InvalidContentException("movieTitle is required");
        }
        if (subtitleNumber <= 0) {
            throw new InvalidContentException("subtitleNumber must be positive");
        }
        if (timecode == null || timecode.strip().isEmpty()) {
            throw new InvalidContentException("timecode is required");
        }
        if (phrase == null || phrase.strip().isEmpty()) {
            throw new InvalidContentException("phrase is required");
        }
    }

    /**
     * Factory for a new movie phrase (labels and notes default to empty).
     *
     * @param movieSlug      movie slug
     * @param movieTitle     movie title
     * @param subtitleNumber subtitle number
     * @param timecode       timecode
     * @param phrase         phrase
     * @param tenseLabels    tense labels
     * @param notes          notes
     * @return a new movie phrase
     */
    public static MoviePhrase create(final String movieSlug, final String movieTitle,
                                     final int subtitleNumber, final String timecode, final String phrase,
                                     final String tenseLabels, final String notes) {
        return new MoviePhrase(null, movieSlug, movieTitle, subtitleNumber, timecode, phrase,
                tenseLabels == null ? "" : tenseLabels, notes == null ? "" : notes, null);
    }
}
