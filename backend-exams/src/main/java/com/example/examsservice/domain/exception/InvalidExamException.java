package com.example.examsservice.domain.exception;

/**
 * Raised when an exams aggregate violates its invariants.
 */
public class InvalidExamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message explanation
     */
    public InvalidExamException(final String message) {
        super(message);
    }
}
