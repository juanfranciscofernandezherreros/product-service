package com.example.transactionservice.domain.model;

import com.example.transactionservice.domain.exception.InvalidTransactionException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain representation of a financial transaction.
 *
 * <p>Enforces its invariants on construction: a positive amount (&ge; 0.01),
 * a three-letter currency code, and non-null type, status, account and
 * creation instant.</p>
 */
public final class Transaction {

    private static final int CURRENCY_LENGTH = 3;
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");

    private final UUID id;
    private final BigDecimal amount;
    private final String currency;
    private final TransactionType type;
    private final TransactionStatus status;
    private final UUID accountId;
    private final String description;
    private final Instant createdAt;

    /**
     * Creates a validated transaction.
     *
     * @param id          unique identifier
     * @param amount      monetary amount, greater than or equal to {@code 0.01}
     * @param currency    ISO currency code of three letters
     * @param type        transaction direction
     * @param status      lifecycle status
     * @param accountId   owning account identifier
     * @param description optional free-text description
     * @param createdAt   creation instant
     * @throws InvalidTransactionException if any invariant is violated
     */
    public Transaction(final UUID id, final BigDecimal amount, final String currency,
                       final TransactionType type, final TransactionStatus status,
                       final UUID accountId, final String description, final Instant createdAt) {
        if (id == null) {
            throw new InvalidTransactionException("Id is required");
        }
        if (amount == null || amount.compareTo(MIN_AMOUNT) < 0) {
            throw new InvalidTransactionException("Amount must be greater than or equal to " + MIN_AMOUNT);
        }
        if (currency == null || currency.trim().length() != CURRENCY_LENGTH) {
            throw new InvalidTransactionException("Currency must be a 3-letter code");
        }
        if (type == null) {
            throw new InvalidTransactionException("Type is required");
        }
        if (status == null) {
            throw new InvalidTransactionException("Status is required");
        }
        if (accountId == null) {
            throw new InvalidTransactionException("Account id is required");
        }
        if (createdAt == null) {
            throw new InvalidTransactionException("Creation instant is required");
        }
        this.id = id;
        this.amount = amount;
        this.currency = currency.trim().toUpperCase();
        this.type = type;
        this.status = status;
        this.accountId = accountId;
        this.description = description;
        this.createdAt = createdAt;
    }

    /**
     * Factory for a brand-new transaction: generates the id, sets status to
     * {@link TransactionStatus#PENDING} and stamps the creation instant.
     *
     * @param amount      monetary amount
     * @param currency    currency code
     * @param type        transaction direction
     * @param accountId   owning account identifier
     * @param description optional description
     * @return a new validated transaction
     */
    public static Transaction create(final BigDecimal amount, final String currency,
                                     final TransactionType type, final UUID accountId,
                                     final String description) {
        return new Transaction(UUID.randomUUID(), amount, currency, type,
                TransactionStatus.PENDING, accountId, description, Instant.now());
    }

    /**
     * Returns the identifier.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the amount.
     *
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Returns the currency code.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the transaction type.
     *
     * @return the type
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Returns the status.
     *
     * @return the status
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * Returns the account identifier.
     *
     * @return the account id
     */
    public UUID getAccountId() {
        return accountId;
    }

    /**
     * Returns the optional description.
     *
     * @return the description, may be {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the creation instant.
     *
     * @return the creation instant
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Transaction)) {
            return false;
        }
        return Objects.equals(id, ((Transaction) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
