package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.OperationType;
import java.math.BigDecimal;

/**
 * Input data for placing an order.
 *
 * @param accountId     owning account
 * @param isin          target fund ISIN
 * @param operationType subscription or redemption
 * @param amount        monetary amount
 * @param currency      currency code
 */
public record PlaceOrderCommand(String accountId, String isin, OperationType operationType,
                                BigDecimal amount, String currency) {
}
