package com.example.contentservice.domain.exception;

/**
 * Raised when domain invariants are violated.
 */
public class InvalidContentException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the validation failure detail
     */
    public InvalidContentException(final String message) {
        super(message);
    }
}
