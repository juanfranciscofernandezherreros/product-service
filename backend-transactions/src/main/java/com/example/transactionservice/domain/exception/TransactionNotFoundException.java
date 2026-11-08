package com.example.transactionservice.domain.exception;

import java.util.UUID;

/**
 * Raised when a transaction cannot be located by its identifier.
 */
public class TransactionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a missing transaction id.
     *
     * @param id identifier that was not found
     */
    public TransactionNotFoundException(final UUID id) {
        super("Transaction not found with id: " + id);
    }
}
