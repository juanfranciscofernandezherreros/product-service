package com.example.transactionservice.domain.model;

/**
 * Lifecycle status of a financial transaction.
 */
public enum TransactionStatus {

    /** Registered but not yet processed. */
    PENDING,

    /** Successfully processed. */
    COMPLETED,

    /** Processing failed. */
    FAILED,

    /** Cancelled before completion. */
    CANCELLED
}
