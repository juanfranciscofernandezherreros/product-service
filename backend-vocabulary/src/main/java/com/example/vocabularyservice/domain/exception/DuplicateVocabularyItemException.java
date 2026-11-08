package com.example.vocabularyservice.domain.exception;

/**
 * Raised when creating an item whose English term already exists.
 */
public class DuplicateVocabularyItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a duplicated term.
     *
     * @param en the duplicated English term
     */
    public DuplicateVocabularyItemException(final String en) {
        super("A vocabulary item already exists with en: " + en);
    }
}
