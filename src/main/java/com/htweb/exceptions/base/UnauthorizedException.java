package com.htweb.exceptions.base;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException(String format, Object... args) {
        super(HttpStatus.UNAUTHORIZED, format, args);
    }
}
