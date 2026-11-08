package com.example.miscservice.infrastructure.adapter.in.web;

import com.example.miscservice.application.port.in.MiscCommands;
import com.example.miscservice.application.port.in.MiscUseCase;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CreateDraftRequest;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.DraftResponse;
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
 * REST controller for per-user writing drafts.
 */
@RestController
@RequestMapping("/v1/users/{userId}/writing-drafts")
public class WritingDraftController {

    private final MiscUseCase useCase;

    public WritingDraftController(final MiscUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<DraftResponse> create(
            @PathVariable final UUID userId,
            @Valid @RequestBody final CreateDraftRequest request) {
        final var created = useCase.createDraft(new MiscCommands.CreateWritingDraft(
                userId, request.tipo(), request.titulo(), request.contenido()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WebMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DraftResponse>> list(
            @PathVariable final UUID userId,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final List<DraftResponse> items = useCase.listDrafts(userId, offset, limit)
                .stream().map(WebMapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(items, useCase.countDrafts(userId), offset, limit));
    }
}
