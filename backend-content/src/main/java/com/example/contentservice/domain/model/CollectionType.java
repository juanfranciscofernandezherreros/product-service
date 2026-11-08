package com.example.contentservice.domain.model;

/**
 * Supported material collection types.
 */
public enum CollectionType {
    ARTICULOS("articulos"),
    VOCABULARIO("vocabulario");

    private final String value;

    CollectionType(final String value) {
        this.value = value;
    }

    /**
     * Returns the persisted string value.
     *
     * @return the database value
     */
    public String value() {
        return value;
    }

    /**
     * Resolves a {@link CollectionType} from its string value.
     *
     * @param raw the database/string value
     * @return the matching type
     * @throws IllegalArgumentException if no type matches
     */
    public static CollectionType fromValue(final String raw) {
        for (final CollectionType t : values()) {
            if (t.value.equalsIgnoreCase(raw)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid collection type: " + raw);
    }
}
