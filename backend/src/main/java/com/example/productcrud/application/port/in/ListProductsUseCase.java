package com.example.productcrud.application.port.in;

import com.example.productcrud.domain.model.Product;
import java.util.List;

/**
 * Inbound port for listing all products.
 */
public interface ListProductsUseCase {

    /**
     * Returns every product.
     *
     * @return all products
     */
    List<Product> listAll();
}
