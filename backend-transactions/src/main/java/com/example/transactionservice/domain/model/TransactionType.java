package com.example.transactionservice.domain.model;

/**
 * Direction of a financial transaction.
 */
public enum TransactionType {

    /** Money coming into the account. */
    CREDIT,

    /** Money going out of the account. */
    DEBIT
}
