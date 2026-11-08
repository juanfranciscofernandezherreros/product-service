package com.example.miscservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data repository for {@link WritingDraftEntity}.
 */
public interface WritingDraftJpaRepository extends JpaRepository<WritingDraftEntity, UUID> {

    Page<WritingDraftEntity> findByUsuarioId(UUID usuarioId, Pageable pageable);

    long countByUsuarioId(UUID usuarioId);
}
