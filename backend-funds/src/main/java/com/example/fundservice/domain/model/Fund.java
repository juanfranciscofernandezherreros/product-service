package com.example.fundservice.domain.model;

import com.example.fundservice.domain.exception.InvalidFundException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable domain representation of an investment fund, keyed by its ISIN.
 *
 * <p>Invariants: non-blank ISIN, name, category and currency; {@code riskLevel}
 * between 1 and 7; non-negative fees and assets when present.</p>
 */
public final class Fund {

    private static final int MIN_RISK = 1;
    private static final int MAX_RISK = 7;

    private final String isin;
    private final String name;
    private final String category;
    private final String currency;
    private final BigDecimal currentNav;
    private final int riskLevel;
    private final BigDecimal managementFee;
    private final BigDecimal depositaryFee;
    private final BigDecimal assetsUnderManagement;
    private final String description;

    /**
     * Creates a validated fund.
     *
     * @param isin                  ISIN code (natural identifier)
     * @param name                  fund name
     * @param category              investment category
     * @param currency              currency code
     * @param currentNav            latest net asset value (may be {@code null})
     * @param riskLevel             risk level (1..7)
     * @param managementFee         management fee (may be {@code null})
     * @param depositaryFee         depositary fee (may be {@code null})
     * @param assetsUnderManagement assets under management (may be {@code null})
     * @param description           free-text description (may be {@code null})
     * @throws InvalidFundException if any invariant is violated
     */
    public Fund(final String isin, final String name, final String category, final String currency,
                final BigDecimal currentNav, final int riskLevel, final BigDecimal managementFee,
                final BigDecimal depositaryFee, final BigDecimal assetsUnderManagement,
                final String description) {
        this.isin = requireText(isin, "isin");
        this.name = requireText(name, "name");
        this.category = requireText(category, "category");
        this.currency = requireText(currency, "currency");
        if (riskLevel < MIN_RISK || riskLevel > MAX_RISK) {
            throw new InvalidFundException("riskLevel must be between " + MIN_RISK + " and " + MAX_RISK);
        }
        this.currentNav = currentNav;
        this.riskLevel = riskLevel;
        this.managementFee = requireNonNegative(managementFee, "managementFee");
        this.depositaryFee = requireNonNegative(depositaryFee, "depositaryFee");
        this.assetsUnderManagement = requireNonNegative(assetsUnderManagement, "assetsUnderManagement");
        this.description = description;
    }

    /**
     * Factory for a newly registered fund (no NAV nor assets yet).
     *
     * @param isin          ISIN code
     * @param name          fund name
     * @param category      investment category
     * @param currency      currency code
     * @param riskLevel     risk level (1..7)
     * @param managementFee management fee
     * @param depositaryFee depositary fee
     * @param description   description
     * @return a new validated fund
     */
    public static Fund register(final String isin, final String name, final String category,
                                final String currency, final int riskLevel, final BigDecimal managementFee,
                                final BigDecimal depositaryFee, final String description) {
        return new Fund(isin, name, category, currency, null, riskLevel,
                managementFee, depositaryFee, null, description);
    }

    /**
     * Returns a copy of this fund with updated descriptive attributes, keeping
     * the ISIN, currency, current NAV and assets under management.
     *
     * @param newName          new name (keeps current when {@code null})
     * @param newCategory      new category (keeps current when {@code null})
     * @param newRiskLevel     new risk level (keeps current when {@code null})
     * @param newManagementFee new management fee (keeps current when {@code null})
     * @param newDepositaryFee new depositary fee (keeps current when {@code null})
     * @param newDescription   new description (keeps current when {@code null})
     * @return an updated fund
     */
    public Fund withUpdatedDetails(final String newName, final String newCategory, final Integer newRiskLevel,
                                   final BigDecimal newManagementFee, final BigDecimal newDepositaryFee,
                                   final String newDescription) {
        return new Fund(
                this.isin,
                newName == null ? this.name : newName,
                newCategory == null ? this.category : newCategory,
                this.currency,
                this.currentNav,
                newRiskLevel == null ? this.riskLevel : newRiskLevel,
                newManagementFee == null ? this.managementFee : newManagementFee,
                newDepositaryFee == null ? this.depositaryFee : newDepositaryFee,
                this.assetsUnderManagement,
                newDescription == null ? this.description : newDescription);
    }

    private static String requireText(final String value, final String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidFundException(field + " is required");
        }
        return value.trim();
    }

    private static BigDecimal requireNonNegative(final BigDecimal value, final String field) {
        if (value != null && value.signum() < 0) {
            throw new InvalidFundException(field + " must not be negative");
        }
        return value;
    }

    /**
     * Returns the ISIN.
     *
     * @return the isin
     */
    public String getIsin() {
        return isin;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the current NAV.
     *
     * @return the current NAV, may be {@code null}
     */
    public BigDecimal getCurrentNav() {
        return currentNav;
    }

    /**
     * Returns the risk level.
     *
     * @return the risk level
     */
    public int getRiskLevel() {
        return riskLevel;
    }

    /**
     * Returns the management fee.
     *
     * @return the management fee, may be {@code null}
     */
    public BigDecimal getManagementFee() {
        return managementFee;
    }

    /**
     * Returns the depositary fee.
     *
     * @return the depositary fee, may be {@code null}
     */
    public BigDecimal getDepositaryFee() {
        return depositaryFee;
    }

    /**
     * Returns the assets under management.
     *
     * @return the assets under management, may be {@code null}
     */
    public BigDecimal getAssetsUnderManagement() {
        return assetsUnderManagement;
    }

    /**
     * Returns the description.
     *
     * @return the description, may be {@code null}
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Fund)) {
            return false;
        }
        return Objects.equals(isin, ((Fund) other).isin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isin);
    }
}
