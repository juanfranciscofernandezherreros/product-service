package com.example.fundservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * JPA persistence entity for NAV observations of a fund.
 */
@Entity
@Table(name = "nav_points")
public class NavPointJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "isin", nullable = false)
    private String isin;

    @Column(name = "nav_date", nullable = false)
    private LocalDate date;

    @Column(name = "nav", nullable = false)
    private BigDecimal nav;

    /**
     * Default constructor required by JPA.
     */
    protected NavPointJpaEntity() {
    }

    /**
     * Creates a NAV point entity.
     *
     * @param isin fund ISIN
     * @param date valuation date
     * @param nav  net asset value
     */
    public NavPointJpaEntity(final String isin, final LocalDate date, final BigDecimal nav) {
        this.isin = isin;
        this.date = date;
        this.nav = nav;
    }

    /** @return the id */
    public Long getId() {
        return id;
    }

    /** @return the ISIN */
    public String getIsin() {
        return isin;
    }

    /** @return the valuation date */
    public LocalDate getDate() {
        return date;
    }

    /** @return the net asset value */
    public BigDecimal getNav() {
        return nav;
    }
}
