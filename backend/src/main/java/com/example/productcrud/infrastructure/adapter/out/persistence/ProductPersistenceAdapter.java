package com.example.productcrud.infrastructure.adapter.out.persistence;

import com.example.productcrud.application.port.out.ProductRepositoryPort;
import com.example.productcrud.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Outbound adapter implementing {@link ProductRepositoryPort} with Spring Data JPA.
 */
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public ProductPersistenceAdapter(final ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(final Product product) {
        final ProductJpaEntity saved = jpaRepository.save(ProductPersistenceMapper.toEntity(product));
        return ProductPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(final UUID id) {
        return jpaRepository.findById(id).map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(final UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(final UUID id) {
        jpaRepository.deleteById(id);
    }
}
