package com.example.transactionservice.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Paginated list response for transactions.
 *
 * @param total  total number of matching transactions
 * @param limit  applied page size
 * @param offset applied offset
 * @param data   the transactions in this page
 */
public record PagedTransactionsResponse(long total, int limit, int offset, List<TransactionResponse> data) {
}
