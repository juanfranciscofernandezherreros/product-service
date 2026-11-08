package com.example.fundservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * JPA persistence entity for funds, keyed by ISIN.
 */
@Entity
@Table(name = "funds")
public class FundJpaEntity {

    @Id
    @Column(name = "isin", nullable = false, updatable = false)
    private String isin;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "current_nav")
    private BigDecimal currentNav;

    @Column(name = "risk_level", nullable = false)
    private int riskLevel;

    @Column(name = "management_fee")
    private BigDecimal managementFee;

    @Column(name = "depositary_fee")
    private BigDecimal depositaryFee;

    @Column(name = "assets_under_management")
    private BigDecimal assetsUnderManagement;

    @Column(name = "description", length = 2000)
    private String description;

    /**
     * Default constructor required by JPA.
     */
    protected FundJpaEntity() {
    }

    /**
     * Creates a fully populated entity.
     *
     * @param isin                  ISIN
     * @param name                  name
     * @param category              category
     * @param currency              currency
     * @param currentNav            current NAV
     * @param riskLevel             risk level
     * @param managementFee         management fee
     * @param depositaryFee         depositary fee
     * @param assetsUnderManagement assets under management
     * @param description           description
     */
    public FundJpaEntity(final String isin, final String name, final String category, final String currency,
                         final BigDecimal currentNav, final int riskLevel, final BigDecimal managementFee,
                         final BigDecimal depositaryFee, final BigDecimal assetsUnderManagement,
                         final String description) {
        this.isin = isin;
        this.name = name;
        this.category = category;
        this.currency = currency;
        this.currentNav = currentNav;
        this.riskLevel = riskLevel;
        this.managementFee = managementFee;
        this.depositaryFee = depositaryFee;
        this.assetsUnderManagement = assetsUnderManagement;
        this.description = description;
    }

    /** @return the ISIN */
    public String getIsin() {
        return isin;
    }

    /** @return the name */
    public String getName() {
        return name;
    }

    /** @return the category */
    public String getCategory() {
        return category;
    }

    /** @return the currency */
    public String getCurrency() {
        return currency;
    }

    /** @return the current NAV */
    public BigDecimal getCurrentNav() {
        return currentNav;
    }

    /** @return the risk level */
    public int getRiskLevel() {
        return riskLevel;
    }

    /** @return the management fee */
    public BigDecimal getManagementFee() {
        return managementFee;
    }

    /** @return the depositary fee */
    public BigDecimal getDepositaryFee() {
        return depositaryFee;
    }

    /** @return the assets under management */
    public BigDecimal getAssetsUnderManagement() {
        return assetsUnderManagement;
    }

    /** @return the description */
    public String getDescription() {
        return description;
    }
}
