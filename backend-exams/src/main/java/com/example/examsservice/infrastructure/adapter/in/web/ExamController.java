package com.example.examsservice.infrastructure.adapter.in.web;

import com.example.examsservice.application.port.in.ExamsCommands.AddQuestionCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateExamCommand;
import com.example.examsservice.application.port.in.ExamsCommands.ListExamsQuery;
import com.example.examsservice.application.port.in.ExamsCommands.PagedExams;
import com.example.examsservice.application.port.in.ExamsCommands.UpdateExamCommand;
import com.example.examsservice.application.port.in.ExamsUseCases;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ExamRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ExamResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ExamUpdateRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.PagedExamsResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.QuestionRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.QuestionResponse;
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
 * REST adapter for exams and their questions under {@code /v1/exams}.
 */
@RestController
@RequestMapping("/v1/exams")
@Validated
public class ExamController {

    private final ExamsUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases exams use cases
     */
    public ExamController(final ExamsUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Creates an exam.
     *
     * @param request payload
     * @return 201 with the exam
     */
    @PostMapping
    public ResponseEntity<ExamResponse> create(@Valid @RequestBody final ExamRequest request) {
        final ExamResponse response = ExamsWebMapper.toResponse(useCases.createExam(new CreateExamCommand(
                request.codigo(), request.titulo(), request.fecha(), request.nivel(), request.descripcion(),
                request.publicado(), request.articuloSlug())));
        return ResponseEntity.created(URI.create("/v1/exams/" + response.id())).body(response);
    }

    /**
     * Lists exams.
     *
     * @param nivel     optional level
     * @param publicado optional published
     * @param limit     page size
     * @param offset    results to skip
     * @return 200 with a paged envelope
     */
    @GetMapping
    public PagedExamsResponse list(
            @RequestParam(required = false) final String nivel,
            @RequestParam(required = false) final Boolean publicado,
            @RequestParam(defaultValue = "20") @Min(1) final int limit,
            @RequestParam(defaultValue = "0") @Min(0) final int offset) {
        final PagedExams page = useCases.listExams(new ListExamsQuery(nivel, publicado, limit, offset));
        final List<ExamResponse> items = page.items().stream()
                .map(ExamsWebMapper::toResponse).collect(Collectors.toList());
        return new PagedExamsResponse(page.total(), items);
    }

    /**
     * Gets an exam.
     *
     * @param id id
     * @return 200 with the exam
     */
    @GetMapping("/{id}")
    public ExamResponse get(@PathVariable final long id) {
        return ExamsWebMapper.toResponse(useCases.getExam(id));
    }

    /**
     * Updates an exam.
     *
     * @param id      id
     * @param request payload
     * @return 200 with the exam
     */
    @PutMapping("/{id}")
    public ExamResponse update(@PathVariable final long id, @Valid @RequestBody final ExamUpdateRequest request) {
        return ExamsWebMapper.toResponse(useCases.updateExam(id, new UpdateExamCommand(request.titulo(),
                request.fecha(), request.nivel(), request.descripcion(), request.publicado(),
                request.articuloSlug())));
    }

    /**
     * Deletes an exam.
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        useCases.deleteExam(id);
    }

    /**
     * Lists an exam's questions.
     *
     * @param id exam id
     * @return 200 with questions
     */
    @GetMapping("/{id}/questions")
    public List<QuestionResponse> listQuestions(@PathVariable final long id) {
        return useCases.listQuestions(id).stream()
                .map(ExamsWebMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Adds a question to an exam.
     *
     * @param id      exam id
     * @param request payload
     * @return 201 with the question
     */
    @PostMapping("/{id}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(@PathVariable final long id,
                                                        @Valid @RequestBody final QuestionRequest request) {
        final QuestionResponse response = ExamsWebMapper.toResponse(useCases.addQuestion(id,
                new AddQuestionCommand(request.orden(), request.enunciado(), request.opciones(),
                        request.fuenteUrl())));
        return ResponseEntity.created(URI.create("/v1/exams/" + id + "/questions/" + response.id()))
                .body(response);
    }
}
