package com.example.examsservice.infrastructure.adapter.in.web;

import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.DiagnosticResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ExamResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.QuestionResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ResultResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.SimulationResponse;

/**
 * Maps domain models to web responses.
 */
public final class ExamsWebMapper {

    private ExamsWebMapper() {
    }

    /** @param e exam @return response */
    public static ExamResponse toResponse(final Exam e) {
        return new ExamResponse(e.getId(), e.getCodigo(), e.getTitulo(), e.getFecha(), e.getNivel(),
                e.getDescripcion(), e.isPublicado(), e.getArticuloSlug(), e.getCreatedAt());
    }

    /** @param q question @return response */
    public static QuestionResponse toResponse(final Question q) {
        return new QuestionResponse(q.getId(), q.getExamenId(), q.getOrden(), q.getEnunciado(),
                q.getOpciones(), q.getFuenteUrl());
    }

    /** @param r result @return response */
    public static ResultResponse toResponse(final Result r) {
        return new ResultResponse(r.getId(), r.getUsuarioId(), r.getExamenId(), r.getRespuestas(),
                r.getAciertos(), r.getTotal(), r.getNota(), r.getFecha());
    }

    /** @param d diagnostic @return response */
    public static DiagnosticResponse toResponse(final Diagnostic d) {
        return new DiagnosticResponse(d.getId(), d.getUsuarioId(), d.getAciertos(), d.getTotal(),
                d.getDesglose(), d.getCreadoEn());
    }

    /** @param s simulation @return response */
    public static SimulationResponse toResponse(final Simulation s) {
        return new SimulationResponse(s.getId(), s.getUsuarioId(), s.getModalidad(), s.getAciertos(),
                s.getTotal(), s.getRespuestas(), s.getDuracionSegundos(), s.getCreadoEn());
    }
}
