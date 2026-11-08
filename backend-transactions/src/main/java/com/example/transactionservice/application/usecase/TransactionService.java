package com.example.transactionservice.application.usecase;

import com.example.transactionservice.application.port.in.CreateTransactionCommand;
import com.example.transactionservice.application.port.in.CreateTransactionUseCase;
import com.example.transactionservice.application.port.in.GetTransactionUseCase;
import com.example.transactionservice.application.port.in.ListTransactionsQuery;
import com.example.transactionservice.application.port.in.ListTransactionsUseCase;
import com.example.transactionservice.application.port.in.PagedTransactions;
import com.example.transactionservice.application.port.out.TransactionRepositoryPort;
import com.example.transactionservice.domain.exception.TransactionNotFoundException;
import com.example.transactionservice.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the transaction use cases.
 *
 * <p>Depends only on the outbound {@link TransactionRepositoryPort} abstraction
 * and is wired through constructor injection.</p>
 */
public class TransactionService implements CreateTransactionUseCase, GetTransactionUseCase,
        ListTransactionsUseCase {

    private final TransactionRepositoryPort repository;

    /**
     * Creates the service.
     *
     * @param repository outbound persistence port
     */
    public TransactionService(final TransactionRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Transaction create(final CreateTransactionCommand command) {
        final Transaction transaction = Transaction.create(
                command.amount(), command.currency(), command.type(),
                command.accountId(), command.description());
        return repository.save(transaction);
    }

    @Override
    public Transaction getById(final UUID id) {
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public PagedTransactions list(final ListTransactionsQuery query) {
        final List<Transaction> data = repository.findByFilter(query.status(), query.limit(), query.offset());
        final long total = repository.countByFilter(query.status());
        return new PagedTransactions(total, query.limit(), query.offset(), data);
    }
}
