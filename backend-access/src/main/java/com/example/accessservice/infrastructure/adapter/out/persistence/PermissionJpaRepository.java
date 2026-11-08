package com.example.accessservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link PermissionJpaEntity}.
 */
public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {
}
