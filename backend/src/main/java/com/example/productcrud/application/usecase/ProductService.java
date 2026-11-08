package com.example.productcrud.application.usecase;

import com.example.productcrud.application.port.in.CreateProductUseCase;
import com.example.productcrud.application.port.in.DeleteProductUseCase;
import com.example.productcrud.application.port.in.GetProductUseCase;
import com.example.productcrud.application.port.in.ListProductsUseCase;
import com.example.productcrud.application.port.in.ProductCommand;
import com.example.productcrud.application.port.in.UpdateProductUseCase;
import com.example.productcrud.application.port.out.ProductRepositoryPort;
import com.example.productcrud.domain.exception.ProductNotFoundException;
import com.example.productcrud.domain.model.Product;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing every product use case.
 *
 * <p>Pure application logic wired through constructor injection; it depends
 * only on the outbound {@link ProductRepositoryPort} abstraction.</p>
 */
public class ProductService implements CreateProductUseCase, GetProductUseCase,
        ListProductsUseCase, UpdateProductUseCase, DeleteProductUseCase {

    private final ProductRepositoryPort repository;

    /**
     * Creates the service.
     *
     * @param repository outbound persistence port
     */
    public ProductService(final ProductRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Product create(final ProductCommand command) {
        final Product product = Product.create(command.sku(), command.name(), command.price());
        return repository.save(product);
    }

    @Override
    public Product getById(final UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> listAll() {
        return repository.findAll();
    }

    @Override
    public Product update(final UUID id, final ProductCommand command) {
        final Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        final Product updated = existing.withUpdatedDetails(command.sku(), command.name(), command.price());
        return repository.save(updated);
    }

    @Override
    public void delete(final UUID id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
