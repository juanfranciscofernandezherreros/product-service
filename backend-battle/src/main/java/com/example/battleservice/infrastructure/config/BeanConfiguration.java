package com.example.battleservice.infrastructure.config;

import com.example.battleservice.application.port.in.BattleUseCases;
import com.example.battleservice.application.port.out.PlayerRepositoryPort;
import com.example.battleservice.application.port.out.QuestionRepositoryPort;
import com.example.battleservice.application.port.out.RoomRepositoryPort;
import com.example.battleservice.application.usecase.BattleService;
import com.example.battleservice.infrastructure.adapter.out.persistence.BattlePersistenceAdapters.PlayerPersistenceAdapter;
import com.example.battleservice.infrastructure.adapter.out.persistence.BattlePersistenceAdapters.QuestionPersistenceAdapter;
import com.example.battleservice.infrastructure.adapter.out.persistence.BattlePersistenceAdapters.RoomPersistenceAdapter;
import com.example.battleservice.infrastructure.adapter.out.persistence.PlayerJpaRepository;
import com.example.battleservice.infrastructure.adapter.out.persistence.QuestionJpaRepository;
import com.example.battleservice.infrastructure.adapter.out.persistence.RoomJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Question port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public QuestionRepositoryPort questionRepositoryPort(final QuestionJpaRepository repository) {
        return new QuestionPersistenceAdapter(repository);
    }

    /**
     * Room port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public RoomRepositoryPort roomRepositoryPort(final RoomJpaRepository repository) {
        return new RoomPersistenceAdapter(repository);
    }

    /**
     * Player port.
     *
     * @param repository repository
     * @return the port
     */
    @Bean
    public PlayerRepositoryPort playerRepositoryPort(final PlayerJpaRepository repository) {
        return new PlayerPersistenceAdapter(repository);
    }

    /**
     * Battle use cases.
     *
     * @param questionPort question port
     * @param roomPort     room port
     * @param playerPort   player port
     * @return the use cases
     */
    @Bean
    public BattleUseCases battleUseCases(final QuestionRepositoryPort questionPort,
                                         final RoomRepositoryPort roomPort,
                                         final PlayerRepositoryPort playerPort) {
        return new BattleService(questionPort, roomPort, playerPort);
    }
}
