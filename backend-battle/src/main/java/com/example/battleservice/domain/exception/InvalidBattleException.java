package com.example.battleservice.domain.exception;

/**
 * Raised when a battle aggregate violates its invariants.
 */
public class InvalidBattleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message explanation
     */
    public InvalidBattleException(final String message) {
        super(message);
    }
}
