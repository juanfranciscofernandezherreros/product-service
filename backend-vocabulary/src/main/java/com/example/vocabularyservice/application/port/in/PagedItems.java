package com.example.vocabularyservice.application.port.in;

import com.example.vocabularyservice.domain.model.VocabularyItem;
import java.util.List;

/**
 * A page of vocabulary items with the total count.
 *
 * @param total total matching items
 * @param items items in this page
 */
public record PagedItems(long total, List<VocabularyItem> items) {
}
