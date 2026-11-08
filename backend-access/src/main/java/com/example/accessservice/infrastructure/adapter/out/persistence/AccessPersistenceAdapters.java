package com.example.accessservice.infrastructure.adapter.out.persistence;

import com.example.accessservice.application.port.out.PermissionRepositoryPort;
import com.example.accessservice.application.port.out.RolePermissionRepositoryPort;
import com.example.accessservice.application.port.out.RoleRepositoryPort;
import com.example.accessservice.application.port.out.UserRoleRepositoryPort;
import com.example.accessservice.domain.model.Permission;
import com.example.accessservice.domain.model.Role;
import com.example.accessservice.domain.model.UserRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Outbound persistence adapters for the access domain.
 */
public final class AccessPersistenceAdapters {

    private AccessPersistenceAdapters() {
    }

    /**
     * Role persistence adapter.
     */
    public static final class RolePersistenceAdapter implements RoleRepositoryPort {

        private final RoleJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository Spring Data repository
         */
        public RolePersistenceAdapter(final RoleJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Role save(final Role role) {
            final RoleJpaEntity saved = repository.save(new RoleJpaEntity(role.getCodigo(), role.getDescripcion()));
            return new Role(saved.getCodigo(), saved.getDescripcion());
        }

        @Override
        public Optional<Role> findByCodigo(final String codigo) {
            return repository.findById(codigo).map(e -> new Role(e.getCodigo(), e.getDescripcion()));
        }

        @Override
        public boolean existsByCodigo(final String codigo) {
            return repository.existsById(codigo);
        }

        @Override
        public List<Role> findAll() {
            return repository.findAll().stream()
                    .map(e -> new Role(e.getCodigo(), e.getDescripcion()))
                    .collect(Collectors.toList());
        }

        @Override
        public void deleteByCodigo(final String codigo) {
            repository.deleteById(codigo);
        }
    }

    /**
     * Permission persistence adapter.
     */
    public static final class PermissionPersistenceAdapter implements PermissionRepositoryPort {

        private final PermissionJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository Spring Data repository
         */
        public PermissionPersistenceAdapter(final PermissionJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Permission save(final Permission permission) {
            final PermissionJpaEntity saved = repository.save(
                    new PermissionJpaEntity(permission.getCodigo(), permission.getDescripcion()));
            return new Permission(saved.getCodigo(), saved.getDescripcion());
        }

        @Override
        public boolean existsByCodigo(final String codigo) {
            return repository.existsById(codigo);
        }

        @Override
        public List<Permission> findAll() {
            return repository.findAll().stream()
                    .map(e -> new Permission(e.getCodigo(), e.getDescripcion()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Role-permission link adapter.
     */
    public static final class RolePermissionPersistenceAdapter implements RolePermissionRepositoryPort {

        private final RolePermissionJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository Spring Data repository
         */
        public RolePermissionPersistenceAdapter(final RolePermissionJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public void add(final String rol, final String permiso) {
            repository.save(new RolePermissionJpaEntity(rol, permiso));
        }

        @Override
        public List<String> findPermissionsByRole(final String rol) {
            return repository.findByRol(rol).stream()
                    .map(RolePermissionJpaEntity::getPermiso)
                    .collect(Collectors.toList());
        }
    }

    /**
     * User-role assignment adapter.
     */
    public static final class UserRolePersistenceAdapter implements UserRoleRepositoryPort {

        private final UserRoleJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository Spring Data repository
         */
        public UserRolePersistenceAdapter(final UserRoleJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public UserRole save(final UserRole userRole) {
            final UserRoleJpaEntity saved = repository.save(new UserRoleJpaEntity(
                    userRole.getUsuarioId(), userRole.getRol(), userRole.getAsignadoPor(), userRole.getCreadoEn()));
            return new UserRole(saved.getUsuarioId(), saved.getRol(), saved.getAsignadoPor(), saved.getCreadoEn());
        }

        @Override
        public List<UserRole> findByUser(final UUID usuarioId) {
            return repository.findByUsuarioId(usuarioId).stream()
                    .map(e -> new UserRole(e.getUsuarioId(), e.getRol(), e.getAsignadoPor(), e.getCreadoEn()))
                    .collect(Collectors.toList());
        }
    }
}
