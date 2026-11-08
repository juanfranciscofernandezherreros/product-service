package com.example.transactionservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.transactionservice.application.port.in.CreateTransactionCommand;
import com.example.transactionservice.application.port.in.ListTransactionsQuery;
import com.example.transactionservice.application.port.in.PagedTransactions;
import com.example.transactionservice.application.port.out.TransactionRepositoryPort;
import com.example.transactionservice.domain.exception.TransactionNotFoundException;
import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionStatus;
import com.example.transactionservice.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link TransactionService}.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepositoryPort repository;

    private TransactionService service;

    @BeforeEach
    void setUp() {
        service = new TransactionService(repository);
    }

    private Transaction sample(final UUID id) {
        return new Transaction(id, new BigDecimal("150.50"), "EUR", TransactionType.DEBIT,
                TransactionStatus.PENDING, UUID.randomUUID(), "x", Instant.now());
    }

    @Test
    void createPersistsPendingTransaction() {
        when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        final Transaction result = service.create(new CreateTransactionCommand(
                new BigDecimal("150.50"), "EUR", TransactionType.DEBIT, UUID.randomUUID(), "x"));

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(result.getAmount()).isEqualByComparingTo("150.50");
    }

    @Test
    void getByIdReturnsWhenPresent() {
        final UUID id = UUID.randomUUID();
        final Transaction tx = sample(id);
        when(repository.findById(id)).thenReturn(Optional.of(tx));

        assertThat(service.getById(id)).isEqualTo(tx);
    }

    @Test
    void getByIdThrowsWhenMissing() {
        final UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id)).isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void listReturnsPagedResultWithTotal() {
        final Transaction tx = sample(UUID.randomUUID());
        when(repository.findByFilter(eq(TransactionStatus.COMPLETED), eq(2), eq(0))).thenReturn(List.of(tx));
        when(repository.countByFilter(TransactionStatus.COMPLETED)).thenReturn(5L);

        final PagedTransactions page = service.list(new ListTransactionsQuery(TransactionStatus.COMPLETED, 2, 0));

        assertThat(page.total()).isEqualTo(5L);
        assertThat(page.limit()).isEqualTo(2);
        assertThat(page.offset()).isZero();
        assertThat(page.data()).containsExactly(tx);
    }
}
