package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response for a single NAV observation.
 *
 * @param date valuation date
 * @param nav  net asset value
 */
public record NavPointResponse(LocalDate date, BigDecimal nav) {
}
