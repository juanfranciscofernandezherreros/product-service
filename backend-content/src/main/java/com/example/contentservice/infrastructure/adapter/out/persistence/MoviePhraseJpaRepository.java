package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link MoviePhraseEntity}.
 */
public interface MoviePhraseJpaRepository extends JpaRepository<MoviePhraseEntity, Long> {

    boolean existsByMovieSlugAndSubtitleNumber(String movieSlug, int subtitleNumber);

    List<MoviePhraseEntity> findAllByOrderBySubtitleNumberAsc();

    List<MoviePhraseEntity> findByMovieSlugOrderBySubtitleNumberAsc(String movieSlug);
}
