package com.example.transactionservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA persistence entity for transactions, separate from the domain model.
 */
@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Default constructor required by JPA.
     */
    protected TransactionJpaEntity() {
    }

    /**
     * Creates a fully populated entity.
     *
     * @param id          identifier
     * @param amount      amount
     * @param currency    currency code
     * @param type        type name
     * @param status      status name
     * @param accountId   account identifier
     * @param description optional description
     * @param createdAt   creation instant
     */
    public TransactionJpaEntity(final UUID id, final BigDecimal amount, final String currency,
                                final String type, final String status, final UUID accountId,
                                final String description, final Instant createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.accountId = accountId;
        this.description = description;
        this.createdAt = createdAt;
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
     * Returns the type name.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the status name.
     *
     * @return the status
     */
    public String getStatus() {
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
     * Returns the description.
     *
     * @return the description
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
}
