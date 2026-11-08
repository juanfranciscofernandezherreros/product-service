package com.example.accessservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for user-role assignments.
 */
@Entity
@Table(name = "usuarios_roles")
@IdClass(UserRoleId.class)
public class UserRoleJpaEntity {

    @Id
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Id
    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "asignado_por")
    private UUID asignadoPor;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    /** Default constructor required by JPA. */
    protected UserRoleJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param usuarioId   user id
     * @param rol         role code
     * @param asignadoPor assigning user id
     * @param creadoEn    creation instant
     */
    public UserRoleJpaEntity(final UUID usuarioId, final String rol, final UUID asignadoPor,
                             final Instant creadoEn) {
        this.usuarioId = usuarioId;
        this.rol = rol;
        this.asignadoPor = asignadoPor;
        this.creadoEn = creadoEn;
    }

    /** @return the user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return the role code */
    public String getRol() {
        return rol;
    }

    /** @return the assigning user id */
    public UUID getAsignadoPor() {
        return asignadoPor;
    }

    /** @return the creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
