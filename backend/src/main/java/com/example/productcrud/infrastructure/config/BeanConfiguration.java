package com.example.productcrud.infrastructure.config;

import com.example.productcrud.application.port.in.CreateProductUseCase;
import com.example.productcrud.application.port.in.DeleteProductUseCase;
import com.example.productcrud.application.port.in.GetProductUseCase;
import com.example.productcrud.application.port.in.ListProductsUseCase;
import com.example.productcrud.application.port.in.UpdateProductUseCase;
import com.example.productcrud.application.port.out.ProductRepositoryPort;
import com.example.productcrud.application.usecase.ProductService;
import com.example.productcrud.infrastructure.adapter.out.persistence.ProductJpaRepository;
import com.example.productcrud.infrastructure.adapter.out.persistence.ProductPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans so every dependency is visible in one place.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Provides the outbound persistence adapter bound to the {@link ProductRepositoryPort}.
     *
     * @param jpaRepository Spring Data repository
     * @return the persistence port implementation
     */
    @Bean
    public ProductRepositoryPort productRepositoryPort(final ProductJpaRepository jpaRepository) {
        return new ProductPersistenceAdapter(jpaRepository);
    }

    /**
     * Provides the application service that implements every product use case.
     *
     * @param repositoryPort outbound persistence port
     * @return the product service
     */
    @Bean
    public ProductService productService(final ProductRepositoryPort repositoryPort) {
        return new ProductService(repositoryPort);
    }

    /**
     * Exposes the create use case.
     *
     * @param productService application service
     * @return the create use case
     */
    @Bean
    public CreateProductUseCase createProductUseCase(final ProductService productService) {
        return productService;
    }

    /**
     * Exposes the get use case.
     *
     * @param productService application service
     * @return the get use case
     */
    @Bean
    public GetProductUseCase getProductUseCase(final ProductService productService) {
        return productService;
    }

    /**
     * Exposes the list use case.
     *
     * @param productService application service
     * @return the list use case
     */
    @Bean
    public ListProductsUseCase listProductsUseCase(final ProductService productService) {
        return productService;
    }

    /**
     * Exposes the update use case.
     *
     * @param productService application service
     * @return the update use case
     */
    @Bean
    public UpdateProductUseCase updateProductUseCase(final ProductService productService) {
        return productService;
    }

    /**
     * Exposes the delete use case.
     *
     * @param productService application service
     * @return the delete use case
     */
    @Bean
    public DeleteProductUseCase deleteProductUseCase(final ProductService productService) {
        return productService;
    }
}
