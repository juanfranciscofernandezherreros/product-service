package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Response for a fund's NAV history.
 *
 * @param isin    ISIN code
 * @param history NAV points ordered by date
 */
public record NavHistoryResponse(String isin, List<NavPointResponse> history) {
}
