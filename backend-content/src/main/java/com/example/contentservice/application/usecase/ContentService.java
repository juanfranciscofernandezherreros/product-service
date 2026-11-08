package com.example.contentservice.application.usecase;

import com.example.contentservice.application.port.in.ContentCommands;
import com.example.contentservice.application.port.in.ContentUseCase;
import com.example.contentservice.application.port.out.ContentOutPorts.ArticlePort;
import com.example.contentservice.application.port.out.ContentOutPorts.CategoryPort;
import com.example.contentservice.application.port.out.ContentOutPorts.CollectionPort;
import com.example.contentservice.application.port.out.ContentOutPorts.CommentPort;
import com.example.contentservice.application.port.out.ContentOutPorts.DownloadablePort;
import com.example.contentservice.application.port.out.ContentOutPorts.MoviePhrasePort;
import com.example.contentservice.application.port.out.ContentOutPorts.PagePort;
import com.example.contentservice.domain.exception.DuplicateResourceException;
import com.example.contentservice.domain.exception.ResourceNotFoundException;
import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.CollectionType;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;

import java.util.List;

/**
 * Application service orchestrating the Content domain use cases.
 */
public class ContentService implements ContentUseCase {

    private final ArticlePort articlePort;
    private final CommentPort commentPort;
    private final CategoryPort categoryPort;
    private final PagePort pagePort;
    private final CollectionPort collectionPort;
    private final DownloadablePort downloadablePort;
    private final MoviePhrasePort moviePhrasePort;

    /**
     * Constructor injection of all output ports.
     *
     * @param articlePort      articles port
     * @param commentPort      comments port
     * @param categoryPort     categories port
     * @param pagePort         pages port
     * @param collectionPort   collections port
     * @param downloadablePort downloadables port
     * @param moviePhrasePort  movie phrases port
     */
    public ContentService(final ArticlePort articlePort, final CommentPort commentPort,
                          final CategoryPort categoryPort, final PagePort pagePort,
                          final CollectionPort collectionPort, final DownloadablePort downloadablePort,
                          final MoviePhrasePort moviePhrasePort) {
        this.articlePort = articlePort;
        this.commentPort = commentPort;
        this.categoryPort = categoryPort;
        this.pagePort = pagePort;
        this.collectionPort = collectionPort;
        this.downloadablePort = downloadablePort;
        this.moviePhrasePort = moviePhrasePort;
    }

    @Override
    public Article createArticle(final ContentCommands.CreateArticle c) {
        if (articlePort.existsByRutaOrSlug(c.ruta(), c.slug())) {
            throw new DuplicateResourceException("Article with ruta or slug already exists");
        }
        return articlePort.save(Article.create(c.ruta(), c.slug(), c.titulo(), c.descripcion(),
                c.html(), c.urlPublica(), c.categoriaSlug(), c.fechaPublicacion(), c.nivel()));
    }

    @Override
    public Article getArticle(final String slug) {
        return articlePort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + slug));
    }

    @Override
    public List<Article> listArticles(final String categoriaSlug, final String nivel,
                                      final int offset, final int limit) {
        return articlePort.findAll(categoriaSlug, nivel, offset, limit);
    }

    @Override
    public long countArticles(final String categoriaSlug, final String nivel) {
        return articlePort.count(categoriaSlug, nivel);
    }

    @Override
    public Article updateArticle(final ContentCommands.UpdateArticle c) {
        final Article existing = getArticle(c.slug());
        return articlePort.save(existing.update(c.titulo(), c.descripcion(), c.html(),
                c.categoriaSlug(), c.nivel()));
    }

    @Override
    public void deleteArticle(final String slug) {
        getArticle(slug);
        articlePort.deleteBySlug(slug);
    }

    @Override
    public Comment createComment(final ContentCommands.CreateComment c) {
        getArticle(c.articuloSlug());
        return commentPort.save(Comment.create(c.articuloSlug(), c.padreId(), c.autorId(), c.contenido()));
    }

    @Override
    public List<Comment> listComments(final String articuloSlug) {
        getArticle(articuloSlug);
        return commentPort.findByArticulo(articuloSlug);
    }

    @Override
    public Category createCategory(final ContentCommands.CreateCategory c) {
        if (categoryPort.existsBySlug(c.slug())) {
            throw new DuplicateResourceException("Category already exists: " + c.slug());
        }
        return categoryPort.save(Category.create(c.slug(), c.nombre(), c.descripcion(), c.orden()));
    }

    @Override
    public List<Category> listCategories() {
        return categoryPort.findAll();
    }

    @Override
    public Page createPage(final ContentCommands.CreatePage c) {
        if (pagePort.existsByRuta(c.ruta())) {
            throw new DuplicateResourceException("Page already exists: " + c.ruta());
        }
        return pagePort.save(Page.create(c.ruta(), c.titulo(), c.descripcion(), c.html(), c.urlPublica()));
    }

    @Override
    public List<Page> listPages() {
        return pagePort.findAll();
    }

    @Override
    public Collection createCollection(final ContentCommands.CreateCollection c) {
        if (collectionPort.existsBySlugOrFile(c.slug(), c.nombreArchivo())) {
            throw new DuplicateResourceException("Collection slug or file already exists");
        }
        return collectionPort.save(Collection.create(c.slug(), c.titulo(), c.descripcion(),
                CollectionType.fromValue(c.tipo()), c.incluirTodosArticulos(), c.nombreArchivo(),
                c.fechaPublicacion()));
    }

    @Override
    public List<Collection> listCollections() {
        return collectionPort.findAll();
    }

    @Override
    public Downloadable createDownloadable(final ContentCommands.CreateDownloadable c) {
        if (downloadablePort.existsBySlug(c.slug())) {
            throw new DuplicateResourceException("Downloadable slug already exists: " + c.slug());
        }
        return downloadablePort.save(Downloadable.create(c.slug(), c.titulo(), c.descripcion(),
                c.categoria(), c.fechaPublicacion(), c.urlArchivo(), c.urlLectura()));
    }

    @Override
    public List<Downloadable> listDownloadables(final String categoria) {
        return downloadablePort.findAll(categoria);
    }

    @Override
    public MoviePhrase createMoviePhrase(final ContentCommands.CreateMoviePhrase c) {
        if (moviePhrasePort.existsByMovieAndNumber(c.movieSlug(), c.subtitleNumber())) {
            throw new DuplicateResourceException("Subtitle number already exists for movie");
        }
        return moviePhrasePort.save(MoviePhrase.create(c.movieSlug(), c.movieTitle(), c.subtitleNumber(),
                c.timecode(), c.phrase(), c.tenseLabels(), c.notes()));
    }

    @Override
    public List<MoviePhrase> listMoviePhrases(final String movieSlug) {
        return moviePhrasePort.findAll(movieSlug);
    }
}
