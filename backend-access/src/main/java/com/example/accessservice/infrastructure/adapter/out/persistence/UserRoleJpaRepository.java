package com.example.accessservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link UserRoleJpaEntity}.
 */
public interface UserRoleJpaRepository extends JpaRepository<UserRoleJpaEntity, UserRoleId> {

    /**
     * Finds assignments by user.
     *
     * @param usuarioId user id
     * @return the assignments
     */
    List<UserRoleJpaEntity> findByUsuarioId(UUID usuarioId);
}
