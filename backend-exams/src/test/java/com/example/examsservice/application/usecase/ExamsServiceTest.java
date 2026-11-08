package com.example.examsservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.examsservice.application.port.in.ExamsCommands.AddQuestionCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateDiagnosticCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateExamCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateResultCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateSimulationCommand;
import com.example.examsservice.application.port.in.ExamsCommands.ListExamsQuery;
import com.example.examsservice.application.port.in.ExamsCommands.PagedExams;
import com.example.examsservice.application.port.in.ExamsCommands.UpdateExamCommand;
import com.example.examsservice.application.port.out.ExamsOutPorts.DiagnosticPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ExamPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.QuestionPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ResultPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.SimulationPort;
import com.example.examsservice.domain.exception.DuplicateResourceException;
import com.example.examsservice.domain.exception.ResourceNotFoundException;
import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Modalidad;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ExamsService}.
 */
@ExtendWith(MockitoExtension.class)
class ExamsServiceTest {

    @Mock
    private ExamPort examPort;
    @Mock
    private QuestionPort questionPort;
    @Mock
    private ResultPort resultPort;
    @Mock
    private DiagnosticPort diagnosticPort;
    @Mock
    private SimulationPort simulationPort;

    private ExamsService service;

    @BeforeEach
    void setUp() {
        service = new ExamsService(examPort, questionPort, resultPort, diagnosticPort, simulationPort);
    }

    private CreateExamCommand examCmd() {
        return new CreateExamCommand("EX-1", "Titulo", null, null, null, true, null);
    }

    @Test
    void createExamOkAndDuplicate() {
        when(examPort.existsByCodigo("EX-1")).thenReturn(false);
        when(examPort.save(any(Exam.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createExam(examCmd()).getCodigo()).isEqualTo("EX-1");
        when(examPort.existsByCodigo("EX-1")).thenReturn(true);
        assertThatThrownBy(() -> service.createExam(examCmd())).isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void getAndUpdateAndDelete() {
        final Exam e = Exam.create("EX-1", "T", null, null, null, false, null);
        when(examPort.findById(1L)).thenReturn(Optional.of(e));
        assertThat(service.getExam(1L).getCodigo()).isEqualTo("EX-1");
        when(examPort.save(any(Exam.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.updateExam(1L, new UpdateExamCommand("New", null, null, null, true, null))
                .getTitulo()).isEqualTo("New");
        when(examPort.existsById(1L)).thenReturn(true);
        service.deleteExam(1L);
        verify(examPort).deleteById(1L);
    }

    @Test
    void missingExamPaths() {
        when(examPort.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getExam(9L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.updateExam(9L, new UpdateExamCommand(null, null, null, null, null, null)))
                .isInstanceOf(ResourceNotFoundException.class);
        when(examPort.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteExam(9L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listExams() {
        final Exam e = Exam.create("EX-1", "T", null, null, null, true, null);
        when(examPort.findByFilter(eq("B2"), eq(true), anyInt(), anyInt())).thenReturn(List.of(e));
        when(examPort.countByFilter("B2", true)).thenReturn(1L);
        final PagedExams page = service.listExams(new ListExamsQuery("B2", true, 20, 0));
        assertThat(page.total()).isEqualTo(1L);
    }

    @Test
    void questionsListAndAdd() {
        when(examPort.existsById(1L)).thenReturn(true);
        when(questionPort.findByExam(1L)).thenReturn(List.of(
                new Question(1L, 1L, 1, "p", List.of("a", "b"), null)));
        assertThat(service.listQuestions(1L)).hasSize(1);
        when(questionPort.existsByExamAndOrden(1L, 2)).thenReturn(false);
        when(questionPort.save(any(Question.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.addQuestion(1L, new AddQuestionCommand(2, "q", List.of("a", "b"), null)).getOrden())
                .isEqualTo(2);
        when(questionPort.existsByExamAndOrden(1L, 2)).thenReturn(true);
        assertThatThrownBy(() -> service.addQuestion(1L, new AddQuestionCommand(2, "q", List.of("a", "b"), null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void questionExamMissing() {
        when(examPort.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.listQuestions(9L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.addQuestion(9L, new AddQuestionCommand(1, "q", List.of("a", "b"), null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void resultsCreateListAndExamMissing() {
        final UUID user = UUID.randomUUID();
        when(examPort.existsById(1L)).thenReturn(true);
        when(resultPort.save(any(Result.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createResult(new CreateResultCommand(user, 1L, "{}", 5, 10, new BigDecimal("50")))
                .getAciertos()).isEqualTo(5);
        when(resultPort.findByUser(user)).thenReturn(List.of(
                new Result(1L, user, 1L, "{}", 5, 10, new BigDecimal("50"), null)));
        assertThat(service.listUserResults(user)).hasSize(1);
        when(examPort.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.createResult(new CreateResultCommand(user, 9L, "{}", 1, 1,
                BigDecimal.ONE))).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void diagnosticAndSimulation() {
        final UUID user = UUID.randomUUID();
        when(diagnosticPort.save(any(Diagnostic.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createDiagnostic(new CreateDiagnosticCommand(user, 30, 40, "{}")).getAciertos())
                .isEqualTo(30);
        when(simulationPort.save(any(Simulation.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createSimulation(new CreateSimulationCommand(user, Modalidad.LISTENING, 20, 40,
                "{}", 600)).getModalidad()).isEqualTo(Modalidad.LISTENING);
    }
}
