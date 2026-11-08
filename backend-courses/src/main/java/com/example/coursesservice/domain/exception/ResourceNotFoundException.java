package com.example.coursesservice.domain.exception;

/**
 * Raised when a requested resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the not-found detail
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
