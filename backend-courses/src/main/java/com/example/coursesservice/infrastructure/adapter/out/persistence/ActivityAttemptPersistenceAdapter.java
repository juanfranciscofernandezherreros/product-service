package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityAttemptPort;
import com.example.coursesservice.domain.model.ActivityAttempt;

import java.time.OffsetDateTime;

/**
 * Persistence adapter implementing {@link ActivityAttemptPort}.
 */
public class ActivityAttemptPersistenceAdapter implements ActivityAttemptPort {

    private final ActivityAttemptJpaRepository repository;

    public ActivityAttemptPersistenceAdapter(final ActivityAttemptJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActivityAttempt save(final ActivityAttempt attempt) {
        final ActivityAttemptEntity entity = new ActivityAttemptEntity();
        entity.setId(attempt.id());
        entity.setUsuarioId(attempt.usuarioId());
        entity.setActividadId(attempt.actividadId());
        entity.setRespuestas(attempt.respuestas());
        entity.setDetalle(attempt.detalle());
        entity.setAciertos(attempt.aciertos());
        entity.setEvaluables(attempt.evaluables());
        entity.setPendientesRevision(attempt.pendientesRevision());
        entity.setFecha(attempt.fecha() != null ? attempt.fecha() : OffsetDateTime.now());
        return CoursesPersistenceMapper.toDomain(repository.save(entity));
    }
}
