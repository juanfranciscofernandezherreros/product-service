package com.example.fundservice.domain.exception;

/**
 * Raised when an order violates its domain invariants.
 */
public class InvalidOrderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message explanation of the broken invariant
     */
    public InvalidOrderException(final String message) {
        super(message);
    }
}
