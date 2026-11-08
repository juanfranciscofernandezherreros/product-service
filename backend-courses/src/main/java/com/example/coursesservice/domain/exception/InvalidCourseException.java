package com.example.coursesservice.domain.exception;

/**
 * Raised when domain invariants are violated.
 */
public class InvalidCourseException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the validation failure detail
     */
    public InvalidCourseException(final String message) {
        super(message);
    }
}
