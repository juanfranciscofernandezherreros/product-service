package com.example.productcrud.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Outbound response payload representing a product.
 *
 * @param id    unique identifier
 * @param sku   stock keeping unit
 * @param name  product name
 * @param price unit price
 */
public record ProductResponse(UUID id, String sku, String name, BigDecimal price) {
}
