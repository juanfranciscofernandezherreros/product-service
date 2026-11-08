package com.example.miscservice.domain.model;

/**
 * Lifecycle states of a suggestion.
 */
public enum SuggestionStatus {
    PENDIENTE("pendiente"),
    PUBLICADA("publicada"),
    RECHAZADA("rechazada");

    private final String value;

    SuggestionStatus(final String value) {
        this.value = value;
    }

    /**
     * Returns the persisted string value.
     *
     * @return the lowercase database value
     */
    public String value() {
        return value;
    }

    /**
     * Resolves a {@link SuggestionStatus} from its string value.
     *
     * @param raw the database/string value
     * @return the matching status
     * @throws IllegalArgumentException if no status matches
     */
    public static SuggestionStatus fromValue(final String raw) {
        for (final SuggestionStatus status : values()) {
            if (status.value.equalsIgnoreCase(raw)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid suggestion status: " + raw);
    }
}
