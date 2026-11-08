package com.example.accessservice.application.port.in;

import com.example.accessservice.domain.model.Permission;
import com.example.accessservice.domain.model.Role;
import com.example.accessservice.domain.model.UserRole;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the access use cases.
 */
public interface AccessUseCases {

    /**
     * Creates a role.
     *
     * @param role role
     * @return created role
     */
    Role createRole(Role role);

    /**
     * Gets a role by code.
     *
     * @param codigo role code
     * @return the role
     */
    Role getRole(String codigo);

    /**
     * Lists roles.
     *
     * @return the roles
     */
    List<Role> listRoles();

    /**
     * Deletes a role.
     *
     * @param codigo role code
     */
    void deleteRole(String codigo);

    /**
     * Creates a permission.
     *
     * @param permission permission
     * @return created permission
     */
    Permission createPermission(Permission permission);

    /**
     * Lists permissions.
     *
     * @return the permissions
     */
    List<Permission> listPermissions();

    /**
     * Assigns a permission to a role.
     *
     * @param rol     role code
     * @param permiso permission code
     */
    void addRolePermission(String rol, String permiso);

    /**
     * Lists the permissions of a role.
     *
     * @param rol role code
     * @return the permission codes
     */
    List<String> listRolePermissions(String rol);

    /**
     * Assigns a role to a user.
     *
     * @param usuarioId   user id
     * @param rol         role code
     * @param asignadoPor assigning user id (may be {@code null})
     * @return the assignment
     */
    UserRole assignUserRole(UUID usuarioId, String rol, UUID asignadoPor);

    /**
     * Lists the roles of a user.
     *
     * @param usuarioId user id
     * @return the assignments
     */
    List<UserRole> listUserRoles(UUID usuarioId);
}
