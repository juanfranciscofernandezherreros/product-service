package com.example.examsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Exams service.
 */
@SpringBootApplication
public class ExamsServiceApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ExamsServiceApplication.class, args);
    }
}
