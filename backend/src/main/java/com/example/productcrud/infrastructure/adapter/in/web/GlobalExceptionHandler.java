package com.example.productcrud.infrastructure.adapter.in.web;

import com.example.productcrud.domain.exception.InvalidProductException;
import com.example.productcrud.domain.exception.ProductNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralised translation of domain and validation errors into HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maps a missing product to 404 Not Found.
     *
     * @param ex the raised exception
     * @return 404 response with error details
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(final ProductNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maps a broken domain invariant to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response with error details
     */
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Map<String, Object>> handleInvalid(final InvalidProductException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maps bean validation failures to 400 Bad Request.
     *
     * @param ex the raised exception
     * @return 400 response with aggregated field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<Map<String, Object>> build(final HttpStatus status, final String message) {
        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
