package com.example.coursesservice.infrastructure.adapter.in.web;

import com.example.coursesservice.application.port.in.CoursesCommands;
import com.example.coursesservice.application.port.in.CoursesUseCase;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.ActivityResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.AttemptResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CreateActivityRequest;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CreateAttemptRequest;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CreateQuestionRequest;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.PagedResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.QuestionResponse;
import com.fasterxml.jackson.databind.JsonNode;
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

/**
 * REST controller for activities, their questions and attempts.
 */
@RestController
@RequestMapping("/v1/courses/activities")
public class ActivityController {

    private final CoursesUseCase useCase;

    public ActivityController(final CoursesUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> create(@Valid @RequestBody final CreateActivityRequest r) {
        final var created = useCase.createActivity(new CoursesCommands.CreateActivity(
                r.slug(), r.titulo(), r.descripcion(), r.destreza(), r.unidadId(), r.articuloSlug(),
                r.orden(), r.publicado()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CoursesWebMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ActivityResponse>> list(
            @RequestParam(required = false) final String destreza,
            @RequestParam(required = false) final Boolean publicado,
            @RequestParam(defaultValue = "0") final int offset,
            @RequestParam(defaultValue = "20") final int limit) {
        final Destreza d = destreza == null ? null : Destreza.fromValue(destreza);
        final List<ActivityResponse> items = useCase.listActivities(d, publicado, offset, limit)
                .stream().map(CoursesWebMapper::toResponse).toList();
        return ResponseEntity.ok(new PagedResponse<>(items,
                useCase.countActivities(d, publicado), offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(CoursesWebMapper.toResponse(useCase.getActivity(id)));
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionResponse>> listQuestions(@PathVariable final Long id) {
        return ResponseEntity.ok(useCase.listQuestions(id)
                .stream().map(CoursesWebMapper::toResponse).toList());
    }

    @PostMapping("/{id}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(@PathVariable final Long id,
                                                        @Valid @RequestBody final CreateQuestionRequest r) {
        final var created = useCase.addQuestion(new CoursesCommands.CreateQuestion(
                id, r.orden(), r.tipo(), r.enunciado(), toJson(r.configuracion()),
                r.medioTipo(), r.medioUrl(), r.medioAlt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CoursesWebMapper.toResponse(created));
    }

    @PostMapping("/{id}/attempts")
    public ResponseEntity<AttemptResponse> createAttempt(@PathVariable final Long id,
                                                         @Valid @RequestBody final CreateAttemptRequest r) {
        final var created = useCase.createAttempt(new CoursesCommands.CreateAttempt(
                id, r.usuarioId(), r.respuestas().toString(), r.detalle().toString(),
                r.aciertos(), r.evaluables(), r.pendientesRevision()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CoursesWebMapper.toResponse(created));
    }

    private static String toJson(final JsonNode node) {
        return node == null ? null : node.toString();
    }
}
