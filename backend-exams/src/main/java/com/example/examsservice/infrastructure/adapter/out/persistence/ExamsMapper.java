package com.example.examsservice.infrastructure.adapter.out.persistence;

import com.example.examsservice.domain.exception.InvalidExamException;
import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Modalidad;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Locale;

/**
 * Explicit mapper between domain models and JPA entities.
 */
public final class ExamsMapper {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<List<String>> LIST = new TypeReference<>() { };

    private ExamsMapper() {
    }

    private static String toJson(final List<String> options) {
        try {
            return JSON.writeValueAsString(options);
        } catch (Exception ex) {
            throw new InvalidExamException("opciones cannot be serialised");
        }
    }

    private static List<String> fromJson(final String json) {
        try {
            return JSON.readValue(json, LIST);
        } catch (Exception ex) {
            throw new InvalidExamException("opciones cannot be read");
        }
    }

    /** @param e exam @return entity */
    public static ExamEntity toEntity(final Exam e) {
        return new ExamEntity(e.getId(), e.getCodigo(), e.getTitulo(), e.getFecha(), e.getNivel(),
                e.getDescripcion(), e.isPublicado(), e.getArticuloSlug(), e.getCreatedAt());
    }

    /** @param e entity @return exam */
    public static Exam toDomain(final ExamEntity e) {
        return new Exam(e.getId(), e.getCodigo(), e.getTitulo(), e.getFecha(), e.getNivel(),
                e.getDescripcion(), e.isPublicado(), e.getArticuloSlug(), e.getCreatedAt());
    }

    /** @param q question @return entity */
    public static QuestionEntity toEntity(final Question q) {
        return new QuestionEntity(q.getId(), q.getExamenId(), q.getOrden(), q.getEnunciado(),
                toJson(q.getOpciones()), q.getFuenteUrl());
    }

    /** @param e entity @return question */
    public static Question toDomain(final QuestionEntity e) {
        return new Question(e.getId(), e.getExamenId(), e.getOrden(), e.getEnunciado(),
                fromJson(e.getOpciones()), e.getFuenteUrl());
    }

    /** @param r result @return entity */
    public static ResultEntity toEntity(final Result r) {
        return new ResultEntity(r.getId(), r.getUsuarioId(), r.getExamenId(), r.getRespuestas(),
                r.getAciertos(), r.getTotal(), r.getNota(), r.getFecha());
    }

    /** @param e entity @return result */
    public static Result toDomain(final ResultEntity e) {
        return new Result(e.getId(), e.getUsuarioId(), e.getExamenId(), e.getRespuestas(), e.getAciertos(),
                e.getTotal(), e.getNota(), e.getFecha());
    }

    /** @param d diagnostic @return entity */
    public static DiagnosticEntity toEntity(final Diagnostic d) {
        return new DiagnosticEntity(d.getId(), d.getUsuarioId(), d.getAciertos(), d.getTotal(),
                d.getDesglose(), d.getCreadoEn());
    }

    /** @param e entity @return diagnostic */
    public static Diagnostic toDomain(final DiagnosticEntity e) {
        return new Diagnostic(e.getId(), e.getUsuarioId(), e.getAciertos(), e.getTotal(), e.getDesglose(),
                e.getCreadoEn());
    }

    /** @param s simulation @return entity */
    public static SimulationEntity toEntity(final Simulation s) {
        return new SimulationEntity(s.getId(), s.getUsuarioId(), s.getModalidad().name().toLowerCase(Locale.ROOT),
                s.getAciertos(), s.getTotal(), s.getRespuestas(), s.getDuracionSegundos(), s.getCreadoEn());
    }

    /** @param e entity @return simulation */
    public static Simulation toDomain(final SimulationEntity e) {
        return new Simulation(e.getId(), e.getUsuarioId(),
                Modalidad.valueOf(e.getModalidad().toUpperCase(Locale.ROOT)), e.getAciertos(), e.getTotal(),
                e.getRespuestas(), e.getDuracionSegundos(), e.getCreadoEn());
    }
}
