package com.example.accessservice.domain.exception;

/**
 * Raised when an access aggregate violates its invariants.
 */
public class InvalidAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message.
     *
     * @param message explanation
     */
    public InvalidAccessException(final String message) {
        super(message);
    }
}
