package com.gameflix.exception;

import com.gameflix.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        String message = "A record with this value already exists";
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("email")) {
            message = "Email already registered";
        } else if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("username")) {
            message = "Username already taken";
        }
        return buildError(HttpStatus.CONFLICT, "CONFLICT", message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid email or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return buildError(HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("errorCode", "VALIDATION_ERROR");
        body.put("message", "Validation failed");
        body.put("errors", fieldErrors);
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("errorCode", errorCode);
        body.put("message", message);
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(status).body(body);
    }
}
