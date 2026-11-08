package com.example.transactionservice.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Inbound request payload for creating a transaction.
 *
 * @param amount      monetary amount (&ge; 0.01)
 * @param currency    ISO currency code (exactly 3 characters)
 * @param type        transaction direction ({@code credit} or {@code debit})
 * @param accountId   owning account identifier
 * @param description optional description
 */
public record TransactionRequest(
        @NotNull @DecimalMin(value = "0.01", message = "amount must be at least 0.01") BigDecimal amount,
        @NotNull @Size(min = 3, max = 3, message = "currency must be a 3-letter code") String currency,
        @NotBlank(message = "type is required") String type,
        @JsonProperty("account_id") @NotNull UUID accountId,
        String description) {
}
