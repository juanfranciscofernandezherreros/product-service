package com.example.coursesservice.infrastructure.adapter.in.web;

import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Unit;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.ActivityResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.AttemptResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.CourseResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.QuestionResponse;
import com.example.coursesservice.infrastructure.adapter.in.web.dto.CoursesDtos.UnitResponse;

/**
 * Maps domain models to web response DTOs.
 */
public final class CoursesWebMapper {

    private CoursesWebMapper() {
    }

    public static CourseResponse toResponse(final Course c) {
        return new CourseResponse(c.id(), c.nombre(), c.descripcion(), c.activo(), c.creadoEn());
    }

    public static UnitResponse toResponse(final Unit u) {
        return new UnitResponse(u.id(), u.slug(), u.titulo(), u.descripcion(), u.nivel(),
                u.orden(), u.publicado());
    }

    public static ActivityResponse toResponse(final Activity a) {
        return new ActivityResponse(a.id(), a.slug(), a.titulo(), a.descripcion(), a.destreza().value(),
                a.unidadId(), a.articuloSlug(), a.orden(), a.publicado(), a.creadoEn());
    }

    public static QuestionResponse toResponse(final ActivityQuestion q) {
        return new QuestionResponse(q.id(), q.actividadId(), q.orden(), q.tipo().value(), q.enunciado(),
                q.configuracion(), q.medioTipo(), q.medioUrl(), q.medioAlt());
    }

    public static AttemptResponse toResponse(final ActivityAttempt a) {
        return new AttemptResponse(a.id(), a.usuarioId(), a.actividadId(), a.respuestas(), a.detalle(),
                a.aciertos(), a.evaluables(), a.pendientesRevision(), a.fecha());
    }
}
