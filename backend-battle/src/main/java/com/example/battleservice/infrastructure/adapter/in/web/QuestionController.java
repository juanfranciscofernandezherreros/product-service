package com.example.battleservice.infrastructure.adapter.in.web;

import com.example.battleservice.application.port.in.BattleCommands.ListQuestionsQuery;
import com.example.battleservice.application.port.in.BattleCommands.PagedQuestions;
import com.example.battleservice.application.port.in.BattleCommands.QuestionCommand;
import com.example.battleservice.application.port.in.BattleUseCases;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.PagedQuestionsResponse;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.QuestionRequest;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.QuestionResponse;
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
 * REST adapter for battle questions under {@code /v1/battle/questions}.
 */
@RestController
@RequestMapping("/v1/battle/questions")
@Validated
public class QuestionController {

    private final BattleUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases battle use cases
     */
    public QuestionController(final BattleUseCases useCases) {
        this.useCases = useCases;
    }

    private QuestionCommand toCommand(final QuestionRequest r) {
        return new QuestionCommand(r.level(), r.topic(), r.prompt(), r.options(),
                r.correctIndex(), r.explanation());
    }

    /**
     * Creates a question.
     *
     * @param request payload
     * @return 201 with the question
     */
    @PostMapping
    public ResponseEntity<QuestionResponse> create(@Valid @RequestBody final QuestionRequest request) {
        final QuestionResponse response = BattleWebMapper.toResponse(useCases.createQuestion(toCommand(request)));
        return ResponseEntity.created(URI.create("/v1/battle/questions/" + response.id())).body(response);
    }

    /**
     * Lists questions.
     *
     * @param level  optional level
     * @param topic  optional topic
     * @param limit  page size
     * @param offset results to skip
     * @return 200 with a paged envelope
     */
    @GetMapping
    public PagedQuestionsResponse list(
            @RequestParam(required = false) final Level level,
            @RequestParam(required = false) final QuestionTopic topic,
            @RequestParam(defaultValue = "20") @Min(1) final int limit,
            @RequestParam(defaultValue = "0") @Min(0) final int offset) {
        final PagedQuestions page = useCases.listQuestions(new ListQuestionsQuery(level, topic, limit, offset));
        final List<QuestionResponse> items = page.items().stream()
                .map(BattleWebMapper::toResponse).collect(Collectors.toList());
        return new PagedQuestionsResponse(page.total(), items);
    }

    /**
     * Gets a question.
     *
     * @param id id
     * @return 200 with the question
     */
    @GetMapping("/{id}")
    public QuestionResponse get(@PathVariable final long id) {
        return BattleWebMapper.toResponse(useCases.getQuestion(id));
    }

    /**
     * Updates a question.
     *
     * @param id      id
     * @param request payload
     * @return 200 with the updated question
     */
    @PutMapping("/{id}")
    public QuestionResponse update(@PathVariable final long id, @Valid @RequestBody final QuestionRequest request) {
        return BattleWebMapper.toResponse(useCases.updateQuestion(id, toCommand(request)));
    }

    /**
     * Deletes a question.
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        useCases.deleteQuestion(id);
    }
}
