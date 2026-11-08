package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.CollectionType;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;

/**
 * Maps between domain models and JPA entities.
 */
public final class ContentPersistenceMapper {

    private ContentPersistenceMapper() {
    }

    public static Article toDomain(final ArticleEntity e) {
        return new Article(e.getId(), e.getRuta(), e.getSlug(), e.getTitulo(), e.getDescripcion(),
                e.getHtml(), e.getUrlPublica(), e.getOrigen(), e.getCategoriaSlug(),
                e.getFechaPublicacion(), e.getNivel(), e.getPalabras(), e.isEsArticuloIndice(),
                e.getUsuarioId(), e.getActualizadoEn());
    }

    public static Comment toDomain(final CommentEntity e) {
        return new Comment(e.getId(), e.getArticuloSlug(), e.getPadreId(), e.getAutorId(),
                e.getContenido(), e.getCreadoEn());
    }

    public static Category toDomain(final CategoryEntity e) {
        return new Category(e.getSlug(), e.getNombre(), e.getDescripcion(), e.getOrden());
    }

    public static Page toDomain(final PageEntity e) {
        return new Page(e.getRuta(), e.getTitulo(), e.getDescripcion(), e.getHtml(),
                e.getUrlPublica(), e.getActualizadoEn());
    }

    public static Collection toDomain(final CollectionEntity e) {
        return new Collection(e.getId(), e.getSlug(), e.getTitulo(), e.getDescripcion(),
                CollectionType.fromValue(e.getTipo()), e.isIncluirTodosArticulos(),
                e.getNombreArchivo(), e.getFechaPublicacion(), e.isPublicado());
    }

    public static Downloadable toDomain(final DownloadableEntity e) {
        return new Downloadable(e.getId(), e.getSlug(), e.getTitulo(), e.getDescripcion(),
                e.getCategoria(), e.getFechaPublicacion(), e.getUrlArchivo(), e.getUrlLectura(),
                e.getOrden(), e.isPublicado(), e.getCreadoEn());
    }

    public static MoviePhrase toDomain(final MoviePhraseEntity e) {
        return new MoviePhrase(e.getId(), e.getMovieSlug(), e.getMovieTitle(), e.getSubtitleNumber(),
                e.getTimecode(), e.getPhrase(), e.getTenseLabels(), e.getNotes(), e.getCreatedAt());
    }
}
