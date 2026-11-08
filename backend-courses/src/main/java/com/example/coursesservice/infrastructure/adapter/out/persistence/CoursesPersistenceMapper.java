package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.QuestionType;
import com.example.coursesservice.domain.model.Unit;

/**
 * Maps between domain models and JPA entities.
 */
public final class CoursesPersistenceMapper {

    private CoursesPersistenceMapper() {
    }

    public static Course toDomain(final CourseEntity e) {
        return new Course(e.getId(), e.getNombre(), e.getDescripcion(), e.isActivo(), e.getCreadoEn());
    }

    public static Unit toDomain(final UnitEntity e) {
        return new Unit(e.getId(), e.getSlug(), e.getTitulo(), e.getDescripcion(), e.getNivel(),
                e.getOrden(), e.isPublicado());
    }

    public static Activity toDomain(final ActivityEntity e) {
        return new Activity(e.getId(), e.getSlug(), e.getTitulo(), e.getDescripcion(),
                Destreza.fromValue(e.getDestreza()), e.getUnidadId(), e.getArticuloSlug(),
                e.getOrden(), e.isPublicado(), e.getCreadoEn());
    }

    public static ActivityQuestion toDomain(final ActivityQuestionEntity e) {
        return new ActivityQuestion(e.getId(), e.getActividadId(), e.getOrden(),
                QuestionType.fromValue(e.getTipo()), e.getEnunciado(), e.getConfiguracion(),
                e.getMedioTipo(), e.getMedioUrl(), e.getMedioAlt());
    }

    public static ActivityAttempt toDomain(final ActivityAttemptEntity e) {
        return new ActivityAttempt(e.getId(), e.getUsuarioId(), e.getActividadId(), e.getRespuestas(),
                e.getDetalle(), e.getAciertos(), e.getEvaluables(), e.getPendientesRevision(),
                e.getFecha());
    }
}
