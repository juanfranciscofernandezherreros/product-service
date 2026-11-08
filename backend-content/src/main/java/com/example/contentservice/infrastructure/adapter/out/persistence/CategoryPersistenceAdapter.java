package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.CategoryPort;
import com.example.contentservice.domain.model.Category;

import java.util.List;

/**
 * Persistence adapter implementing {@link CategoryPort}.
 */
public class CategoryPersistenceAdapter implements CategoryPort {

    private final CategoryJpaRepository repository;

    public CategoryPersistenceAdapter(final CategoryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category save(final Category category) {
        final CategoryEntity entity = new CategoryEntity();
        entity.setSlug(category.slug());
        entity.setNombre(category.nombre());
        entity.setDescripcion(category.descripcion());
        entity.setOrden(category.orden());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsBySlug(final String slug) {
        return repository.existsById(slug);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAllByOrderByOrdenAsc()
                .stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
