package com.example.miscservice.domain.model;

/**
 * Supported writing draft types.
 */
public enum WritingType {
    ESSAY("essay"),
    ARTICLE("article"),
    REVIEW("review"),
    EMAIL_INFORMAL("email_informal"),
    EMAIL_FORMAL("email_formal"),
    REPORT("report"),
    STORY("story");

    private final String value;

    WritingType(final String value) {
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
     * Resolves a {@link WritingType} from its string value.
     *
     * @param raw the database/string value
     * @return the matching type
     * @throws IllegalArgumentException if no type matches
     */
    public static WritingType fromValue(final String raw) {
        for (final WritingType type : values()) {
            if (type.value.equalsIgnoreCase(raw)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid writing type: " + raw);
    }
}
