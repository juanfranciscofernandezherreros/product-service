package com.example.fundservice.infrastructure.adapter.out.persistence;

import com.example.fundservice.application.port.out.NavRepositoryPort;
import com.example.fundservice.domain.model.NavPoint;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Outbound adapter implementing {@link NavRepositoryPort} with Spring Data JPA.
 */
public class NavPersistenceAdapter implements NavRepositoryPort {

    private final NavPointJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public NavPersistenceAdapter(final NavPointJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<NavPoint> findHistory(final String isin, final LocalDate startDate, final LocalDate endDate) {
        return jpaRepository.findHistory(isin, startDate, endDate).stream()
                .map(entity -> new NavPoint(entity.getDate(), entity.getNav()))
                .collect(Collectors.toList());
    }
}
