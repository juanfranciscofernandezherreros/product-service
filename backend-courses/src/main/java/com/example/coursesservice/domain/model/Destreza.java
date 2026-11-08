package com.example.coursesservice.domain.model;

/**
 * Supported activity skills.
 */
public enum Destreza {
    READING("reading"),
    USE_OF_ENGLISH("use_of_english"),
    WRITING("writing"),
    LISTENING("listening"),
    SPEAKING("speaking"),
    VOCABULARY("vocabulary"),
    GRAMMAR("grammar");

    private final String value;

    Destreza(final String value) {
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
     * Resolves a {@link Destreza} from its string value.
     *
     * @param raw the database/string value
     * @return the matching skill
     * @throws IllegalArgumentException if no skill matches
     */
    public static Destreza fromValue(final String raw) {
        for (final Destreza d : values()) {
            if (d.value.equalsIgnoreCase(raw)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid destreza: " + raw);
    }
}
