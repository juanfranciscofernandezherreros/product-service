package com.example.studyplanservice.application.port.in;

import com.example.studyplanservice.application.port.in.StudyPlanCommands.AddSessionCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertAiPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertGoalCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertPlanCommand;
import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the study-plan use cases.
 */
public interface StudyPlanUseCases {

    /**
     * Gets a user's plan.
     *
     * @param userId user id
     * @return the plan
     */
    StudyPlan getPlan(UUID userId);

    /**
     * Creates/updates a user's plan.
     *
     * @param command attributes
     * @return the plan
     */
    StudyPlan upsertPlan(UpsertPlanCommand command);

    /**
     * Gets a user's AI plan.
     *
     * @param userId user id
     * @return the AI plan
     */
    AiPlan getAiPlan(UUID userId);

    /**
     * Creates/updates a user's AI plan.
     *
     * @param command attributes
     * @return the AI plan
     */
    AiPlan upsertAiPlan(UpsertAiPlanCommand command);

    /**
     * Gets a user's goal.
     *
     * @param userId user id
     * @return the goal
     */
    WeeklyGoal getGoal(UUID userId);

    /**
     * Creates/updates a user's goal.
     *
     * @param command attributes
     * @return the goal
     */
    WeeklyGoal upsertGoal(UpsertGoalCommand command);

    /**
     * Lists a user's sessions.
     *
     * @param userId user id
     * @return the sessions
     */
    List<StudySession> listSessions(UUID userId);

    /**
     * Adds a session.
     *
     * @param command attributes
     * @return the session
     */
    StudySession addSession(AddSessionCommand command);

    /**
     * Lists a user's weekly reports.
     *
     * @param userId user id
     * @return the reports
     */
    List<WeeklyReport> listReports(UUID userId);
}
