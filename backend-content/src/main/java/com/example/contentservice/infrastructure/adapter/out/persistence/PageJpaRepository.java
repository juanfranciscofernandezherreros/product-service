package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link PageEntity}.
 */
public interface PageJpaRepository extends JpaRepository<PageEntity, String> {
}
