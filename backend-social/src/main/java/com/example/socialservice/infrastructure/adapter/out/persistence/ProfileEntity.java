package com.example.socialservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for public profiles. */
@Entity
@Table(name = "perfiles_publicos")
public class ProfileEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "alias", nullable = false, length = 24)
    private String alias;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "biografia", nullable = false, length = 180)
    private String biografia;

    @Column(name = "avatar_emoji", nullable = false, length = 8)
    private String avatarEmoji;

    /** Default constructor required by JPA. */
    protected ProfileEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param userId      user id
     * @param alias       alias
     * @param creadoEn    creation instant
     * @param biografia   biography
     * @param avatarEmoji avatar name
     */
    public ProfileEntity(final UUID userId, final String alias, final Instant creadoEn,
                         final String biografia, final String avatarEmoji) {
        this.userId = userId;
        this.alias = alias;
        this.creadoEn = creadoEn;
        this.biografia = biografia;
        this.avatarEmoji = avatarEmoji;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return alias */
    public String getAlias() {
        return alias;
    }

    /** @return creation instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }

    /** @return biography */
    public String getBiografia() {
        return biografia;
    }

    /** @return avatar name */
    public String getAvatarEmoji() {
        return avatarEmoji;
    }
}
