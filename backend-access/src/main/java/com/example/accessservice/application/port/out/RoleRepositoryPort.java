package com.example.accessservice.application.port.out;

import com.example.accessservice.domain.model.Role;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for role persistence.
 */
public interface RoleRepositoryPort {

    /**
     * Persists a role.
     *
     * @param role role
     * @return stored role
     */
    Role save(Role role);

    /**
     * Finds a role by code.
     *
     * @param codigo role code
     * @return the role if present
     */
    Optional<Role> findByCodigo(String codigo);

    /**
     * Indicates whether a role exists.
     *
     * @param codigo role code
     * @return {@code true} if present
     */
    boolean existsByCodigo(String codigo);

    /**
     * Returns all roles.
     *
     * @return the roles
     */
    List<Role> findAll();

    /**
     * Deletes a role by code.
     *
     * @param codigo role code
     */
    void deleteByCodigo(String codigo);
}
