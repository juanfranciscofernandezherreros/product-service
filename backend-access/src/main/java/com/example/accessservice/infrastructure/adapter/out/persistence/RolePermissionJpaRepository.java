package com.example.accessservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link RolePermissionJpaEntity}.
 */
public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionJpaEntity, RolePermissionId> {

    /**
     * Finds links by role.
     *
     * @param rol role code
     * @return the links
     */
    List<RolePermissionJpaEntity> findByRol(String rol);
}
