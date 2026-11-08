package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link CollectionEntity}.
 */
public interface CollectionJpaRepository extends JpaRepository<CollectionEntity, Long> {

    boolean existsBySlugOrNombreArchivo(String slug, String nombreArchivo);
}
