package com.example.accessservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * JPA entity for role-permission links.
 */
@Entity
@Table(name = "roles_permisos")
@IdClass(RolePermissionId.class)
public class RolePermissionJpaEntity {

    @Id
    @Column(name = "rol", nullable = false)
    private String rol;

    @Id
    @Column(name = "permiso", nullable = false)
    private String permiso;

    /** Default constructor required by JPA. */
    protected RolePermissionJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param rol     role code
     * @param permiso permission code
     */
    public RolePermissionJpaEntity(final String rol, final String permiso) {
        this.rol = rol;
        this.permiso = permiso;
    }

    /** @return the role code */
    public String getRol() {
        return rol;
    }

    /** @return the permission code */
    public String getPermiso() {
        return permiso;
    }
}
