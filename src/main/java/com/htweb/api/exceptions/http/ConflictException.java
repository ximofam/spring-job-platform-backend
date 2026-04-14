package com.htweb.api.exceptions.http;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseHttpException {
    private static final String CODE = "CONFLICT";

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, CODE, message);
    }

    public ConflictException(String format, Object... args) {
        super(HttpStatus.CONFLICT, CODE, format, args);
    }
}