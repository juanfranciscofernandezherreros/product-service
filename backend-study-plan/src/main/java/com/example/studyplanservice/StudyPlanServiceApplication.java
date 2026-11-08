package com.example.studyplanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Study Plan service.
 */
@SpringBootApplication
public class StudyPlanServiceApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(StudyPlanServiceApplication.class, args);
    }
}
