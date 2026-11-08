package com.example.miscservice.infrastructure.adapter.out.persistence;

import com.example.miscservice.application.port.out.MiscOutPorts.WritingDraftPort;
import com.example.miscservice.domain.model.WritingDraft;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link WritingDraftPort}.
 */
public class WritingDraftPersistenceAdapter implements WritingDraftPort {

    private final WritingDraftJpaRepository repository;

    public WritingDraftPersistenceAdapter(final WritingDraftJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public WritingDraft save(final WritingDraft draft) {
        final OffsetDateTime now = OffsetDateTime.now();
        final WritingDraftEntity entity = new WritingDraftEntity();
        entity.setId(draft.id());
        entity.setUsuarioId(draft.usuarioId());
        entity.setTipo(draft.tipo().value());
        entity.setTitulo(draft.titulo());
        entity.setContenido(draft.contenido());
        entity.setCreadoEn(draft.creadoEn() != null ? draft.creadoEn() : now);
        entity.setActualizadoEn(draft.actualizadoEn() != null ? draft.actualizadoEn() : now);
        return MiscPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public List<WritingDraft> findByUser(final UUID usuarioId, final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "creadoEn");
        return repository.findByUsuarioId(usuarioId, new OffsetBasedPageRequest(offset, limit, sort))
                .map(MiscPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long countByUser(final UUID usuarioId) {
        return repository.countByUsuarioId(usuarioId);
    }
}
