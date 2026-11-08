package com.example.miscservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data repository for {@link LogbookNoteEntity}.
 */
public interface LogbookNoteJpaRepository extends JpaRepository<LogbookNoteEntity, UUID> {

    Page<LogbookNoteEntity> findByUsuarioId(UUID usuarioId, Pageable pageable);

    long countByUsuarioId(UUID usuarioId);
}
