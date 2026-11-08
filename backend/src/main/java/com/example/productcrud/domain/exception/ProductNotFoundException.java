package com.example.productcrud.domain.exception;

import java.util.UUID;

/**
 * Raised when a product cannot be located by its identifier.
 */
public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a missing product id.
     *
     * @param id identifier that was not found
     */
    public ProductNotFoundException(final UUID id) {
        super("Product not found with id: " + id);
    }
}
