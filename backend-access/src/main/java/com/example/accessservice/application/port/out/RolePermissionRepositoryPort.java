package com.example.accessservice.application.port.out;

import java.util.List;

/**
 * Outbound port for role-permission links.
 */
public interface RolePermissionRepositoryPort {

    /**
     * Links a permission to a role.
     *
     * @param rol     role code
     * @param permiso permission code
     */
    void add(String rol, String permiso);

    /**
     * Returns the permission codes assigned to a role.
     *
     * @param rol role code
     * @return the permission codes
     */
    List<String> findPermissionsByRole(String rol);
}
