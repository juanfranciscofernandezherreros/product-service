package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite identifier for {@link VocabularyProgressJpaEntity}.
 */
public class ProgressId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    private Long itemId;

    /**
     * Default constructor required by JPA.
     */
    public ProgressId() {
    }

    /**
     * Creates the composite id.
     *
     * @param userId user id
     * @param itemId item id
     */
    public ProgressId(final UUID userId, final Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    /** @return the user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return the item id */
    public Long getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProgressId)) {
            return false;
        }
        final ProgressId that = (ProgressId) other;
        return Objects.equals(userId, that.userId) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, itemId);
    }
}
