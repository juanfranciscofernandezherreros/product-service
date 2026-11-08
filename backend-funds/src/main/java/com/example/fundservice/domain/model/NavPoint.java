package com.example.fundservice.domain.model;

import com.example.fundservice.domain.exception.InvalidFundException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A single net-asset-value observation for a fund on a given date.
 *
 * @param date valuation date
 * @param nav  net asset value
 */
public record NavPoint(LocalDate date, BigDecimal nav) {

    /**
     * Validates the NAV point on construction.
     *
     * @param date valuation date
     * @param nav  net asset value
     */
    public NavPoint {
        if (date == null) {
            throw new InvalidFundException("nav date is required");
        }
        if (nav == null || nav.signum() < 0) {
            throw new InvalidFundException("nav must not be negative");
        }
    }
}
