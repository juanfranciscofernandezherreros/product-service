package com.example.vocabularyservice.infrastructure.adapter.in.web;

import com.example.vocabularyservice.application.port.in.ItemCommands.CreateItemCommand;
import com.example.vocabularyservice.application.port.in.ItemCommands.ListItemsQuery;
import com.example.vocabularyservice.application.port.in.ItemCommands.UpdateItemCommand;
import com.example.vocabularyservice.application.port.in.PagedItems;
import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ItemRequest;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ItemResponse;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.ItemUpdateRequest;
import com.example.vocabularyservice.infrastructure.adapter.in.web.dto.VocabularyDtos.PagedItemsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for vocabulary items under {@code /v1/vocabulary/items}.
 */
@RestController
@RequestMapping("/v1/vocabulary/items")
@Validated
public class VocabularyItemController {

    private final VocabularyUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases vocabulary use cases
     */
    public VocabularyItemController(final VocabularyUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Creates an item.
     *
     * @param request payload
     * @return 201 with the created item
     */
    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody final ItemRequest request) {
        final ItemResponse response = VocabularyWebMapper.toResponse(useCases.createItem(
                new CreateItemCommand(request.en(), request.es(), request.example(), request.level())));
        return ResponseEntity.created(URI.create("/v1/vocabulary/items/" + response.id())).body(response);
    }

    /**
     * Lists items.
     *
     * @param level  optional level filter
     * @param limit  page size
     * @param offset results to skip
     * @return 200 with a paged envelope
     */
    @GetMapping
    public PagedItemsResponse list(
            @RequestParam(required = false) final Level level,
            @RequestParam(defaultValue = "20") @Min(1) final int limit,
            @RequestParam(defaultValue = "0") @Min(0) final int offset) {
        final PagedItems page = useCases.listItems(new ListItemsQuery(level, limit, offset));
        final List<ItemResponse> items = page.items().stream()
                .map(VocabularyWebMapper::toResponse)
                .collect(Collectors.toList());
        return new PagedItemsResponse(page.total(), items);
    }

    /**
     * Gets an item.
     *
     * @param id item id
     * @return 200 with the item
     */
    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable final long id) {
        return VocabularyWebMapper.toResponse(useCases.getItem(id));
    }

    /**
     * Updates an item.
     *
     * @param id      item id
     * @param request payload
     * @return 200 with the updated item
     */
    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable final long id, @Valid @RequestBody final ItemUpdateRequest request) {
        return VocabularyWebMapper.toResponse(useCases.updateItem(id,
                new UpdateItemCommand(request.es(), request.example(), request.level())));
    }

    /**
     * Deletes an item.
     *
     * @param id item id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        useCases.deleteItem(id);
    }
}
