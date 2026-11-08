package com.example.socialservice.domain.exception;

/**
 * Raised when a social aggregate violates its invariants.
 */
public class InvalidSocialException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message explanation
     */
    public InvalidSocialException(final String message) {
        super(message);
    }
}
