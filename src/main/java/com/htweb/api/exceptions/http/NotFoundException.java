package com.htweb.api.exceptions.http;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseHttpException {
    private static final String CODE = "NOT_FOUND";

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, CODE, message);
    }

    public NotFoundException(String format, Object... args) {
        super(HttpStatus.NOT_FOUND, CODE, format, args);
    }
}
