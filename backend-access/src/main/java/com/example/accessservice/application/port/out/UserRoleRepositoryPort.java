package com.example.accessservice.application.port.out;

import com.example.accessservice.domain.model.UserRole;
import java.util.List;
import java.util.UUID;

/**
 * Outbound port for user-role assignments.
 */
public interface UserRoleRepositoryPort {

    /**
     * Persists an assignment.
     *
     * @param userRole assignment
     * @return stored assignment
     */
    UserRole save(UserRole userRole);

    /**
     * Returns the roles assigned to a user.
     *
     * @param usuarioId user id
     * @return the assignments
     */
    List<UserRole> findByUser(UUID usuarioId);
}
