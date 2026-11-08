package com.example.transactionservice.application.port.in;

import com.example.transactionservice.domain.model.TransactionStatus;

/**
 * Query parameters for listing transactions with pagination and optional status filter.
 *
 * @param status optional status filter, {@code null} means no filter
 * @param limit  maximum number of results
 * @param offset number of results to skip
 */
public record ListTransactionsQuery(TransactionStatus status, int limit, int offset) {
}
