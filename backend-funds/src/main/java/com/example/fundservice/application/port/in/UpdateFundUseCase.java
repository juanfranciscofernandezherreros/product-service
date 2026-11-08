package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.Fund;

/**
 * Inbound port for updating a fund.
 */
public interface UpdateFundUseCase {

    /**
     * Updates an existing fund.
     *
     * @param isin    fund ISIN
     * @param command new attributes
     * @return the updated fund
     */
    Fund update(String isin, UpdateFundCommand command);
}
