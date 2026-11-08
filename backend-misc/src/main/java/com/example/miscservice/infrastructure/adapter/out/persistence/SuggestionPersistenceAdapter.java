package com.example.miscservice.infrastructure.adapter.out.persistence;

import com.example.miscservice.application.port.out.MiscOutPorts.SuggestionPort;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing {@link SuggestionPort}.
 */
public class SuggestionPersistenceAdapter implements SuggestionPort {

    private final SuggestionJpaRepository repository;

    public SuggestionPersistenceAdapter(final SuggestionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Suggestion save(final Suggestion suggestion) {
        final SuggestionEntity entity = new SuggestionEntity();
        entity.setId(suggestion.id());
        entity.setTitulo(suggestion.titulo());
        entity.setDescripcion(suggestion.descripcion());
        entity.setEstado(suggestion.estado().value());
        entity.setCreadoEn(suggestion.creadoEn() != null ? suggestion.creadoEn() : OffsetDateTime.now());
        return MiscPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Suggestion> findById(final Long id) {
        return repository.findById(id).map(MiscPersistenceMapper::toDomain);
    }

    @Override
    public List<Suggestion> findAll(final SuggestionStatus estado, final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "creadoEn");
        final OffsetBasedPageRequest page = new OffsetBasedPageRequest(offset, limit, sort);
        if (estado == null) {
            return repository.findAll(page).map(MiscPersistenceMapper::toDomain).getContent();
        }
        return repository.findByEstado(estado.value(), page).map(MiscPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long count(final SuggestionStatus estado) {
        return estado == null ? repository.count() : repository.countByEstado(estado.value());
    }
}
