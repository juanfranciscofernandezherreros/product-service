package com.example.fundservice.domain.exception;

/**
 * Raised when creating a fund whose ISIN already exists.
 */
public class FundAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a duplicated ISIN.
     *
     * @param isin the duplicated ISIN
     */
    public FundAlreadyExistsException(final String isin) {
        super("A fund already exists with isin: " + isin);
    }
}
