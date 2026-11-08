package com.example.miscservice.domain.exception;

/**
 * Raised when domain invariants are violated.
 */
public class InvalidMiscException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the validation failure detail
     */
    public InvalidMiscException(final String message) {
        super(message);
    }
}
