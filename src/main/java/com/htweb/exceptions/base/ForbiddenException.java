package com.htweb.exceptions.base;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public ForbiddenException(String format, Object... args) {
        super(HttpStatus.FORBIDDEN, format, args);
    }
}