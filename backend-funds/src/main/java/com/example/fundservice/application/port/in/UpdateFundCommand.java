package com.example.fundservice.application.port.in;

import java.math.BigDecimal;

/**
 * Input data for updating a fund (null fields keep the current value).
 *
 * @param name          new name
 * @param category      new category
 * @param riskLevel     new risk level
 * @param managementFee new management fee
 * @param depositaryFee new depositary fee
 * @param description   new description
 */
public record UpdateFundCommand(String name, String category, Integer riskLevel,
                                BigDecimal managementFee, BigDecimal depositaryFee, String description) {
}
