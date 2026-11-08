package com.example.fundservice.domain.exception;

/**
 * Raised when attempting to delete a fund that still has active positions.
 */
public class FundHasActivePositionsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a fund with active positions.
     *
     * @param isin the fund ISIN
     */
    public FundHasActivePositionsException(final String isin) {
        super("Fund cannot be deleted because it has active positions: " + isin);
    }
}
