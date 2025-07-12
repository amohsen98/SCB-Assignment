package com.scb.application.exception;

import lombok.Getter;

/**
 * Enum representing error codes with meaningful numbers.
 * The error codes are structured as follows:
 * - 1000-1999: Authentication and authorization errors
 * - 2000-2999: Validation errors
 * - 3000-3999: Business logic errors
 * - 9000-9999: System and unexpected errors
 */
@Getter
public enum ErrorCode {
    // Authentication errors (1000-1999)
    AUTHENTICATION_FAILED(1000, "Authentication failed"),
    INVALID_CREDENTIALS(1001, "Invalid credentials"),
    UNAUTHORIZED_ACCESS(1002, "Unauthorized access"),
    TOKEN_EXPIRED(1003, "Token expired"),
    
    // Validation errors (2000-2999)
    VALIDATION_ERROR(2000, "Validation error"),
    INVALID_INPUT(2001, "Invalid input"),
    MISSING_REQUIRED_FIELD(2002, "Missing required field"),
    
    // Business logic errors (3000-3999)
    RESOURCE_NOT_FOUND(3000, "Resource not found"),
    DUPLICATE_RESOURCE(3001, "Resource already exists"),
    OPERATION_FAILED(3002, "Operation failed"),
    
    // System errors (9000-9999)
    INTERNAL_SERVER_ERROR(9000, "Internal server error"),
    SERVICE_UNAVAILABLE(9001, "Service unavailable"),
    UNKNOWN_ERROR(9999, "Unknown error");
    
    private final int code;
    private final String defaultMessage;
    
    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}