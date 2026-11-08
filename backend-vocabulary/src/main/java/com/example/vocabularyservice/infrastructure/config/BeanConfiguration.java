package com.example.vocabularyservice.infrastructure.config;

import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.application.port.out.VocabularyItemRepositoryPort;
import com.example.vocabularyservice.application.port.out.VocabularyProgressRepositoryPort;
import com.example.vocabularyservice.application.usecase.VocabularyService;
import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyItemJpaRepository;
import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyItemPersistenceAdapter;
import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyProgressJpaRepository;
import com.example.vocabularyservice.infrastructure.adapter.out.persistence.VocabularyProgressPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Provides the item persistence adapter.
     *
     * @param repository Spring Data repository
     * @return the item port
     */
    @Bean
    public VocabularyItemRepositoryPort vocabularyItemRepositoryPort(final VocabularyItemJpaRepository repository) {
        return new VocabularyItemPersistenceAdapter(repository);
    }

    /**
     * Provides the progress persistence adapter.
     *
     * @param repository Spring Data repository
     * @return the progress port
     */
    @Bean
    public VocabularyProgressRepositoryPort vocabularyProgressRepositoryPort(
            final VocabularyProgressJpaRepository repository) {
        return new VocabularyProgressPersistenceAdapter(repository);
    }

    /**
     * Provides the application service implementing the use cases.
     *
     * @param itemPort     item port
     * @param progressPort progress port
     * @return the use cases
     */
    @Bean
    public VocabularyUseCases vocabularyUseCases(final VocabularyItemRepositoryPort itemPort,
                                                 final VocabularyProgressRepositoryPort progressPort) {
        return new VocabularyService(itemPort, progressPort);
    }
}
