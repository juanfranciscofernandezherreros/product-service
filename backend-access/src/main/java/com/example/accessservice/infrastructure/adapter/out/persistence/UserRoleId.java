package com.example.accessservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite id for {@link UserRoleJpaEntity}.
 */
public class UserRoleId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID usuarioId;
    private String rol;

    /** Default constructor required by JPA. */
    public UserRoleId() {
    }

    /**
     * Creates the id.
     *
     * @param usuarioId user id
     * @param rol       role code
     */
    public UserRoleId(final UUID usuarioId, final String rol) {
        this.usuarioId = usuarioId;
        this.rol = rol;
    }

    /** @return the user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return the role code */
    public String getRol() {
        return rol;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserRoleId)) {
            return false;
        }
        final UserRoleId that = (UserRoleId) other;
        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(rol, that.rol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, rol);
    }
}
