package com.example.transactionservice.application.port.in;

import com.example.transactionservice.domain.model.Transaction;

/**
 * Inbound port for creating a transaction.
 */
public interface CreateTransactionUseCase {

    /**
     * Creates a new transaction.
     *
     * @param command transaction attributes
     * @return the created transaction
     */
    Transaction create(CreateTransactionCommand command);
}
