package com.example.fundservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Request payload for creating a fund.
 *
 * @param isin          ISIN code
 * @param name          fund name
 * @param category      category
 * @param currency      currency code
 * @param riskLevel     risk level (1..7)
 * @param managementFee management fee
 * @param depositaryFee depositary fee
 * @param description   description
 */
public record FundCreateRequest(
        @NotBlank String isin,
        @NotBlank String name,
        @NotBlank String category,
        @NotBlank String currency,
        @NotNull @Min(1) @Max(7) Integer riskLevel,
        BigDecimal managementFee,
        BigDecimal depositaryFee,
        String description) {
}
