package com.example.examsservice.application.port.in;

import com.example.examsservice.application.port.in.ExamsCommands.AddQuestionCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateDiagnosticCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateExamCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateResultCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateSimulationCommand;
import com.example.examsservice.application.port.in.ExamsCommands.ListExamsQuery;
import com.example.examsservice.application.port.in.ExamsCommands.PagedExams;
import com.example.examsservice.application.port.in.ExamsCommands.UpdateExamCommand;
import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the exams use cases.
 */
public interface ExamsUseCases {

    /** @param command attributes @return created exam */
    Exam createExam(CreateExamCommand command);

    /** @param id id @return the exam */
    Exam getExam(long id);

    /** @param query pagination/filter @return a page */
    PagedExams listExams(ListExamsQuery query);

    /** @param id id @param command attributes @return updated exam */
    Exam updateExam(long id, UpdateExamCommand command);

    /** @param id id */
    void deleteExam(long id);

    /** @param examenId exam id @return the questions */
    List<Question> listQuestions(long examenId);

    /** @param examenId exam id @param command attributes @return the question */
    Question addQuestion(long examenId, AddQuestionCommand command);

    /** @param command attributes @return the result */
    Result createResult(CreateResultCommand command);

    /** @param usuarioId user id @return the results */
    List<Result> listUserResults(UUID usuarioId);

    /** @param command attributes @return the diagnostic */
    Diagnostic createDiagnostic(CreateDiagnosticCommand command);

    /** @param command attributes @return the simulation */
    Simulation createSimulation(CreateSimulationCommand command);
}
