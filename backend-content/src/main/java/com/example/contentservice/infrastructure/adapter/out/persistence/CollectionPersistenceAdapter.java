package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.CollectionPort;
import com.example.contentservice.domain.model.Collection;

import java.util.List;

/**
 * Persistence adapter implementing {@link CollectionPort}.
 */
public class CollectionPersistenceAdapter implements CollectionPort {

    private final CollectionJpaRepository repository;

    public CollectionPersistenceAdapter(final CollectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection save(final Collection collection) {
        final CollectionEntity entity = new CollectionEntity();
        entity.setId(collection.id());
        entity.setSlug(collection.slug());
        entity.setTitulo(collection.titulo());
        entity.setDescripcion(collection.descripcion());
        entity.setTipo(collection.tipo().value());
        entity.setIncluirTodosArticulos(collection.incluirTodosArticulos());
        entity.setNombreArchivo(collection.nombreArchivo());
        entity.setFechaPublicacion(collection.fechaPublicacion());
        entity.setPublicado(collection.publicado());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsBySlugOrFile(final String slug, final String nombreArchivo) {
        return repository.existsBySlugOrNombreArchivo(slug, nombreArchivo);
    }

    @Override
    public List<Collection> findAll() {
        return repository.findAll().stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
