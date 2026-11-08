package com.example.transactionservice.infrastructure.adapter.in.web.dto;

/**
 * Error payload as defined by the API contract.
 *
 * @param code    machine-readable error code
 * @param message human-readable error message
 */
public record ErrorResponse(String code, String message) {
}
