package com.example.accessservice.infrastructure.config;

import com.example.accessservice.application.port.in.AccessUseCases;
import com.example.accessservice.application.port.out.PermissionRepositoryPort;
import com.example.accessservice.application.port.out.RolePermissionRepositoryPort;
import com.example.accessservice.application.port.out.RoleRepositoryPort;
import com.example.accessservice.application.port.out.UserRoleRepositoryPort;
import com.example.accessservice.application.usecase.AccessService;
import com.example.accessservice.infrastructure.adapter.out.persistence.AccessPersistenceAdapters.PermissionPersistenceAdapter;
import com.example.accessservice.infrastructure.adapter.out.persistence.AccessPersistenceAdapters.RolePermissionPersistenceAdapter;
import com.example.accessservice.infrastructure.adapter.out.persistence.AccessPersistenceAdapters.RolePersistenceAdapter;
import com.example.accessservice.infrastructure.adapter.out.persistence.AccessPersistenceAdapters.UserRolePersistenceAdapter;
import com.example.accessservice.infrastructure.adapter.out.persistence.PermissionJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.RoleJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.RolePermissionJpaRepository;
import com.example.accessservice.infrastructure.adapter.out.persistence.UserRoleJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Role port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public RoleRepositoryPort roleRepositoryPort(final RoleJpaRepository repository) {
        return new RolePersistenceAdapter(repository);
    }

    /**
     * Permission port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public PermissionRepositoryPort permissionRepositoryPort(final PermissionJpaRepository repository) {
        return new PermissionPersistenceAdapter(repository);
    }

    /**
     * Role-permission port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public RolePermissionRepositoryPort rolePermissionRepositoryPort(final RolePermissionJpaRepository repository) {
        return new RolePermissionPersistenceAdapter(repository);
    }

    /**
     * User-role port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public UserRoleRepositoryPort userRoleRepositoryPort(final UserRoleJpaRepository repository) {
        return new UserRolePersistenceAdapter(repository);
    }

    /**
     * Access use cases.
     *
     * @param rolePort           role port
     * @param permissionPort     permission port
     * @param rolePermissionPort role-permission port
     * @param userRolePort       user-role port
     * @return the use cases
     */
    @Bean
    public AccessUseCases accessUseCases(final RoleRepositoryPort rolePort,
                                         final PermissionRepositoryPort permissionPort,
                                         final RolePermissionRepositoryPort rolePermissionPort,
                                         final UserRoleRepositoryPort userRolePort) {
        return new AccessService(rolePort, permissionPort, rolePermissionPort, userRolePort);
    }
}
