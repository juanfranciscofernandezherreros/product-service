package com.example.miscservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link SuggestionEntity}.
 */
public interface SuggestionJpaRepository extends JpaRepository<SuggestionEntity, Long> {

    Page<SuggestionEntity> findByEstado(String estado, Pageable pageable);

    long countByEstado(String estado);
}
