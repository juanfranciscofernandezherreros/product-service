package com.example.fundservice.domain.exception;

/**
 * Raised when a fund cannot be located by its ISIN.
 */
public class FundNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a missing ISIN.
     *
     * @param isin the ISIN that was not found
     */
    public FundNotFoundException(final String isin) {
        super("Fund not found with isin: " + isin);
    }
}
