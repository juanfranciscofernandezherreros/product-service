package com.example.fundservice.domain.model;

/**
 * Lifecycle status of an order.
 */
public enum OrderStatus {

    /** Registered, awaiting execution. */
    PENDING,

    /** Successfully executed. */
    EXECUTED,

    /** Rejected and not active. */
    REJECTED
}
