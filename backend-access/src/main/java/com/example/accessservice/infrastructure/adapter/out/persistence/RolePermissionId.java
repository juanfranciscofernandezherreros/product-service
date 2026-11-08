package com.example.accessservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite id for {@link RolePermissionJpaEntity}.
 */
public class RolePermissionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rol;
    private String permiso;

    /** Default constructor required by JPA. */
    public RolePermissionId() {
    }

    /**
     * Creates the id.
     *
     * @param rol     role code
     * @param permiso permission code
     */
    public RolePermissionId(final String rol, final String permiso) {
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

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RolePermissionId)) {
            return false;
        }
        final RolePermissionId that = (RolePermissionId) other;
        return Objects.equals(rol, that.rol) && Objects.equals(permiso, that.permiso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rol, permiso);
    }
}
