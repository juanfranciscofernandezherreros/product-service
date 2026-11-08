package com.example.studyplanservice.application.port.out;

import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound ports for the study-plan domain.
 */
public final class StudyPlanOutPorts {

    private StudyPlanOutPorts() {
    }

    /** Study plan persistence port. */
    public interface PlanPort {
        /**
         * Finds the plan of a user.
         *
         * @param userId user id
         * @return the plan if present
         */
        Optional<StudyPlan> findByUser(UUID userId);

        /**
         * Persists (upsert) a plan.
         *
         * @param plan plan
         * @return stored plan
         */
        StudyPlan save(StudyPlan plan);
    }

    /** AI plan persistence port. */
    public interface AiPlanPort {
        /**
         * Finds the AI plan of a user.
         *
         * @param userId user id
         * @return the AI plan if present
         */
        Optional<AiPlan> findByUser(UUID userId);

        /**
         * Persists (upsert) an AI plan.
         *
         * @param plan AI plan
         * @return stored AI plan
         */
        AiPlan save(AiPlan plan);
    }

    /** Weekly goal persistence port. */
    public interface GoalPort {
        /**
         * Finds the goal of a user.
         *
         * @param userId user id
         * @return the goal if present
         */
        Optional<WeeklyGoal> findByUser(UUID userId);

        /**
         * Persists (upsert) a goal.
         *
         * @param goal goal
         * @return stored goal
         */
        WeeklyGoal save(WeeklyGoal goal);
    }

    /** Study session persistence port. */
    public interface SessionPort {
        /**
         * Lists the sessions of a user.
         *
         * @param userId user id
         * @return the sessions
         */
        List<StudySession> findByUser(UUID userId);

        /**
         * Persists (upsert) a session.
         *
         * @param session session
         * @return stored session
         */
        StudySession save(StudySession session);
    }

    /** Weekly report persistence port. */
    public interface ReportPort {
        /**
         * Lists the weekly reports of a user.
         *
         * @param userId user id
         * @return the reports
         */
        List<WeeklyReport> findByUser(UUID userId);
    }
}
