package com.example.transactionservice.application.port.in;

import com.example.transactionservice.domain.model.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Input data for creating a transaction.
 *
 * @param amount      monetary amount
 * @param currency    currency code
 * @param type        transaction direction
 * @param accountId   owning account identifier
 * @param description optional description
 */
public record CreateTransactionCommand(BigDecimal amount, String currency, TransactionType type,
                                       UUID accountId, String description) {
}
