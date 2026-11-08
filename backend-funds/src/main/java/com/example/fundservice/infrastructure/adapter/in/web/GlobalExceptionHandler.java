package com.example.fundservice.infrastructure.adapter.in.web;

import com.example.fundservice.domain.exception.FundAlreadyExistsException;
import com.example.fundservice.domain.exception.FundHasActivePositionsException;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.exception.InvalidFundException;
import com.example.fundservice.domain.exception.InvalidOrderException;
import com.example.fundservice.infrastructure.adapter.in.web.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Centralised translation of domain and validation errors into the contract's
 * {@code {code, message}} error payload.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";
    private static final String NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String ALREADY_EXISTS = "RESOURCE_ALREADY_EXISTS";
    private static final String ACTIVE_POSITIONS = "FUND_HAS_ACTIVE_POSITIONS";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    /**
     * Maps a missing fund to 404.
     *
     * @param ex the exception
     * @return 404 response
     */
    @ExceptionHandler(FundNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final FundNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, NOT_FOUND, ex.getMessage());
    }

    /**
     * Maps a duplicated ISIN to 409.
     *
     * @param ex the exception
     * @return 409 response
     */
    @ExceptionHandler(FundAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final FundAlreadyExistsException ex) {
        return response(HttpStatus.CONFLICT, ALREADY_EXISTS, ex.getMessage());
    }

    /**
     * Maps a fund with active positions to 422.
     *
     * @param ex the exception
     * @return 422 response
     */
    @ExceptionHandler(FundHasActivePositionsException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessable(final FundHasActivePositionsException ex) {
        return response(HttpStatus.UNPROCESSABLE_ENTITY, ACTIVE_POSITIONS, ex.getMessage());
    }

    /**
     * Maps broken domain invariants to 400.
     *
     * @param ex the exception
     * @return 400 response
     */
    @ExceptionHandler({InvalidFundException.class, InvalidOrderException.class})
    public ResponseEntity<ErrorResponse> handleInvalid(final RuntimeException ex) {
        return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maps body validation failures to 400.
     *
     * @param ex the exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, message.isEmpty() ? "Validation failed" : message);
    }

    /**
     * Maps query-parameter constraint violations to 400.
     *
     * @param ex the exception
     * @return 400 response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(final ConstraintViolationException ex) {
        return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maps malformed request bodies to 400.
     *
     * @param ex the exception
     * @return 400 response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(final HttpMessageNotReadableException ex) {
        return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Malformed request body");
    }

    /**
     * Maps type mismatches to 400.
     *
     * @param ex the exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Invalid value for parameter: " + ex.getName());
    }

    /**
     * Maps any unexpected error to 500.
     *
     * @param ex the exception
     * @return 500 response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "Unexpected error");
    }

    private ResponseEntity<ErrorResponse> response(final HttpStatus status, final String code,
                                                   final String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
