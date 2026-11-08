package com.example.fundservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA persistence entity for orders.
 */
@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "isin", nullable = false)
    private String isin;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Default constructor required by JPA.
     */
    protected OrderJpaEntity() {
    }

    /**
     * Creates a fully populated entity.
     *
     * @param orderId       identifier
     * @param accountId     account
     * @param isin          fund ISIN
     * @param operationType operation type name
     * @param amount        amount
     * @param currency      currency
     * @param status        status name
     * @param createdAt     creation instant
     */
    public OrderJpaEntity(final UUID orderId, final String accountId, final String isin,
                          final String operationType, final BigDecimal amount, final String currency,
                          final String status, final Instant createdAt) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.isin = isin;
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    /** @return the order id */
    public UUID getOrderId() {
        return orderId;
    }

    /** @return the account id */
    public String getAccountId() {
        return accountId;
    }

    /** @return the ISIN */
    public String getIsin() {
        return isin;
    }

    /** @return the operation type name */
    public String getOperationType() {
        return operationType;
    }

    /** @return the amount */
    public BigDecimal getAmount() {
        return amount;
    }

    /** @return the currency */
    public String getCurrency() {
        return currency;
    }

    /** @return the status name */
    public String getStatus() {
        return status;
    }

    /** @return the creation instant */
    public Instant getCreatedAt() {
        return createdAt;
    }
}
