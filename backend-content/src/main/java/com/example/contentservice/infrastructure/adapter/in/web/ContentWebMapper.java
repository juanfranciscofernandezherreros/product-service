package com.example.contentservice.infrastructure.adapter.in.web;

import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.ArticleResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CategoryResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CollectionResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CommentResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.DownloadableResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.MoviePhraseResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.PageResponseDto;

/**
 * Maps domain models to web response DTOs.
 */
public final class ContentWebMapper {

    private ContentWebMapper() {
    }

    public static ArticleResponse toResponse(final Article a) {
        return new ArticleResponse(a.id(), a.ruta(), a.slug(), a.titulo(), a.descripcion(), a.html(),
                a.urlPublica(), a.origen(), a.categoriaSlug(), a.fechaPublicacion(), a.nivel(),
                a.palabras(), a.esArticuloIndice(), a.actualizadoEn());
    }

    public static CommentResponse toResponse(final Comment c) {
        return new CommentResponse(c.id(), c.articuloSlug(), c.padreId(), c.autorId(),
                c.contenido(), c.creadoEn());
    }

    public static CategoryResponse toResponse(final Category c) {
        return new CategoryResponse(c.slug(), c.nombre(), c.descripcion(), c.orden());
    }

    public static PageResponseDto toResponse(final Page p) {
        return new PageResponseDto(p.ruta(), p.titulo(), p.descripcion(), p.html(),
                p.urlPublica(), p.actualizadoEn());
    }

    public static CollectionResponse toResponse(final Collection c) {
        return new CollectionResponse(c.id(), c.slug(), c.titulo(), c.descripcion(), c.tipo().value(),
                c.incluirTodosArticulos(), c.nombreArchivo(), c.fechaPublicacion(), c.publicado());
    }

    public static DownloadableResponse toResponse(final Downloadable d) {
        return new DownloadableResponse(d.id(), d.slug(), d.titulo(), d.descripcion(), d.categoria(),
                d.fechaPublicacion(), d.urlArchivo(), d.urlLectura(), d.orden(), d.publicado(),
                d.creadoEn());
    }

    public static MoviePhraseResponse toResponse(final MoviePhrase m) {
        return new MoviePhraseResponse(m.id(), m.movieSlug(), m.movieTitle(), m.subtitleNumber(),
                m.timecode(), m.phrase(), m.tenseLabels(), m.notes(), m.createdAt());
    }
}
