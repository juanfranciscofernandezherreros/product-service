package com.example.fundservice.application.port.in;

/**
 * Inbound port for deleting a fund.
 */
public interface DeleteFundUseCase {

    /**
     * Deletes a fund by its ISIN.
     *
     * @param isin fund ISIN
     */
    void delete(String isin);
}
