package com.example.vocabularyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Vocabulary service.
 */
@SpringBootApplication
public class VocabularyServiceApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(VocabularyServiceApplication.class, args);
    }
}
