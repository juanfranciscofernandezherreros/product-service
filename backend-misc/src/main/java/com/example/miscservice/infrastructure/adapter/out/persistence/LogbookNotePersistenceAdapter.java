package com.example.miscservice.infrastructure.adapter.out.persistence;

import com.example.miscservice.application.port.out.MiscOutPorts.LogbookNotePort;
import com.example.miscservice.domain.model.LogbookNote;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link LogbookNotePort}.
 */
public class LogbookNotePersistenceAdapter implements LogbookNotePort {

    private final LogbookNoteJpaRepository repository;

    public LogbookNotePersistenceAdapter(final LogbookNoteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogbookNote save(final LogbookNote note) {
        final OffsetDateTime now = OffsetDateTime.now();
        final LogbookNoteEntity entity = new LogbookNoteEntity();
        entity.setId(note.id());
        entity.setUsuarioId(note.usuarioId());
        entity.setCategoriaId(note.categoriaId());
        entity.setContenido(note.contenido());
        entity.setCreadoEn(note.creadoEn() != null ? note.creadoEn() : now);
        entity.setActualizadoEn(note.actualizadoEn() != null ? note.actualizadoEn() : now);
        return MiscPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public List<LogbookNote> findByUser(final UUID usuarioId, final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "creadoEn");
        return repository.findByUsuarioId(usuarioId, new OffsetBasedPageRequest(offset, limit, sort))
                .map(MiscPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long countByUser(final UUID usuarioId) {
        return repository.countByUsuarioId(usuarioId);
    }
}
