package com.example.productcrud.application.port.in;

import com.example.productcrud.domain.model.Product;
import java.util.UUID;

/**
 * Inbound port for retrieving a single product.
 */
public interface GetProductUseCase {

    /**
     * Retrieves a product by its identifier.
     *
     * @param id product identifier
     * @return the product
     */
    Product getById(UUID id);
}
