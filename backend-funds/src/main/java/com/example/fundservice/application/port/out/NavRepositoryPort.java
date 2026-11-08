package com.example.fundservice.application.port.out;

import com.example.fundservice.domain.model.NavPoint;
import java.time.LocalDate;
import java.util.List;

/**
 * Outbound port abstracting NAV history persistence.
 */
public interface NavRepositoryPort {

    /**
     * Returns the NAV history for a fund within an optional date range.
     *
     * @param isin      fund ISIN
     * @param startDate optional inclusive start date ({@code null} = open)
     * @param endDate   optional inclusive end date ({@code null} = open)
     * @return the NAV points ordered by date
     */
    List<NavPoint> findHistory(String isin, LocalDate startDate, LocalDate endDate);
}
