package com.example.fundservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for {@link FundJpaEntity}.
 */
public interface FundJpaRepository extends JpaRepository<FundJpaEntity, String> {

    /**
     * Finds funds filtered by optional category and currency.
     *
     * @param category optional category ({@code null} = ignore)
     * @param currency optional currency ({@code null} = ignore)
     * @param pageable pagination settings
     * @return the matching entities
     */
    @Query("select f from FundJpaEntity f where (:category is null or f.category = :category) "
            + "and (:currency is null or f.currency = :currency)")
    List<FundJpaEntity> findByFilter(@Param("category") String category,
                                     @Param("currency") String currency, Pageable pageable);

    /**
     * Counts funds filtered by optional category and currency.
     *
     * @param category optional category ({@code null} = ignore)
     * @param currency optional currency ({@code null} = ignore)
     * @return the number of matching entities
     */
    @Query("select count(f) from FundJpaEntity f where (:category is null or f.category = :category) "
            + "and (:currency is null or f.currency = :currency)")
    long countByFilter(@Param("category") String category, @Param("currency") String currency);
}
