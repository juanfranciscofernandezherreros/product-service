package com.example.fundservice.application.port.out;

import com.example.fundservice.domain.model.Fund;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port abstracting fund persistence.
 */
public interface FundRepositoryPort {

    /**
     * Persists (creates or updates) a fund.
     *
     * @param fund fund to store
     * @return the stored fund
     */
    Fund save(Fund fund);

    /**
     * Finds a fund by its ISIN.
     *
     * @param isin fund ISIN
     * @return the fund if present
     */
    Optional<Fund> findByIsin(String isin);

    /**
     * Indicates whether a fund exists.
     *
     * @param isin fund ISIN
     * @return {@code true} if present
     */
    boolean existsByIsin(String isin);

    /**
     * Returns a page of funds filtered by optional category and currency.
     *
     * @param category optional category filter ({@code null} = no filter)
     * @param currency optional currency filter ({@code null} = no filter)
     * @param limit    maximum number of results
     * @param offset   number of results to skip
     * @return the matching funds
     */
    List<Fund> findByFilter(String category, String currency, int limit, int offset);

    /**
     * Counts funds filtered by optional category and currency.
     *
     * @param category optional category filter
     * @param currency optional currency filter
     * @return the number of matching funds
     */
    long countByFilter(String category, String currency);

    /**
     * Deletes a fund by its ISIN.
     *
     * @param isin fund ISIN
     */
    void deleteByIsin(String isin);
}
