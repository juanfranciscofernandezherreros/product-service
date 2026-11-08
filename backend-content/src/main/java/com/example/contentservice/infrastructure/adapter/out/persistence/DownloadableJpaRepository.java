package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link DownloadableEntity}.
 */
public interface DownloadableJpaRepository extends JpaRepository<DownloadableEntity, Long> {

    boolean existsBySlug(String slug);

    List<DownloadableEntity> findAllByOrderByOrdenAsc();

    List<DownloadableEntity> findByCategoriaOrderByOrdenAsc(String categoria);
}
