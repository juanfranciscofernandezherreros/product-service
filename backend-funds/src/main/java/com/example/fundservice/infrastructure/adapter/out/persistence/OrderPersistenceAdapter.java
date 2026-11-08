package com.example.fundservice.infrastructure.adapter.out.persistence;

import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.domain.model.OperationType;
import com.example.fundservice.domain.model.Order;
import com.example.fundservice.domain.model.OrderStatus;

/**
 * Outbound adapter implementing {@link OrderRepositoryPort} with Spring Data JPA.
 */
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public OrderPersistenceAdapter(final OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(final Order order) {
        final OrderJpaEntity saved = jpaRepository.save(new OrderJpaEntity(
                order.getOrderId(), order.getAccountId(), order.getIsin(),
                order.getOperationType().name(), order.getAmount(), order.getCurrency(),
                order.getStatus().name(), order.getCreatedAt()));
        return new Order(saved.getOrderId(), saved.getAccountId(), saved.getIsin(),
                OperationType.valueOf(saved.getOperationType()), saved.getAmount(), saved.getCurrency(),
                OrderStatus.valueOf(saved.getStatus()), saved.getCreatedAt());
    }

    @Override
    public boolean hasActivePositions(final String isin) {
        return jpaRepository.existsByIsinAndStatusNot(isin, OrderStatus.REJECTED.name());
    }
}
