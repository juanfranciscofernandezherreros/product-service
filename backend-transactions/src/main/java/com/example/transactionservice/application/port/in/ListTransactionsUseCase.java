package com.example.transactionservice.application.port.in;

/**
 * Inbound port for listing transactions with pagination and filtering.
 */
public interface ListTransactionsUseCase {

    /**
     * Lists transactions matching the query.
     *
     * @param query pagination and filter parameters
     * @return a page of transactions with metadata
     */
    PagedTransactions list(ListTransactionsQuery query);
}
