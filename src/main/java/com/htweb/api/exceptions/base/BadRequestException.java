package com.htweb.api.exceptions.base;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException(String format, Object... args) {
        super(HttpStatus.BAD_REQUEST, format, args);
    }
}