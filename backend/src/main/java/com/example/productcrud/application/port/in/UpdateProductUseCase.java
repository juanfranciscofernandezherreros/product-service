package com.example.productcrud.application.port.in;

import com.example.productcrud.domain.model.Product;
import java.util.UUID;

/**
 * Inbound port for updating an existing product.
 */
public interface UpdateProductUseCase {

    /**
     * Updates an existing product.
     *
     * @param id      identifier of the product to update
     * @param command new product attributes
     * @return the updated product
     */
    Product update(UUID id, ProductCommand command);
}
