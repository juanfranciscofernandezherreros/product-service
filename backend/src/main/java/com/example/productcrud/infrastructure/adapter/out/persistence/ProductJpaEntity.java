package com.example.productcrud.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA persistence entity for products, kept separate from the domain model.
 */
@Entity
@Table(name = "products")
public class ProductJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Default constructor required by JPA.
     */
    protected ProductJpaEntity() {
    }

    /**
     * Creates a fully populated entity.
     *
     * @param id    identifier
     * @param sku   stock keeping unit
     * @param name  name
     * @param price price
     */
    public ProductJpaEntity(final UUID id, final String sku, final String name, final BigDecimal price) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    /**
     * Returns the identifier.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the SKU.
     *
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }
}
