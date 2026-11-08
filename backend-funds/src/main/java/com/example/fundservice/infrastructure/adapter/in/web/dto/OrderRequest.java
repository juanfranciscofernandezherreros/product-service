package com.example.fundservice.infrastructure.adapter.in.web.dto;

import com.example.fundservice.domain.model.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Request payload for placing an order.
 *
 * @param accountId     owning account
 * @param isin          target fund ISIN
 * @param operationType subscription or redemption
 * @param amount        monetary amount (&ge; 0.01)
 * @param currency      currency code (optional)
 */
public record OrderRequest(
        @NotBlank String accountId,
        @NotBlank String isin,
        @NotNull OperationType operationType,
        @NotNull @DecimalMin(value = "0.01", message = "amount must be at least 0.01") BigDecimal amount,
        String currency) {
}
