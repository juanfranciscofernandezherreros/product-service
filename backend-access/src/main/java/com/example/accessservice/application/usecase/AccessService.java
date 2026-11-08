package com.example.accessservice.application.usecase;

import com.example.accessservice.application.port.in.AccessUseCases;
import com.example.accessservice.application.port.out.PermissionRepositoryPort;
import com.example.accessservice.application.port.out.RolePermissionRepositoryPort;
import com.example.accessservice.application.port.out.RoleRepositoryPort;
import com.example.accessservice.application.port.out.UserRoleRepositoryPort;
import com.example.accessservice.domain.exception.ResourceAlreadyExistsException;
import com.example.accessservice.domain.exception.ResourceNotFoundException;
import com.example.accessservice.domain.model.Permission;
import com.example.accessservice.domain.model.Role;
import com.example.accessservice.domain.model.UserRole;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the access use cases.
 */
public class AccessService implements AccessUseCases {

    private final RoleRepositoryPort roleRepository;
    private final PermissionRepositoryPort permissionRepository;
    private final RolePermissionRepositoryPort rolePermissionRepository;
    private final UserRoleRepositoryPort userRoleRepository;

    /**
     * Creates the service.
     *
     * @param roleRepository           role port
     * @param permissionRepository     permission port
     * @param rolePermissionRepository role-permission port
     * @param userRoleRepository       user-role port
     */
    public AccessService(final RoleRepositoryPort roleRepository,
                         final PermissionRepositoryPort permissionRepository,
                         final RolePermissionRepositoryPort rolePermissionRepository,
                         final UserRoleRepositoryPort userRoleRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Role createRole(final Role role) {
        if (roleRepository.existsByCodigo(role.getCodigo())) {
            throw new ResourceAlreadyExistsException("Role", role.getCodigo());
        }
        return roleRepository.save(role);
    }

    @Override
    public Role getRole(final String codigo) {
        return roleRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Role", codigo));
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(final String codigo) {
        if (!roleRepository.existsByCodigo(codigo)) {
            throw new ResourceNotFoundException("Role", codigo);
        }
        roleRepository.deleteByCodigo(codigo);
    }

    @Override
    public Permission createPermission(final Permission permission) {
        if (permissionRepository.existsByCodigo(permission.getCodigo())) {
            throw new ResourceAlreadyExistsException("Permission", permission.getCodigo());
        }
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> listPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public void addRolePermission(final String rol, final String permiso) {
        if (!roleRepository.existsByCodigo(rol)) {
            throw new ResourceNotFoundException("Role", rol);
        }
        if (!permissionRepository.existsByCodigo(permiso)) {
            throw new ResourceNotFoundException("Permission", permiso);
        }
        rolePermissionRepository.add(rol, permiso);
    }

    @Override
    public List<String> listRolePermissions(final String rol) {
        if (!roleRepository.existsByCodigo(rol)) {
            throw new ResourceNotFoundException("Role", rol);
        }
        return rolePermissionRepository.findPermissionsByRole(rol);
    }

    @Override
    public UserRole assignUserRole(final UUID usuarioId, final String rol, final UUID asignadoPor) {
        if (!roleRepository.existsByCodigo(rol)) {
            throw new ResourceNotFoundException("Role", rol);
        }
        return userRoleRepository.save(new UserRole(usuarioId, rol, asignadoPor, null));
    }

    @Override
    public List<UserRole> listUserRoles(final UUID usuarioId) {
        return userRoleRepository.findByUser(usuarioId);
    }
}
