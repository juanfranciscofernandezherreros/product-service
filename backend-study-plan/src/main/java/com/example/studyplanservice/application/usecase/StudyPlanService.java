package com.example.studyplanservice.application.usecase;

import com.example.studyplanservice.application.port.in.StudyPlanCommands.AddSessionCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertAiPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertGoalCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanUseCases;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.AiPlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.GoalPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.PlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.ReportPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.SessionPort;
import com.example.studyplanservice.domain.exception.ResourceNotFoundException;
import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the study-plan use cases.
 */
public class StudyPlanService implements StudyPlanUseCases {

    private final PlanPort planPort;
    private final AiPlanPort aiPlanPort;
    private final GoalPort goalPort;
    private final SessionPort sessionPort;
    private final ReportPort reportPort;

    /**
     * Creates the service.
     *
     * @param planPort    plan port
     * @param aiPlanPort  AI plan port
     * @param goalPort    goal port
     * @param sessionPort session port
     * @param reportPort  report port
     */
    public StudyPlanService(final PlanPort planPort, final AiPlanPort aiPlanPort, final GoalPort goalPort,
                            final SessionPort sessionPort, final ReportPort reportPort) {
        this.planPort = planPort;
        this.aiPlanPort = aiPlanPort;
        this.goalPort = goalPort;
        this.sessionPort = sessionPort;
        this.reportPort = reportPort;
    }

    @Override
    public StudyPlan getPlan(final UUID userId) {
        return planPort.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("StudyPlan", String.valueOf(userId)));
    }

    @Override
    public StudyPlan upsertPlan(final UpsertPlanCommand c) {
        return planPort.save(new StudyPlan(c.userId(), c.fechaExamen(), c.minutosDia(), c.diasSemana(),
                c.prioridad(), c.horaRecordatorio(), c.recordatorioActivo(), null));
    }

    @Override
    public AiPlan getAiPlan(final UUID userId) {
        return aiPlanPort.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AiPlan", String.valueOf(userId)));
    }

    @Override
    public AiPlan upsertAiPlan(final UpsertAiPlanCommand c) {
        return aiPlanPort.save(new AiPlan(c.userId(), c.semanas(), c.plan(), null));
    }

    @Override
    public WeeklyGoal getGoal(final UUID userId) {
        return goalPort.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("WeeklyGoal", String.valueOf(userId)));
    }

    @Override
    public WeeklyGoal upsertGoal(final UpsertGoalCommand c) {
        return goalPort.save(new WeeklyGoal(c.userId(), c.tipo(), c.cantidad(), null));
    }

    @Override
    public List<StudySession> listSessions(final UUID userId) {
        return sessionPort.findByUser(userId);
    }

    @Override
    public StudySession addSession(final AddSessionCommand c) {
        return sessionPort.save(new StudySession(c.userId(), c.dia(), c.tarea(), null));
    }

    @Override
    public List<WeeklyReport> listReports(final UUID userId) {
        return reportPort.findByUser(userId);
    }
}
