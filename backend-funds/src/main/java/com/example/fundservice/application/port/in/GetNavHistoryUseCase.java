package com.example.fundservice.application.port.in;

/**
 * Inbound port for retrieving a fund's NAV history.
 */
public interface GetNavHistoryUseCase {

    /**
     * Returns the NAV history for a fund.
     *
     * @param query ISIN and optional date range
     * @return the NAV history
     */
    NavHistory history(NavHistoryQuery query);
}
