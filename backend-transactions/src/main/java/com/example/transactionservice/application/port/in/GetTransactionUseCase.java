package com.example.transactionservice.application.port.in;

import com.example.transactionservice.domain.model.Transaction;
import java.util.UUID;

/**
 * Inbound port for retrieving a single transaction.
 */
public interface GetTransactionUseCase {

    /**
     * Retrieves a transaction by its identifier.
     *
     * @param id transaction identifier
     * @return the transaction
     */
    Transaction getById(UUID id);
}
