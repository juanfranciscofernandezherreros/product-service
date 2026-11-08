package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

/**
 * Summary response for a fund (list view).
 *
 * @param isin       ISIN code
 * @param name       name
 * @param category   category
 * @param currency   currency
 * @param currentNav current NAV
 */
public record FundSummaryResponse(String isin, String name, String category, String currency,
                                  BigDecimal currentNav) {
}
