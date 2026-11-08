package com.example.contentservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * JPA entity mapped to the {@code movie_subtitle_phrases} table.
 */
@Entity
@Table(name = "movie_subtitle_phrases")
public class MoviePhraseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_slug", nullable = false)
    private String movieSlug;

    @Column(name = "movie_title", nullable = false)
    private String movieTitle;

    @Column(name = "subtitle_number", nullable = false)
    private int subtitleNumber;

    @Column(nullable = false, length = 50)
    private String timecode;

    @Column(nullable = false, columnDefinition = "text")
    private String phrase;

    @Column(name = "tense_labels", nullable = false, columnDefinition = "text")
    private String tenseLabels;

    @Column(nullable = false, columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMovieSlug() {
        return movieSlug;
    }

    public void setMovieSlug(final String movieSlug) {
        this.movieSlug = movieSlug;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(final String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getSubtitleNumber() {
        return subtitleNumber;
    }

    public void setSubtitleNumber(final int subtitleNumber) {
        this.subtitleNumber = subtitleNumber;
    }

    public String getTimecode() {
        return timecode;
    }

    public void setTimecode(final String timecode) {
        this.timecode = timecode;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(final String phrase) {
        this.phrase = phrase;
    }

    public String getTenseLabels() {
        return tenseLabels;
    }

    public void setTenseLabels(final String tenseLabels) {
        this.tenseLabels = tenseLabels;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
