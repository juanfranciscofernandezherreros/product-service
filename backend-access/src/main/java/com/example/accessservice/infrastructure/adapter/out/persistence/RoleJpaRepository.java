package com.example.accessservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link RoleJpaEntity}.
 */
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {
}
