package com.example.socialservice.domain.exception;

/**
 * Raised when a resource cannot be located.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param type resource type
     * @param id   identifier
     */
    public ResourceNotFoundException(final String type, final String id) {
        super(type + " not found: " + id);
    }
}
