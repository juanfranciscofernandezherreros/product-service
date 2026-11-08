package com.example.socialservice.domain.model;

import com.example.socialservice.domain.exception.InvalidSocialException;
import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Immutable domain representation of a public profile.
 */
public final class PublicProfile {

    private static final Pattern ALIAS = Pattern.compile("^[a-z0-9_]{3,24}$");
    private static final int MAX_BIO = 180;

    private final UUID userId;
    private final String alias;
    private final Instant creadoEn;
    private final String biografia;
    private final AvatarEmoji avatarEmoji;

    /**
     * Creates a validated profile.
     *
     * @param userId      user id
     * @param alias       alias (matches {@code ^[a-z0-9_]{3,24}$})
     * @param creadoEn    creation instant
     * @param biografia   biography (max 180 chars)
     * @param avatarEmoji avatar
     */
    public PublicProfile(final UUID userId, final String alias, final Instant creadoEn,
                         final String biografia, final AvatarEmoji avatarEmoji) {
        if (userId == null) {
            throw new InvalidSocialException("userId is required");
        }
        if (alias == null || !ALIAS.matcher(alias).matches()) {
            throw new InvalidSocialException("alias must match ^[a-z0-9_]{3,24}$");
        }
        final String bio = biografia == null ? "" : biografia;
        if (bio.length() > MAX_BIO) {
            throw new InvalidSocialException("biografia must be at most 180 characters");
        }
        this.userId = userId;
        this.alias = alias;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
        this.biografia = bio;
        this.avatarEmoji = avatarEmoji == null ? AvatarEmoji.BOOK : avatarEmoji;
    }

    /**
     * Factory for a new profile.
     *
     * @param userId      user id
     * @param alias       alias
     * @param biografia   biography
     * @param avatarEmoji avatar
     * @return a new profile
     */
    public static PublicProfile create(final UUID userId, final String alias, final String biografia,
                                       final AvatarEmoji avatarEmoji) {
        return new PublicProfile(userId, alias, Instant.now(), biografia, avatarEmoji);
    }

    /**
     * Returns a copy with updated bio/avatar, keeping id/alias/creation.
     *
     * @param newBio    new biography (keeps current when {@code null})
     * @param newAvatar new avatar (keeps current when {@code null})
     * @return updated profile
     */
    public PublicProfile withUpdate(final String newBio, final AvatarEmoji newAvatar) {
        return new PublicProfile(this.userId, this.alias, this.creadoEn,
                newBio == null ? this.biografia : newBio,
                newAvatar == null ? this.avatarEmoji : newAvatar);
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

    /** @return avatar */
    public AvatarEmoji getAvatarEmoji() {
        return avatarEmoji;
    }
}
