package com.htweb.api.exceptions.http;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseHttpException {
    private static final String CODE = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, CODE, message);
    }

    public ForbiddenException(String format, Object... args) {
        super(HttpStatus.FORBIDDEN, CODE, format, args);
    }
}