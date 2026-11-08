package com.example.miscservice.infrastructure.adapter.out.persistence;

import com.example.miscservice.application.port.out.MiscOutPorts.LogbookCategoryPort;
import com.example.miscservice.domain.model.LogbookCategory;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link LogbookCategoryPort}.
 */
public class LogbookCategoryPersistenceAdapter implements LogbookCategoryPort {

    private final LogbookCategoryJpaRepository repository;

    public LogbookCategoryPersistenceAdapter(final LogbookCategoryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogbookCategory save(final LogbookCategory category) {
        final LogbookCategoryEntity entity = new LogbookCategoryEntity();
        entity.setId(category.id());
        entity.setUsuarioId(category.usuarioId());
        entity.setNombre(category.nombre());
        entity.setCreadoEn(category.creadoEn() != null ? category.creadoEn() : OffsetDateTime.now());
        return MiscPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<LogbookCategory> findByIdAndUser(final UUID id, final UUID usuarioId) {
        return repository.findByIdAndUsuarioId(id, usuarioId).map(MiscPersistenceMapper::toDomain);
    }

    @Override
    public List<LogbookCategory> findByUser(final UUID usuarioId, final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "creadoEn");
        return repository.findByUsuarioId(usuarioId, new OffsetBasedPageRequest(offset, limit, sort))
                .map(MiscPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long countByUser(final UUID usuarioId) {
        return repository.countByUsuarioId(usuarioId);
    }
}
