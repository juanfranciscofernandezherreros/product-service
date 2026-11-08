package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.CommentPort;
import com.example.contentservice.domain.model.Comment;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Persistence adapter implementing {@link CommentPort}.
 */
public class CommentPersistenceAdapter implements CommentPort {

    private final CommentJpaRepository repository;

    public CommentPersistenceAdapter(final CommentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment save(final Comment comment) {
        final CommentEntity entity = new CommentEntity();
        entity.setId(comment.id());
        entity.setArticuloSlug(comment.articuloSlug());
        entity.setPadreId(comment.padreId());
        entity.setAutorId(comment.autorId());
        entity.setContenido(comment.contenido());
        entity.setCreadoEn(comment.creadoEn() != null ? comment.creadoEn() : OffsetDateTime.now());
        entity.setEliminado(false);
        entity.setOculto(false);
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Comment> findByArticulo(final String articuloSlug) {
        return repository.findByArticuloSlugOrderByCreadoEnAsc(articuloSlug)
                .stream().map(ContentPersistenceMapper::toDomain).toList();
    }
}
