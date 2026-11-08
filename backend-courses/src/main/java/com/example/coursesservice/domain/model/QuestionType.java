package com.example.coursesservice.domain.model;

/**
 * Supported activity question types.
 */
public enum QuestionType {
    SELECCION("seleccion"),
    TEXTO_CORTO("texto_corto"),
    HUECOS("huecos"),
    TRANSFORMACION("transformacion"),
    REDACCION("redaccion");

    private final String value;

    QuestionType(final String value) {
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
     * Resolves a {@link QuestionType} from its string value.
     *
     * @param raw the database/string value
     * @return the matching type
     * @throws IllegalArgumentException if no type matches
     */
    public static QuestionType fromValue(final String raw) {
        for (final QuestionType t : values()) {
            if (t.value.equalsIgnoreCase(raw)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid question type: " + raw);
    }
}
