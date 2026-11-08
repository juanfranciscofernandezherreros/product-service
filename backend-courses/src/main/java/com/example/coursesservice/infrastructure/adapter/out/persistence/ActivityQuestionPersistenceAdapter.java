package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityQuestionPort;
import com.example.coursesservice.domain.model.ActivityQuestion;

import java.util.List;

/**
 * Persistence adapter implementing {@link ActivityQuestionPort}.
 */
public class ActivityQuestionPersistenceAdapter implements ActivityQuestionPort {

    private final ActivityQuestionJpaRepository repository;

    public ActivityQuestionPersistenceAdapter(final ActivityQuestionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActivityQuestion save(final ActivityQuestion question) {
        final ActivityQuestionEntity entity = new ActivityQuestionEntity();
        entity.setId(question.id());
        entity.setActividadId(question.actividadId());
        entity.setOrden(question.orden());
        entity.setTipo(question.tipo().value());
        entity.setEnunciado(question.enunciado());
        entity.setConfiguracion(question.configuracion());
        entity.setMedioTipo(question.medioTipo());
        entity.setMedioUrl(question.medioUrl());
        entity.setMedioAlt(question.medioAlt());
        return CoursesPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existsByActivityAndOrden(final Long actividadId, final int orden) {
        return repository.existsByActividadIdAndOrden(actividadId, orden);
    }

    @Override
    public List<ActivityQuestion> findByActivity(final Long actividadId) {
        return repository.findByActividadIdOrderByOrdenAsc(actividadId)
                .stream().map(CoursesPersistenceMapper::toDomain).toList();
    }
}
