package com.example.fundservice.infrastructure.adapter.out.persistence;

import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.domain.model.Fund;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Outbound adapter implementing {@link FundRepositoryPort} with Spring Data JPA.
 */
public class FundPersistenceAdapter implements FundRepositoryPort {

    private final FundJpaRepository jpaRepository;

    /**
     * Creates the adapter.
     *
     * @param jpaRepository Spring Data repository
     */
    public FundPersistenceAdapter(final FundJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Fund save(final Fund fund) {
        return FundPersistenceMapper.toDomain(jpaRepository.save(FundPersistenceMapper.toEntity(fund)));
    }

    @Override
    public Optional<Fund> findByIsin(final String isin) {
        return jpaRepository.findById(isin).map(FundPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByIsin(final String isin) {
        return jpaRepository.existsById(isin);
    }

    @Override
    public List<Fund> findByFilter(final String category, final String currency,
                                   final int limit, final int offset) {
        final Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("isin").ascending());
        return jpaRepository.findByFilter(category, currency, pageable).stream()
                .map(FundPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByFilter(final String category, final String currency) {
        return jpaRepository.countByFilter(category, currency);
    }

    @Override
    public void deleteByIsin(final String isin) {
        jpaRepository.deleteById(isin);
    }
}
