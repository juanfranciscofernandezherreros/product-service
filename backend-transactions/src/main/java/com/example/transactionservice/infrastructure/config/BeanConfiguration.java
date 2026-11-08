package com.example.transactionservice.infrastructure.config;

import com.example.transactionservice.application.port.in.CreateTransactionUseCase;
import com.example.transactionservice.application.port.in.GetTransactionUseCase;
import com.example.transactionservice.application.port.in.ListTransactionsUseCase;
import com.example.transactionservice.application.port.out.TransactionRepositoryPort;
import com.example.transactionservice.application.usecase.TransactionService;
import com.example.transactionservice.infrastructure.adapter.out.persistence.TransactionJpaRepository;
import com.example.transactionservice.infrastructure.adapter.out.persistence.TransactionPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans so every dependency is visible in one place.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Provides the outbound persistence adapter bound to the repository port.
     *
     * @param jpaRepository Spring Data repository
     * @return the persistence port implementation
     */
    @Bean
    public TransactionRepositoryPort transactionRepositoryPort(final TransactionJpaRepository jpaRepository) {
        return new TransactionPersistenceAdapter(jpaRepository);
    }

    /**
     * Provides the application service implementing every transaction use case.
     *
     * @param repositoryPort outbound persistence port
     * @return the transaction service
     */
    @Bean
    public TransactionService transactionService(final TransactionRepositoryPort repositoryPort) {
        return new TransactionService(repositoryPort);
    }

    /**
     * Exposes the create use case.
     *
     * @param transactionService application service
     * @return the create use case
     */
    @Bean
    public CreateTransactionUseCase createTransactionUseCase(final TransactionService transactionService) {
        return transactionService;
    }

    /**
     * Exposes the get use case.
     *
     * @param transactionService application service
     * @return the get use case
     */
    @Bean
    public GetTransactionUseCase getTransactionUseCase(final TransactionService transactionService) {
        return transactionService;
    }

    /**
     * Exposes the list use case.
     *
     * @param transactionService application service
     * @return the list use case
     */
    @Bean
    public ListTransactionsUseCase listTransactionsUseCase(final TransactionService transactionService) {
        return transactionService;
    }
}
