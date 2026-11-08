package com.example.fundservice.infrastructure.adapter.out.persistence;

import com.example.fundservice.domain.model.Fund;

/**
 * Explicit mapper between the domain {@link Fund} and its JPA entity.
 */
public final class FundPersistenceMapper {

    private FundPersistenceMapper() {
    }

    /**
     * Converts a domain fund into a JPA entity.
     *
     * @param fund domain fund
     * @return the JPA entity
     */
    public static FundJpaEntity toEntity(final Fund fund) {
        return new FundJpaEntity(fund.getIsin(), fund.getName(), fund.getCategory(), fund.getCurrency(),
                fund.getCurrentNav(), fund.getRiskLevel(), fund.getManagementFee(), fund.getDepositaryFee(),
                fund.getAssetsUnderManagement(), fund.getDescription());
    }

    /**
     * Converts a JPA entity into a domain fund.
     *
     * @param entity JPA entity
     * @return the domain fund
     */
    public static Fund toDomain(final FundJpaEntity entity) {
        return new Fund(entity.getIsin(), entity.getName(), entity.getCategory(), entity.getCurrency(),
                entity.getCurrentNav(), entity.getRiskLevel(), entity.getManagementFee(),
                entity.getDepositaryFee(), entity.getAssetsUnderManagement(), entity.getDescription());
    }
}
