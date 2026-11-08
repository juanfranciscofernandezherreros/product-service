package com.example.productcrud.infrastructure.adapter.in.web;

import com.example.productcrud.application.port.in.CreateProductUseCase;
import com.example.productcrud.application.port.in.DeleteProductUseCase;
import com.example.productcrud.application.port.in.GetProductUseCase;
import com.example.productcrud.application.port.in.ListProductsUseCase;
import com.example.productcrud.application.port.in.UpdateProductUseCase;
import com.example.productcrud.infrastructure.adapter.in.web.dto.ProductRequest;
import com.example.productcrud.infrastructure.adapter.in.web.dto.ProductResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter exposing the product CRUD endpoints under {@code /api/v1/products}.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    /**
     * Creates the controller with its use case collaborators.
     *
     * @param createProductUseCase create use case
     * @param getProductUseCase    get use case
     * @param listProductsUseCase  list use case
     * @param updateProductUseCase update use case
     * @param deleteProductUseCase delete use case
     */
    public ProductController(final CreateProductUseCase createProductUseCase,
                             final GetProductUseCase getProductUseCase,
                             final ListProductsUseCase listProductsUseCase,
                             final UpdateProductUseCase updateProductUseCase,
                             final DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    /**
     * Creates a new product.
     *
     * @param request validated product payload
     * @return 201 Created with the stored product
     */
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody final ProductRequest request) {
        final ProductResponse response = ProductWebMapper.toResponse(
                createProductUseCase.create(ProductWebMapper.toCommand(request)));
        return ResponseEntity.created(URI.create("/api/v1/products/" + response.id())).body(response);
    }

    /**
     * Returns every product.
     *
     * @return 200 OK with the full list
     */
    @GetMapping
    public List<ProductResponse> getAll() {
        return listProductsUseCase.listAll().stream()
                .map(ProductWebMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Returns a single product by id.
     *
     * @param id product identifier
     * @return 200 OK with the product
     */
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable final UUID id) {
        return ProductWebMapper.toResponse(getProductUseCase.getById(id));
    }

    /**
     * Updates an existing product.
     *
     * @param id      product identifier
     * @param request validated product payload
     * @return 200 OK with the updated product
     */
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable final UUID id, @Valid @RequestBody final ProductRequest request) {
        return ProductWebMapper.toResponse(
                updateProductUseCase.update(id, ProductWebMapper.toCommand(request)));
    }

    /**
     * Deletes a product.
     *
     * @param id product identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID id) {
        deleteProductUseCase.delete(id);
    }
}
