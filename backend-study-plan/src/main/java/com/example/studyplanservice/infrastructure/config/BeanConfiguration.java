package com.example.studyplanservice.infrastructure.config;

import com.example.studyplanservice.application.port.in.StudyPlanUseCases;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.AiPlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.GoalPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.PlanPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.ReportPort;
import com.example.studyplanservice.application.port.out.StudyPlanOutPorts.SessionPort;
import com.example.studyplanservice.application.usecase.StudyPlanService;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanPersistenceAdapters.AiPlanAdapter;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanPersistenceAdapters.GoalAdapter;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanPersistenceAdapters.PlanAdapter;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanPersistenceAdapters.ReportAdapter;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanPersistenceAdapters.SessionAdapter;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.AiPlanJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.GoalJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.ReportJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.SessionJpaRepository;
import com.example.studyplanservice.infrastructure.adapter.out.persistence.StudyPlanJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /** @param r repository @return port */
    @Bean
    public PlanPort planPort(final StudyPlanJpaRepository r) {
        return new PlanAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public AiPlanPort aiPlanPort(final AiPlanJpaRepository r) {
        return new AiPlanAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public GoalPort goalPort(final GoalJpaRepository r) {
        return new GoalAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public SessionPort sessionPort(final SessionJpaRepository r) {
        return new SessionAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public ReportPort reportPort(final ReportJpaRepository r) {
        return new ReportAdapter(r);
    }

    /**
     * Study-plan use cases.
     *
     * @param planPort    plan port
     * @param aiPlanPort  AI plan port
     * @param goalPort    goal port
     * @param sessionPort session port
     * @param reportPort  report port
     * @return the use cases
     */
    @Bean
    public StudyPlanUseCases studyPlanUseCases(final PlanPort planPort, final AiPlanPort aiPlanPort,
                                               final GoalPort goalPort, final SessionPort sessionPort,
                                               final ReportPort reportPort) {
        return new StudyPlanService(planPort, aiPlanPort, goalPort, sessionPort, reportPort);
    }
}
