package com.example.battleservice.infrastructure.adapter.in.web;

import com.example.battleservice.domain.exception.DuplicateResourceException;
import com.example.battleservice.domain.exception.InvalidBattleException;
import com.example.battleservice.domain.exception.ResourceNotFoundException;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.ErrorResponse;
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
 * Translates domain and validation errors into {@code {code, message}} responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";
    private static final String NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String ALREADY_EXISTS = "RESOURCE_ALREADY_EXISTS";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    /**
     * Maps a missing resource to 404.
     *
     * @param ex exception
     * @return 404 response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, NOT_FOUND, ex.getMessage());
    }

    /**
     * Maps a duplicate resource to 409.
     *
     * @param ex exception
     * @return 409 response
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final DuplicateResourceException ex) {
        return build(HttpStatus.CONFLICT, ALREADY_EXISTS, ex.getMessage());
    }

    /**
     * Maps broken invariants to 400.
     *
     * @param ex exception
     * @return 400 response
     */
    @ExceptionHandler(InvalidBattleException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(final InvalidBattleException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maps body validation failures to 400.
     *
     * @param ex exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, message.isEmpty() ? "Validation failed" : message);
    }

    /**
     * Maps query-parameter constraint violations to 400.
     *
     * @param ex exception
     * @return 400 response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(final ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maps malformed/invalid bodies to 400.
     *
     * @param ex exception
     * @return 400 response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(final HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Malformed request body");
    }

    /**
     * Maps type mismatches to 400.
     *
     * @param ex exception
     * @return 400 response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Invalid value for parameter: " + ex.getName());
    }

    /**
     * Maps unexpected errors to 500.
     *
     * @param ex exception
     * @return 500 response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "Unexpected error");
    }

    private ResponseEntity<ErrorResponse> build(final HttpStatus status, final String code, final String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
