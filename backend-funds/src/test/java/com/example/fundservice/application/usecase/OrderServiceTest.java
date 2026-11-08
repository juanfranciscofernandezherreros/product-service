package com.example.fundservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.fundservice.application.port.in.PlaceOrderCommand;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.OperationType;
import com.example.fundservice.domain.model.Order;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link OrderService}.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;
    @Mock
    private FundRepositoryPort fundRepository;

    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService(orderRepository, fundRepository);
    }

    @Test
    void placesOrderWhenFundExists() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        final Order order = service.place(new PlaceOrderCommand("ACC-1", "ES01",
                OperationType.SUBSCRIPTION, new BigDecimal("1000.00"), "EUR"));

        assertThat(order.getIsin()).isEqualTo("ES01");
        assertThat(order.getOperationType()).isEqualTo(OperationType.SUBSCRIPTION);
    }

    @Test
    void throwsWhenFundMissing() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(false);

        assertThatThrownBy(() -> service.place(new PlaceOrderCommand("ACC-1", "ES01",
                OperationType.SUBSCRIPTION, new BigDecimal("1000.00"), "EUR")))
                .isInstanceOf(FundNotFoundException.class);
    }
}
