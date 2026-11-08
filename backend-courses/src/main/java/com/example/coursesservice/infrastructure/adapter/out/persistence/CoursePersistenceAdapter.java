package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.application.port.out.CoursesOutPorts.CoursePort;
import com.example.coursesservice.domain.model.Course;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing {@link CoursePort}.
 */
public class CoursePersistenceAdapter implements CoursePort {

    private final CourseJpaRepository repository;

    public CoursePersistenceAdapter(final CourseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Course save(final Course course) {
        final CourseEntity entity = new CourseEntity();
        entity.setId(course.id());
        entity.setNombre(course.nombre());
        entity.setDescripcion(course.descripcion());
        entity.setActivo(course.activo());
        entity.setCreadoEn(course.creadoEn() != null ? course.creadoEn() : OffsetDateTime.now());
        return CoursesPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Course> findById(final Long id) {
        return repository.findById(id).map(CoursesPersistenceMapper::toDomain);
    }

    @Override
    public List<Course> findAll(final Boolean activo) {
        final List<CourseEntity> entities = activo == null
                ? repository.findAllByOrderByCreadoEnDesc()
                : repository.findByActivoOrderByCreadoEnDesc(activo);
        return entities.stream().map(CoursesPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
