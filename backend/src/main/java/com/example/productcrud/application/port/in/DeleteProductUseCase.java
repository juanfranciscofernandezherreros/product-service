package com.example.productcrud.application.port.in;

import java.util.UUID;

/**
 * Inbound port for deleting a product.
 */
public interface DeleteProductUseCase {

    /**
     * Deletes a product by its identifier.
     *
     * @param id product identifier
     */
    void delete(UUID id);
}
