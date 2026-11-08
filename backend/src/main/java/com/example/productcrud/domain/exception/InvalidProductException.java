package com.example.productcrud.domain.exception;

/**
 * Raised when a product violates its domain invariants.
 */
public class InvalidProductException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message explanation of the broken invariant
     */
    public InvalidProductException(final String message) {
        super(message);
    }
}
