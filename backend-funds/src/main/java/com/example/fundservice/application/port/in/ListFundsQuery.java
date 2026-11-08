package com.example.fundservice.application.port.in;

/**
 * Query parameters for listing funds with pagination and optional filters.
 *
 * @param category optional category filter
 * @param currency optional currency filter
 * @param limit    maximum number of results
 * @param offset   number of results to skip
 */
public record ListFundsQuery(String category, String currency, int limit, int offset) {
}
