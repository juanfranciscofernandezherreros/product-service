package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.NavPoint;
import java.util.List;

/**
 * A fund's NAV history.
 *
 * @param isin    fund ISIN
 * @param history NAV points ordered by date
 */
public record NavHistory(String isin, List<NavPoint> history) {
}
