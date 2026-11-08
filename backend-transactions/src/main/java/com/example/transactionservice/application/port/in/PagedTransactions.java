package com.example.transactionservice.application.port.in;

import com.example.transactionservice.domain.model.Transaction;
import java.util.List;

/**
 * A page of transactions together with pagination metadata.
 *
 * @param total  total number of matching transactions
 * @param limit  applied page size
 * @param offset applied offset
 * @param data   the transactions in this page
 */
public record PagedTransactions(long total, int limit, int offset, List<Transaction> data) {
}
