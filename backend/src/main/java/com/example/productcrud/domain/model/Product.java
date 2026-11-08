package com.example.productcrud.domain.model;

import com.example.productcrud.domain.exception.InvalidProductException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain representation of a product.
 *
 * <p>Enforces its own invariants on construction: a non-blank SKU of at least
 * three characters, a non-blank name of at least two characters and a price
 * greater than or equal to {@code 0.01}.</p>
 */
public final class Product {

    private static final int MIN_SKU_LENGTH = 3;
    private static final int MIN_NAME_LENGTH = 2;
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");

    private final UUID id;
    private final String sku;
    private final String name;
    private final BigDecimal price;

    /**
     * Creates a validated product.
     *
     * @param id    unique identifier (generated when {@code null})
     * @param sku   stock keeping unit, at least three characters
     * @param name  product name, at least two characters
     * @param price unit price, greater than or equal to {@code 0.01}
     * @throws InvalidProductException if any invariant is violated
     */
    public Product(final UUID id, final String sku, final String name, final BigDecimal price) {
        if (sku == null || sku.trim().length() < MIN_SKU_LENGTH) {
            throw new InvalidProductException("SKU must be at least " + MIN_SKU_LENGTH + " characters");
        }
        if (name == null || name.trim().length() < MIN_NAME_LENGTH) {
            throw new InvalidProductException("Name must be at least " + MIN_NAME_LENGTH + " characters");
        }
        if (price == null || price.compareTo(MIN_PRICE) < 0) {
            throw new InvalidProductException("Price must be greater than or equal to " + MIN_PRICE);
        }
        this.id = id == null ? UUID.randomUUID() : id;
        this.sku = sku.trim();
        this.name = name.trim();
        this.price = price;
    }

    /**
     * Factory for a brand-new product with a generated identifier.
     *
     * @param sku   stock keeping unit
     * @param name  product name
     * @param price unit price
     * @return a new validated product
     */
    public static Product create(final String sku, final String name, final BigDecimal price) {
        return new Product(UUID.randomUUID(), sku, name, price);
    }

    /**
     * Returns a copy of this product with updated mutable fields.
     *
     * @param newSku   new SKU
     * @param newName  new name
     * @param newPrice new price
     * @return an updated product keeping the same identifier
     */
    public Product withUpdatedDetails(final String newSku, final String newName, final BigDecimal newPrice) {
        return new Product(this.id, newSku, newName, newPrice);
    }

    /**
     * Returns the product identifier.
     *
     * @return the unique id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the product SKU.
     *
     * @return the stock keeping unit
     */
    public String getSku() {
        return sku;
    }

    /**
     * Returns the product name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the product price.
     *
     * @return the unit price
     */
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Product)) {
            return false;
        }
        final Product product = (Product) other;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
