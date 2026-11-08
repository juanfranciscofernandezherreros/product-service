package com.example.fundservice.application.port.in;

/**
 * Inbound port for listing funds.
 */
public interface ListFundsUseCase {

    /**
     * Lists funds matching the query.
     *
     * @param query pagination and filter parameters
     * @return a page of funds with the total count
     */
    PagedFunds list(ListFundsQuery query);
}
