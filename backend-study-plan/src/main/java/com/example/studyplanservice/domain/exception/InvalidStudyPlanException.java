package com.example.studyplanservice.domain.exception;

/**
 * Raised when a study-plan aggregate violates its invariants.
 */
public class InvalidStudyPlanException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message explanation
     */
    public InvalidStudyPlanException(final String message) {
        super(message);
    }
}
