package com.example.fundservice.domain.exception;

/**
 * Raised when a fund violates its domain invariants.
 */
public class InvalidFundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message explanation of the broken invariant
     */
    public InvalidFundException(final String message) {
        super(message);
    }
}
