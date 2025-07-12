package com.scb.application.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int statusCode;

    public ApiException(String message, ErrorCode errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public ApiException(ErrorCode errorCode, int statusCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public ApiException(String message, Throwable cause, ErrorCode errorCode, int statusCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
