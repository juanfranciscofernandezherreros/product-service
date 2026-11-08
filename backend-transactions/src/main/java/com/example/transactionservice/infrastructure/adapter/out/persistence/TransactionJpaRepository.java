package com.example.transactionservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link TransactionJpaEntity}.
 */
public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {

    /**
     * Finds transactions by status with pagination.
     *
     * @param status   status name
     * @param pageable pagination settings
     * @return the matching entities
     */
    List<TransactionJpaEntity> findByStatus(String status, Pageable pageable);

    /**
     * Counts transactions by status.
     *
     * @param status status name
     * @return the number of matching entities
     */
    long countByStatus(String status);
}
