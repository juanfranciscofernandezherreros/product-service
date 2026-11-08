package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.Fund;

/**
 * Inbound port for creating a fund.
 */
public interface CreateFundUseCase {

    /**
     * Creates a new fund.
     *
     * @param command fund attributes
     * @return the created fund
     */
    Fund create(CreateFundCommand command);
}
