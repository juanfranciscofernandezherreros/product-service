package com.example.contentservice.infrastructure.adapter.in.web;

import com.example.contentservice.application.port.in.ContentCommands;
import com.example.contentservice.application.port.in.ContentUseCase;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CategoryRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CategoryResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CollectionResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CreateCollectionRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CreateDownloadableRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.CreateMoviePhraseRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.DownloadableResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.MoviePhraseResponse;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.PageRequest;
import com.example.contentservice.infrastructure.adapter.in.web.dto.ContentDtos.PageResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for categories, pages, collections, downloadables and movie phrases.
 */
@RestController
@RequestMapping("/v1/content")
public class CatalogController {

    private final ContentUseCase useCase;

    public CatalogController(final ContentUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        return ResponseEntity.ok(useCase.listCategories()
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody final CategoryRequest r) {
        final var created = useCase.createCategory(new ContentCommands.CreateCategory(
                r.slug(), r.nombre(), r.descripcion(), r.orden()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }

    @GetMapping("/pages")
    public ResponseEntity<List<PageResponseDto>> listPages() {
        return ResponseEntity.ok(useCase.listPages()
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/pages")
    public ResponseEntity<PageResponseDto> createPage(@Valid @RequestBody final PageRequest r) {
        final var created = useCase.createPage(new ContentCommands.CreatePage(
                r.ruta(), r.titulo(), r.descripcion(), r.html(), r.urlPublica()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }

    @GetMapping("/collections")
    public ResponseEntity<List<CollectionResponse>> listCollections() {
        return ResponseEntity.ok(useCase.listCollections()
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/collections")
    public ResponseEntity<CollectionResponse> createCollection(
            @Valid @RequestBody final CreateCollectionRequest r) {
        final var created = useCase.createCollection(new ContentCommands.CreateCollection(
                r.slug(), r.titulo(), r.descripcion(), r.tipo(), r.incluirTodosArticulos(),
                r.nombreArchivo(), r.fechaPublicacion()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }

    @GetMapping("/downloadables")
    public ResponseEntity<List<DownloadableResponse>> listDownloadables(
            @RequestParam(required = false) final String categoria) {
        return ResponseEntity.ok(useCase.listDownloadables(categoria)
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/downloadables")
    public ResponseEntity<DownloadableResponse> createDownloadable(
            @Valid @RequestBody final CreateDownloadableRequest r) {
        final var created = useCase.createDownloadable(new ContentCommands.CreateDownloadable(
                r.slug(), r.titulo(), r.descripcion(), r.categoria(), r.fechaPublicacion(),
                r.urlArchivo(), r.urlLectura()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }

    @GetMapping("/movie-phrases")
    public ResponseEntity<List<MoviePhraseResponse>> listMoviePhrases(
            @RequestParam(required = false) final String movieSlug) {
        return ResponseEntity.ok(useCase.listMoviePhrases(movieSlug)
                .stream().map(ContentWebMapper::toResponse).toList());
    }

    @PostMapping("/movie-phrases")
    public ResponseEntity<MoviePhraseResponse> createMoviePhrase(
            @Valid @RequestBody final CreateMoviePhraseRequest r) {
        final var created = useCase.createMoviePhrase(new ContentCommands.CreateMoviePhrase(
                r.movieSlug(), r.movieTitle(), r.subtitleNumber(), r.timecode(), r.phrase(),
                r.tenseLabels(), r.notes()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentWebMapper.toResponse(created));
    }
}
