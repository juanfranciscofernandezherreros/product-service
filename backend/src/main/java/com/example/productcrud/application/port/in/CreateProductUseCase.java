package com.example.productcrud.application.port.in;

import com.example.productcrud.domain.model.Product;

/**
 * Inbound port for creating a product.
 */
public interface CreateProductUseCase {

    /**
     * Creates a new product.
     *
     * @param command product attributes
     * @return the created product
     */
    Product create(ProductCommand command);
}
