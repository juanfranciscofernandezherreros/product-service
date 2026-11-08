package com.example.fundservice.domain.model;

import com.example.fundservice.domain.exception.InvalidOrderException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain representation of a subscription/redemption order.
 */
public final class Order {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    private static final String DEFAULT_CURRENCY = "EUR";

    private final UUID orderId;
    private final String accountId;
    private final String isin;
    private final OperationType operationType;
    private final BigDecimal amount;
    private final String currency;
    private final OrderStatus status;
    private final Instant createdAt;

    /**
     * Creates a validated order.
     *
     * @param orderId       unique identifier
     * @param accountId     owning account
     * @param isin          target fund ISIN
     * @param operationType subscription or redemption
     * @param amount        monetary amount (&ge; 0.01)
     * @param currency      currency code (defaults to EUR when blank)
     * @param status        lifecycle status
     * @param createdAt     creation instant
     * @throws InvalidOrderException if any invariant is violated
     */
    public Order(final UUID orderId, final String accountId, final String isin,
                 final OperationType operationType, final BigDecimal amount, final String currency,
                 final OrderStatus status, final Instant createdAt) {
        if (orderId == null) {
            throw new InvalidOrderException("orderId is required");
        }
        this.accountId = requireText(accountId, "accountId");
        this.isin = requireText(isin, "isin");
        if (operationType == null) {
            throw new InvalidOrderException("operationType is required");
        }
        if (amount == null || amount.compareTo(MIN_AMOUNT) < 0) {
            throw new InvalidOrderException("amount must be greater than or equal to " + MIN_AMOUNT);
        }
        if (status == null) {
            throw new InvalidOrderException("status is required");
        }
        if (createdAt == null) {
            throw new InvalidOrderException("createdAt is required");
        }
        this.orderId = orderId;
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency == null || currency.isBlank() ? DEFAULT_CURRENCY : currency.trim();
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Factory for a brand-new order: generates the id, sets status to
     * {@link OrderStatus#PENDING} and stamps the creation instant.
     *
     * @param accountId     owning account
     * @param isin          target fund ISIN
     * @param operationType subscription or redemption
     * @param amount        monetary amount
     * @param currency      currency code
     * @return a new validated order
     */
    public static Order place(final String accountId, final String isin, final OperationType operationType,
                              final BigDecimal amount, final String currency) {
        return new Order(UUID.randomUUID(), accountId, isin, operationType, amount, currency,
                OrderStatus.PENDING, Instant.now());
    }

    private static String requireText(final String value, final String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidOrderException(field + " is required");
        }
        return value.trim();
    }

    /**
     * Returns the order id.
     *
     * @return the order id
     */
    public UUID getOrderId() {
        return orderId;
    }

    /**
     * Returns the account id.
     *
     * @return the account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Returns the fund ISIN.
     *
     * @return the isin
     */
    public String getIsin() {
        return isin;
    }

    /**
     * Returns the operation type.
     *
     * @return the operation type
     */
    public OperationType getOperationType() {
        return operationType;
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
     * Returns the currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the status.
     *
     * @return the status
     */
    public OrderStatus getStatus() {
        return status;
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
        if (!(other instanceof Order)) {
            return false;
        }
        return Objects.equals(orderId, ((Order) other).orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
