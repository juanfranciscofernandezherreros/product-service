package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.AiPlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.GoalPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.PlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.ReportPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.SessionPort;
import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Outbound persistence adapters for the study-plan domain.
 */
public final class StudyPlanPersistenceAdapters {

    private StudyPlanPersistenceAdapters() {
    }

    /** Plan adapter. */
    public static final class PlanAdapter implements PlanPort {
        private final StudyPlanJpaRepository repository;

        /** @param repository repository */
        public PlanAdapter(final StudyPlanJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Optional<StudyPlan> findByUser(final UUID userId) {
            return repository.findById(userId).map(StudyPlanMapper::toDomain);
        }

        @Override
        public StudyPlan save(final StudyPlan plan) {
            return StudyPlanMapper.toDomain(repository.save(StudyPlanMapper.toEntity(plan)));
        }
    }

    /** AI plan adapter. */
    public static final class AiPlanAdapter implements AiPlanPort {
        private final AiPlanJpaRepository repository;

        /** @param repository repository */
        public AiPlanAdapter(final AiPlanJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Optional<AiPlan> findByUser(final UUID userId) {
            return repository.findById(userId).map(StudyPlanMapper::toDomain);
        }

        @Override
        public AiPlan save(final AiPlan plan) {
            return StudyPlanMapper.toDomain(repository.save(StudyPlanMapper.toEntity(plan)));
        }
    }

    /** Goal adapter. */
    public static final class GoalAdapter implements GoalPort {
        private final GoalJpaRepository repository;

        /** @param repository repository */
        public GoalAdapter(final GoalJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Optional<WeeklyGoal> findByUser(final UUID userId) {
            return repository.findById(userId).map(StudyPlanMapper::toDomain);
        }

        @Override
        public WeeklyGoal save(final WeeklyGoal goal) {
            return StudyPlanMapper.toDomain(repository.save(StudyPlanMapper.toEntity(goal)));
        }
    }

    /** Session adapter. */
    public static final class SessionAdapter implements SessionPort {
        private final SessionJpaRepository repository;

        /** @param repository repository */
        public SessionAdapter(final SessionJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public List<StudySession> findByUser(final UUID userId) {
            return repository.findByUserId(userId).stream()
                    .map(StudyPlanMapper::toDomain).collect(Collectors.toList());
        }

        @Override
        public StudySession save(final StudySession session) {
            return StudyPlanMapper.toDomain(repository.save(StudyPlanMapper.toEntity(session)));
        }
    }

    /** Report adapter. */
    public static final class ReportAdapter implements ReportPort {
        private final ReportJpaRepository repository;

        /** @param repository repository */
        public ReportAdapter(final ReportJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public List<WeeklyReport> findByUser(final UUID userId) {
            return repository.findByUserId(userId).stream()
                    .map(StudyPlanMapper::toDomain).collect(Collectors.toList());
        }
    }
}
