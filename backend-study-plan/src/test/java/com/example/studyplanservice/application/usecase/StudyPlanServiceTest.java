package com.example.studyplanservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.studyplanservice.application.port.in.StudyPlanCommands.AddSessionCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertAiPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertGoalCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertPlanCommand;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.AiPlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.GoalPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.PlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.ReportPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.SessionPort;
import com.example.studyplanservice.domain.exception.ResourceNotFoundException;
import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.GoalType;
import com.example.studyplanservice.domain.model.Prioridad;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.Tarea;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link StudyPlanService}.
 */
@ExtendWith(MockitoExtension.class)
class StudyPlanServiceTest {

    @Mock
    private PlanPort planPort;
    @Mock
    private AiPlanPort aiPlanPort;
    @Mock
    private GoalPort goalPort;
    @Mock
    private SessionPort sessionPort;
    @Mock
    private ReportPort reportPort;

    private StudyPlanService service;
    private final UUID user = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new StudyPlanService(planPort, aiPlanPort, goalPort, sessionPort, reportPort);
    }

    @Test
    void planGetAndUpsert() {
        final StudyPlan plan = new StudyPlan(user, null, 30, List.of(1, 2), Prioridad.AUTO, null, false, null);
        when(planPort.findByUser(user)).thenReturn(Optional.of(plan));
        assertThat(service.getPlan(user)).isEqualTo(plan);
        when(planPort.save(any(StudyPlan.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.upsertPlan(new UpsertPlanCommand(user, null, 45, List.of(1, 3),
                Prioridad.READING, null, true)).getMinutosDia()).isEqualTo(45);
    }

    @Test
    void planGetMissing() {
        when(planPort.findByUser(user)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getPlan(user)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void aiPlanGetUpsertAndMissing() {
        final AiPlan ai = new AiPlan(user, 8, "{}", null);
        when(aiPlanPort.findByUser(user)).thenReturn(Optional.of(ai));
        assertThat(service.getAiPlan(user)).isEqualTo(ai);
        when(aiPlanPort.save(any(AiPlan.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.upsertAiPlan(new UpsertAiPlanCommand(user, 12, "{\"a\":1}")).getSemanas()).isEqualTo(12);
        final UUID other = UUID.randomUUID();
        when(aiPlanPort.findByUser(other)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getAiPlan(other)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void goalGetUpsertAndMissing() {
        final WeeklyGoal goal = new WeeklyGoal(user, GoalType.UNIDADES, 3, null);
        when(goalPort.findByUser(user)).thenReturn(Optional.of(goal));
        assertThat(service.getGoal(user)).isEqualTo(goal);
        when(goalPort.save(any(WeeklyGoal.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.upsertGoal(new UpsertGoalCommand(user, GoalType.EXAMENES, 5)).getCantidad()).isEqualTo(5);
        final UUID other = UUID.randomUUID();
        when(goalPort.findByUser(other)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getGoal(other)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void sessionsListAndAdd() {
        when(sessionPort.findByUser(user)).thenReturn(List.of(
                new StudySession(user, LocalDate.now(), Tarea.UNIDAD, null)));
        assertThat(service.listSessions(user)).hasSize(1);
        when(sessionPort.save(any(StudySession.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.addSession(new AddSessionCommand(user, null, Tarea.REPASO)).getTarea())
                .isEqualTo(Tarea.REPASO);
    }

    @Test
    void reportsList() {
        when(reportPort.findByUser(user)).thenReturn(List.of(
                new WeeklyReport(user, LocalDate.now(), LocalDate.now(), "{}", null)));
        assertThat(service.listReports(user)).hasSize(1);
    }
}
