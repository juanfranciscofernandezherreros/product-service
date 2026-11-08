package com.example.fundservice.application.usecase;

import com.example.fundservice.application.port.in.PlaceOrderCommand;
import com.example.fundservice.application.port.in.PlaceOrderUseCase;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.Order;

/**
 * Application service implementing order placement.
 */
public class OrderService implements PlaceOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final FundRepositoryPort fundRepository;

    /**
     * Creates the service.
     *
     * @param orderRepository outbound order persistence port
     * @param fundRepository  outbound fund persistence port (to validate the target fund)
     */
    public OrderService(final OrderRepositoryPort orderRepository, final FundRepositoryPort fundRepository) {
        this.orderRepository = orderRepository;
        this.fundRepository = fundRepository;
    }

    @Override
    public Order place(final PlaceOrderCommand command) {
        if (!fundRepository.existsByIsin(command.isin())) {
            throw new FundNotFoundException(command.isin());
        }
        final Order order = Order.place(command.accountId(), command.isin(),
                command.operationType(), command.amount(), command.currency());
        return orderRepository.save(order);
    }
}
