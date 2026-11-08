package com.example.contentservice.application.port.in;

import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;

import java.util.List;

/**
 * Inbound port exposing the Content domain operations.
 */
public interface ContentUseCase {

    Article createArticle(ContentCommands.CreateArticle command);

    Article getArticle(String slug);

    List<Article> listArticles(String categoriaSlug, String nivel, int offset, int limit);

    long countArticles(String categoriaSlug, String nivel);

    Article updateArticle(ContentCommands.UpdateArticle command);

    void deleteArticle(String slug);

    Comment createComment(ContentCommands.CreateComment command);

    List<Comment> listComments(String articuloSlug);

    Category createCategory(ContentCommands.CreateCategory command);

    List<Category> listCategories();

    Page createPage(ContentCommands.CreatePage command);

    List<Page> listPages();

    Collection createCollection(ContentCommands.CreateCollection command);

    List<Collection> listCollections();

    Downloadable createDownloadable(ContentCommands.CreateDownloadable command);

    List<Downloadable> listDownloadables(String categoria);

    MoviePhrase createMoviePhrase(ContentCommands.CreateMoviePhrase command);

    List<MoviePhrase> listMoviePhrases(String movieSlug);
}
