package com.example.examsservice.application.port.out;

import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound ports for the exams domain.
 */
public final class ExamsOutPorts {

    private ExamsOutPorts() {
    }

    /** Exam port. */
    public interface ExamPort {
        /** @param exam exam @return stored exam */
        Exam save(Exam exam);

        /** @param id id @return the exam if present */
        Optional<Exam> findById(long id);

        /** @param id id @return {@code true} if present */
        boolean existsById(long id);

        /** @param codigo code @return {@code true} if taken */
        boolean existsByCodigo(String codigo);

        /** @param nivel level @param publicado published @param limit limit @param offset offset @return exams */
        List<Exam> findByFilter(String nivel, Boolean publicado, int limit, int offset);

        /** @param nivel level @param publicado published @return count */
        long countByFilter(String nivel, Boolean publicado);

        /** @param id id */
        void deleteById(long id);
    }

    /** Question port. */
    public interface QuestionPort {
        /** @param question question @return stored question */
        Question save(Question question);

        /** @param examenId exam id @return the questions */
        List<Question> findByExam(long examenId);

        /** @param examenId exam id @param orden order @return {@code true} if present */
        boolean existsByExamAndOrden(long examenId, int orden);
    }

    /** Result port. */
    public interface ResultPort {
        /** @param result result @return stored result */
        Result save(Result result);

        /** @param usuarioId user id @return the results */
        List<Result> findByUser(UUID usuarioId);
    }

    /** Diagnostic port. */
    public interface DiagnosticPort {
        /** @param diagnostic diagnostic @return stored diagnostic */
        Diagnostic save(Diagnostic diagnostic);
    }

    /** Simulation port. */
    public interface SimulationPort {
        /** @param simulation simulation @return stored simulation */
        Simulation save(Simulation simulation);
    }
}
