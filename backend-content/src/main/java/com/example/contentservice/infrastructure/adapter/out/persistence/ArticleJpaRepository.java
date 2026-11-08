package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Spring Data repository for {@link ArticleEntity}.
 */
public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long>,
        JpaSpecificationExecutor<ArticleEntity> {

    Optional<ArticleEntity> findBySlug(String slug);

    boolean existsByRutaOrSlug(String ruta, String slug);

    void deleteBySlug(String slug);
}
