package com.htweb.api.exceptions.http;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseHttpException {
    private static final String CODE = "INTERNAL_SERVER_ERROR";

    public InternalServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, CODE, message);
    }

    public InternalServerException(String format, Object... args) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, CODE, format, args);
    }
}
