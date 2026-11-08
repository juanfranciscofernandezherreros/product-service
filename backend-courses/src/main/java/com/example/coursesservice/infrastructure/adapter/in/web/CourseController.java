package com.example.coursesservice.infrastructure.adapter.in.web;

import com.example.coursesservice.application.port.in.CoursesCommands;
import com.example.coursesservice.application.port.in.CoursesUseCase;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CourseRequest;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CourseResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for courses.
 */
@RestController
@RequestMapping("/v1/courses")
public class CourseController {

    private final CoursesUseCase useCase;

    public CourseController(final CoursesUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody final CourseRequest r) {
        final var created = useCase.createCourse(
                new CoursesCommands.SaveCourse(null, r.nombre(), r.descripcion(), r.activo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(CoursesWebMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> list(
            @RequestParam(required = false) final Boolean activo) {
        return ResponseEntity.ok(useCase.listCourses(activo)
                .stream().map(CoursesWebMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(CoursesWebMapper.toResponse(useCase.getCourse(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable final Long id,
                                                 @Valid @RequestBody final CourseRequest r) {
        final var updated = useCase.updateCourse(
                new CoursesCommands.SaveCourse(id, r.nombre(), r.descripcion(), r.activo()));
        return ResponseEntity.ok(CoursesWebMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        useCase.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
