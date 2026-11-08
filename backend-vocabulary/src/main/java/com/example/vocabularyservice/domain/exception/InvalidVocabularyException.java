package com.example.vocabularyservice.domain.exception;

/**
 * Raised when a vocabulary aggregate violates its invariants.
 */
public class InvalidVocabularyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message explanation of the broken invariant
     */
    public InvalidVocabularyException(final String message) {
        super(message);
    }
}
