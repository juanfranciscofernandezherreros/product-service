package com.example.accessservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link AccessService}.
 */
@ExtendWith(MockitoExtension.class)
class AccessServiceTest {

    @Mock
    private RoleRepositoryPort roleRepository;
    @Mock
    private PermissionRepositoryPort permissionRepository;
    @Mock
    private RolePermissionRepositoryPort rolePermissionRepository;
    @Mock
    private UserRoleRepositoryPort userRoleRepository;

    private AccessService service;

    @BeforeEach
    void setUp() {
        service = new AccessService(roleRepository, permissionRepository,
                rolePermissionRepository, userRoleRepository);
    }

    @Test
    void createRoleOk() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.createRole(new Role("ADMIN", "Administrador")).getCodigo()).isEqualTo("ADMIN");
    }

    @Test
    void createRoleDuplicate() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        assertThatThrownBy(() -> service.createRole(new Role("ADMIN", "d")))
                .isInstanceOf(ResourceAlreadyExistsException.class);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getRoleOkAndMissing() {
        when(roleRepository.findByCodigo("ADMIN")).thenReturn(Optional.of(new Role("ADMIN", "d")));
        assertThat(service.getRole("ADMIN").getCodigo()).isEqualTo("ADMIN");
        when(roleRepository.findByCodigo("X")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getRole("X")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listRolesDelegates() {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("ADMIN", "d")));
        assertThat(service.listRoles()).hasSize(1);
    }

    @Test
    void deleteRoleOkAndMissing() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        service.deleteRole("ADMIN");
        verify(roleRepository).deleteByCodigo("ADMIN");
        when(roleRepository.existsByCodigo("X")).thenReturn(false);
        assertThatThrownBy(() -> service.deleteRole("X")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createPermissionOkAndDuplicate() {
        when(permissionRepository.existsByCodigo("READ")).thenReturn(false);
        when(permissionRepository.save(any(Permission.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.createPermission(new Permission("READ", "d")).getCodigo()).isEqualTo("READ");
        when(permissionRepository.existsByCodigo("READ")).thenReturn(true);
        assertThatThrownBy(() -> service.createPermission(new Permission("READ", "d")))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void listPermissionsDelegates() {
        when(permissionRepository.findAll()).thenReturn(List.of(new Permission("READ", "d")));
        assertThat(service.listPermissions()).hasSize(1);
    }

    @Test
    void addRolePermissionOk() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        when(permissionRepository.existsByCodigo("READ")).thenReturn(true);
        service.addRolePermission("ADMIN", "READ");
        verify(rolePermissionRepository).add("ADMIN", "READ");
    }

    @Test
    void addRolePermissionRoleMissing() {
        when(roleRepository.existsByCodigo("X")).thenReturn(false);
        assertThatThrownBy(() -> service.addRolePermission("X", "READ"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addRolePermissionPermissionMissing() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        when(permissionRepository.existsByCodigo("X")).thenReturn(false);
        assertThatThrownBy(() -> service.addRolePermission("ADMIN", "X"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listRolePermissionsOkAndRoleMissing() {
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        when(rolePermissionRepository.findPermissionsByRole("ADMIN")).thenReturn(List.of("READ"));
        assertThat(service.listRolePermissions("ADMIN")).containsExactly("READ");
        when(roleRepository.existsByCodigo("X")).thenReturn(false);
        assertThatThrownBy(() -> service.listRolePermissions("X"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void assignUserRoleOkAndRoleMissing() {
        final UUID user = UUID.randomUUID();
        when(roleRepository.existsByCodigo("ADMIN")).thenReturn(true);
        when(userRoleRepository.save(any(UserRole.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.assignUserRole(user, "ADMIN", null).getRol()).isEqualTo("ADMIN");
        when(roleRepository.existsByCodigo("X")).thenReturn(false);
        assertThatThrownBy(() -> service.assignUserRole(user, "X", null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listUserRolesDelegates() {
        final UUID user = UUID.randomUUID();
        when(userRoleRepository.findByUser(user)).thenReturn(List.of(new UserRole(user, "ADMIN", null, null)));
        assertThat(service.listUserRoles(user)).hasSize(1);
    }
}
