package com.example.productcrud.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Inbound request payload for creating or updating a product.
 *
 * @param sku   stock keeping unit (at least three characters)
 * @param name  product name (at least two characters)
 * @param price unit price (at least {@code 0.01})
 */
public record ProductRequest(
        @NotNull @Size(min = 3, message = "sku must be at least 3 characters") String sku,
        @NotNull @Size(min = 2, message = "name must be at least 2 characters") String name,
        @NotNull @DecimalMin(value = "0.01", message = "price must be at least 0.01") BigDecimal price) {
}
