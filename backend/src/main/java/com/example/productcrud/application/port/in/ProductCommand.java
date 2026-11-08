package com.example.productcrud.application.port.in;

import java.math.BigDecimal;

/**
 * Immutable input data describing the mutable attributes of a product.
 *
 * @param sku   stock keeping unit
 * @param name  product name
 * @param price unit price
 */
public record ProductCommand(String sku, String name, BigDecimal price) {
}
