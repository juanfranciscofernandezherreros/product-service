package com.example.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Content service.
 */
@SpringBootApplication
public class ContentServiceApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }
}
