package com.example.fundservice.application.port.out;

import com.example.fundservice.domain.model.Order;

/**
 * Outbound port abstracting order persistence.
 */
public interface OrderRepositoryPort {

    /**
     * Persists an order.
     *
     * @param order order to store
     * @return the stored order
     */
    Order save(Order order);

    /**
     * Indicates whether a fund has active positions, i.e. at least one order
     * whose status is not {@code REJECTED}.
     *
     * @param isin fund ISIN
     * @return {@code true} if the fund has active positions
     */
    boolean hasActivePositions(String isin);
}
