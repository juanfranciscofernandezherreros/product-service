package com.example.contentservice.domain.exception;

/**
 * Raised when a resource violates a uniqueness constraint.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the conflict detail
     */
    public DuplicateResourceException(final String message) {
        super(message);
    }
}
