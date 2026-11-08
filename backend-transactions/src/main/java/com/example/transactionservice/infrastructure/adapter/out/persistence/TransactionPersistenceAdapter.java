package com.example.transactionservice.infrastructure.adapter.out.persistence;

import com.example.transactionservice.application.port.out.TransactionRepositoryPort;
import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Outbound adapter implementing {@link TransactionRepositoryPort} with Spring Data JPA.
 */
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final TransactionJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public TransactionPersistenceAdapter(final TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(final Transaction transaction) {
        final TransactionJpaEntity saved = jpaRepository.save(TransactionPersistenceMapper.toEntity(transaction));
        return TransactionPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(final UUID id) {
        return jpaRepository.findById(id).map(TransactionPersistenceMapper::toDomain);
    }

    @Override
    public List<Transaction> findByFilter(final TransactionStatus status, final int limit, final int offset) {
        final Pageable pageable = new OffsetBasedPageRequest(offset, limit,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        final List<TransactionJpaEntity> entities = status == null
                ? jpaRepository.findAll(pageable).getContent()
                : jpaRepository.findByStatus(status.name(), pageable);
        return entities.stream().map(TransactionPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countByFilter(final TransactionStatus status) {
        return status == null ? jpaRepository.count() : jpaRepository.countByStatus(status.name());
    }
}
