package com.example.miscservice.infrastructure.adapter.in.web;

import com.example.miscservice.application.port.in.MiscCommands;
import com.example.miscservice.application.port.in.MiscUseCase;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CategoryResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CreateCategoryRequest;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CreateNoteRequest;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.NoteResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for the per-user logbook (categories and notes).
 */
@RestController
@RequestMapping("/v1/users/{userId}/logbook")
public class LogbookController {

    private final MiscUseCase useCase;

    public LogbookController(final MiscUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @PathVariable final UUID userId,
            @Valid @RequestBody final CreateCategoryRequest request) {
        final var created = useCase.createCategory(
                new MiscCommands.CreateLogbookCategory(userId, request.nombre()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WebMapper.toResponse(created));
    }

    @GetMapping("/categories")
    public ResponseEntity<PageResponse<CategoryResponse>> listCategories(
            @PathVariable final UUID userId,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final List<CategoryResponse> items = useCase.listCategories(userId, offset, limit)
                .stream().map(WebMapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(items, useCase.countCategories(userId), offset, limit));
    }

    @PostMapping("/notes")
    public ResponseEntity<NoteResponse> createNote(
            @PathVariable final UUID userId,
            @Valid @RequestBody final CreateNoteRequest request) {
        final var created = useCase.createNote(
                new MiscCommands.CreateLogbookNote(userId, request.categoriaId(), request.contenido()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WebMapper.toResponse(created));
    }

    @GetMapping("/notes")
    public ResponseEntity<PageResponse<NoteResponse>> listNotes(
            @PathVariable final UUID userId,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final List<NoteResponse> items = useCase.listNotes(userId, offset, limit)
                .stream().map(WebMapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(items, useCase.countNotes(userId), offset, limit));
    }
}
