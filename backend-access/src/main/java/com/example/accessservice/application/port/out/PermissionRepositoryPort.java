package com.example.accessservice.application.port.out;

import com.example.accessservice.domain.model.Permission;
import java.util.List;

/**
 * Outbound port for permission persistence.
 */
public interface PermissionRepositoryPort {

    /**
     * Persists a permission.
     *
     * @param permission permission
     * @return stored permission
     */
    Permission save(Permission permission);

    /**
     * Indicates whether a permission exists.
     *
     * @param codigo permission code
     * @return {@code true} if present
     */
    boolean existsByCodigo(String codigo);

    /**
     * Returns all permissions.
     *
     * @return the permissions
     */
    List<Permission> findAll();
}
