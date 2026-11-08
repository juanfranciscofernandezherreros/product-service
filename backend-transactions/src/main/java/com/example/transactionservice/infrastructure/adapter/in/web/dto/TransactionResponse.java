package com.example.transactionservice.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Outbound response payload representing a transaction.
 *
 * @param id          identifier
 * @param amount      amount
 * @param currency    currency code
 * @param type        transaction direction (lowercase)
 * @param status      lifecycle status (lowercase)
 * @param accountId   account identifier
 * @param description optional description
 * @param createdAt   creation instant
 */
public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        String type,
        String status,
        @JsonProperty("account_id") UUID accountId,
        String description,
        @JsonProperty("created_at") Instant createdAt) {
}
