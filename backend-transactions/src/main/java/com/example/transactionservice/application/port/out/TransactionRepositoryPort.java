package com.example.transactionservice.application.port.out;

import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port abstracting transaction persistence from the application core.
 */
public interface TransactionRepositoryPort {

    /**
     * Persists a transaction.
     *
     * @param transaction transaction to store
     * @return the stored transaction
     */
    Transaction save(Transaction transaction);

    /**
     * Finds a transaction by its identifier.
     *
     * @param id transaction identifier
     * @return the transaction if present
     */
    Optional<Transaction> findById(UUID id);

    /**
     * Returns a page of transactions, optionally filtered by status.
     *
     * @param status optional status filter, {@code null} means no filter
     * @param limit  maximum number of results
     * @param offset number of results to skip
     * @return the matching transactions
     */
    List<Transaction> findByFilter(TransactionStatus status, int limit, int offset);

    /**
     * Counts transactions, optionally filtered by status.
     *
     * @param status optional status filter, {@code null} means no filter
     * @return the number of matching transactions
     */
    long countByFilter(TransactionStatus status);
}
