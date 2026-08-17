package com.fhlbdm.eadvantage.exception;

import org.springframework.http.HttpStatus;

public class SecurityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    public SecurityException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public SecurityException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
