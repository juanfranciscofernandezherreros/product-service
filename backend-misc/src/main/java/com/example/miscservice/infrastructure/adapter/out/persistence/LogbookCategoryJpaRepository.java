package com.example.miscservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for {@link LogbookCategoryEntity}.
 */
public interface LogbookCategoryJpaRepository extends JpaRepository<LogbookCategoryEntity, UUID> {

    Page<LogbookCategoryEntity> findByUsuarioId(UUID usuarioId, Pageable pageable);

    long countByUsuarioId(UUID usuarioId);

    Optional<LogbookCategoryEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
