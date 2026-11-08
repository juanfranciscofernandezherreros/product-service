package com.example.examsservice.infrastructure.adapter.in.web;

import com.example.examsservice.domain.exception.DuplicateResourceException;
import com.example.examsservice.domain.exception.InvalidExamException;
import com.example.examsservice.domain.exception.ResourceNotFoundException;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ErrorResponse;
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

    /** @param ex exception @return 404 */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, NOT_FOUND, ex.getMessage());
    }

    /** @param ex exception @return 409 */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final DuplicateResourceException ex) {
        return build(HttpStatus.CONFLICT, ALREADY_EXISTS, ex.getMessage());
    }

    /** @param ex exception @return 400 */
    @ExceptionHandler(InvalidExamException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(final InvalidExamException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /** @param ex exception @return 400 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, message.isEmpty() ? "Validation failed" : message);
    }

    /** @param ex exception @return 400 */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(final ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    /** @param ex exception @return 400 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(final HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Malformed request body");
    }

    /** @param ex exception @return 400 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Invalid value for parameter: " + ex.getName());
    }

    /** @param ex exception @return 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "Unexpected error");
    }

    private ResponseEntity<ErrorResponse> build(final HttpStatus status, final String code, final String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message));
    }
}
