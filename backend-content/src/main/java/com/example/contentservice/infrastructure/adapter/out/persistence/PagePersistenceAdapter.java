package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.PagePort;
import com.example.contentservice.domain.model.Page;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Persistence adapter implementing {@link PagePort}.
 */
public class PagePersistenceAdapter implements PagePort {

    private final PageJpaRepository repository;

    public PagePersistenceAdapter(final PageJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page save(final Page page) {
        final PageEntity entity = new PageEntity();
        entity.setRuta(page.ruta());
        entity.setTitulo(page.titulo());
        entity.setDescripcion(page.descripcion());
        entity.setHtml(page.html());
        entity.setUrlPublica(page.urlPublica());
        entity.setActualizadoEn(page.actualizadoEn() != null ? page.actualizadoEn() : OffsetDateTime.now());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsByRuta(final String ruta) {
        return repository.existsById(ruta);
    }

    @Override
    public List<Page> findAll() {
        return repository.findAll().stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
