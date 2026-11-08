package com.example.transactionservice.domain.exception;

/**
 * Raised when a transaction violates its domain invariants.
 */
public class InvalidTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message explanation of the broken invariant
     */
    public InvalidTransactionException(final String message) {
        super(message);
    }
}
