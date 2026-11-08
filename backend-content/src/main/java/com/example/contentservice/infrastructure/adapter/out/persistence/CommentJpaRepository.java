package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link CommentEntity}.
 */
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByArticuloSlugOrderByCreadoEnAsc(String articuloSlug);
}
