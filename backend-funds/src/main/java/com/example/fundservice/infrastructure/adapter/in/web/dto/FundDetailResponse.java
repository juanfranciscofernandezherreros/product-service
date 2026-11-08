package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

/**
 * Detailed response for a fund.
 *
 * @param isin                  ISIN code
 * @param name                  name
 * @param category              category
 * @param currency              currency
 * @param currentNav            current NAV
 * @param riskLevel             risk level
 * @param managementFee         management fee
 * @param depositaryFee         depositary fee
 * @param assetsUnderManagement assets under management
 * @param description           description
 */
public record FundDetailResponse(String isin, String name, String category, String currency,
                                 BigDecimal currentNav, int riskLevel, BigDecimal managementFee,
                                 BigDecimal depositaryFee, BigDecimal assetsUnderManagement,
                                 String description) {
}
