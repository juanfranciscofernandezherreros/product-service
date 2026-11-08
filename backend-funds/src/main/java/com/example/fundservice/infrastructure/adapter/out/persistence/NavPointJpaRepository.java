package com.example.fundservice.infrastructure.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for {@link NavPointJpaEntity}.
 */
public interface NavPointJpaRepository extends JpaRepository<NavPointJpaEntity, Long> {

    /**
     * Finds the NAV history for a fund within an optional date range, ordered by date.
     *
     * @param isin  fund ISIN
     * @param start optional inclusive start date ({@code null} = open)
     * @param end   optional inclusive end date ({@code null} = open)
     * @return the matching NAV points
     */
    @Query("select n from NavPointJpaEntity n where n.isin = :isin "
            + "and (:start is null or n.date >= :start) and (:end is null or n.date <= :end) "
            + "order by n.date asc")
    List<NavPointJpaEntity> findHistory(@Param("isin") String isin,
                                        @Param("start") LocalDate start, @Param("end") LocalDate end);
}
