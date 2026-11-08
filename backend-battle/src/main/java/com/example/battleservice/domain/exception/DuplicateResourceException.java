package com.example.battleservice.domain.exception;

/**
 * Raised when creating a resource that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message explanation
     */
    public DuplicateResourceException(final String message) {
        super(message);
    }
}
