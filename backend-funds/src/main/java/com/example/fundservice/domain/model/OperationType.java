package com.example.fundservice.domain.model;

/**
 * Type of an order operation on a fund.
 */
public enum OperationType {

    /** Buying into the fund. */
    SUBSCRIPTION,

    /** Selling out of the fund. */
    REDEMPTION
}
