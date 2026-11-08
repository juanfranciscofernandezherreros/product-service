package com.example.productcrud.infrastructure.adapter.out.persistence;

import com.example.productcrud.domain.model.Product;

/**
 * Explicit mapper between the domain {@link Product} and its JPA entity.
 */
public final class ProductPersistenceMapper {

    private ProductPersistenceMapper() {
    }

    /**
     * Converts a domain product into a JPA entity.
     *
     * @param product domain product
     * @return the JPA entity
     */
    public static ProductJpaEntity toEntity(final Product product) {
        return new ProductJpaEntity(product.getId(), product.getSku(), product.getName(), product.getPrice());
    }

    /**
     * Converts a JPA entity into a domain product.
     *
     * @param entity JPA entity
     * @return the domain product
     */
    public static Product toDomain(final ProductJpaEntity entity) {
        return new Product(entity.getId(), entity.getSku(), entity.getName(), entity.getPrice());
    }
}
