package com.example.transactionservice.infrastructure.adapter.in.web;

import com.example.transactionservice.application.port.in.CreateTransactionUseCase;
import com.example.transactionservice.application.port.in.GetTransactionUseCase;
import com.example.transactionservice.application.port.in.ListTransactionsQuery;
import com.example.transactionservice.application.port.in.ListTransactionsUseCase;
import com.example.transactionservice.application.port.in.PagedTransactions;
import com.example.transactionservice.domain.exception.InvalidTransactionException;
import com.example.transactionservice.domain.model.TransactionStatus;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.PagedTransactionsResponse;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.TransactionRequest;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.TransactionResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter exposing the transaction endpoints under {@code /v1/transactions}.
 */
@RestController
@RequestMapping("/v1/transactions")
@Validated
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionUseCase getTransactionUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;

    /**
     * Creates the controller with its use case collaborators.
     *
     * @param createTransactionUseCase create use case
     * @param getTransactionUseCase    get use case
     * @param listTransactionsUseCase  list use case
     */
    public TransactionController(final CreateTransactionUseCase createTransactionUseCase,
                                 final GetTransactionUseCase getTransactionUseCase,
                                 final ListTransactionsUseCase listTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionUseCase = getTransactionUseCase;
        this.listTransactionsUseCase = listTransactionsUseCase;
    }

    /**
     * Creates a new transaction.
     *
     * @param request validated transaction payload
     * @return 201 Created with the stored transaction
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody final TransactionRequest request) {
        final TransactionResponse response = TransactionWebMapper.toResponse(
                createTransactionUseCase.create(TransactionWebMapper.toCommand(request)));
        return ResponseEntity.created(URI.create("/v1/transactions/" + response.id())).body(response);
    }

    /**
     * Returns a single transaction by id.
     *
     * @param id transaction identifier
     * @return 200 OK with the transaction
     */
    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable final UUID id) {
        return TransactionWebMapper.toResponse(getTransactionUseCase.getById(id));
    }

    /**
     * Lists transactions with pagination and optional status filter.
     *
     * @param limit  maximum number of results (1..100)
     * @param offset number of results to skip (&ge; 0)
     * @param status optional status filter
     * @return 200 OK with a paginated envelope
     */
    @GetMapping
    public PagedTransactionsResponse list(
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) final int limit,
            @RequestParam(defaultValue = "0") @Min(0) final int offset,
            @RequestParam(required = false) final String status) {
        final ListTransactionsQuery query = new ListTransactionsQuery(parseStatus(status), limit, offset);
        final PagedTransactions page = listTransactionsUseCase.list(query);
        final List<TransactionResponse> data = page.data().stream()
                .map(TransactionWebMapper::toResponse)
                .collect(Collectors.toList());
        return new PagedTransactionsResponse(page.total(), page.limit(), page.offset(), data);
    }

    private TransactionStatus parseStatus(final String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return TransactionStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidTransactionException("status must be one of: pending, completed, failed, cancelled");
        }
    }
}
