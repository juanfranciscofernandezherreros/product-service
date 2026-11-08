package com.example.socialservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for follow relationships. */
@Entity
@Table(name = "seguimientos")
@IdClass(FollowId.class)
public class FollowEntity {

    @Id
    @Column(name = "seguidor", nullable = false)
    private UUID seguidor;

    @Id
    @Column(name = "seguido", nullable = false)
    private UUID seguido;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    /** Default constructor required by JPA. */
    protected FollowEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param seguidor follower
     * @param seguido  followed
     * @param creadoEn creation instant
     */
    public FollowEntity(final UUID seguidor, final UUID seguido, final Instant creadoEn) {
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.creadoEn = creadoEn;
    }

    /** @return follower */
    public UUID getSeguidor() {
        return seguidor;
    }

    /** @return followed */
    public UUID getSeguido() {
        return seguido;
    }

    /** @return creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
