package com.example.fundservice.application.port.in;

import java.math.BigDecimal;

/**
 * Input data for creating a fund.
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
public record CreateFundCommand(String isin, String name, String category, String currency,
                                int riskLevel, BigDecimal managementFee, BigDecimal depositaryFee,
                                String description) {
}
