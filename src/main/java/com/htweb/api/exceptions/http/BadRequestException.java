package com.htweb.api.exceptions.http;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseHttpException {
    private final static String CODE = "BAD_REQUEST";

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, CODE, message);
    }

    public BadRequestException(String format, Object... args) {
        super(HttpStatus.BAD_REQUEST, CODE, format, args);
    }
}