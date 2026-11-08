package com.example.vocabularyservice.infrastructure.adapter.in.web;

import com.example.vocabularyservice.application.port.in.UpsertProgressCommand;
import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ProgressResponse;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ProgressUpsertRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for vocabulary progress under {@code /v1/vocabulary/users/{userId}/progress}.
 */
@RestController
@RequestMapping("/v1/vocabulary/users/{userId}/progress")
public class VocabularyProgressController {

    private final VocabularyUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases vocabulary use cases
     */
    public VocabularyProgressController(final VocabularyUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Lists a user's progress.
     *
     * @param userId user id
     * @return 200 with the progress list
     */
    @GetMapping
    public List<ProgressResponse> list(@PathVariable final UUID userId) {
        return useCases.listProgress(userId).stream()
                .map(VocabularyWebMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Creates or updates a user's progress on an item.
     *
     * @param userId  user id
     * @param request payload
     * @return 200 with the stored progress
     */
    @PutMapping
    public ProgressResponse upsert(@PathVariable final UUID userId,
                                   @Valid @RequestBody final ProgressUpsertRequest request) {
        return VocabularyWebMapper.toResponse(useCases.upsertProgress(new UpsertProgressCommand(
                userId, request.itemId(), request.streak(), request.lastGrade(), request.nextReviewAt())));
    }
}
