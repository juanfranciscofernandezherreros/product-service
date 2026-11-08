package com.example.contentservice.application.port.out;

import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;

import java.util.List;
import java.util.Optional;

/**
 * Output ports (persistence contracts) for the Content domain.
 */
public final class ContentOutPorts {

    private ContentOutPorts() {
    }

    /** Persistence port for articles. */
    public interface ArticlePort {
        Article save(Article article);

        Optional<Article> findBySlug(String slug);

        boolean existsByRutaOrSlug(String ruta, String slug);

        List<Article> findAll(String categoriaSlug, String nivel, int offset, int limit);

        long count(String categoriaSlug, String nivel);

        void deleteBySlug(String slug);
    }

    /** Persistence port for comments. */
    public interface CommentPort {
        Comment save(Comment comment);

        List<Comment> findByArticulo(String articuloSlug);
    }

    /** Persistence port for categories. */
    public interface CategoryPort {
        Category save(Category category);

        boolean existsBySlug(String slug);

        List<Category> findAll();
    }

    /** Persistence port for pages. */
    public interface PagePort {
        Page save(Page page);

        boolean existsByRuta(String ruta);

        List<Page> findAll();
    }

    /** Persistence port for collections. */
    public interface CollectionPort {
        Collection save(Collection collection);

        boolean existsBySlugOrFile(String slug, String nombreArchivo);

        List<Collection> findAll();
    }

    /** Persistence port for downloadables. */
    public interface DownloadablePort {
        Downloadable save(Downloadable downloadable);

        boolean existsBySlug(String slug);

        List<Downloadable> findAll(String categoria);
    }

    /** Persistence port for movie phrases. */
    public interface MoviePhrasePort {
        MoviePhrase save(MoviePhrase phrase);

        boolean existsByMovieAndNumber(String movieSlug, int subtitleNumber);

        List<MoviePhrase> findAll(String movieSlug);
    }
}
