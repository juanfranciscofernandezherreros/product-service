package com.example.miscservice.infrastructure.adapter.in.web;

import com.example.miscservice.application.port.in.MiscCommands;
import com.example.miscservice.application.port.in.MiscUseCase;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.CreateSuggestionRequest;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.PageResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.SuggestionResponse;
import com.example.miscservice.infrastructure.adapter.in.web.dto.MiscDtos.UpdateSuggestionStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for suggestions.
 */
@RestController
@RequestMapping("/v1/suggestions")
public class SuggestionController {

    private final MiscUseCase useCase;

    public SuggestionController(final MiscUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<SuggestionResponse> create(@Valid @RequestBody final CreateSuggestionRequest request) {
        final var created = useCase.createSuggestion(
                new MiscCommands.CreateSuggestion(request.titulo(), request.descripcion()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WebMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuggestionResponse> getById(@PathVariable final Long id) {
        return ResponseEntity.ok(WebMapper.toResponse(useCase.getSuggestion(id)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SuggestionResponse>> list(
            @RequestParam(required = false) final String estado,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final SuggestionStatus status = estado == null ? null : SuggestionStatus.fromValue(estado);
        final List<SuggestionResponse> items = useCase.listSuggestions(status, offset, limit)
                .stream().map(WebMapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(items, useCase.countSuggestions(status), offset, limit));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SuggestionResponse> updateStatus(
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateSuggestionStatusRequest request) {
        final var updated = useCase.updateSuggestionStatus(
                new MiscCommands.UpdateSuggestionStatus(id, request.estado()));
        return ResponseEntity.ok(WebMapper.toResponse(updated));
    }
}
