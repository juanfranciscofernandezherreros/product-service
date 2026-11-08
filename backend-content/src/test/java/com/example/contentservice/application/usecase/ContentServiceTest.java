package com.example.contentservice.application.usecase;

import com.example.contentservice.application.port.in.ContentCommands;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContentServiceTest {

    private ArticlePort articlePort;
    private CommentPort commentPort;
    private CategoryPort categoryPort;
    private PagePort pagePort;
    private CollectionPort collectionPort;
    private DownloadablePort downloadablePort;
    private MoviePhrasePort moviePhrasePort;
    private ContentService service;

    @BeforeEach
    void setUp() {
        articlePort = mock(ArticlePort.class);
        commentPort = mock(CommentPort.class);
        categoryPort = mock(CategoryPort.class);
        pagePort = mock(PagePort.class);
        collectionPort = mock(CollectionPort.class);
        downloadablePort = mock(DownloadablePort.class);
        moviePhrasePort = mock(MoviePhrasePort.class);
        service = new ContentService(articlePort, commentPort, categoryPort, pagePort,
                collectionPort, downloadablePort, moviePhrasePort);
    }

    private Article sampleArticle() {
        return new Article(1L, "post/hola.html", "hola", "Hola", "desc", "<p>hi</p>",
                "https://x/hola", "github", "gram", null, "B2", 100, false, null, OffsetDateTime.now());
    }

    @Test
    void createArticleSucceeds() {
        when(articlePort.existsByRutaOrSlug(anyString(), anyString())).thenReturn(false);
        when(articlePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Article a = service.createArticle(new ContentCommands.CreateArticle(
                "post/hola.html", "hola", "Hola", "desc", "<p>hi</p>", "https://x/hola",
                "gram", null, "B2"));
        assertThat(a.slug()).isEqualTo("hola");
    }

    @Test
    void createArticleDuplicateThrows() {
        when(articlePort.existsByRutaOrSlug(anyString(), anyString())).thenReturn(true);
        assertThatThrownBy(() -> service.createArticle(new ContentCommands.CreateArticle(
                "post/hola.html", "hola", "Hola", "desc", "<p>hi</p>", "https://x/hola", null, null, null)))
                .isInstanceOf(DuplicateResourceException.class);
        verify(articlePort, never()).save(any());
    }

    @Test
    void getArticleMissingThrows() {
        when(articlePort.findBySlug("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getArticle("x")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listAndCountArticles() {
        when(articlePort.findAll(null, null, 0, 10)).thenReturn(List.of(sampleArticle()));
        when(articlePort.count(null, null)).thenReturn(1L);
        assertThat(service.listArticles(null, null, 0, 10)).hasSize(1);
        assertThat(service.countArticles(null, null)).isEqualTo(1L);
    }

    @Test
    void updateArticleSucceeds() {
        when(articlePort.findBySlug("hola")).thenReturn(Optional.of(sampleArticle()));
        when(articlePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Article a = service.updateArticle(new ContentCommands.UpdateArticle(
                "hola", "Nuevo", null, null, null, "C1"));
        assertThat(a.titulo()).isEqualTo("Nuevo");
        assertThat(a.nivel()).isEqualTo("C1");
    }

    @Test
    void deleteArticleSucceeds() {
        when(articlePort.findBySlug("hola")).thenReturn(Optional.of(sampleArticle()));
        service.deleteArticle("hola");
        verify(articlePort).deleteBySlug("hola");
    }

    @Test
    void createCommentRequiresArticle() {
        when(articlePort.findBySlug("hola")).thenReturn(Optional.of(sampleArticle()));
        when(commentPort.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createComment(new ContentCommands.CreateComment(
                "hola", null, null, "Buen post")).contenido()).isEqualTo("Buen post");
    }

    @Test
    void listCommentsRequiresArticle() {
        when(articlePort.findBySlug("hola")).thenReturn(Optional.of(sampleArticle()));
        when(commentPort.findByArticulo("hola")).thenReturn(List.of());
        assertThat(service.listComments("hola")).isEmpty();
    }

    @Test
    void createCategoryDuplicateThrows() {
        when(categoryPort.existsBySlug("g")).thenReturn(true);
        assertThatThrownBy(() -> service.createCategory(new ContentCommands.CreateCategory(
                "g", "Gram", null, null))).isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createCategoryAndList() {
        when(categoryPort.existsBySlug("g")).thenReturn(false);
        when(categoryPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(categoryPort.findAll()).thenReturn(List.of());
        assertThat(service.createCategory(new ContentCommands.CreateCategory("g", "Gram", "d", 2)).orden())
                .isEqualTo(2);
        assertThat(service.listCategories()).isEmpty();
    }

    @Test
    void createPageDuplicateThrows() {
        when(pagePort.existsByRuta("r")).thenReturn(true);
        assertThatThrownBy(() -> service.createPage(new ContentCommands.CreatePage(
                "r", "T", null, "<p>x</p>", "https://x"))).isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createPageAndList() {
        when(pagePort.existsByRuta("r")).thenReturn(false);
        when(pagePort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pagePort.findAll()).thenReturn(List.of());
        assertThat(service.createPage(new ContentCommands.CreatePage(
                "r", "T", null, "<p>x</p>", "https://x")).titulo()).isEqualTo("T");
        assertThat(service.listPages()).isEmpty();
    }

    @Test
    void createCollectionDuplicateThrows() {
        when(collectionPort.existsBySlugOrFile(eq("s"), anyString())).thenReturn(true);
        assertThatThrownBy(() -> service.createCollection(new ContentCommands.CreateCollection(
                "s", "T", null, "articulos", false, "file.pdf", null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createCollectionAndList() {
        when(collectionPort.existsBySlugOrFile(anyString(), anyString())).thenReturn(false);
        when(collectionPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(collectionPort.findAll()).thenReturn(List.of());
        assertThat(service.createCollection(new ContentCommands.CreateCollection(
                "s", "T", "d", "vocabulario", true, "file.pdf", null)).slug()).isEqualTo("s");
        assertThat(service.listCollections()).isEmpty();
    }

    @Test
    void createDownloadableDuplicateThrows() {
        when(downloadablePort.existsBySlug("s")).thenReturn(true);
        assertThatThrownBy(() -> service.createDownloadable(new ContentCommands.CreateDownloadable(
                "s", "T", null, null, null, "https://f", null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createDownloadableAndList() {
        when(downloadablePort.existsBySlug("s")).thenReturn(false);
        when(downloadablePort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(downloadablePort.findAll(null)).thenReturn(List.of());
        assertThat(service.createDownloadable(new ContentCommands.CreateDownloadable(
                "s", "T", null, null, null, "https://f", null)).categoria()).isEqualTo("apuntes");
        assertThat(service.listDownloadables(null)).isEmpty();
    }

    @Test
    void createMoviePhraseDuplicateThrows() {
        when(moviePhrasePort.existsByMovieAndNumber(eq("m"), anyInt())).thenReturn(true);
        assertThatThrownBy(() -> service.createMoviePhrase(new ContentCommands.CreateMoviePhrase(
                "m", "Movie", 1, "00:01", "hi", null, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createMoviePhraseAndList() {
        when(moviePhrasePort.existsByMovieAndNumber(anyString(), anyInt())).thenReturn(false);
        when(moviePhrasePort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(moviePhrasePort.findAll("m")).thenReturn(List.of());
        assertThat(service.createMoviePhrase(new ContentCommands.CreateMoviePhrase(
                "m", "Movie", 2, "00:02", "hola", "PST", "n")).subtitleNumber()).isEqualTo(2);
        assertThat(service.listMoviePhrases("m")).isEmpty();
    }
}
