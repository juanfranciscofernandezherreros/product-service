package com.example.transactionservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.transactionservice.domain.exception.InvalidTransactionException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Transaction} domain invariants.
 */
class TransactionTest {

    @Test
    void createGeneratesIdPendingStatusAndTimestamp() {
        final Transaction tx = Transaction.create(new BigDecimal("150.50"), "EUR",
                TransactionType.DEBIT, UUID.randomUUID(), "shopping");

        assertThat(tx.getId()).isNotNull();
        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(tx.getCreatedAt()).isNotNull();
        assertThat(tx.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void rejectsAmountBelowMinimum() {
        assertThatThrownBy(() -> Transaction.create(new BigDecimal("0.00"), "EUR",
                TransactionType.CREDIT, UUID.randomUUID(), null))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    void rejectsInvalidCurrencyLength() {
        assertThatThrownBy(() -> Transaction.create(new BigDecimal("10.00"), "EU",
                TransactionType.CREDIT, UUID.randomUUID(), null))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    void rejectsNullAccount() {
        assertThatThrownBy(() -> Transaction.create(new BigDecimal("10.00"), "EUR",
                TransactionType.CREDIT, null, null))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    void equalsBasedOnId() {
        final UUID id = UUID.randomUUID();
        final Instant now = Instant.now();
        final Transaction a = new Transaction(id, new BigDecimal("1.00"), "EUR",
                TransactionType.CREDIT, TransactionStatus.PENDING, UUID.randomUUID(), null, now);
        final Transaction b = new Transaction(id, new BigDecimal("2.00"), "USD",
                TransactionType.DEBIT, TransactionStatus.FAILED, UUID.randomUUID(), "x", now);

        assertThat(a).isEqualTo(b);
    }
}
