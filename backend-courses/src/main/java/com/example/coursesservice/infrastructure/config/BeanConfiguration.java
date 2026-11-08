package com.example.coursesservice.infrastructure.config;

import com.example.coursesservice.application.port.in.CoursesUseCase;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityAttemptPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityQuestionPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.CoursePort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.UnitPort;
import com.example.coursesservice.application.usecase.CoursesService;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityAttemptJpaRepository;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityAttemptPersistenceAdapter;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityJpaRepository;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityPersistenceAdapter;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityQuestionJpaRepository;
import com.example.coursesservice.infrastructure.adapter.out.persistence.ActivityQuestionPersistenceAdapter;
import com.example.coursesservice.infrastructure.adapter.out.persistence.CourseJpaRepository;
import com.example.coursesservice.infrastructure.adapter.out.persistence.CoursePersistenceAdapter;
import com.example.coursesservice.infrastructure.adapter.out.persistence.UnitJpaRepository;
import com.example.coursesservice.infrastructure.adapter.out.persistence.UnitPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit bean wiring for the hexagonal components.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public CoursePort coursePort(final CourseJpaRepository r) {
        return new CoursePersistenceAdapter(r);
    }

    @Bean
    public UnitPort unitPort(final UnitJpaRepository r) {
        return new UnitPersistenceAdapter(r);
    }

    @Bean
    public ActivityPort activityPort(final ActivityJpaRepository r) {
        return new ActivityPersistenceAdapter(r);
    }

    @Bean
    public ActivityQuestionPort activityQuestionPort(final ActivityQuestionJpaRepository r) {
        return new ActivityQuestionPersistenceAdapter(r);
    }

    @Bean
    public ActivityAttemptPort activityAttemptPort(final ActivityAttemptJpaRepository r) {
        return new ActivityAttemptPersistenceAdapter(r);
    }

    @Bean
    public CoursesUseCase coursesUseCase(final CoursePort coursePort, final UnitPort unitPort,
                                         final ActivityPort activityPort,
                                         final ActivityQuestionPort questionPort,
                                         final ActivityAttemptPort attemptPort) {
        return new CoursesService(coursePort, unitPort, activityPort, questionPort, attemptPort);
    }
}
