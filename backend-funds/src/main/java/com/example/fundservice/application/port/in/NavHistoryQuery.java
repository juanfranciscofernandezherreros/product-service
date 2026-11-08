package com.example.fundservice.application.port.in;

import java.time.LocalDate;

/**
 * Query parameters for retrieving a fund's NAV history.
 *
 * @param isin      fund ISIN
 * @param startDate optional inclusive start date
 * @param endDate   optional inclusive end date
 */
public record NavHistoryQuery(String isin, LocalDate startDate, LocalDate endDate) {
}
