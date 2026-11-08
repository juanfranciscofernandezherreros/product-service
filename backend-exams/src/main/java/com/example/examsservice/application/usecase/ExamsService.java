package com.example.examsservice.application.usecase;

import com.example.examsservice.application.port.in.ExamsCommands.AddQuestionCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateDiagnosticCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateExamCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateResultCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateSimulationCommand;
import com.example.examsservice.application.port.in.ExamsCommands.ListExamsQuery;
import com.example.examsservice.application.port.in.ExamsCommands.PagedExams;
import com.example.examsservice.application.port.in.ExamsCommands.UpdateExamCommand;
import com.example.examsservice.application.port.in.ExamsUseCases;
import com.example.examsservice.application.port.out.ExamsOutPorts.DiagnosticPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ExamPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.QuestionPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ResultPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.SimulationPort;
import com.example.examsservice.domain.exception.DuplicateResourceException;
import com.example.examsservice.domain.exception.ResourceNotFoundException;
import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the exams use cases.
 */
public class ExamsService implements ExamsUseCases {

    private final ExamPort examPort;
    private final QuestionPort questionPort;
    private final ResultPort resultPort;
    private final DiagnosticPort diagnosticPort;
    private final SimulationPort simulationPort;

    /**
     * Creates the service.
     *
     * @param examPort       exam port
     * @param questionPort   question port
     * @param resultPort     result port
     * @param diagnosticPort diagnostic port
     * @param simulationPort simulation port
     */
    public ExamsService(final ExamPort examPort, final QuestionPort questionPort, final ResultPort resultPort,
                        final DiagnosticPort diagnosticPort, final SimulationPort simulationPort) {
        this.examPort = examPort;
        this.questionPort = questionPort;
        this.resultPort = resultPort;
        this.diagnosticPort = diagnosticPort;
        this.simulationPort = simulationPort;
    }

    @Override
    public Exam createExam(final CreateExamCommand c) {
        if (c.codigo() != null && examPort.existsByCodigo(c.codigo().trim())) {
            throw new DuplicateResourceException("Exam code already exists: " + c.codigo());
        }
        return examPort.save(Exam.create(c.codigo(), c.titulo(), c.fecha(), c.nivel(), c.descripcion(),
                c.publicado(), c.articuloSlug()));
    }

    @Override
    public Exam getExam(final long id) {
        return examPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam", String.valueOf(id)));
    }

    @Override
    public PagedExams listExams(final ListExamsQuery q) {
        final List<Exam> items = examPort.findByFilter(q.nivel(), q.publicado(), q.limit(), q.offset());
        return new PagedExams(examPort.countByFilter(q.nivel(), q.publicado()), items);
    }

    @Override
    public Exam updateExam(final long id, final UpdateExamCommand c) {
        final Exam existing = examPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", String.valueOf(id)));
        return examPort.save(existing.withUpdate(c.titulo(), c.fecha(), c.nivel(), c.descripcion(),
                c.publicado(), c.articuloSlug()));
    }

    @Override
    public void deleteExam(final long id) {
        if (!examPort.existsById(id)) {
            throw new ResourceNotFoundException("Exam", String.valueOf(id));
        }
        examPort.deleteById(id);
    }

    @Override
    public List<Question> listQuestions(final long examenId) {
        if (!examPort.existsById(examenId)) {
            throw new ResourceNotFoundException("Exam", String.valueOf(examenId));
        }
        return questionPort.findByExam(examenId);
    }

    @Override
    public Question addQuestion(final long examenId, final AddQuestionCommand c) {
        if (!examPort.existsById(examenId)) {
            throw new ResourceNotFoundException("Exam", String.valueOf(examenId));
        }
        if (questionPort.existsByExamAndOrden(examenId, c.orden())) {
            throw new DuplicateResourceException("Question order already exists for exam");
        }
        return questionPort.save(new Question(null, examenId, c.orden(), c.enunciado(), c.opciones(),
                c.fuenteUrl()));
    }

    @Override
    public Result createResult(final CreateResultCommand c) {
        if (!examPort.existsById(c.examenId())) {
            throw new ResourceNotFoundException("Exam", String.valueOf(c.examenId()));
        }
        return resultPort.save(Result.record(c.usuarioId(), c.examenId(), c.respuestas(), c.aciertos(),
                c.total(), c.nota()));
    }

    @Override
    public List<Result> listUserResults(final UUID usuarioId) {
        return resultPort.findByUser(usuarioId);
    }

    @Override
    public Diagnostic createDiagnostic(final CreateDiagnosticCommand c) {
        return diagnosticPort.save(Diagnostic.record(c.usuarioId(), c.aciertos(), c.total(), c.desglose()));
    }

    @Override
    public Simulation createSimulation(final CreateSimulationCommand c) {
        return simulationPort.save(Simulation.record(c.usuarioId(), c.modalidad(), c.aciertos(), c.total(),
                c.respuestas(), c.duracionSegundos()));
    }
}
