package com.example.examsservice.infrastructure.config;

import com.example.examsservice.application.port.in.ExamsUseCases;
import com.example.examsservice.application.port.out.ExamsOutPorts.DiagnosticPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ExamPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.QuestionPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ResultPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.SimulationPort;
import com.example.examsservice.application.usecase.ExamsService;
import com.example.examsservice.infrastructure.adapter.out.persistence.DiagnosticJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamsPersistenceAdapters.DiagnosticAdapter;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamsPersistenceAdapters.ExamAdapter;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamsPersistenceAdapters.QuestionAdapter;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamsPersistenceAdapters.ResultAdapter;
import com.example.examsservice.infrastructure.adapter.out.persistence.ExamsPersistenceAdapters.SimulationAdapter;
import com.example.examsservice.infrastructure.adapter.out.persistence.QuestionJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.ResultJpaRepository;
import com.example.examsservice.infrastructure.adapter.out.persistence.SimulationJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /** @param r repository @return port */
    @Bean
    public ExamPort examPort(final ExamJpaRepository r) {
        return new ExamAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public QuestionPort questionPort(final QuestionJpaRepository r) {
        return new QuestionAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public ResultPort resultPort(final ResultJpaRepository r) {
        return new ResultAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public DiagnosticPort diagnosticPort(final DiagnosticJpaRepository r) {
        return new DiagnosticAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public SimulationPort simulationPort(final SimulationJpaRepository r) {
        return new SimulationAdapter(r);
    }

    /**
     * Exams use cases.
     *
     * @param examPort       exam port
     * @param questionPort   question port
     * @param resultPort     result port
     * @param diagnosticPort diagnostic port
     * @param simulationPort simulation port
     * @return the use cases
     */
    @Bean
    public ExamsUseCases examsUseCases(final ExamPort examPort, final QuestionPort questionPort,
                                       final ResultPort resultPort, final DiagnosticPort diagnosticPort,
                                       final SimulationPort simulationPort) {
        return new ExamsService(examPort, questionPort, resultPort, diagnosticPort, simulationPort);
    }
}
