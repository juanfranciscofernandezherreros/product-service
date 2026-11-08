package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.Fund;

/**
 * Inbound port for retrieving a fund by ISIN.
 */
public interface GetFundUseCase {

    /**
     * Retrieves a fund by its ISIN.
     *
     * @param isin fund ISIN
     * @return the fund
     */
    Fund getByIsin(String isin);
}
