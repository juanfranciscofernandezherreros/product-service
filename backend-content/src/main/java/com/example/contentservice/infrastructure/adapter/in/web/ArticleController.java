package com.example.contentservice.infrastructure.adapter.in.web;

import com.example.contentservice.application.port.in.ContentCommands;
import com.example.contentservice.application.port.in.ContentUseCase;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.ArticleResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CommentResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CreateArticleRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CreateCommentRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.PagedResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.UpdateArticleRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for articles and their comments.
 */
@RestController
@RequestMapping("/v1/content/articles")
public class ArticleController {

    private final ContentUseCase useCase;

    public ArticleController(final ContentUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody final CreateArticleRequest r) {
        final var created = useCase.createArticle(new ContentCommands.CreateArticle(
                r.ruta(), r.slug(), r.titulo(), r.descripcion(), r.html(), r.urlPublica(),
                r.categoriaSlug(), r.fechaPublicacion(), r.nivel()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ArticleResponse>> list(
            @RequestParam(required = false) final String categoriaSlug,
            @RequestParam(required = false) final String nivel,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final List<ArticleResponse> items = useCase.listArticles(categoriaSlug, nivel, offset, limit)
                .stream().map(ContentWebMapper::toResponse).toList();
        return ResponseEntity.ok(new PagedResponse<>(items,
                useCase.countArticles(categoriaSlug, nivel), offset, limit));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponse> get(@PathVariable final String slug) {
        return ResponseEntity.ok(ContentWebMapper.toResponse(useCase.getArticle(slug)));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ArticleResponse> update(@PathVariable final String slug,
                                                  @Valid @RequestBody final UpdateArticleRequest r) {
        final var updated = useCase.updateArticle(new ContentCommands.UpdateArticle(
                slug, r.titulo(), r.descripcion(), r.html(), r.categoriaSlug(), r.nivel()));
        return ResponseEntity.ok(ContentWebMapper.toResponse(updated));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable final String slug) {
        useCase.deleteArticle(slug);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<List<CommentResponse>> listComments(@PathVariable final String slug) {
        return ResponseEntity.ok(useCase.listComments(slug)
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable final String slug,
                                                         @Valid @RequestBody final CreateCommentRequest r) {
        final var created = useCase.createComment(new ContentCommands.CreateComment(
                slug, r.padreId(), r.autorId(), r.contenido()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }
}
