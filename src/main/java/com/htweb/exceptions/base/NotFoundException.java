package com.htweb.exceptions.base;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String format, Object... args) {
        super(HttpStatus.NOT_FOUND, format, args);
    }
}
