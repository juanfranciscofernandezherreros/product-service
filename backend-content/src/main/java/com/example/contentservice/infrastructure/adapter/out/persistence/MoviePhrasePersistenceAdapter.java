package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.MoviePhrasePort;
import com.example.contentservice.domain.model.MoviePhrase;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Persistence adapter implementing {@link MoviePhrasePort}.
 */
public class MoviePhrasePersistenceAdapter implements MoviePhrasePort {

    private final MoviePhraseJpaRepository repository;

    public MoviePhrasePersistenceAdapter(final MoviePhraseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public MoviePhrase save(final MoviePhrase p) {
        final MoviePhraseEntity entity = new MoviePhraseEntity();
        entity.setId(p.id());
        entity.setMovieSlug(p.movieSlug());
        entity.setMovieTitle(p.movieTitle());
        entity.setSubtitleNumber(p.subtitleNumber());
        entity.setTimecode(p.timecode());
        entity.setPhrase(p.phrase());
        entity.setTenseLabels(p.tenseLabels());
        entity.setNotes(p.notes());
        entity.setCreatedAt(p.createdAt() != null ? p.createdAt() : OffsetDateTime.now());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsByMovieAndNumber(final String movieSlug, final int subtitleNumber) {
        return repository.existsByMovieSlugAndSubtitleNumber(movieSlug, subtitleNumber);
    }

    @Override
    public List<MoviePhrase> findAll(final String movieSlug) {
        final List<MoviePhraseEntity> entities = (movieSlug == null || movieSlug.isBlank())
                ? repository.findAllByOrderBySubtitleNumberAsc()
                : repository.findByMovieSlugOrderBySubtitleNumberAsc(movieSlug);
        return entities.stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
