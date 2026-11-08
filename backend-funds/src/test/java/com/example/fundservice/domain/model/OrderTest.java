package com.example.fundservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.fundservice.domain.exception.InvalidOrderException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Order} domain invariants.
 */
class OrderTest {

    @Test
    void placeGeneratesIdPendingStatusAndDefaultsCurrency() {
        final Order order = Order.place("ACC-1", "ES01", OperationType.SUBSCRIPTION,
                new BigDecimal("1000.00"), null);

        assertThat(order.getOrderId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getCurrency()).isEqualTo("EUR");
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    void rejectsAmountBelowMinimum() {
        assertThatThrownBy(() -> Order.place("ACC-1", "ES01", OperationType.REDEMPTION,
                new BigDecimal("0.00"), "EUR"))
                .isInstanceOf(InvalidOrderException.class);
    }

    @Test
    void rejectsBlankAccount() {
        assertThatThrownBy(() -> Order.place(" ", "ES01", OperationType.SUBSCRIPTION,
                new BigDecimal("10"), "EUR"))
                .isInstanceOf(InvalidOrderException.class);
    }
}
