package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.DownloadablePort;
import com.example.contentservice.domain.model.Downloadable;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Persistence adapter implementing {@link DownloadablePort}.
 */
public class DownloadablePersistenceAdapter implements DownloadablePort {

    private final DownloadableJpaRepository repository;

    public DownloadablePersistenceAdapter(final DownloadableJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Downloadable save(final Downloadable d) {
        final DownloadableEntity entity = new DownloadableEntity();
        entity.setId(d.id());
        entity.setSlug(d.slug());
        entity.setTitulo(d.titulo());
        entity.setDescripcion(d.descripcion());
        entity.setCategoria(d.categoria());
        entity.setFechaPublicacion(d.fechaPublicacion());
        entity.setUrlArchivo(d.urlArchivo());
        entity.setUrlLectura(d.urlLectura());
        entity.setOrden(d.orden());
        entity.setPublicado(d.publicado());
        entity.setCreadoEn(d.creadoEn() != null ? d.creadoEn() : OffsetDateTime.now());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsBySlug(final String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<Downloadable> findAll(final String categoria) {
        final List<DownloadableEntity> entities = (categoria == null || categoria.isBlank())
                ? repository.findAllByOrderByOrdenAsc()
                : repository.findByCategoriaOrderByOrdenAsc(categoria);
        return entities.stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
