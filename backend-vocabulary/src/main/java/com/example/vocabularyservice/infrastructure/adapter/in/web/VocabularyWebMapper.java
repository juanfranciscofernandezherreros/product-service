package com.example.vocabularyservice.infrastructure.adapter.in.web;

import com.example.vocabularyservice.domain.model.VocabularyItem;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ItemResponse;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ProgressResponse;

/**
 * Maps domain models to web responses.
 */
public final class VocabularyWebMapper {

    private VocabularyWebMapper() {
    }

    /**
     * Maps an item to its response.
     *
     * @param item domain item
     * @return the response
     */
    public static ItemResponse toResponse(final VocabularyItem item) {
        return new ItemResponse(item.getId(), item.getEn(), item.getEs(), item.getExample(), item.getLevel());
    }

    /**
     * Maps a progress record to its response.
     *
     * @param progress domain progress
     * @return the response
     */
    public static ProgressResponse toResponse(final VocabularyProgress progress) {
        return new ProgressResponse(progress.getUserId(), progress.getItemId(), progress.getStreak(),
                progress.getLastGrade(), progress.getNextReviewAt(), progress.getFirstSeenDay());
    }
}
