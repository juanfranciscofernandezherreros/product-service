package com.example.transactionservice.infrastructure.adapter.out.persistence;

import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionStatus;
import com.example.transactionservice.domain.model.TransactionType;

/**
 * Explicit mapper between the domain {@link Transaction} and its JPA entity.
 */
public final class TransactionPersistenceMapper {

    private TransactionPersistenceMapper() {
    }

    /**
     * Converts a domain transaction into a JPA entity.
     *
     * @param transaction domain transaction
     * @return the JPA entity
     */
    public static TransactionJpaEntity toEntity(final Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType().name(),
                transaction.getStatus().name(),
                transaction.getAccountId(),
                transaction.getDescription(),
                transaction.getCreatedAt());
    }

    /**
     * Converts a JPA entity into a domain transaction.
     *
     * @param entity JPA entity
     * @return the domain transaction
     */
    public static Transaction toDomain(final TransactionJpaEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAmount(),
                entity.getCurrency(),
                TransactionType.valueOf(entity.getType()),
                TransactionStatus.valueOf(entity.getStatus()),
                entity.getAccountId(),
                entity.getDescription(),
                entity.getCreatedAt());
    }
}
