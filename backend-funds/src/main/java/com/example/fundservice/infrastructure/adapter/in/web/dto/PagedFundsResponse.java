package com.example.fundservice.infrastructure.adapter.in.web.dto;

import java.util.List;

/**
 * Paginated list response for funds.
 *
 * @param total total number of matching funds
 * @param items the funds in this page
 */
public record PagedFundsResponse(long total, List<FundSummaryResponse> items) {
}
