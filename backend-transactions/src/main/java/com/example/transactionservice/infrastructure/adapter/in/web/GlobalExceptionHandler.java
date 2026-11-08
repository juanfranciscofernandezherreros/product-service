package com.example.transactionservice.infrastructure.adapter.in.web;

import com.example.transactionservice.domain.exception.InvalidTransactionException;
import com.example.transactionservice.domain.exception.TransactionNotFoundException;
import com.example.transactionservice.infrastructure.adapter.in.web.dto.ErrorResponse;
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

    private static final String INVALID_REQUEST = "INVALID_REQUEST";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    /**
     * Maps a missing transaction to 404 Not Found.
     *
     * @param ex the raised exception
     * @return 404 response
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(NOT_FOUND, ex.getMessage()));
    }

    /**
     * Maps a broken domain invariant to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response
     */
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(final InvalidTransactionException ex) {
        return badRequest(ex.getMessage());
    }

    /**
     * Maps bean validation failures on the request body to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return badRequest(message.isEmpty() ? "Validation failed" : message);
    }

    /**
     * Maps query-parameter constraint violations to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(final ConstraintViolationException ex) {
        return badRequest(ex.getMessage());
    }

    /**
     * Maps malformed or unreadable request bodies to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(final HttpMessageNotReadableException ex) {
        return badRequest("Malformed request body");
    }

    /**
     * Maps type mismatches (e.g. an invalid UUID path variable) to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        return badRequest("Invalid value for parameter: " + ex.getName());
    }

    /**
     * Maps any unexpected error to 500 Internal Server Error.
     *
     * @param ex the raised exception
     * @return 500 response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(INTERNAL_ERROR, "Unexpected error"));
    }

    private ResponseEntity<ErrorResponse> badRequest(final String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(INVALID_REQUEST, message));
    }
}
