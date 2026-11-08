package com.example.accessservice.domain.exception;

/**
 * Raised when creating a resource that already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param type resource type
     * @param id   resource identifier
     */
    public ResourceAlreadyExistsException(final String type, final String id) {
        super(type + " already exists: " + id);
    }
}
