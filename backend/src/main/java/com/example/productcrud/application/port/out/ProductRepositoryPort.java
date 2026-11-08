package com.example.productcrud.application.port.out;

import com.example.productcrud.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port abstracting product persistence from the application core.
 */
public interface ProductRepositoryPort {

    /**
     * Persists (creates or updates) a product.
     *
     * @param product product to store
     * @return the stored product
     */
    Product save(Product product);

    /**
     * Finds a product by its identifier.
     *
     * @param id product identifier
     * @return the product if present
     */
    Optional<Product> findById(UUID id);

    /**
     * Returns every stored product.
     *
     * @return all products
     */
    List<Product> findAll();

    /**
     * Indicates whether a product exists.
     *
     * @param id product identifier
     * @return {@code true} if a product with the id exists
     */
    boolean existsById(UUID id);

    /**
     * Deletes a product by its identifier.
     *
     * @param id product identifier
     */
    void deleteById(UUID id);
}
