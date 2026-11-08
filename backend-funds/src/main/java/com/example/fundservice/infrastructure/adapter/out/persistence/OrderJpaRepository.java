package com.example.fundservice.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link OrderJpaEntity}.
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    /**
     * Indicates whether a fund has at least one order whose status is not the given one.
     *
     * @param isin   fund ISIN
     * @param status status to exclude (e.g. {@code REJECTED})
     * @return {@code true} if such an order exists
     */
    boolean existsByIsinAndStatusNot(String isin, String status);
}
