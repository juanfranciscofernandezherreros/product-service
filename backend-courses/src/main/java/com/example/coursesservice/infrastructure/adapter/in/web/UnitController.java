package com.example.coursesservice.infrastructure.adapter.in.web;

import com.example.coursesservice.application.port.in.CoursesCommands;
import com.example.coursesservice.application.port.in.CoursesUseCase;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CreateUnitRequest;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.UnitResponse;
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
 * REST controller for course units.
 */
@RestController
@RequestMapping("/v1/courses/units")
public class UnitController {

    private final CoursesUseCase useCase;

    public UnitController(final CoursesUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<UnitResponse> create(@Valid @RequestBody final CreateUnitRequest r) {
        final var created = useCase.createUnit(new CoursesCommands.CreateUnit(
                r.slug(), r.titulo(), r.descripcion(), r.nivel(), r.orden(), r.publicado()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CoursesWebMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> list(
            @RequestParam(required = false) final String nivel,
            @RequestParam(required = false) final Boolean publicado) {
        return ResponseEntity.ok(useCase.listUnits(nivel, publicado)
                .stream().map(CoursesWebMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(CoursesWebMapper.toResponse(useCase.getUnit(id)));
    }
}
