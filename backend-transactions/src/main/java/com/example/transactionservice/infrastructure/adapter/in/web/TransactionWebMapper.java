package com.example.transactionservice.infrastructure.adapter.in.web;

import com.example.transactionservice.application.port.in.CreateTransactionCommand;
import com.example.transactionservice.domain.exception.InvalidTransactionException;
import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionType;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.TransactionRequest;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.TransactionResponse;
import java.util.Locale;

/**
 * Explicit mapper between web DTOs and application/domain types.
 */
public final class TransactionWebMapper {

    private TransactionWebMapper() {
    }

    /**
     * Converts a request DTO into an application command, validating the type enum.
     *
     * @param request inbound request
     * @return the create command
     * @throws InvalidTransactionException if the type is not a valid value
     */
    public static CreateTransactionCommand toCommand(final TransactionRequest request) {
        return new CreateTransactionCommand(
                request.amount(),
                request.currency(),
                parseType(request.type()),
                request.accountId(),
                request.description());
    }

    /**
     * Converts a domain transaction into a response DTO (enums rendered lowercase).
     *
     * @param transaction domain transaction
     * @return the response DTO
     */
    public static TransactionResponse toResponse(final Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType().name().toLowerCase(Locale.ROOT),
                transaction.getStatus().name().toLowerCase(Locale.ROOT),
                transaction.getAccountId(),
                transaction.getDescription(),
                transaction.getCreatedAt());
    }

    private static TransactionType parseType(final String type) {
        try {
            return TransactionType.valueOf(type.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidTransactionException("type must be one of: credit, debit");
        }
    }
}
