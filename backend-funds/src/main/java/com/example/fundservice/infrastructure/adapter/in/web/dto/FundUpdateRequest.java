package com.example.fundservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Request payload for updating a fund (null fields keep the current value).
 *
 * @param name          new name
 * @param category      new category
 * @param riskLevel     new risk level (1..7)
 * @param managementFee new management fee
 * @param depositaryFee new depositary fee
 * @param description   new description
 */
public record FundUpdateRequest(
        String name,
        String category,
        @Min(1) @Max(7) Integer riskLevel,
        BigDecimal managementFee,
        BigDecimal depositaryFee,
        String description) {
}
