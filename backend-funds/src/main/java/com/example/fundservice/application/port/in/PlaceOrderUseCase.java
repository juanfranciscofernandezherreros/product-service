package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.Order;

/**
 * Inbound port for placing an order.
 */
public interface PlaceOrderUseCase {

    /**
     * Places a new order.
     *
     * @param command order attributes
     * @return the registered order
     */
    Order place(PlaceOrderCommand command);
}
