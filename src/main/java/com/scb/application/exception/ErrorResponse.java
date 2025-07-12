package com.scb.application.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Standard error response class for API errors.
 * Contains a message, error code, timestamp, and optional field errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int errorCode;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
    private String errorId;

    /**
     * Creates a basic error response with the given error code and message.
     */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        String errorId = UUID.randomUUID().toString();
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(new HashMap<>())
                .errorId(errorId)
                .build();
    }

    /**
     * Creates an error response with the default message from the error code.
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getDefaultMessage());
    }

    /**
     * Adds a field error to the response.
     */
    public ErrorResponse addError(String field, String errorMessage) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }
        this.errors.put(field, errorMessage);
        return this;
    }

    /**
     * Adds multiple field errors to the response.
     */
    public ErrorResponse addErrors(Map<String, String> fieldErrors) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }
        this.errors.putAll(fieldErrors);
        return this;
    }
}
