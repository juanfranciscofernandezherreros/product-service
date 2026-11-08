package com.example.productcrud.infrastructure.adapter.in.web;

import com.example.productcrud.application.port.in.ProductCommand;
import com.example.productcrud.domain.model.Product;
import com.example.productcrud.infrastructure.adapter.in.web.dto.ProductRequest;
import com.example.productcrud.infrastructure.adapter.in.web.dto.ProductResponse;

/**
 * Explicit mapper between web DTOs and application/domain types.
 */
public final class ProductWebMapper {

    private ProductWebMapper() {
    }

    /**
     * Converts a request DTO into an application command.
     *
     * @param request inbound request
     * @return the product command
     */
    public static ProductCommand toCommand(final ProductRequest request) {
        return new ProductCommand(request.sku(), request.name(), request.price());
    }

    /**
     * Converts a domain product into a response DTO.
     *
     * @param product domain product
     * @return the response DTO
     */
    public static ProductResponse toResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getSku(), product.getName(), product.getPrice());
    }
}
