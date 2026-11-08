package com.example.vocabularyservice.domain.exception;

/**
 * Raised when a vocabulary item cannot be located.
 */
public class VocabularyItemNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a missing id.
     *
     * @param id the item id
     */
    public VocabularyItemNotFoundException(final long id) {
        super("Vocabulary item not found with id: " + id);
    }
}
